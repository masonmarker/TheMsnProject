# Interpreters MSNScript2
#
# See documentation for more information,
# documentation could lack functions or
# capabilities in this interpreter, as
# this is a work in progress.
#
# docs: masonmarker.com/#/msn2docs
# run '{python_alias} msn2cli.py help' for help
#
# Author : Mason Marker
# Start date : 09/15/2022


# _TODO updated 10/16/2024
# TODO
# implement string parsing of the same character at which
# it was defined. ex: "hello \"world\"" -> hello "world"
# currently, this is not possible
#
# TODO
# implement linear interpretation in areas of heavy logic, this applies
# incredibly non linear approaches in several blocks
# such as <<>> or functions such as script()
#
# TODO
# implement warnings and warning handling, as this
# language was designed to be safe yet flexible
#
# TODO
# implement an interpretation for block syntax
# that permits the existence of whitespace / tabs / carriage returns
# in the multilined block to interpret
#
# TODO
# ensure no code repetition
# (it definitely exists)


# the current logical implementation is conceptual,
# deoptimized, and exists to prove functionality as speed can
# be enhanced later, however, opportunities for speed enhancement
# are still being implemented.

# NOTE
# Python runner's alias are determined by
# global python_alias (defined below), defaulting to 'python'
#
# console operations will not run successfully
# if the python runner is incorrect,
# is includes all console() calls, process splitting,
# etc.

# importing necessary dependencies
# for all lines of execution
import os
import threading

# classes
from core.classes.excel.sheet import Sheet, Workbook
from core.classes.var import Var
from core.classes.instruction import Instruction
from core.classes.method import Method

# function dispatch table
from core.dispatch.functions import FUNCTION_DISPATCH

# remove warnings for calling of integers: "10()"
import warnings
warnings.filterwarnings("ignore", category=SyntaxWarning)


# path to the common settings file
settings_path = "msn2_settings.json"
# latest version of the interpreter'
# set later
latest_version = None
# if settings does not exist
if not os.path.exists(settings_path):
    import json

    # get the latest version number from system/latest.json
    with open("system/latest.json") as f:
        # using the variable declared above
        latest_version = json.load(f)["latest"]
    # create settings
    with open(settings_path, "w") as f:
        # dump default settings
        json.dump(
            {
                "settings": {"has_ran": False, "runner_alias": "python"},
                "version": latest_version,
            },
            f,
        )
# global settings
settings = None
# python alias is in the msn2 settings json
python_alias = "python"
# obtains the python alias
with open("msn2_settings.json") as f:
    import json

    settings = json.load(f)
    python_alias = settings["settings"]["runner_alias"]
# msn2 implementation of None
msn2_none = "___msn2_None_"
# thread serial
thread_serial = 0
# global vars
lock = threading.Lock()
auxlock = threading.Lock()
# lock for pointer controls
pointer_lock = threading.Lock()
# pywinauto automation lock
auto_lock = threading.Lock()
# timings_set switch
timings_set = False
# user defined syntax
syntax = {}
# user defined enclosing syntax
enclosed = {}
# user defined macros
macros = {}
# user defined post macros
# aka macros that are defined that the end of a line
postmacros = {}
# user defined inline syntax
inlines = {}
# OpenAI model presets
# these are the latest models at the time of creation
models = None
# accounting information
inst_tree = {}
lines_ran = []
total_ints = 0
# automation
apps = {}
# colors for colored printing,
# defaults to "" until lazily loaded
colors = ""


# interprets MSNScript2, should create a new interpreter for each execution iteration


class Interpreter:
    # initializer
    def __init__(self):
        # check if this environment has executed
        # a script before
        if not settings["settings"]["has_ran"]:
            # set has_ran in the json file to true
            settings["settings"]["has_ran"] = True
            # write to the json file
            with open(settings_path, "w") as f:
                import json

                json.dump(settings, f)
        # get the latest version number from system/latest.json
        with open("system/latest.json") as f:
            import json

            global latest_version
            latest_version = json.load(f)["latest"]
        # if the version is not the latest
        if settings["version"] != latest_version:
            # update the version
            settings["version"] = latest_version
            # write to the json file
            with open(settings_path, "w") as f:
                import json

                json.dump(settings, f)
        # set the current settings
        self.settings = settings["settings"]
        # get the version in the json file
        self.version = settings["version"]
        # basic logging
        self.lines = []
        self.out = ""
        self.log = ""
        self.errors = []
        # environment logging
        self.vars = {}
        self.methods = {}
        self.loggedmethod = []
        self.objects = {}
        self.calledmethod = None
        self.env_max_chars = 200
        # advanced environment logging
        self.current_line = 0
        self.breaking = False
        self.breaking_return = []
        self.redirect = None
        self.redirecting = False
        self.redirect_inside = []
        self.imports = set()
        self.domains = set()
        # threading
        self.thread = None
        self.threads = {}
        self.parent = None
        # ChatGPT connections
        self.openaikey = None
        self.tokens = 100
        self.browser_path = None
        self.serial_1 = 0
        # hosting endpoints
        self.endpoints = {}
        self.endpoint_datas = {}
        self.endpoint_path = "demos/practical/apidata/apitestdata.csv"
        # multiprocessing
        self.processes = {}
        # global and local scopes for internal Python environment
        self._globals = {}
        self._locals = {}
        # in a try block
        self.trying = False
        # web
        # converting JavaScript?
        self.using_js = False
        # web imports
        self.web_imports = set()
        # states
        self.states = {}
        # routes
        self.routes = {}
        # NextJS generation
        self.next_entry_path = None
        self.next_project_path = None

    # determines if a line is a comment or not

    def is_comment(self, _line):
        return _line.startswith("#") or _line.startswith("::")

    # executes stored script
    def execute(self, script):
        # convert script to lines
        self.lines = []
        # for aggregate syntax support !{}
        inml = False
        ml = ""
        # for block syntax
        inblock = False
        p = 0
        # whether or not to keep
        keep_space = False
        keep_block = ""
        skipping = False
        # for each line of code
        for line in script.split("\n"):
            # add to list of liness
            self.lines.append(line)
            # continue if this line is a comment
            if self.is_comment(line):
                continue
            # for running Python
            if not keep_space and line.endswith("\\\\"):
                # determine if this script should run
                # based on the expression at the beginning of the line
                line = line[:-2]
                line = line.strip()
                # if there is a conditional Python execution
                if line:
                    # execute the conditional line
                    # with import capabilities
                    result = self.interpret(line)
                    keep_space = True
                    # if the conditional evaluated to True
                    if result:
                        # if the conditional line is true, then execute the Python
                        skipping = False
                        keep_block = ""
                    # otherwise, skip the Python
                    else:
                        skipping = True
                        keep_block = ""
                else:
                    skipping = False
                    keep_space = True
                    keep_block = ""
            # if the Python block has been ended
            elif keep_space and line.endswith("\\\\"):
                # if skipping
                if skipping:
                    skipping = False
                    keep_block = ""
                    keep_space = False
                    continue
                # fix line
                line = line[:-2]
                line = line.strip()
                # remove the trailing newline
                # character
                keep_block = keep_block[:-1]
                # modify conditionals
                keep_space = False
                # if setting to a variable
                if line:
                    # set variable to python block
                    self.vars[line] = Var(line, keep_block)
                else:
                    # execute Python
                    self.exec_python(keep_block)
                # reset python block and skipping flag
                keep_block = ""
                skipping = False
            # skipping this Python block
            # due to conditional
            elif keep_space and skipping:
                continue
            # if in a python block
            elif keep_space:
                keep_block += f"{line}\n"
                self.current_line += 1
                continue
            else:
                line = line.strip()
            if self.is_comment(line):
                self.current_line += 1
                continue
            else:
                # aggregate syntax !{} (not recommended for most cases)
                if line.startswith("!{") and line.endswith("}"):
                    ml = line[2:-1]
                    self.interpret(ml)
                    ml = ""
                elif not inml and line.startswith("!{"):
                    inml = True
                    ml += line[2:]
                elif inml and line.endswith("}"):
                    inml = False
                    ml += line[0 : len(line) - 1]
                    self.interpret(ml)
                    ml = ""
                elif inml:
                    ml += line
                # block syntax (recommended for most cases)
                elif (
                    not inblock
                    and line.endswith("(")
                    or line.endswith(",")
                    or line.endswith("{")
                    or line.endswith("[")
                    or line.endswith("=")
                    or line.endswith("{=")
                ):
                    for c in line:
                        if c == "(":
                            p += 1
                        if c == ")":
                            p -= 1
                        ml += c
                    inblock = True
                elif inblock:
                    for i in range(len(line)):
                        c = line[i]
                        if c == "(":
                            p += 1
                        if c == ")":
                            p -= 1
                        # end of syntax met
                        if p == 0:
                            ml += line[i:]
                            inter = ml
                            ml = ""
                            inblock = False
                            self.interpret(inter, keep_space=keep_space)
                            break
                        ml += c
                else:
                    self.interpret(line, keep_space=keep_space)
            self.current_line += 1
        return self.out

    def replace_vars(self, line):
        boo = line
        for varname in sorted(self.vars.keys(), key=len, reverse=True):
            try:
                boo = boo.replace(
                    varname, str(self.get_var(eval(f'"{varname}"', {}, {})))
                )
            except:
                None
        return boo

    # function to attempt to pull an integer out of the function
    # if it is an integer, then it is a loop that runs func times
    def get_int(self, func):
        try:
            return int(func)
        except:
            return None

    def interpret(self, line, block={}, keep_space=False):

        # acquiring globals
        global total_ints
        global lock
        global auxlock
        global auto_lock
        global pointer_lock
        global python_alias
        global inst_tree
        # accounting
        total_ints += 1
        lines_ran.append(line)
        # determine the location of this instruction
        # in the inst_tree depending on lines_ran
        # and the current line
        inst_tree[total_ints] = [self.current_line, line]
        # interpreter is breaking
        if self.breaking:
            return self.breaking_return
        # strip line for interpretation
        try:
            if not keep_space:
                line = line.strip()
        except:
            return
        l = len(line)
        # whether the line should immediately continue or not
        cont = False
        if not line:
            return

        # the below conditions interpret a line based on initial appearances
        # beneath these conditions will the Interpreter then parse the arguments from the line as a method call

        # method-specific line reached
        if line.startswith("--"):
            line = line[2:]
            try:
                if not self.methods[self.loggedmethod[-1]].ended:
                    self.methods[self.loggedmethod[-1]].add_body(line)
            except:
                None
            return
        # new variable setting and modification syntax as of 12/20/2022
        # iterates to the first '=' sign, capturing the variable name in the
        # process (as it should)
        # msn1 fallback
        if line[0] == "@":
            return self.interpret_msnscript_1(line[1:])
            # determine if we're using JS
        if line.startswith("JS:"):
            self.using_js = True
            return self.using_js
        if line.endswith(":JS"):
            self.using_js = False
            return self.using_js
        # python fallback mode specification,
        # both <<>> and
        if line.startswith("<<"):
            # parse all text in the line for text surrounded by |
            funccalls = []
            infunc = False
            func = ""
            for i in range(line.rindex(">>")):
                if line[i] == "|" and not infunc:
                    infunc = True
                elif line[i] == "|" and infunc:
                    infunc = False
                    funccalls.append(func)
                    func = ""
                elif infunc:
                    func += line[i]
            for function in funccalls:
                ret = self.interpret(function)
                if isinstance(ret, str):
                    line = line.replace(f"|{function}|", f'"{str(ret)}"')
                else:
                    line = line.replace(f"|{function}|", str(ret))
            line = line[2:-2]
            try:
                return eval(line)
            except:
                return line

        # embedded MSN2 interpretation macro
        if line.startswith("<2>"):
            # parse all text in the line for text surrounded by %
            funccalls = []
            infunc = False
            func = ""
            for i in range(3, line.rindex("<2>")):
                if line[i] == "%" and not infunc:
                    infunc = True
                elif line[i] == "%" and infunc:
                    infunc = False
                    funccalls.append(func)
                    func = ""
                elif infunc:
                    func += line[i]

            # for each msn2 evaluation
            for function in funccalls:
                ret = self.interpret(function)
                if isinstance(ret, str):
                    line = line.replace(f"%{function}%", f'"{str(ret)}"')
                else:
                    line = line.replace(f"%{function}%", str(ret))
            line = line[3:-3]
            try:
                return self.interpret(line)
            except:
                try:
                    return eval(line, {}, {})
                except:
                    return line

        # user defined syntax
        for key in syntax:
            if line.startswith(key):
                return self.run_syntax(key, line)
        # user defined macro
        for token in macros:
            if line.startswith(token):
                # if the macro returns a value instead of executing a function
                if len(macros[token]) == 4:
                    return macros[token][3]
                # variable name
                varname = macros[token][1]
                val = line[len(token) :]
                # store extended for user defined syntax
                self.vars[varname] = Var(varname, val)
                # execute function
                return self.interpret(macros[token][2])
        # user defined postmacro
        for token in postmacros:
            if line.endswith(token):
                # if the macro returns a value instead of executing a function
                if len(postmacros[token]) == 4:
                    return postmacros[token][3]
                varname = postmacros[token][1]
                val = line[0 : len(line) - len(token)]
                self.vars[varname] = Var(varname, val)
                return self.interpret(postmacros[token][2])
        # variable replacement, generally unsafe, but replaces
        # all variable names as they're written the the expression after
        # the '*'
        if line[0] == "*":
            return self.interpret(self.replace_vars(line[1:]))
        # invoking user defined enclosing syntax
        for key in enclosed:
            start = enclosed[key][0]
            end = enclosed[key][1]
            if line.startswith(start) and line.endswith(end):
                if len(enclosed[key]) == 5:
                    return enclosed[key][4]
                varname = enclosed[key][2]
                self.vars[varname] = Var(
                    varname, line[len(start) : len(line) - len(end)]
                )
                return self.interpret(enclosed[key][3])
        # checks for active Interpreter redirect request
        # generally slow, due to checking for the redirect
        if self.redirecting and not "stopredirect()" in line.replace(" ", ""):
            self.redirect_inside.append([self.redirect[1], line])
            return self.redirect

        # try base literal
        try:
            if not line.startswith("--"):
                # try evaluating the line
                _ret = eval(line, {}, {})
                # eval cannot be a python class, because names of variables
                # could result in python classes
                # should also not be a built in function
                if not isinstance(_ret, type) and not isinstance(_ret, type(eval)):
                    return _ret
        except:
            pass

        func = ""
        objfunc = ""
        obj = ""
        s = 0
        sp = 0
        for i in range(l):
            if cont:
                continue
            try:
                c = line[i]
            except:
                break
            if c == " " and s == 0:
                sp += 1
                continue
            if c == ".":
                obj = func
                func = ""
                objfunc = ""
                continue
            # basic method creation
            if c == "~":
                returnvariable = ""
                self.loggedmethod.append("")
                for j in range(i + 1, len(line)):
                    if line[j] != " ":
                        if line[j] == "(":
                            args = self.method_args(line, j)
                            for k in range(args[1], len(line)):
                                if line[k] != " ":
                                    if line[k] == "-" and line[k + 1] == ">":
                                        for l in range(k + 2, len(line)):
                                            if line[l] != " ":
                                                returnvariable += line[l]
                            break
                        self.loggedmethod[-1] += line[j]
                if self.loggedmethod[-1] not in self.methods.keys():
                    self.methods[self.loggedmethod[-1]] = Method(
                        self.loggedmethod[-1], self
                    )
                else:
                    break
                for arg in args[0]:
                    if arg != "":
                        self.vars[arg] = None
                        self.methods[self.loggedmethod[-1]].add_arg(arg)
                self.methods[self.loggedmethod[-1]].add_return(returnvariable)
                return self.loggedmethod[-1]

            # interpreting a function
            elif c == "(":
                mergedargs = ""
                p = 1
                for j in range(i + 1, l - 1):
                    c2 = line[j]
                    if p == 0:
                        break
                    if c2 == "(":
                        p += 1
                    if c2 == ")":
                        p -= 1
                    mergedargs += c2
                args = self.get_args(mergedargs)
                f = len(func)
                # clean function for handling
                func = func.strip()
                objfunc = objfunc.strip()
                # create an instruction from parsed data
                inst = Instruction(line, func, obj, objfunc, args, inst_tree, self)
                # retrieving arguments
                # if using_js
                if self.using_js:
                    return inst.convert_to_js(lock, lines_ran)

                # class attribute / method access
                if obj in self.vars:
                    vname = obj
                    try:
                        var = self.get_var(vname)
                    except:
                        var = self.vars[vname]
                    try:
                        object = self.vars[obj].value
                    except:
                        object = self.vars[obj]
                    _type_object = type(object)
                    try:
                        # if the object is a class
                        if objfunc in object:
                            # if the object is a Method
                            if type(object[objfunc]) == Method:
                                # get the Method object
                                method = object[objfunc]
                                # get the number of arguments to the method
                                num_args = len(method.args)
                                # args to pass to the function
                                to_pass = [vname]
                                # if there is no argument
                                if args[0][0] != "":
                                    # for each parsed argument
                                    for k in range(num_args):
                                        try:
                                            to_pass.append(self.parse(k, line, args)[2])
                                        except:
                                            None
                                # create return variable
                                ret_name = method.returns[0]
                                # if the return variable doesn't exist
                                if ret_name not in self.vars:
                                    self.vars[ret_name] = Var(ret_name, None)
                                # insert vname into args[0]
                                args.insert(0, [vname])
                                # execute method
                                try:
                                    method.run(to_pass, self, args)
                                # Index out of bounds error
                                except IndexError:
                                    self.raise_incorrect_args(
                                        str(len(method.args)),
                                        str(self.arg_count(args) - 1),
                                        line,
                                        lines_ran,
                                        method,
                                    )
                                try:
                                    return eval(
                                        str(self.vars[method.returns[0]].value), {}, {}
                                    )
                                except:
                                    try:
                                        return str(self.vars[method.returns[0]].value)
                                    except:
                                        return str(self.vars[method.returns[0]])
                            # otherwise if we're accessing an attribute
                            # no arguments given
                            if args[0][0] == "":
                                return object[objfunc]
                            # parameter provided, wants to set attribute
                            param = self.parse(0, line, args)[2]
                            self.vars[obj].value[objfunc] = param
                            return param
                    except self.MSN2Exception as e:
                        raise e
                    except:
                        None

                    # # as of 2.0.388,
                    # # if the objfunc ends with '!',
                    # # it becomes destructive
                    # # and returns the object
                    if objfunc.endswith("!"):
                        return FUNCTION_DISPATCH["obj"]["general"]["!"](
                            self, line, args, vname=vname, objfunc=objfunc, mergedargs=mergedargs
                        )

                    if objfunc in FUNCTION_DISPATCH["obj"]["general"]["default"]:
                        return FUNCTION_DISPATCH["obj"]["general"]["default"][objfunc](
                            self, line, args, vname=vname, objfunc=objfunc, mergedargs=mergedargs, 
                            object=object, obj=obj, lines_ran=lines_ran
                        )

                    # variable type specific methods
                    
                    # do the above but without code repetition
                    # check for general functions
                    if _type_object in FUNCTION_DISPATCH["obj"]["general"]:
                        if objfunc in FUNCTION_DISPATCH["obj"]["general"][_type_object]:
                            return FUNCTION_DISPATCH["obj"]["general"][_type_object][objfunc](
                                self, line, args, vname=vname, objfunc=objfunc,
                                object=object, obj=obj, lines_ran=lines_ran
                            )

                    # check for requests_html.HTML
                    elif str(_type_object) in FUNCTION_DISPATCH["obj"]["general"]["class_based"]:
                        if objfunc in FUNCTION_DISPATCH["obj"]["general"]["class_based"][_type_object]:
                            return FUNCTION_DISPATCH["obj"]["general"]["class_based"][_type_object][objfunc](
                                self, line, args, object=object, objfunc=objfunc,
                            )
                        else:
                            return FUNCTION_DISPATCH["obj"]["general"]["class_based"][_type_object]["else"](
                                self, line, args, object=object, objfunc=objfunc
                            )                    

                  
                    # GENERAL METHODS
                    # gets the immediate children of the parent window

                    def children(parent_window):
                        return [
                            self.AppElement(child, child.window_text())
                            for child in window.children()
                        ]

                    # gets a child at an index
                    # prints the children

                    def child(parent_window, index):
                        child = children(parent_window)[index]
                        return self.AppElement(child, child.window_text())

                    # finds a child with subtext in its name

                    def find_children(parent_window, subtext):
                        subtext = subtext.lower()
                        return [
                            self.AppElement(child, child.window_text())
                            for child in window.children()
                            if subtext in child.window_text().lower()
                        ]

                    # recursively searches the child tree for a certain object type
                    # dont allow ElementAmbiguousError

                    def recursive_search(
                        parent_window, type, as_type, object_string_endswith=None
                    ):
                        found = []
                        # get the children
                        # use kwargs to avoid ElementAmbiguousError
                        # kwargs is a criteria to reduce a list by process, class_name, control_type, content_only and/or title.
                        kwargs = {"process": parent_window.process_id()}
                        c = parent_window.children(**kwargs)
                        for child in c:
                            if isinstance(child, type) or (
                                object_string_endswith
                                and str(child).endswith(object_string_endswith)
                            ):
                                found += [as_type(child, child.window_text())]
                            found += recursive_search(
                                child, type, as_type, object_string_endswith
                            )
                        return found

                    # prints all elements

                    def print_elements(parent_window, retrieve_elements):
                        for i, element in enumerate(retrieve_elements(parent_window)):
                            print(i, ":")
                            print(element)
                        return None

                    # finds an element containing the substring specified

                    def find_elements(parent_window, subtext, retrieve_elements):
                        elements = []
                        subtext = subtext.lower()
                        for element in retrieve_elements(parent_window):
                            if subtext in element.name.lower():
                                elements.append(
                                    self.AppElement(element, element.window_text())
                                )
                        return elements

                    # finds the exact elements specified

                    def find_elements_exact(parent_window, text, retrieve_elements):
                        elements = []
                        for element in retrieve_elements(parent_window):
                            if text == element.name:
                                elements.append(
                                    self.AppElement(element, element.window_text())
                                )
                        return elements

                    # waits for the first element to appear containing the substring specified
                    # is not case sensitive

                    def wait_for_element_subtext(
                        parent_window, retrieve_elements, subtext, timeout=None
                    ):
                        subtext = subtext.lower()
                        # subfunction for locating the element

                        def find_element_():
                            try:
                                for element in retrieve_elements(parent_window):
                                    if subtext in element.name.lower():
                                        return self.AppElement(
                                            element, element.window_text()
                                        )
                            except:
                                pass

                        if not timeout:
                            while True:
                                if (_ret := find_element_()) is not None:
                                    return _ret
                        else:
                            import time

                            # get the current time
                            start_time = time.time()
                            # while the time elapsed is less than the timeout
                            while time.time() - start_time < timeout:
                                if (_ret := find_element_()) is not None:
                                    return _ret

                    # waits for the first element to appear with the exact text specified

                    def wait_for_element_exact(
                        parent_window, retrieve_elements, text, timeout=None
                    ):
                        # subfunction for locating the element
                        def find_element_():
                            try:
                                for element in retrieve_elements(parent_window):
                                    if text == element.name:
                                        return self.AppElement(
                                            element, element.window_text()
                                        )
                            except:
                                pass

                        if not timeout:
                            while True:
                                if (_ret := find_element_()) is not None:
                                    return _ret
                        else:
                            import time

                            # get the current time
                            start_time = time.time()
                            # while the time elapsed is less than the timeout
                            while time.time() - start_time < timeout:
                                if (_ret := find_element_()) is not None:
                                    return _ret

                    # waits for the first element to appear in all children containing the substring specified with the type specified

                    def wait_for_type_subtext_all(
                        parent_window, type, as_type, subtext, timeout=None
                    ):
                        return wait_for_element_subtext(
                            parent_window,
                            lambda parent_window: recursive_search(
                                parent_window, type, as_type
                            ),
                            subtext,
                            timeout=timeout,
                        )

                    # wait for the first element to appear in all children with the exact text specified with the type specified

                    def wait_for_type_exact_all(
                        parent_window, type, as_type, text, timeout=None
                    ):
                        return wait_for_element_exact(
                            parent_window,
                            lambda parent_window: recursive_search(
                                parent_window, type, as_type
                            ),
                            text,
                            timeout=timeout,
                        )

                    # waits for a child to exist with text containing subtext

                    def wait_for_text(parent_window, subtext, timeout=None):
                        return wait_for_element_subtext(
                            parent_window, children, subtext, timeout=timeout
                        )

                    # waits for a child to exist in the entire child tree containing subtext

                    def wait_for_text_all(parent_window, subtext, timeout=None):
                        return wait_for_element_subtext(
                            parent_window, all_children, subtext, timeout=timeout
                        )

                    # waits for a child to exist with text exactly equal to text

                    def wait_for_text_exact(parent_window, text, timeout=None):
                        return wait_for_element_exact(
                            parent_window, children, text, timeout=timeout
                        )

                    # waits for a child to exist in the entire child tree with text exactly equal to text

                    def wait_for_text_exact_all(parent_window, text, timeout=None):
                        return wait_for_element_exact(
                            parent_window, all_children, text, timeout=timeout
                        )

                    # prints all children of a parent window

                    def print_children(parent_window):
                        return print_elements(parent_window, children)

                    # gets all children in the child tree
                    def all_children(parent_window):
                        found = []
                        for child in parent_window.children():
                            found.append(self.AppElement(child, child.window_text()))
                            found += all_children(child)
                        return found

                    # prints the child tree of a parent window

                    def print_all_children(parent_window):
                        return print_elements(parent_window, all_children)

                    # gets from all children at an index

                    def all_child(parent_window, index):
                        return all_children(parent_window)[index]

                    # finds all children with subtext in their name

                    def find_all_children(parent_window, subtext):
                        return find_elements(parent_window, subtext, all_children)

                    # finds all children from an exact text

                    def find_all_children_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, all_children)

                    # ---------------------------

                    # NARROWING GENERAL METHODS
                    # recursively gets all menus existing in the parent_window tree
                    # accumulates all instances of pywinauto.controls.uia_controls.MenuWrapper

                    def menus(parent_window):
                        return recursive_search(
                            parent_window,
                            pywinauto.controls.uia_controls.MenuWrapper,
                            self.Menu,
                        )

                    # gets a single menu

                    def menu(parent_window, index):
                        return menus(parent_window)[index]

                    # prints all the menus

                    def print_menus(parent_window):
                        return print_elements(parent_window, menus)

                    # finds a menu with subtext in its name

                    def find_menus(parent_window, subtext):
                        return find_elements(parent_window, subtext, menus)

                    # gets all toolbars
                    def toolbars(parent_window):
                        return recursive_search(
                            parent_window,
                            pywinauto.controls.uia_controls.ToolbarWrapper,
                            self.ToolBar,
                        )

                    def print_toolbars(parent_window):
                        return print_elements(parent_window, toolbars)

                    def toolbar(parent_window, index):
                        return toolbars(parent_window)[index]

                    def find_toolbars(parent_window, subtext):
                        return find_elements(parent_window, subtext, toolbars)

                    # recursively gets all instances of pywinauto.controls.uia_controls.ButtonWrapper
                    def buttons(parent_window):
                        return recursive_search(
                            parent_window,
                            pywinauto.controls.uia_controls.ButtonWrapper,
                            self.Button,
                        )

                    def button(parent_window, index):
                        return buttons(parent_window)[index]

                    def print_buttons(parent_window):
                        return print_elements(parent_window, buttons)

                    def find_buttons(parent_window, subtext):
                        return find_elements(parent_window, subtext, buttons)

                    # for hyperlinks
                    def links(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.Link,
                            object_string_endswith="Hyperlink",
                        )

                    def link(parent_window, index):
                        return links(parent_window)[index]

                    def print_links(parent_window):
                        return print_elements(parent_window, links)

                    def find_links(parent_window, subtext):
                        return find_elements(parent_window, subtext, links)

                    def find_links_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, links)

                    # for tabitems
                    def tabitems(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.TabItem,
                            object_string_endswith="TabItem",
                        )

                    def tabitem(parent_window, index):
                        return tabitems(parent_window)[index]

                    def print_tabitems(parent_window):
                        return print_elements(parent_window, tabitems)

                    def find_tabitems(parent_window, subtext):
                        return find_elements(parent_window, subtext, tabitems)

                    def find_tabitems_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, tabitems)

                    # for tabcontrols
                    def tabcontrols(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.AppElement,
                            object_string_endswith="TabControl",
                        )

                    def tabcontrol(parent_window, index):
                        return tabcontrols(parent_window)[index]

                    def print_tabcontrols(parent_window):
                        return print_elements(parent_window, tabcontrols)

                    def find_tabcontrols(parent_window, subtext):
                        return find_elements(parent_window, subtext, tabcontrols)

                    def find_tabcontrols_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, tabcontrols)

                    # for EditWrapper
                    def inputs(parent_window):
                        return recursive_search(
                            parent_window,
                            pywinauto.controls.uia_controls.EditWrapper,
                            self.Input,
                        )

                    def input(parent_window, index):
                        return inputs(parent_window)[index]

                    def print_inputs(parent_window):
                        return print_elements(parent_window, inputs)

                    def find_inputs(parent_window, subtext):
                        return find_elements(parent_window, subtext, inputs)

                    def find_inputs_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, inputs)

                    # for ButtonWrapper but endswith CheckBox
                    def checkboxes(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.Button,
                            object_string_endswith="CheckBox",
                        )

                    def checkbox(parent_window, index):
                        return checkboxes(parent_window)[index]

                    def print_checkboxes(parent_window):
                        return print_elements(parent_window, checkboxes)

                    def find_checkboxes(parent_window, subtext):
                        return find_elements(parent_window, subtext, checkboxes)

                    def find_checkboxes_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, checkboxes)

                    # for Image
                    def images(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.AppElement,
                            object_string_endswith="Image",
                        )

                    def image(parent_window, index):
                        return images(parent_window)[index]

                    def print_images(parent_window):
                        return print_elements(parent_window, images)

                    def find_images(parent_window, subtext):
                        return find_elements(parent_window, subtext, images)

                    def find_images_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, images)

                    # for Tables
                    def tables(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.Table,
                            object_string_endswith="Table",
                        )

                    def table(parent_window, index):
                        return tables(parent_window)[index]

                    def print_tables(parent_window):
                        return print_elements(parent_window, tables)

                    def find_tables(parent_window, subtext):
                        return find_elements(parent_window, subtext, tables)

                    def find_tables_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, tables)

                    # for GroupBoxes
                    def groupboxes(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.AppElement,
                            object_string_endswith="GroupBox",
                        )

                    def groupbox(parent_window, index):
                        return groupboxes(parent_window)[index]

                    def print_groupboxes(parent_window):
                        return print_elements(parent_window, groupboxes)

                    def find_groupboxes(parent_window, subtext):
                        return find_elements(parent_window, subtext, groupboxes)

                    def find_groupboxes_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, groupboxes)

                    # for Panes
                    def panes(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.AppElement,
                            object_string_endswith="Pane",
                        )

                    def pane(parent_window, index):
                        return panes(parent_window)[index]

                    def print_panes(parent_window):
                        return print_elements(parent_window, panes)

                    def find_panes(parent_window, subtext):
                        return find_elements(parent_window, subtext, panes)

                    def find_panes_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, panes)

                    # for ListItems
                    def listitems(parent_window):
                        return recursive_search(
                            parent_window,
                            pywinauto.controls.uia_controls.ListItemWrapper,
                            self.AppElement,
                            object_string_endswith="ListItem",
                        )

                    def listitem(parent_window, index):
                        return listitems(parent_window)[index]

                    def print_listitems(parent_window):
                        return print_elements(parent_window, listitems)

                    def find_listitems(parent_window, subtext):
                        return find_elements(parent_window, subtext, listitems)

                    def find_listitems_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, listitems)

                    # for documents
                    def documents(parent_window):
                        return recursive_search(
                            parent_window,
                            int,
                            self.AppElement,
                            object_string_endswith="Document",
                        )

                    def document(parent_window, index):
                        return documents(parent_window)[index]

                    def print_documents(parent_window):
                        return print_elements(parent_window, documents)

                    def find_documents(parent_window, subtext):
                        return find_elements(parent_window, subtext, documents)

                    def find_documents_exact(parent_window, text):
                        return find_elements_exact(parent_window, text, documents)

                    # for decendants
                    def descendants(parent_window):
                        return recursive_search(parent_window, int, self.AppElement)

                    # ---------------------------
                    # GENERALIZING METHOD CALLS FOR ELEMENT DISCOVERY

                    def callables(
                        window,
                        # array elements
                        objfunc1,
                        objfunc1_method,
                        # print the elements
                        objfunc2,
                        objfunc2_method,
                        # get a certain element
                        objfunc3,
                        objfunc3_method,
                        # find elements with subtext in their names
                        objfunc4,
                        objfunc4_method,
                        # find elements with exact text in their names
                        objfunc5=None,
                        objfunc5_method=None,
                        # waits for the first element of a certain type with subtext in name
                        objfunc6=None,
                        objfunc6_method=None,
                        type1=None,
                        as_type1=None,
                        # waits for the first element of a certain type with exact text in name
                        objfunc7=None,
                        objfunc7_method=None,
                        type2=None,
                        as_type2=None,
                    ):

                        # RETRIEVING CHILDREN
                        # gets the available child reference keywords
                        if objfunc == objfunc1:
                            return objfunc1_method(window)
                        # prints the children
                        if objfunc == objfunc2:
                            return objfunc2_method(window)
                        # gets a certain child
                        # first argument is the index of the child
                        if objfunc == objfunc3:
                            return objfunc3_method(window, self.parse(0, line, args)[2])
                        # finds children with subtext in their names
                        if objfunc == objfunc4:
                            return objfunc4_method(window, self.parse(0, line, args)[2])
                        if objfunc == objfunc5:
                            return objfunc5_method(window, self.parse(0, line, args)[2])

                        # waits for the first child of a certain type with exact text in its name
                        if objfunc == objfunc6:
                            # if 1 argument, there is no timeout
                            if len(args) == 1:
                                return wait_for_type_exact_all(
                                    window,
                                    type1,
                                    as_type1,
                                    self.parse(0, line, args)[2],
                                )
                            elif len(args) == 2:
                                return wait_for_type_exact_all(
                                    window,
                                    type1,
                                    as_type1,
                                    self.parse(0, line, args)[2],
                                    self.parse(1, line, args)[2],
                                )
                        # waits for the first child of a certain type with subtext in its name
                        if objfunc == objfunc7:
                            # if 1 argument, there is no timeout
                            if len(args) == 1:
                                return wait_for_type_subtext_all(
                                    window,
                                    type2,
                                    as_type2,
                                    self.parse(0, line, args)[2],
                                )
                            elif len(args) == 2:
                                return wait_for_type_subtext_all(
                                    window,
                                    type2,
                                    as_type2,
                                    self.parse(0, line, args)[2],
                                    self.parse(1, line, args)[2],
                                )

                        return "<msnint2 no callable>"

                    # ---------------------------

                    # moves the mouse to the center of an element, and clicks it

                    def clk(window, button="left", waittime=0):
                        import time
                        from pywinauto import mouse

                        # set the focus to this element
                        window.set_focus()
                        # wait for the element to be ready
                        time.sleep(waittime)
                        # get the new coordinates of this element after the focus
                        coords = window.get_properties()["rectangle"].mid_point()
                        # click the mouse
                        mouse.click(button=button, coords=coords)
                        # return the object
                        return object

                    # determines if a point is visible within a rectangle

                    def has_point(object, x, y):
                        try:
                            rect = object.get_properties()["rectangle"]
                            # if implemented
                            return (
                                rect.top <= y <= rect.bottom
                                and rect.left <= x <= rect.right
                            )
                        except:
                            print(str(object))
                            return True

                    # recursively get the first object that has the point
                    # the first object that has the point and no children

                    def rec(root, x, y):
                        # if the root has children
                        if root.children():
                            # for each child
                            for child in root.children():
                                # if the child has the point
                                if has_point(child, x, y):
                                    # return the child
                                    return rec(child, x, y)
                        # if the root has no children
                        else:
                            # return the root
                            return self.AppElement(root, root.window_text())

                    # get all objects that have the point
                    def get_all(root, x, y):
                        all = []
                        # if the root has children
                        if root.children():
                            # for each child
                            for child in root.children():
                                # if the child has the point
                                if has_point(child, x, y):
                                    # add the child to the list
                                    all.append(
                                        self.AppElement(child, child.window_text())
                                    )
                                    # get all of the child's children
                                    all += get_all(child, x, y)
                        # return the list
                        return all

                    # presses multiple keys at the same time
                    def press_simul(kys):
                        sending = ""
                        # keys down
                        for key in kys:
                            sending += "{" + key + " down}"
                        # keys up
                        for key in kys:
                            sending += "{" + key + " up}"
                        return sending

                    # function for converting keys requiring a shift press
                    #   example: a '3' should be converted to {VK_SHIFT down}3{VK_SHIFT up}
                    #   example: a '"' should be converted to {VK_SHIFT down}'{VK_SHIFT up}
                    #   example: a 'E' should be converted to {VK_SHIFT down}e{VK_SHIFT up}
                    # this function is mainly for converting an exerpt of code to a typable
                    # string for pywinauto to type
                    def convert_keys(keystrokes):
                        new = ""
                        special = {
                            "!": "1",
                            "@": "2",
                            "#": "3",
                            "$": "4",
                            "%": "5",
                            "^": "6",
                            "&": "7",
                            "*": "8",
                            "(": "9",
                            ")": "0",
                            "_": "-",
                            "+": "=",
                            "{": "[",
                            "}": "]",
                            "|": "\\",
                            ":": ";",
                            '"': "'",
                            "<": ",",
                            ">": ".",
                            "?": "/",
                            "~": "`",
                            " ": " ",
                        }
                        # for each keystroke
                        for key in keystrokes:
                            if key == " ":
                                # if the key is a space
                                new += " "
                            elif key in special:
                                # if the key is a special character
                                new += (
                                    "{VK_SHIFT down}" + special[key] + "{VK_SHIFT up}"
                                )
                            elif key.isupper():
                                # if the key is uppercase
                                new += "{VK_SHIFT down}" + key.lower() + "{VK_SHIFT up}"
                            else:
                                # if the key is not a special character
                                new += key
                        return new

                    # types keys with a delay between each key

                    def type_keys_with_delay(window, text, delay):
                        e = False
                        import time
                        import pywinauto

                        for char in text:
                            try:
                                window.type_keys(char, with_spaces=True)
                            except:
                                if not e:
                                    window.set_focus()
                                    e = True
                                pywinauto.keyboard.send_keys(char)
                            time.sleep(delay)

                    # parses object functions for discovering types
                    # of elements

                    def search(window):
                        import pywinauto

                        ret = "<msnint2 no callable>"
                        # RETRIEVING CHILDREN
                        # gets the available child reference keywords
                        if (
                            chldrn := callables(
                                window,
                                "children",
                                children,
                                "print_children",
                                print_children,
                                "child",
                                child,
                                "find_children",
                                find_children,
                            )
                        ) != "<msnint2 no callable>":
                            ret = chldrn
                        # working with the entire child tree
                        elif (
                            all_chldrn := callables(
                                window,
                                "all_children",
                                all_children,
                                "print_all_children",
                                print_all_children,
                                "all_child",
                                all_child,
                                "find_all_children",
                                find_all_children,
                                "find_all_children_exact",
                                find_all_children_exact,
                                objfunc6="wait_for_child",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uiawrapper.UIAWrapper,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_child_exact",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uiawrapper.UIAWrapper,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = all_chldrn
                        # getting all menus
                        elif (
                            mns := callables(
                                window,
                                "menus",
                                menus,
                                "print_menus",
                                print_menus,
                                "menu",
                                menu,
                                "find_menus",
                                find_menus,
                                objfunc5=None,
                                objfunc5_method=None,
                                objfunc6="wait_for_menu_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.MenuWrapper,
                                as_type1=self.Menu,
                                objfunc7="wait_for_menu",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.MenuWrapper,
                                as_type2=self.Menu,
                            )
                        ) != "<msnint2 no callable>":
                            ret = mns
                        # gets all toolbars
                        elif (
                            tbrs := callables(
                                window,
                                "toolbars",
                                toolbars,
                                "print_toolbars",
                                print_toolbars,
                                "toolbar",
                                toolbar,
                                "find_toolbars",
                                find_toolbars,
                                objfunc5=None,
                                objfunc5_method=None,
                                objfunc6="wait_for_toolbar_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.ToolbarWrapper,
                                as_type1=self.ToolBar,
                                objfunc7="wait_for_toolbar",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.ToolbarWrapper,
                                as_type2=self.ToolBar,
                            )
                        ) != "<msnint2 no callable>":
                            ret = tbrs
                        # gets all buttons
                        elif (
                            btns := callables(
                                window,
                                "buttons",
                                buttons,
                                "print_buttons",
                                print_buttons,
                                "button",
                                button,
                                "find_buttons",
                                find_buttons,
                                objfunc5=None,
                                objfunc5_method=None,
                                objfunc6="wait_for_button_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.ButtonWrapper,
                                as_type1=self.Button,
                                objfunc7="wait_for_button",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.ButtonWrapper,
                                as_type2=self.Button,
                            )
                        ) != "<msnint2 no callable>":
                            ret = btns
                        # gets all tabitems
                        elif (
                            tbs := callables(
                                window,
                                "tabitems",
                                tabitems,
                                "print_tabitems",
                                print_tabitems,
                                "tabitem",
                                tabitem,
                                "find_tabitems",
                                find_tabitems,
                                objfunc5=None,
                                objfunc5_method=None,
                                objfunc6="wait_for_tabitem_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.TabItem,
                                objfunc7="wait_for_tabitem",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.TabItem,
                            )
                        ) != "<msnint2 no callable>":
                            ret = tbs
                        # gets all links
                        elif (
                            lnks := callables(
                                window,
                                "links",
                                links,
                                "print_links",
                                print_links,
                                "link",
                                link,
                                "find_links",
                                find_links,
                                objfunc5=None,
                                objfunc5_method=None,
                                objfunc6="wait_for_link_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.Hyperlink,
                                objfunc7="wait_for_link",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.Hyperlink,
                            )
                        ) != "<msnint2 no callable>":
                            ret = lnks
                        # gets all Inputs
                        elif (
                            inpts := callables(
                                window,
                                "inputs",
                                inputs,
                                "print_inputs",
                                print_inputs,
                                "input",
                                input,
                                "find_inputs",
                                find_inputs,
                                objfunc6="wait_for_input_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.EditWrapper,
                                as_type1=self.Input,
                                objfunc7="wait_for_input",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.EditWrapper,
                                as_type2=self.Input,
                            )
                        ) != "<msnint2 no callable>":
                            ret = inpts
                        # gets all checkboxes
                        elif (
                            chks := callables(
                                window,
                                "checkboxes",
                                checkboxes,
                                "print_checkboxes",
                                print_checkboxes,
                                "checkbox",
                                checkbox,
                                "find_checkboxes",
                                find_checkboxes,
                                objfunc6="wait_for_checkbox_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.ButtonWrapper,
                                as_type1=self.Button,
                                objfunc7="wait_for_checkbox",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.ButtonWrapper,
                                as_type2=self.Button,
                            )
                        ) != "<msnint2 no callable>":
                            ret = chks
                        # gets all images
                        elif (
                            imgs := callables(
                                window,
                                "images",
                                images,
                                "print_images",
                                print_images,
                                "image",
                                image,
                                "find_images",
                                find_images,
                            )
                        ) != "<msnint2 no callable>":
                            ret = imgs
                        # gets all tables
                        elif (
                            tbls := callables(
                                window,
                                "tables",
                                tables,
                                "print_tables",
                                print_tables,
                                "table",
                                table,
                                "find_tables",
                                find_tables,
                                objfunc6="wait_for_table_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.ListViewWrapper,
                                as_type1=self.Table,
                                objfunc7="wait_for_table",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.ListViewWrapper,
                                as_type2=self.Table,
                            )
                        ) != "<msnint2 no callable>":
                            ret = tbls
                        # get all GroupBoxes
                        elif (
                            grps := callables(
                                window,
                                "groupboxes",
                                groupboxes,
                                "print_groupboxes",
                                print_groupboxes,
                                "groupbox",
                                groupbox,
                                "find_groupboxes",
                                find_groupboxes,
                                objfunc6="wait_for_groupbox_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_groupbox",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = grps
                        # for Panes
                        elif (
                            pns := callables(
                                window,
                                "panes",
                                panes,
                                "print_panes",
                                print_panes,
                                "pane",
                                pane,
                                "find_panes",
                                find_panes,
                                objfunc6="wait_for_pane_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_pane",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = pns
                        # for ListItems
                        elif (
                            lsts := callables(
                                window,
                                "listitems",
                                listitems,
                                "print_listitems",
                                print_listitems,
                                "listitem",
                                listitem,
                                "find_listitems",
                                find_listitems,
                                objfunc6="wait_for_listitem_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=pywinauto.controls.uia_controls.ListItemWrapper,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_listitem",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=pywinauto.controls.uia_controls.ListItemWrapper,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = lsts
                        # for TabControls
                        elif (
                            tabs := callables(
                                window,
                                "tabcontrols",
                                tabcontrols,
                                "print_tabcontrols",
                                print_tabcontrols,
                                "tabcontrol",
                                tabcontrol,
                                "find_tabcontrols",
                                find_tabcontrols,
                                objfunc6="wait_for_tabcontrol_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_tabcontrol",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = tabs
                        # for Documents
                        elif (
                            docs := callables(
                                window,
                                "documents",
                                documents,
                                "print_documents",
                                print_documents,
                                "document",
                                document,
                                "find_documents",
                                find_documents,
                                objfunc6="wait_for_document_exact",
                                objfunc6_method=wait_for_type_exact_all,
                                type1=int,
                                as_type1=self.AppElement,
                                objfunc7="wait_for_document",
                                objfunc7_method=wait_for_type_subtext_all,
                                type2=int,
                                as_type2=self.AppElement,
                            )
                        ) != "<msnint2 no callable>":
                            ret = docs
                        return ret

                    # if the object is a pywinauto application
                    # KNOWN ISSUES:
                    #   - I've tested this on a Windows 11 laptop and it doesn't
                    #     work for some reason
                    if isinstance(object, self.App):
                        # return for an app
                        ret = object
                        # path to the application to work with
                        path = object.path
                        # actual pwinauto application object
                        app = object.application
                        # window
                        window = app.window() if app else None
                        # thread based operation
                        p_thread = False
                        if objfunc.endswith(":lock"):
                            p_thread = True
                            objfunc = objfunc[:-5]
                            auto_lock.acquire()
                        # element discovery with search()
                        if (srch := search(window)) != "<msnint2 no callable>":
                            ret = srch
                        # STARTING AND STOPPING APPLICATIONS
                        if objfunc == "start":
                            from pywinauto.application import Application

                            # create and start the application
                            if not object.application:
                                object.application = Application(backend="uia").start(
                                    path
                                )
                            # add to global apps
                            global apps
                            apps[len(apps) + 1] = object
                            ret = object.application
                        # kills the application
                        elif (
                            objfunc == "stop" or objfunc == "kill" or objfunc == "close"
                        ):
                            # kill the application
                            ret = app.kill()
                        # gets the top_window
                        elif objfunc == "print_tree":
                            ret = app.dump_tree()
                        # gets a connection to this application
                        elif objfunc == "connection":
                            from pywinauto.application import Application

                            ret = self.App(
                                object.path,
                                Application(backend="uia").connect(
                                    process=object.application.process
                                ),
                            )
                        # gets information about this application
                        # gets the text of the window
                        elif objfunc == "text":
                            ret = window.window_text()
                        # gets the window
                        elif objfunc == "window":
                            ret = window
                        # gets the handle
                        elif objfunc == "handle":
                            ret = window.handle
                        # chrome based children collection

                        def chrome_children_():
                            chrome_window = app.window(title_re=".*Chrome.")
                            chrome_handle = chrome_window.handle
                            wd = app.window(handle=chrome_handle)
                            document = wd.child_window(
                                found_index=0, class_name="Chrome_RenderWidgetHostHWND"
                            )
                            return document.descendants()

                        # GOOGLE CHROME ONLY
                        if objfunc == "chrome_children":
                            # if not arguments
                            if args[0][0] == "":
                                ret = chrome_children_()
                            # if one argument, check if the first argument is contained
                            elif len(args) == 1:
                                subtext = self.parse(0, line, args)[2].lower()
                                # subtext must be str
                                self.type_err([(subtext, (str,))], line, lines_ran)
                                ret = [
                                    self.AppElement(d, d.window_text())
                                    for d in chrome_children_()
                                    if subtext in d.window_text().lower()
                                ]
                            # if two arguments, check if the first argument is exact
                            elif len(args) == 2:
                                subtext = self.parse(0, line, args)[2]
                                # subtext must be str
                                self.type_err([(subtext, (str,))], line, lines_ran)
                                ret = [
                                    self.AppElement(d, d.window_text())
                                    for d in chrome_children_()
                                    if subtext == d.window_text()
                                ]

                        # waits for a child containing text
                        elif objfunc == "wait_for_text":
                            # if no timeout provided
                            if len(args) == 1:
                                txt = self.parse(0, line, args)[2]
                                # text should be str
                                self.type_err([(txt, (str,))], line, lines_ran)
                                ret = wait_for_text(window, txt)
                            # if timeout provided
                            elif len(args) == 2:
                                txt = self.parse(0, line, args)[2]
                                timeout = self.parse(1, line, args)[2]
                                # text should be str and timeout should be float or int or complex
                                self.type_err(
                                    [(txt, (str,)), (timeout, (float, int, complex))],
                                    line,
                                    lines_ran,
                                )
                                ret = wait_for_text(window, txt, timeout=timeout)
                        # waits for a child containing text in the entire child tree
                        elif objfunc == "wait_for_text_all":
                            # if no timeout provided
                            if len(args) == 1:
                                txt = self.parse(0, line, args)[2]
                                # text should be str
                                self.type_err([(txt, (str,))], line, lines_ran)
                                ret = wait_for_text_all(window, txt)
                            elif len(args) == 2:
                                txt = self.parse(0, line, args)[2]
                                timeout = self.parse(1, line, args)[2]
                                # text should be str and timeout should be float or int or complex
                                self.type_err(
                                    [(txt, (str,)), (timeout, (float, int, complex))],
                                    line,
                                    lines_ran,
                                )
                                ret = wait_for_text_all(window, txt, timeout=timeout)

                        # waits for a child containing the exact text
                        elif objfunc == "wait_for_text_exact":
                            # if no timeout provided
                            if len(args) == 1:
                                txt = self.parse(0, line, args)[2]
                                # text should be str
                                self.type_err([(txt, (str,))], line, lines_ran)
                                ret = wait_for_text_exact(window, txt)
                            elif len(args) == 2:
                                txt = self.parse(0, line, args)[2]
                                timeout = self.parse(1, line, args)[2]
                                # text should be str and timeout should be float or int or complex
                                self.type_err(
                                    [(txt, (str,)), (timeout, (float, int, complex))],
                                    line,
                                    lines_ran,
                                )
                                ret = wait_for_text_exact(window, txt, timeout=timeout)
                        # waits for a child containing the exact text in the entire child tree
                        elif objfunc == "wait_for_text_exact_all":
                            # if no timeout provided
                            if len(args) == 1:
                                txt = self.parse(0, line, args)[2]
                                # text should be str
                                self.type_err([(txt, (str,))], line, lines_ran)
                                ret = wait_for_text_exact_all(window, txt)
                            elif len(args) == 2:
                                txt = self.parse(0, line, args)[2]
                                timeout = self.parse(1, line, args)[2]
                                # text should be str and timeout should be float or int or complex
                                self.type_err(
                                    [(txt, (str,)), (timeout, (float, int, complex))],
                                    line,
                                    lines_ran,
                                )
                                ret = wait_for_text_exact_all(
                                    window, txt, timeout=timeout
                                )

                        # APPLICATION ACTIONS
                        # sends keystrokes to the application
                        # takes one argument, being the keystrokes to send
                        elif objfunc == "write":
                            writing = self.parse(0, line, args)[2]
                            # writing should be a str
                            self.type_err([(writing, (str,))], line, lines_ran)
                            try:
                                # sends keystrokes to the application
                                ret = window.type_keys(writing, with_spaces=True)
                            except:
                                # with_spaces not allowed
                                ret = window.type_keys(
                                    convert_keys(writing), with_spaces=True
                                )
                        # writes special characters into the console
                        # takes one argument, being the special characters to write
                        elif objfunc == "write_special":
                            # keystrokes
                            keystrokes = self.parse(0, line, args)[2]
                            # keystrokes should be a str
                            self.type_err([(keystrokes, (str,))], line, lines_ran)
                            # convert to special characters
                            ret = window.type_keys(
                                convert_keys(keystrokes), with_spaces=True
                            )

                        # presses keys at the same time
                        elif objfunc == "press":
                            kys = []
                            for i in range(len(args)):
                                kys.append(self.parse(i, line, args)[2])
                            # presses the keys at the same time
                            ret = window.type_keys(press_simul(kys))
                        # sends keystrokes to the application
                        # takes one argument, being the keystrokes to send
                        elif objfunc == "send_keys":
                            # import pywinauto.keyboard
                            import pywinauto

                            keystrokes = self.parse(0, line, args)[2]
                            # keystrokes should be a str
                            self.type_err([(keystrokes, (str,))], line, lines_ran)
                            # sends keystrokes to the application
                            ret = pywinauto.keyboard.send_keys(
                                convert_keys(keystrokes), with_spaces=True
                            )

                        # gets the element that is currently hovered over
                        # recurses through all children, determining which elements have
                        # the mouses position
                        elif objfunc == "hovered":
                            # import win32api
                            import win32api

                            # get the root window of this application
                            root = window.top_level_parent()
                            # get the current mouse position
                            x, y = win32api.GetCursorPos()
                            # recursively find all children from the root window
                            # that have the point specified
                            ret = get_all(root, x, y)
                        # opens the developer tools
                        elif objfunc == "inspect":
                            # presses the shortcut keys to open the developer tools
                            ret = window.type_keys("{F12}")
                            # waits for the inspect window to appear
                            wait_for_text_all(window, "Console")
                        # closes the developer tools
                        elif objfunc == "close_inspect":
                            # presses the shortcut keys to close the developer tools
                            ret = window.type_keys("{F12}")
                        # refreshes the page
                        elif objfunc == "refresh":
                            # presses the shortcut keys to refresh the page
                            ret = window.type_keys("{F5}")
                        # presses the enter key
                        elif objfunc == "enter":
                            # presses the enter key
                            ret = window.type_keys("{ENTER}")
                        # presses the escape key
                        elif objfunc == "escape":
                            # presses the escape key
                            ret = window.type_keys("{ESC}")
                        # page down
                        elif objfunc == "page_down":
                            # presses the page down key
                            ret = window.type_keys("{PGDN}")
                        # page up
                        elif objfunc == "page_up":
                            # presses the page up key
                            ret = window.type_keys("{PGUP}")
                        # release auto_lock
                        if p_thread:
                            auto_lock.release()
                        # return the object
                        return ret

                    # if the object is a pywinauto window element
                    elif isinstance(object, self.AppElement):
                        # returning
                        ret = object
                        # get the window of the AppElement object
                        window = object.window
                        # get the text of the AppElement object
                        name = object.name
                        # function to move the mouse from start to end,
                        # with a speed of speed

                        def movemouse(start, end, speed):
                            import time
                            from pywinauto import mouse

                            # reverse the speed, so a speed of 50 gives
                            # end_range of 50, and a speed of 75 gives
                            # end_range of 25
                            # dragging the mouse
                            # presses the mouse down at the coordinates
                            mouse.press(coords=start)
                            end_range = 100 - speed
                            for i in range(0, end_range):
                                mouse.move(
                                    coords=(
                                        int(start[0] + (end[0] - start[0]) / 100 * i),
                                        int(start[1] + (end[1] - start[1]) / 100 * i),
                                    )
                                )
                                time.sleep(0.001)

                            # releases the mouse at the end coordinates
                            mouse.release(coords=end)

                        p_thread = False
                        # thread based functions
                        # if the function is a thread based function
                        if objfunc.endswith(":lock"):
                            p_thread = True
                            auto_lock.acquire()
                            objfunc = objfunc[:-5]
                        # OBTAINING DIFFERENT TYPES OF CHILDREN
                        # get the element window
                        if objfunc == "window":
                            ret = window
                        # element discovery with search()
                        if (srch := search(window)) != "<msnint2 no callable>":
                            ret = srch
                        # getting information about the current window
                        # gets the window text
                        elif objfunc == "text":
                            ret = window.window_text()
                        # GETTING LOCATION OF THE WINDOW
                        elif objfunc == "top":
                            ret = window.get_properties()["rectangle"].top
                        elif objfunc == "bottom":
                            ret = window.get_properties()["rectangle"].bottom
                        elif objfunc == "left":
                            ret = window.get_properties()["rectangle"].left
                        elif objfunc == "right":
                            ret = window.get_properties()["rectangle"].right
                        elif objfunc == "center" or objfunc == "mid_point":
                            ret = window.get_properties()["rectangle"].mid_point()
                        # getting the rectangle overall
                        elif objfunc == "rectangle":
                            ret = [
                                window.get_properties()["rectangle"].top,
                                window.get_properties()["rectangle"].bottom,
                                window.get_properties()["rectangle"].left,
                                window.get_properties()["rectangle"].right,
                            ]
                        # computes the diameter of the window
                        elif objfunc == "width":
                            try:
                                left = window.get_properties()["rectangle"].left
                                right = window.get_properties()["rectangle"].right
                                ret = right - left
                            except:
                                ret = None
                        # computes the height of the window
                        elif objfunc == "height":
                            try:
                                top = window.get_properties()["rectangle"].top
                                bottom = window.get_properties()["rectangle"].bottom
                                ret = bottom - top
                            except:
                                ret = None
                        # getting adjacent elements
                        # could or could not be decendants
                        # operation is very slow, should be used mainly
                        # for element discovery
                        elif objfunc == "element_above":
                            from pywinauto import mouse

                            # pixels above
                            pixels = self.parse(0, line, args)[2]
                            # pixels should be int
                            self.type_err([(pixels, (int,))], line, lines_ran)
                            # get the root window of this application
                            root = object.top_level_parent()
                            # get the top middle point of this element
                            top = object.get_properties()["rectangle"].top - pixels
                            mid = object.get_properties()["rectangle"].mid_point()[0]
                            # if there exist two arguments, move the mouse to that location
                            if len(args) == 2:
                                mouse.move(coords=(mid, top))
                            # recursively find all children from the root window
                            # that have the point specified
                            ret = rec(root, mid, top)
                        elif objfunc == "element_below":
                            from pywinauto import mouse

                            # pixels above
                            pixels = self.parse(0, line, args)[2]
                            # pixels should be int
                            self.type_err([(pixels, (int,))], line, lines_ran)
                            # get the root window of this application
                            root = object.top_level_parent()
                            # get the top middle point of this element
                            bottom = (
                                object.get_properties()["rectangle"].bottom + pixels
                            )
                            mid = object.get_properties()["rectangle"].mid_point()[0]
                            if len(args) == 2:
                                mouse.move(coords=(mid, bottom))
                            # recursively find all children from the root window
                            # that have the point specified
                            ret = rec(root, mid, bottom)
                        elif objfunc == "element_left":
                            from pywinauto import mouse

                            # pixels to the left
                            pixels = self.parse(0, line, args)[2]
                            # pixels should be int
                            self.type_err([(pixels, (int,))], line, lines_ran)
                            # get the root window of this application
                            root = object.top_level_parent()
                            # get the left middle point of this element
                            left = object.get_properties()["rectangle"].left - pixels
                            mid = object.get_properties()["rectangle"].mid_point()[1]
                            if len(args) == 2:
                                mouse.move(coords=(left, mid))
                            # recursively find all children from the root window
                            # that have the point specified
                            ret = rec(root, left, mid)
                        elif objfunc == "element_right":
                            from pywinauto import mouse

                            # pixels to the right
                            pixels = self.parse(0, line, args)[2]
                            # pixels should be int
                            self.type_err([(pixels, (int,))], line, lines_ran)
                            # get the root window of this application
                            root = object.top_level_parent()
                            # get the right middle point of this element
                            right = object.get_properties()["rectangle"].right + pixels
                            mid = object.get_properties()["rectangle"].mid_point()[1]
                            if len(args) == 2:
                                mouse.move(coords=(right, mid))
                            # recursively find all children from the root window
                            # that have the point specified
                            ret = rec(root, right, mid)
                        # focus on the window
                        elif objfunc == "focus":
                            ret = window.set_focus()
                        # scrolls to the window
                        elif objfunc == "scroll":
                            from pywinauto import mouse

                            ret = mouse.scroll(
                                coords=(
                                    window.get_properties()["rectangle"].mid_point()[0],
                                    window.get_properties()["rectangle"].mid_point()[1],
                                )
                            )
                        # drags this element to either another AppElement
                        elif objfunc == "drag":
                            # if one argument and that argument isinstance(AppElement)
                            first = self.parse(0, line, args)[2]
                            # first should be AppElement
                            self.type_err(
                                [(first, (self.AppElement,))], line, lines_ran
                            )
                            # midpoint of the element to drag to
                            start = (
                                window.get_properties()["rectangle"].mid_point()[0],
                                window.get_properties()["rectangle"].mid_point()[1],
                            )
                            end = (
                                first.get_properties()["rectangle"].mid_point()[0],
                                first.get_properties()["rectangle"].mid_point()[1],
                            )

                            # slowly moves the mouse to the end coordinates
                            # this is to prevent the mouse from moving too fast
                            # and not dragging the object
                            # the farther the distance, the longer it takes
                            # to move the mouse
                            speed = 50
                            if len(args) == 2:
                                speed = self.parse(1, line, args)[2]
                                # speed should be int
                                self.type_err([(speed, (int,))], line, lines_ran)
                            # drags the mouse
                            movemouse(start, end, speed)
                            ret = True
                        # drags this AppElement to coordinates
                        elif objfunc == "drag_coords":
                            start = (
                                window.get_properties()["rectangle"].mid_point()[0],
                                window.get_properties()["rectangle"].mid_point()[1],
                            )
                            startcoord = self.parse(0, line, args)[2]
                            endcoord = self.parse(1, line, args)[2]
                            # both startcoord and endcoord should be int
                            self.type_err(
                                [(startcoord, (int,)), (endcoord, (int,))],
                                line,
                                lines_ran,
                            )
                            end = (startcoord, endcoord)
                            # gets the speed, if specified
                            speed = 50
                            if len(args) == 3:
                                speed = self.parse(2, line, args)[2]
                                # speed should be int
                                self.type_err([(speed, (int,))], line, lines_ran)
                            # drags the mouse
                            movemouse(start, end, speed)
                            ret = True
                        # WINDOW ACTIONS
                        # sends keystrokes to the application
                        # takes one argument, being the keystrokes to send
                        elif objfunc == "write":
                            writing = self.parse(0, line, args)[2]
                            # writing should be str
                            self.type_err([(writing, (str,))], line, lines_ran)
                            timeout = False
                            # if a timeout between keystrokes is offered
                            if len(args) == 2:
                                timeout = True
                            if timeout:
                                delay = self.parse(1, line, args)[2]
                                # delay should be float or int or complex
                                self.type_err(
                                    [(delay, (float, int, complex))], line, lines_ran
                                )
                                ret = type_keys_with_delay(window, writing, delay)
                            else:
                                try:
                                    ret = window.type_keys(writing, with_spaces=True)
                                except:
                                    window.set_focus()
                                    ret = window.type_keys(writing)
                        # presses backspace
                        # if no arguments, presses it one time
                        # else, presses it the first argument many times
                        if objfunc == "backspace":
                            window.set_focus()
                            # no argument
                            if args[0][0] == "":
                                ret = window.type_keys("{BACKSPACE}")
                            # else, send {BACKSPACE} that many times
                            else:
                                times = self.parse(0, line, args)[2]
                                # times should be int
                                self.type_err([(times, (int,))], line, lines_ran)
                                ret = window.type_keys("{BACKSPACE}" * times)
                        # presses the enter key
                        elif objfunc == "enter":
                            ret = window.type_keys("{ENTER}")
                        # hovers over the window
                        elif objfunc == "hover":
                            from pywinauto import mouse

                            # hovers the mouse over the window, using the mid point of the element
                            ret = mouse.move(
                                coords=(
                                    window.get_properties()["rectangle"].mid_point()
                                )
                            )
                        # different types of AppElements
                        # if the appelement is a button
                        if isinstance(object, self.Button):
                            # clicks the button
                            if objfunc == "click" or objfunc == "left_click":
                                ret = object.click()
                            # left clicks the button
                            elif objfunc == "right_click":
                                ret = object.right_click()
                            ret = object
                        # working with Links
                        elif isinstance(object, self.Link):
                            waittime = (
                                self.parse(0, line, args)[2] if args[0][0] != "" else 1
                            )

                            # waittime should be float or int or complex
                            self.type_err(
                                [(waittime, (float, int, complex))], line, lines_ran
                            )
                            # clicks the link
                            if objfunc == "click" or objfunc == "left_click":
                                ret = clk(window, waittime=waittime)
                            # right clicks the link
                            elif objfunc == "right_click":
                                ret = clk(window, button="right", waittime=waittime)
                            ret = object
                        # working with Tables
                        elif isinstance(object, self.Table):
                            # get table
                            table = object.window
                            # gets a row by index, based on the above logic

                            def row(index):
                                row = []
                                items = []
                                try:
                                    cols = table.column_count()
                                except NotImplementedError:
                                    # not implemented
                                    cols = 5
                                    items = table.items()
                                for i in range(cols):
                                    try:
                                        try:
                                            wrapper = table.cell(row=index, column=i)
                                        except:
                                            # table.items() gets a 1D list of items,
                                            # compute the index of the item
                                            # based on 'i' and 'index'
                                            wrapper = items[i + index * cols]

                                        row.append(
                                            self.AppElement(
                                                wrapper, wrapper.window_text()
                                            )
                                        )
                                    except:
                                        break
                                return row

                            # gets a column by index

                            def col(index):
                                col = []
                                for i in range(table.column_count()):
                                    try:
                                        wrapper = table.cell(row=i, column=index)
                                        col.append(
                                            self.AppElement(
                                                wrapper, wrapper.window_text()
                                            )
                                        )
                                    except:
                                        break
                                return col

                            # gets a cell at a row and column
                            if objfunc == "get":
                                # get column
                                col = self.parse(0, line, args)[2]
                                # get row
                                row = self.parse(1, line, args)[2]
                                # column and row should be int
                                self.type_err(
                                    [(col, (int,)), (row, (int,))], line, lines_ran
                                )
                                wrapper = table.cell(row=row, column=col)
                                # gets the cell
                                ret = self.AppElement(wrapper, wrapper.window_text())
                            # try to accumulate all the rows
                            # up to sys.maxsize
                            elif objfunc == "matrix":
                                import sys

                                matrix = []
                                for i in range(sys.maxsize):
                                    try:
                                        if _r := row(i):
                                            matrix.append(_r)
                                        else:
                                            break
                                    except:
                                        break
                                ret = matrix
                            # gets a row
                            elif objfunc == "row":
                                ind = self.parse(0, line, args)[2]
                                # ind should be int
                                self.type_err([(ind, (int,))], line, lines_ran)
                                ret = row(ind)
                            # gets a column
                            elif objfunc == "column":
                                ind = self.parse(0, line, args)[2]
                                # ind should be int
                                self.type_err([(ind, (int,))], line, lines_ran)
                                ret = col(ind)
                        # working with ToolBars
                        elif isinstance(object, self.ToolBar):
                            toolbar_window = object.window
                            # gets the buttons of the toolbar
                            if objfunc == "buttons":
                                ret = [
                                    toolbar_window.button(i)
                                    for i in range(toolbar_window.button_count())
                                ]
                            # prints the buttons of this toolbar
                            if objfunc == "print_buttons":
                                for i in range(toolbar_window.button_count()):
                                    print(i, ":", toolbar_window.button(i))
                                ret = None
                            # gets a button at an index
                            if objfunc == "button":
                                ret = toolbar_window.button(
                                    self.parse(0, line, args)[2]
                                )
                            # finds all buttons with subtext in their names
                            if objfunc == "find_buttons":
                                txt = self.parse(0, line, args)[2]
                                # txt should be str
                                self.type_err([(txt, (str,))], line, lines_ran)
                                ret = find_buttons(toolbar_window, txt)
                            ret = object
                        # working with scrollbars
                        elif isinstance(object, self.ScrollBar):
                            scrollbar_window = object.window
                            if objfunc == "scroll_down":
                                ret = scrollbar_window.scroll_down(
                                    amount="page", count=1
                                )
                        # extra methods such that this AppElement requires different logic
                        if objfunc == "click" or objfunc == "left_click":
                            waittime = (
                                self.parse(0, line, args)[2] if args[0][0] != "" else 1
                            )
                            # waittime must be float or int or complex
                            self.type_err(
                                [(waittime, (float, int, complex))], line, lines_ran
                            )
                            ret = clk(window, waittime=waittime)
                        elif objfunc == "right_click":
                            waittime = (
                                self.parse(0, line, args)[2] if args[0][0] != "" else 1
                            )
                            # waittime must be float or int or complex
                            self.type_err(
                                [(waittime, (float, int, complex))], line, lines_ran
                            )
                            ret = clk(window, button="right", waittime=waittime)
                        # if thread based, release the lock
                        if p_thread:
                            auto_lock.release()
                        return ret


                # user function execution requested
                # user functions take priority
                # over general msn2 functions
                if func in self.methods:
                    from core.functions import user_function_exec
                    return user_function_exec(inst, lines_ran)

                # the  belowconditions interpret a line based on initial appearances
                # beneath these conditions will the Interpreter then parse the arguments from the line as a method call
                # request for Interpreter redirect to a block of code
                elif func in FUNCTION_DISPATCH:
                    return FUNCTION_DISPATCH[func](
                        self, line, args, 
                        inst=inst, lines_ran=lines_ran, total_ints=total_ints, msn2_none=msn2_none, 
                        macros=macros, postmacros=postmacros, enclosed=enclosed, syntax=syntax, python_alias=python_alias,
                        auxlock=auxlock, pointer_lock=pointer_lock
                    )
                    
                # for objects
                elif obj in FUNCTION_DISPATCH["obj"]:
                    if objfunc in FUNCTION_DISPATCH["obj"][obj]:
                        return FUNCTION_DISPATCH["obj"][obj][objfunc](
                            self, line, args, 
                            inst=inst, lines_ran=lines_ran, total_ints=total_ints, lock=lock
                        )
                    else:
                        return FUNCTION_DISPATCH["obj"][obj]["else"](
                            self, line, args,
                            inst=inst, lines_ran=lines_ran, total_ints=total_ints, objfunc=objfunc
                        )

                # # object instance requested
                # # if the function is in the variables
                # # and the variable is a class
                elif func in self.vars and isinstance(self.vars[func].value, dict):
                    return FUNCTION_DISPATCH["obj"]["instance"]["new"](
                        self, line, args, func=func
                    )

                # quicker conditional operator as functional prefix
                elif len(func) > 0 and func[0] == "?":
                    return FUNCTION_DISPATCH["special"]["?"](
                        self, line, args, func=func
                    )
                    
                # inline function, takes any amount of instructions
                # returns the result of the last instruction
                elif func == "" and objfunc == "":
                    return FUNCTION_DISPATCH["=>"](self, line, args, inst=inst)
                # if the function, when parsed, is an integer,
                # then it is a loop that runs func times
                elif (_i := self.get_int(func)) != None:
                    return FUNCTION_DISPATCH["special"]["intloop"](
                        self, line, args, _i=_i
                    )

                # if the function is a variable name
                elif func in self.vars:
                    return FUNCTION_DISPATCH["special"]["varloop"](
                        self, line, args, func=func
                    )
                
                
                # functional syntax for easier to write loops
                # cannot receive non literal or variable arguments, or any expression containing a '|'
                # syntax:     3|5|i (prnt(i))
                # prnts 3\n4\n5
                # Functional syntax for loops like 3|5|i (prnt(i))
                # elif func.count("|") == 2:
                elif (_func_split := func.split("|")) and len(_func_split) == 3:
                    start = self.interpret(_func_split[0])
                    end = self.interpret(_func_split[1])
                    loopvar = _func_split[2]
                    
                    self.vars[loopvar] = Var(loopvar, start)
                    
                    step = 1 if start < end else -1

                    for i in range(start, end, step):
                        self.vars[loopvar].value = i
                        self.interpret(args[0][0])
                    return
                # fallback
                else:
                    try:
                        line = self.replace_vars2(line)
                        return eval(line, {}, {})
                    except:
                        # maybe its a variable?
                        try:
                            return self.vars[line].value
                        except:
                            # ok hopefully its a string lol
                            return line
            if obj != "":
                objfunc += c
            else:
                func += c
        # try a variable
        if line in self.vars:
            return self.vars[line].value
        # otherwise nothing
        # try replacing variables 2
        try:
            line = self.replace_vars2(line)
        except:
            pass
        # get value of line
        try:
            return eval(line, {}, {})
        except:
            try:
                return eval(str(self.replace_vars(line)), {}, {})
            except:
                return None

    # adds a new program wide syntax

    def add_syntax(self, token, between, function):
        syntax[token] = [between, function]
        return [token, between, function]

    # exporting error messages
    def export_err(self, vname, line):
        if vname not in self.vars:
            return self.err(
                f"Error exporting variable",
                f'Variable "{vname}" does not exist in this context.',
                line,
                lines_ran,
            )

    # class specific error messages
    def no_var_err(self, _vn, _type, _and, _locals, line):
        return self.err(
            f'No {_type} variable "{_vn}" found in Python environment',
            f'Current {_and} environment locals:\n{self._locals}\nsize: {len(self._locals)}\nsee globals with "py.globals()"',
            line,
            lines_ran,
        )

    # ls: long string
    def ls(self, args):
        return self.msn2_replace(",".join([str(arg[0]) for arg in args]).strip())

    # replaces tokens in the string with certain
    # characters or values
    # TODO: implement linear interpretation
    def msn2_replace(self, script):
        # Define the replacements
        replacements = {
            "<tag>": "#",
            "<nl>": "\n",
            # deep newline
            "<dnl>": "\\n",
            "<rp>": ")",
            "<lp>": "(",
            "<rb>": "]",
            "<lb>": "[",
            "<rcb>": "}",
            "<lcb>": "{",
            "(,)": ",",
            "<or>": "||",
            "< >": " ",
            "<lt>": "<",
            "<gt>": ">",
        }

        # do the above but faster and more efficient
        for key in replacements:
            script = script.replace(key, replacements[key])

        # replaces whats in between the tags
        # with the interpretation of whats between the tags
        #
        # interpretation  is with self.interpret(script)
        #
        # script(
        #     {='hello1'=}

        #     {=
        #         cat('hello',
        #             {='hi there'=}
        #         )
        #     =}
        # )
        #
        def recurse_tags(scr, force_string=False):
            # get the first tag
            # if there is no tag, return the script
            if (first := scr.find(tag)) == -1:
                return scr
            # find the matching end tag
            stack = []
            i = first + len(tag)
            while i < len(scr):
                if scr[i:i + len(endtag)] == endtag:
                    if len(stack) == 0:
                        break
                    stack.pop()
                    i += len(endtag)
                elif scr[i:i + len(tag)] == tag:
                    stack.append(tag)
                    i += len(tag)
                else:
                    i += 1
            # recursively interpret the code between the tags
            interpreted_code = self.interpret(recurse_tags(scr[first + len(tag) : i]))
            if force_string:
                interpreted_code = f'"{interpreted_code}"'
            new_scr = f"{scr[:first]}{interpreted_code}{scr[i+len(endtag):]}"
            # recursively continue replacing tags in the remaining script
            return recurse_tags(new_scr)

        # applying '{=' '=}' tags
        tag = "{="
        endtag = "=}"
        with_msn2 = recurse_tags(script)
        return with_msn2

    #

    # determines the number of arguments based on the args array
    def arg_count(self, args):
        # no arguments supplied
        if args[0][0] == "":
            return 0
        else:
            return len(args)

    # returns True if should go to the next file to import
    def imp(self, i, line, args, can_exit):
        path = self.parse(i, line, args)[2]
        # path must be a string
        self.type_err([(path, (str,))], line, lines_ran)
        if not path.endswith(".msn2"):
            path += ".msn2"
        if path in can_exit:
            return False
        can_exit.add(path)
        contents = ""
        with open(path) as f:
            contents = f.readlines()
            script = ""
            for line in contents:
                script += line
        self.logg("importing library", path)
        return script

    # creates a child interpreter
    def new_int(self):
        ret = Interpreter()
        ret.parent = self
        ret.trying = self.trying
        return ret

    def run_syntax(self, key, line):
        # get everything between after syntax and before next index of syntax
        # or end of line
        inside = line[len(key) : line.rindex(key)]
        # variable name
        invarname = syntax[key][0]
        # function to be run
        function = syntax[key][1]
        # store the in between for the user function
        self.vars[invarname] = Var(invarname, inside)
        return self.interpret(function)

    def replace_vars2(self, line):
        for varname, var in self.vars.items():
            try:
                val = var.value
            except:
                try:
                    val = eval(str(var), {}, {})
                except:
                    val = str(var)
            if isinstance(val, str):
                val = f'"{val}"'
            line = line.replace(f"?{varname}?", str(val))
        return line

    # gets a thread by name

    def thread_by_name(self, name):
        try:
            # thread exists
            return self.env_by_name(name)[0]
        except:
            # thread does not exist (yet)
            return

    # gets an environment by name

    def env_by_name(self, name):
        try:
            return self.threads[name]
        except KeyError:
            return

    # splits a named argument in a function

    def split_named_arg(self, as_s, method, func_args, user_func=False):
        # name of argument
        meth_argname = ""
        # iterate up to the first '=' in as_s
        for j in range(1, len(as_s)):
            if as_s[j] == "=":
                last_ind = j + 1
                break
            # cannot be space
            if as_s[j] != " ":
                meth_argname += as_s[j]
        # argument to interpret
        arg = as_s[last_ind:]
        # set the method arg at the specified index
        # to the corresponding argument name in method.args
        # similar to that of Python's argument picker func(arg1=None, arg2=None)
        # get the index of the named argument in method.args
        try:
            ind = method.args.index(meth_argname)
        except ValueError:
            # if not user_func:
            # find the argument
            method.args.append(meth_argname)
            ind = method.args.index(meth_argname)
            # else:

        # adjust the func_args list to the correct size
        if len(func_args) < ind + 1:
            func_args += [None] * (ind + 1 - len(func_args))
        # print(func_args, ind)
        # set the argument at the index to the value
        func_args[ind] = self.interpret(arg)
        return func_args, meth_argname, arg, ind

    # parses an argument from a function
    def parse(self, arg_number, line, args):
        # put the self.convert_arg() logic here instead:
        return line, (as_s := args[arg_number][0]), self.interpret(as_s)

    # gets the shortened version of a variable's value
    def shortened(self, needs_short):
        # if the string is greater than 200 chars
        if len(_t := str(needs_short)) > self.env_max_chars:
            return f"{_t[:self.env_max_chars]}..."
        # otherwise, return the string
        return _t

    # starts a user shell

    def shell(self):
        ip = None
        while ip != "exit":
            ip = input(">>> ")
            self.interpret(ip)

    # logs something to the console

    def logg(self, msg, line):
        # self.log += "[*] " + msg + " : " + line + "\n"
        # do the above but with f strings
        self.log += f"[*] {msg} : {line}\n"

    # prints an array with styling
    # the only argument is an array of maps,
    # this is an example:
    #
    # [{'text': 'Hello', 'style': 'bold', 'fore': 'green', 'back': 'blue'},
    # {'text': ' there', 'style': 'italic'}]
    # version : 2.0.386
    def styled_print(self, array):
        global colors
        # set colors if not available for the entire
        # environment
        if not colors:
            # set the colors / styles
            colors = {
                "": "",
                "reset": "\033[0m",
                "black": "\033[30m",
                "red": "\033[31m",
                "green": "\033[32m",
                "yellow": "\033[33m",
                "blue": "\033[34m",
                "magenta": "\033[35m",
                "cyan": "\033[36m",
                "white": "\033[37m",
                "bold": "\033[1m",
                "italic": "\033[3m",
                "underline": "\033[4m",
                "blink": "\033[5m",
            }
        # for each map in the array
        for mp in array:
            # get all the parameters if they exist
            text = mp["text"] if "text" in mp else ""
            # get the style
            style = mp["style"] if "style" in mp else ""
            # get the foreground color
            fore = mp["fore"] if "fore" in mp else ""
            # get the background color
            back = mp["back"] if "back" in mp else ""
            # print the text with the style
            print(
                colors[style]
                + colors[fore]
                + colors[back]
                + str(text)
                + colors["reset"],
                end="",
            )
        # print a newline
        print()

    # checks for and throws a type error
    def type_err(self, values: list[(any, tuple)], line, lines_ran):
        # for each entry in values
        for value, permitted_types in values:
            # if the value is not in the permitted types
            if (current_type := type(value)) not in permitted_types:
                # create string for error
                error_s = ""
                # for each permitted type
                for i, permitted_type in enumerate(permitted_types):
                    # add the type to the error string
                    error_s += str(permitted_type)
                    # if this is not the last type
                    if i != len(permitted_types) - 1:
                        # add a comma
                        error_s += " or "
                # throw error
                self.err(
                    "Incorrect type specified",
                    f"In value: {value}, expected {error_s} got {current_type}",
                    line,
                    lines_ran,
                )

    # general error printing
    def err(self, err, msg, line, lines_ran):
        # if we're not trying something, and there's an error,
        # print the error
        if not self.trying:
            # the total words printed for this error
            words_printed = ""
            # prints the error

            def print_err(array):
                # print the error
                self.styled_print(array)
                # add to words printed
                nonlocal words_printed
                words_printed += str(array)

            # printing the traceback
            print_err(
                [
                    {"text": "MSN2 Traceback:\n", "style": "bold", "fore": "green"},
                    {"text": (divider := "-" * 15), "style": "bold", "fore": "green"},
                ]
            )
            _branches = []
            root_nums = {root_num for _, (root_num, _) in inst_tree.items()}
            # for k, (root_num, code_line) in inst_tree.items():
            #     root_nums.add(root_num)
            # #root_nums = list(set(root_nums))
            # #root_nums.sort()
            for root_num in sorted(root_nums):
                branches = []
                for k, (root_num2, code_line2) in inst_tree.items():
                    if root_num2 == root_num:
                        branches.append(code_line2)
                _branches.append(branches)
            # print the traceback
            # only the last 7 branches
            for i, _branch in enumerate((new_branches := _branches[-7:])):
                # color of the text
                _branch_color = "black"
                # if this is the last branch
                if is_caller := i == len(new_branches) - 1:
                    _branch_color = "red"
                else:
                    _branch_color = "white"
                # print the caller
                if (_b := _branch[0].strip()) != "":
                    print_err(
                        [
                            {"text": ">> ", "style": "bold", "fore": "black"},
                            {
                                "text": self.shortened(_b),
                                "style": "bold",
                                "fore": _branch_color,
                            },
                            {
                                "text": " <<< " if is_caller else "",
                                "style": "bold",
                                "fore": "yellow",
                            },
                            {
                                "text": "SOURCE" if is_caller else "",
                                "style": "bold",
                                "fore": "yellow",
                            },
                        ]
                    )
                # if branches more than 3
                if len(_branch) > 4 and not is_caller:
                    # print the lines branching off
                    print_err(
                        [
                            {"text": "    at   ", "style": "bold", "fore": "black"},
                            {
                                "text": self.shortened(_branch[0].strip()),
                                "style": "bold",
                                "fore": _branch_color,
                            },
                        ]
                    )
                    # print ...
                    print_err(
                        [
                            {"text": "    at   ", "style": "bold", "fore": "black"},
                            {
                                "text": f"... ({len(_branch) - 4} more)",
                                "style": "bold",
                                "fore": "black",
                            },
                        ]
                    )
                # if branches less than 3
                else:
                    if len(_branch) > 7:
                        # print the before elipses
                        print_err(
                            [
                                {"text": "    at   ", "style": "bold", "fore": "black"},
                                {
                                    "text": f"... ({len(_branch) - 7} more)",
                                    "style": "bold",
                                    "fore": "black",
                                },
                            ]
                        )
                        for i, _branch2 in enumerate(_branch[len(_branch) - 7 :]):
                            print_err(
                                [
                                    {
                                        "text": "    at   ",
                                        "style": "bold",
                                        "fore": "black",
                                    },
                                    {
                                        "text": self.shortened(_branch2.strip()),
                                        "style": "bold",
                                        "fore": _branch_color,
                                    },
                                ]
                            )
                    else:
                        for _branch2 in _branch[1:]:
                            print_err(
                                [
                                    {
                                        "text": "    at   ",
                                        "style": "bold",
                                        "fore": "black",
                                    },
                                    {
                                        "text": self.shortened(_branch2.strip()),
                                        "style": "bold",
                                        "fore": _branch_color,
                                    },
                                ]
                            )
            # print the finishing divider
            print_err([{"text": divider, "style": "bold", "fore": "green"}])
            # print this error with print_err()
            print_err(
                [
                    {"text": "[-] ", "style": "bold", "fore": "red"},
                    {"text": err, "style": "bold", "fore": "red"},
                    {"text": "\n"},
                    {"text": msg, "style": "bold", "fore": "red"},
                ]
            )
            # add to log
            self.log += f"{words_printed}\n"
        raise self.MSN2Exception("MSN2 Exception thrown, see above for details")

    # throws a keyerror
    def raise_key(self, key, line):
        return self.err(
            "Key not found in dictionary",
            f'Key "{key}" not found in dictionary',
            line,
            lines_ran,
        )

    # throws msn2 error for a work in progress mechanism
    def raise_wip(self, what_is_wip, line, lines_ran):
        # get the version for this interpreter
        return self.err(
            "Work in progress",
            f"{what_is_wip} is a work in progress in MSN2 v{self.version} and not been fully implemented.",
            line,
            lines_ran,
        )

    # verifies a type is iterable
    # iterable types include: list, tuple, dict, set, str

    def check_iterable(self, value, line):
        import collections.abc

        if not isinstance(value, collections.abc.Iterable):
            return self.type_err(
                [(value, (collections.abc.Iterable,))], line, lines_ran
            )
        return True

    # checks for validity of a variable name being set
    # by its string representation
    def check_varname(self, varname, line):
        # varname must be a string
        self.type_err([(varname, (str,))], line, lines_ran)
        # varname must exist
        if not varname:
            self.raise_varname_chars(varname, line, lines_ran)

    # throws an operation error
    def raise_operation_err(self, vname, operator, line):
        # operation error
        return self.err(
            f'Operation error: "{operator}"',
            f'There was an error applying the operator "{operator}" to "{vname}" and its argument(s).\n',
            line,
            lines_ran,
        )

    # throws an error asking for a variable name to at least contain characters

    def raise_varname_chars(self, varname, line, lines_ran):
        return self.err(
            "New variable name must contain characters.",
            f'Variable name "{varname}" is an invalid name.',
            line,
            lines_ran,
        )

    # throws msn2 error for comparison error

    def raise_comp(self, comp_operation, vname, line):
        return self.err(
            "Comparison error",
            f'Could not perform comparison operation "{comp_operation}" on variable "{vname}" and the supplied argument(s)',
            line,
            lines_ran,
        )

    # empty array error

    def raise_empty_array(self, line):
        return self.err("Error computing average", f"Array is empty", line, lines_ran)

    # non_numeric value error

    def raise_avg(self, line):
        return self.err(
            "Error computing average",
            f"Array contains non-numeric values",
            line,
            lines_ran,
        )

    # value is not in the list

    def raise_value(self, value, line):
        return self.err(
            "Value error", f'Value "{value}" is not in the list.', line, lines_ran
        )

    # throws msn2 error for Incorrect number of arguments

    def raise_incorrect_args(self, expected, actual, line, lines_ran, method):
        return self.err(
            f"Incorrect number of function arguments for {method.name}",
            f"Expected {expected}, got {actual}",
            line,
            lines_ran,
        )

    # throws msn2 error for Index out of bounds

    def raise_index_out_of_bounds(self, line, lines_ran, method):
        return self.err(
            f"Index out of bounds in body of {method.name}",
            f"Index out of bounds",
            line,
            lines_ran,
        )

    def __del__(self):
        None

    def method_args(self, line, j):
        argstring = ""
        instring = False
        for k in range(j + 1, len(line)):
            if line[k] == '"' and not instring:
                instring = True
            elif line[k] == '"' and instring:
                instring = False
            if not instring:
                if line[k] != " ":
                    if line[k] == ")":
                        break
                    argstring += line[k]
            else:
                argstring += line[k]
        return argstring.split(","), k

    def thread_split(self, line):
        self.interpret(line)

    # splits a process
    def process_split(self, line):
        inter = self.new_int()
        return inter.interpret(line)

    # executing Python scripts
    def exec_python(self, python_block):
        # get the python script with arguments inserted
        py_script = str(self.interpret(f"script({python_block})"))
        # try to execute the script
        try:
            exec(py_script, self._globals, self._locals)
        except Exception as e:
            # send an error
            self.err("Error running Python script", str(e), py_script, lines_ran)
        return py_script

    def var_exists(self, varname):
        if varname in self.vars:
            return True
        return False

    def var_names(self):
        names = []
        for key in self.vars:
            names.append(key)
        return names

    def is_py_str(self, line):
        try:
            return (line[0] == '"' and line[len(line) - 1] == '"') or (
                line[0] == "'" and line[len(line) - 1] == "'"
            )
        except:
            return False

    def is_str(self, line):
        return line[0] == "<" and line[len(line) - 1] == ">"

    # gets the variable value from the variable name
    def get_var(self, name):
        return self.vars[name].value

    # extracts the argument lines from the merged arguments passed
    def get_args(self, line):
        args = []
        l = len(line)
        arg = ""
        start = 0
        p = 0
        a = 0
        s = 0
        indouble = False
        s2 = 0
        insingle = False
        b = 0
        for i in range(l + 1):
            c = ""
            try:
                c = line[i]
            except:
                None
            if c == "[" and not s2 > 0 and not s > 0:
                a += 1
            if c == "]" and not s2 > 0 and not s > 0:
                a -= 1
            if c == "(" and not s2 > 0 and not s > 0:
                p += 1
            if c == ")" and not s2 > 0 and not s > 0:
                p -= 1
            if not self.in_string(s, s2):
                if c == "{":
                    b += 1
                if c == "}":
                    b -= 1
            if not indouble and not s2 > 0 and c == '"':
                s += 1
                indouble = True
            elif indouble and c == '"':
                s -= 1
                indouble = False
            if not insingle and not s > 0 and c == "'":
                s2 += 1
                insingle = True
            elif insingle and c == "'":
                s2 -= 1
                insingle = False
            if c == "," and s == 0 and s2 == 0 and p == 0 and a == 0 and b == 0:
                args.append([arg, start, start + len(arg)])
                start = i + 1
                arg = ""
                continue
            elif i == l:
                args.append([arg, start, start + len(arg)])
            arg += c
        return args

    def in_string(self, s, s2):
        return s > 0 or s2 > 0

    def interpret_msnscript_1(self, line):
        element = ""
        variable = ""
        for i in range(0, len(line)):
            c = line[i]
            if c != " ":
                if c == "+" and line[i + 1] == "=":
                    variable = element
                    element = ""
                    for j in range(i + 2, len(line)):
                        element += line[j]

                    # if element is a number
                    if isinstance(element, float) or isinstance(element, int):
                        self.vars[variable].value += self.interpret(element)
                    # if element is a string
                    elif isinstance(element, str):
                        try:
                            self.vars[variable].value += self.interpret(element)
                        except:
                            self.vars[variable].value += self.interpret(element)
                    return self.vars[variable].value
                elif c == "-" and line[i + 1] == "=":
                    variable = element
                    element = ""
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value -= self.interpret(element)
                    return self.vars[variable].value
                elif c == "*" and line[i + 1] == "=":
                    variable = element
                    element = ""
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value *= self.interpret(element)
                    return self.vars[variable].value
                elif c == "/" and line[i + 1] == "=":
                    variable = element
                    element = ""
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value /= self.interpret(element)
                    return self.vars[variable].value
                elif c == "=":
                    variable = element
                    element = ""
                    string = False
                    array = False
                    for j in range(i + 1, len(line)):
                        if line[j] == '"':
                            string = True
                        if line[j] == "[":
                            array = True
                        element += line[j]
                    self.vars[variable] = Var(variable, self.interpret(element))
                    return self.vars[variable].value
                else:
                    element += c

    # scrapes all html elements from a URL

    def html_all_elements(self, url):
        import requests

        # web scraping
        from bs4 import BeautifulSoup

        # obtains a response from the URL
        response = requests.get(url)
        soup = BeautifulSoup(response.content, "html5lib")
        # obtains all html elements
        return soup.find_all()

    def me(self):
        return (
            str(self)
            .replace(" ", "")
            .replace("<", "")
            .replace(">", "")
            .replace("Interpreter", "")
        )

    # prints text with a box around it
    def bordered(text):
        lines = text.splitlines()
        width = max(len(s) for s in lines)
        res = ["┌" + "─" * width + "┐"]
        for s in lines:
            res.append("│" + (s + " " * width)[:width] + "│")
        res.append("└" + "─" * width + "┘")
        return "\n".join(res)

    # exception
    class MSN2Exception(Exception):
        pass

    
    # class for an App

    class App:
        # constructor
        def __init__(self, path, application=None, name=None, extension=None):
            # path of application being launched
            self.path = path
            if name:
                self.name = name
                self.extension = extension
            else:
                _spl = path.split("\\")[-1].split(".")
                # extension of the application
                self.extension = _spl[-1]
                self.name = _spl[0]
            # pwinauto application object
            self.application = application

    # element for an application
    class AppElement:
        # constructor
        def __init__(self, window, name):

            # creates a modified window
            self.window = window
            # # set the window
            # self.window = window
            # set the name
            self.name = name

        # gets the text of the window
        def window_text(self):
            return self.name

        # gets all children of the window
        def children(self):
            return self.window.children()

        # sets the focus to the window
        def set_focus(self):
            self.window.set_focus()

        # gets the properties of the window
        def get_properties(self):
            return self.window.get_properties()

        # gets the highest level parent of this element
        def top_level_parent(self):
            return self.window.top_level_parent()

        # computes the height of the window
        def height(self):
            try:
                return (
                    self.window.get_properties()["rectangle"].bottom
                    - self.window.get_properties()["rectangle"].top
                )
            except:
                return

        # computes the width of the window

        def width(self):
            try:
                return (
                    self.window.get_properties()["rectangle"].right
                    - self.window.get_properties()["rectangle"].left
                )
            except:
                return

        # string

        def __str__(self):
            return Interpreter.bordered(
                f'Text: {self.name if self.name else "[No Text Found]"}\nSize:\
{f"{self.width()}x{self.height()}"}\nObject:\n{self.window}'
            )

    # class for a button
    class Button(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

        # clicks the button

        def click(self):
            self.window.click()

        # right clicks the button

        def right_click(self):
            self.window.click_input(button="right")

    # class for a Link

    class Link(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for a Menu

    class Menu(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for a ToolBar

    class ToolBar(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for a scrollbar

    class ScrollBar(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for TabItems

    class TabItem(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for Hyperlink

    class Hyperlink(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # class for Inputs

    class Input(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

        # types text into the input
        def type_keys(self, text):
            self.window.type_keys(text)

    # class for Tables

    class Table(AppElement):
        # constructor
        def __init__(self, window, name):
            # call super constructor
            super().__init__(window, name)

    # ------------------------------------
