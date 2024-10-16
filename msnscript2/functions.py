"""Joint functions and dispatch table."""


# utilties
from core.common import aliases

# common
from core.obj.function.access import OBJ_FUNCTION_ACCESS_DISPATCH
from core.obj.function.default import OBJ_FUNCTION_DEFAULT_DISPATCH
from core.obj.function.modify import OBJ_FUNCTION_MODIFY_DISPATCH
from core.obj.function.run import OBJ_FUNCTION_RUN_DISPATCH
from core.obj.general.default.cast import OBJ_GENERAL_DEFAULT_CAST_DISPATCH
from core.obj.general.default.chained import OBJ_GENERAL_DEFAULT_CHAINED_DISPATCH
from core.obj.general.default.general import OBJ_GENERAL_DEFAULT_GENERAL_DISPATCH
from core.obj.general.default.ops import OBJ_GENERAL_DEFAULT_OPS_DISPATCH
from core.obj.general.default.properties import OBJ_GENERAL_DEFAULT_PROPERTIES_DISPATCH
from core.obj.general.default.strings import OBJ_GENERAL_DEFAULT_STRINGS_DISPATCH
from core.obj.html.basic import OBJ_HTML_BASIC_DISPATCH
from core.obj.html.default import OBJ_HTML_DEFAULT_DISPATCH
from core.obj.obj_instance.creation import OBJ_INSTANCE_CREATION_DISPATCH
from core.obj.op.basic import OBJ_OP_BASIC_DISPATCH
from core.obj.op.default import OBJ_OP_DEFAULT_DISPATCH
from core.obj.py.access import OBJ_PY_ACCESS_DISPATCH
from core.obj.py.default import OBJ_PY_DEFAULT_DISPATCH
from core.obj.py.run import OBJ_PY_RUN_DISPATCH
from core.obj.trace.general import OBJ_TRACE_GENERAL_DISPATCH
from var import Var

# accumulating grouped default functions

# basic
from core.system import SYSTEM_DISPATCH
from core.functions import FUNCTION_BASED_DISPATCH
from core.vars import VARS_DISPATCH
from core.math import MATH_DISPATCH
from core.strings import STRINGS_DISPATCH
from core.numbers import NUMBERS_DISPATCH
from core.type_testing import TYPE_TESTING_DISPATCH
from core.iterables import ITERABLES_DISPATCH
from core.assertions import ASSERTIONS_DISPATCH
from core.object_general import OBJECT_GENERAL_DISPATCH
from core.syntax import SYNTAX_DISPATCH
from core.logical import LOGICAL_DISPATCH
from core.domains import DOMAINS_DISPATCH
from core.in_out import IN_OUT_DISPATCH
from core.stdout import STDOUT_DISPATCH
from core.js import JS_DISPATCH
from core.time import TIME_DISPATCH
from core.contexts import CONTEXTS_DISPATCH
from core.multiprogramming import MULTIPROGRAMMING_DISPATCH
from core.pointer import POINTER_DISPATCH
from core.api import API_DISPATCH
from core.insertions import INSERTIONS_DISPATCH
from core.lang import LANG_DISPATCH
from core.win_auto import WIN_AUTO_DISPATCH
from core.excel import EXCEL_DISPATCH
from core.cast import CAST_DISPATCH
from core.conditionals import CONDITIONALS_DISPATCH
from core.redirects import REDIRECTS_DISPATCH

# object based
from core.obj.general.destructive import OBJ_GENERAL_DESTRUCTIVE_DISPATCH
from core.obj.general.number.comparisons import OBJ_GENERAL_NUMBER_COMPARISONS_DISPATCH
from core.obj.general.number.ops import OBJ_GENERAL_NUMBER_OPS_DISPATCH
from core.obj.general.number.ops_ip import OBJ_GENERAL_NUMBER_OPS_IP_DISPATCH
from core.obj.general.set.basic import OBJ_GENERAL_SET_BASIC_DISPATCH
from core.obj.general.list.access import OBJ_GENERAL_LIST_ACCESS_DISPATCH
from core.obj.general.list.modify import OBJ_GENERAL_LIST_MODIFY_DISPATCH
from core.obj.general.str.access import OBJ_GENERAL_STR_ACCESS_DISPATCH
from core.obj.general.str.modify import OBJ_GENERAL_STR_MODIFY_DISPATCH
from core.obj.general.dict.access import OBJ_GENERAL_DICT_ACCESS_DISPATCH
from core.obj.general.dict.modify import OBJ_GENERAL_DICT_MODIFY_DISPATCH
from core.obj.general.class_based.requests_html_HTML import OBJ_GENERAL_CLASS_BASED_REQUESTS_HTML_HTML_DISPATCH
from core.obj.general.class_based.requests_html_HTMLSession import OBJ_GENERAL_CLASS_BASED_REQUESTS_HTML_HTMLSession_DISPATCH

# special functions
from core.special.loops import SPECIAL_LOOPS_DISPATCH

# misc
from core.misc import MISC_DISPATCH

# directory functions

# whether or not /api/functions has been created or not
api_functions_created = False
# serialized value for generating api routes
serialized_value = 0
# global serial for states
states_serial = 0

# gets the pages/ directory path from next entry point


def get_pages_path(inst):
    # get the next entry point
    next_entry = inst.interpreter.next_entry_path
    # get the pages/ directory path
    pages_path = next_entry[:next_entry.rfind('/') + 1]
    return pages_path

# tries to create the api/ directory in the pages/ directory
# if it doesn't exist


def try_create_api_dir(inst):
    import os
    # get the pages/ directory path
    pages_path = get_pages_path(inst)
    # if the api/ directory doesn't exist
    if 'api' not in os.listdir(pages_path):
        # create the api/ directory
        os.mkdir(pages_path + 'api')

# gets the root project directory path from next entry point


def get_root_dir(inst):
    # get the next entry point
    next_entry = inst.interpreter.next_entry_path
    # get the root project directory path,
    # aka parent of pages/ directory
    root_dir = get_pages_path(inst)[:-6]
    return root_dir

# tries to create an api/functions directory in
# the parent directory of the next entry point
# (root project directory)


def try_create_api_functions(inst, api_functions_script):
    import os
    global api_functions_created
    # get the next entry point
    next_entry = inst.interpreter.next_entry_path
    # get the root project directory (parent of pages directory)
    root_dir = get_root_dir(inst)
    file_path = root_dir + '/api/functions.jsx'
    # remove any double slashes
    file_path = file_path.replace('//', '/')
    # print updating
    print_updating_file(inst, file_path)
    # if the api/ directory doesn't exist
    if 'api' not in os.listdir(root_dir):
        # create the api/ directory
        os.mkdir(root_dir + '/api')
    # create /api/functions.jsx if it doesn't exist
    if 'functions.jsx' not in os.listdir(root_dir + '/api'):
        # create the file
        file = open(file_path, 'w')
        # write the file
        file.write(api_functions_script)
        # close the file
        file.close()
    # if not api_functions_created (/api/functions.jsx has not been created yet):
    if not api_functions_created:
        # create the file at the path
        file = open(file_path, 'w')
        # write the file
        file.write(api_functions_script)
        # set api_functions_created to True
        api_functions_created = True
    # otherwise, append the function to the file
    else:
        # append the function to the file
        file = open(file_path, 'a')
        # write the file
        file.write(api_functions_script)
    # close the file
    file.close()
    # print finished file
    print_finished_file(inst, file_path)


# adds an api route to the api/ directory
# path : the path of the api route from the api directory
#        example: a 'path' of 'users/login' would create
#                 an api route at pages/api/users/login.js
def add_api_route(inst, path, pages_api_script, api_functions_script):
    # try to add the api/ directory
    try_create_api_dir(inst)
    p = get_pages_path(inst) + 'api/' + path + '.js'
    # print starting
    print_updating_file(inst, p)
    # create the file
    file = open(p, 'w')
    # write the file
    file.write(pages_api_script)
    # close the file
    file.close()
    # print finished
    print_finished_file(inst, p)
    # try to create the api/functions directory
    try_create_api_functions(inst, api_functions_script)
# generates a serialized route name


def generate_serialized_route_name(path):
    global serialized_value
    # increment the serialized value
    serialized_value += 1
    # return the serialized route name
    return f"route{serialized_value}"

# gets the path of a route given a route_name


def get_route_path(inst, route_name):
    return f"/{route_name}"

# adds a component route (page route)


def add_route(inst, path, script):
    # creates a file at the pages directory
    file = open(get_pages_path(inst) + path + '.jsx', 'w')
    # write the file
    file.write(script)
    # close the file
    file.close()


# writes to a file
def file_write(inst, lock, lines_ran):
    lock.acquire()
    path = inst.parse(0)
    # path must be str
    inst.type_err([(path, (str,))], lines_ran)
    file = open(path, "w")
    towrite = str(inst.parse(1))
    file.write(towrite)
    file.close()
    lock.release()
    return towrite

# appends to a file


def file_append(inst, lock, lines_ran):
    lock.acquire()
    path = inst.parse(0)
    # path must be str
    inst.type_err([(path, (str,))], lines_ran)
    file = open(path, "a")
    towrite = str(inst.parse(1))
    file.write(towrite)
    file.close()
    lock.release()
    return towrite

# interprets the first argument


# defines a function


# runs a user function


# interprets a multi-lined function call


# executes a user function


def user_function_exec(inst, lines_ran):
    from msnint2 import Var
    method = inst.interpreter.methods[inst.func]
    # create func args
    func_args = []
    # if arguments supplied
    if not inst.args[0][0] == '':
        for i in range(len(inst.args)):
            # check if we're setting a certain argument
            as_s = inst.args[i][0].strip()
            meth_argname = None
            if as_s[0] == '&':
                func_args, meth_argname, arg, ind = inst.interpreter.split_named_arg(
                    as_s, method, func_args, user_func=True)
            # else, just append the argument
            else:
                arg = inst.parse(i)
                func_args.append(arg)
            try:
                meth_argname = method.args[i]
            # incorrect amount of function arguments supplied
            except IndexError:
                inst.interpreter.raise_incorrect_args(str(len(method.args)), str(
                    inst.interpreter.arg_count(inst.args)), inst.line, inst.lines_ran, method)
            try:
                inst.interpreter.vars[meth_argname] = Var(
                    meth_argname, arg)
            # unhashable type:list, this means a named argument
            # is being requested to be set
            except TypeError:
                inst.interpreter.vars[meth_argname[0]] = Var(
                    meth_argname[0], arg)
    # create return variable
    ret_name = method.returns[0]
    # add the return variable if not exists
    if ret_name not in inst.interpreter.vars:
        inst.interpreter.vars[ret_name] = Var(ret_name, None)
    # execute method
    try:
        method.run(func_args, inst.interpreter, inst.args)
    # index out of bounds error in method run
    except IndexError:
        # raise msn2 error
        inst.interpreter.raise_index_out_of_bounds(
            inst.line, lines_ran, method)
    # if its a variable
    if ret_name in inst.interpreter.vars:
        return inst.interpreter.vars[ret_name].value
    try:
        return eval(str(inst.interpreter.vars[ret_name].value), {}, {})
    except:
        pass
    try:
        return str(inst.interpreter.vars[ret_name].value)
    except:
        return str(inst.interpreter.vars[ret_name])

# converts a value to a string for JavaScript


def stringify(val):
    return f"`{val}`"

# generates a comment


def comment(text):
    return f"\n// {text} ::\n"

# reads from the main _app.js path


# creates a callback


def callback(inst, is_async=False):
    from js import parse
    # insert a mobile arrow function
    as_js = "(" + ("async " if is_async else "") + \
        "() => {" if inst.has_args() else ""
    # add each instruction
    for i in range(len(inst.args) - 1):
        if i != len(inst.args) - 1:
            as_js += f"{(_v := parse(inst, i))}{';' if _v != '' and _v != None else ''}"
    last = parse(inst, -1)
    # return the last instruction
    as_js += f"return " + (str(last) if last else ";") + \
        "})()" if inst.has_args() else last
    return as_js

# insert a line at a marker
#
# 1. read in the file's lines
# 2. lines ending in '::' are markers
# 3. find the marker
# 4. insert the line at the next EMPTY line below the marker
# 5. write the lines back to the file


def insert_line_at_marker(inst, path, keyword, line, check_for_dups=False):
    # read in lines from path
    file = open(path, "r")
    lines = file.readlines()
    file.close()
    # find the marker
    for i, l in enumerate(lines):
        if not (_1 := l.endswith('::\n')) and not (_2 := l.endswith('*/}\n')):
            continue
        l = l[2:-3].strip() if _1 else (l[5:-4].strip() if _2 else l)
        if l == keyword:
            # insert the line at the next empty line
            for j in range(i + 1, len(lines)):
                curr_line = lines[j].strip()
                if check_for_dups and curr_line == line:
                    break
                if curr_line == '':
                    lines.insert(j, line + '\n')
                    break
            break
    # write the lines back to the file
    file = open(path, "w")
    file.writelines(lines)
    file.close()

# inserts a line at a marker, but does not iterate to the next empty line
# def insert_line_at_marker_no_iter(inst, path, keyword, line):
#     file = open(path, "r")
#     lines = file.readlines()
#     file.close()
#     # find the marker
#     for i, l in enumerate(lines):
#         if not l.endswith('::\n'):
#             continue
#         l = l[2:-3].strip()
#         print(l)
#         if l == keyword:
#             # insert the line at the next empty line
#             lines.insert(i + 1, line + '\n')
#             break
#     # write the lines back to the file
#     file = open(path, "w")
#     file.writelines(lines)
#     file.close()

# web based functions


unique_hash_counter = 0

# parse prop


def unique_hash(inst):
    global unique_hash_counter
    import time
    import hashlib
    # Increment the counter first to ensure uniqueness
    unique_hash_counter += 1

    # Get the current clock tick in nanoseconds
    current_tick_ns = time.perf_counter_ns()

    # Combine the tick and the counter to create a unique string
    unique_str = f"{current_tick_ns}{unique_hash_counter}"

    return unique_str


def parse_props(inst):
    from js import html_attribute_defaults, parse, parse_string
    props = {}
    # parse props from line
    # line is formatted: style=...
    # use inst.interpreter.interpret(line) to get the value of style
    # there can be any amount of spaces anywhere
    # there can be any amount of props
    # props are formatted: prop_name=prop_value
    # prop_value is interpreted
    prop = ""
    value = ""
    # parse props
    for i in range(len(inst.args)):
        # get the prop
        prop = inst.args[i][0].strip()
        # if the prop is style
        if is_prop(inst, i):
            # get slice from first '=' sign onward
            ind = prop.find('=')
            if ind == -1:
                continue
            # prop is everything before the '=' sign
            key = prop[:ind]
            # value is everything after the '=' sign
            value = prop[ind + 1:]
            # interpret value
            value = inst.interpreter.interpret(value)
            # value = parse_string(inst, value)
            # add the value to the props
            if key not in html_attribute_defaults:
                # remove
                props[key] = value
            elif value:
                props[key] = value
            else:
                props[key] = html_attribute_defaults[key]
            # props[key] = value if value else html_attribute_defaults[key]
    # return component with props
    return props

# custom prop on react node


def custom_prop(inst, props, key, value):
    # determine the custom prop
    # delete this key from props
    if key == "classNames":
        # print the value
        # className prop value to return
        classNameValue = ""
        # for each value in the iterable,
        # append the value to the classNameValue
        for className in value:
            classNameValue += f" {className}"
        return "className", f"`{classNameValue.strip()}`"
    # just return the key and value
    return key, value

    # if none of the above, throw unknown prop error
    raise Exception(f"Unknown prop '{key}'")

# creates a string representation of an HTML tag


def tag(inst, children, html_tag="", props={}):
    # apply style, if applicable
    if isinstance(children, str):
        # create props string
        prop_str = ""
        for key, value in props.items():
            if value:
                prop_str += " " + key + "={" + str(value) + "}"
        return f"<{html_tag}{prop_str}>{children}</{html_tag}>" if html_tag != "" else children
    return tag(inst, "".join([(tag(inst, i) if i != None else "") for i in children]), html_tag, props=props)

# determines if an argument is a prop or children


def is_prop(inst, i):
    from js import html_attributes
    # determine if the argument stripped starts with
    # any html attributes in html_attributes tuple
    for attr in html_attributes:
        if inst.args[i][0].strip().startswith(attr):
            return True
    return False

# merges parsed props and default props

# determines if a string is representing a JavaScript function or function call


def is_function(string):
    import esprima
    q = string.strip()
    try:
        parsed = esprima.parseScript(q)
        ret = (parsed.body[0].type == "FunctionDeclaration") or \
              (parsed.body[0].expression.type == "ArrowFunctionExpression" or
               parsed.body[0].expression.type == "FunctionExpression" or
               parsed.body[0].expression.type == "CallExpression")
        return ret
    except:
        return False

# determines if a call is a call from msn2 react


def is_msn2_react_call(inst):
    return inst.line.strip().startswith('react:')


def merge_props(props, inst):
    from js import html_attributes, html_attribute_defaults, parse_string
    merged = props.copy()
    parsed_props = parse_props(inst)

    # for each possible html attribute
    for attr in html_attributes:

        if attr in props and type(props[attr]) == str:
            props[attr] = parse_string(inst, props[attr])
            merged[attr] = f"`{props[attr]}`"
            continue
        # for boolean
        if attr in parsed_props and type(parsed_props[attr]) == bool:
            merged[attr] = f"{parsed_props[attr]}"
            continue
        props_t = {key: value.strip() for key, value in props[attr].items(
        ) if value} if attr in props and props[attr] else {}
        parsed_props_t = {key: value.strip() for key, value in parsed_props[attr].items(
        ) if value} if attr in parsed_props and parsed_props[attr] and \
            type(parsed_props[attr]) != type(html_attribute_defaults[attr]) else {}
        merged[attr] = {**props_t, **parsed_props_t}
    # add rest of props
    for key, value in parsed_props.items():
        # append based on type
        if key not in merged:
            newKey, value = custom_prop(inst, merged, key, value)
            merged[newKey] = value
        elif not merged[key]:
            if is_function(value):
                merged[key] = value
            elif type(value) == dict:
                merged[key] = {**merged[key], **value}
            elif inst.interpreter.is_py_str(value):
                merged[key] = f"`{value}`"
            elif type(html_attribute_defaults[key]) == str:
                merged[key] = f"`{value}`"
    return merged

# sub function to determine if a string is react code


def is_react_code(string):
    import esprima
    # parse the string
    try:
        parsed = esprima.parseScript(string)
    except:
        return False
    # return whether or not the string is react code
    return parsed.body[0].type == "ExpressionStatement" and parsed.body[0].expression.type == "JSXElement"

# determines if an element is a JSX Element


def is_jsx_element(string):
    import esprima
    string = string.strip()
    # parse the string
    try:
        parsed = esprima.parseScript(string)
        ret = (parsed.body[0].type == "ExpressionStatement" and parsed.body[0].expression.type == "JSXElement") or \
            (parsed.body[0].type == "BlockStatement")
        return ret
    except:
        return (string.startswith("<") and string.endswith(">")) or \
            (string.startswith("{") and string.endswith("}"))

# renders a component / collection of components


def component(inst, html_tag="div", props={}):
    inst.in_html = True
    return tag(inst, [((inst.parse(i) if not is_prop(inst, i) else "")
                       if not (as_s := inst.args[i][0].strip()) in inst.interpreter.states and not as_s in inst.interpreter.methods else
                       ("{" + as_s + "}")) if not is_jsx_element(_v := inst.args[i][0].strip()) else
                      _v for i in range(len(inst.args))], html_tag, props=merge_props(props, inst))

# creates a useEffect hook


def use_effect(inst):
    from js import parse
    # cannot be in html
    inst.in_html = False
    # determine if useEffect has been imported
    if (imp := ('useEffect', 'react')) not in inst.interpreter.web_imports:
        # insert_line_at_marker()
        inst.interpreter.web_imports.add(imp)
        # insert the import
        insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
                              "import { useEffect } from 'react';", check_for_dups=True)
    # create the effect
    return f"useEffect(() => {{\n{parse(inst, 0)}\n}}, {parse(inst, 1)})\n"

# generates a module.css file for a component


def generate_css_module(inst):
    # generates
    ...

# generates an api route as js script


def generate_api_scripts(route_name, route_req_name, route_res_name, script, fetch_body_name, fetch_body_script):
    # create the api route function to place at the default export
    # function called when fetched at this route
    api_route_script = "export default async function " + \
        route_name + "(" + route_req_name + ", " + route_res_name + ")  \
        {\n" + script + "\n}"
    # fetches the api route
    api_func_script = "export async function " + route_name +  \
        "(" + fetch_body_name + ") {\nreturn " + fetch_body_script + "\n}"
    return api_route_script, api_func_script

# prints updating a file


def print_updating_file(inst, path):
    # remove the next project path from path
    path = path.replace(get_root_dir(inst), '')
    inst.interpreter.styled_print([
        {'text': '[', 'fore': 'white', 'style': 'bold'},
        {'text': 'MSN2', 'fore': 'black',  'style': 'bold'},
        {'text': '] ', 'fore': 'white',  'style': 'bold'},
        {'text': 'updating: ', 'fore': 'blue',  'style': 'bold'},
        {'text': path, 'fore': 'green',  'style': 'bold'}
    ])
# prints finishing a file


def print_finished_file(inst, path):
    # remove the next project path from path
    path = path.replace(get_root_dir(inst), '')
    inst.interpreter.styled_print([
        {'text': '[', 'fore': 'white', 'style': 'bold'},
        {'text': 'MSN2', 'fore': 'black',  'style': 'bold'},
        {'text': '] ', 'fore': 'white',  'style': 'bold'},
        {'text': 'finished updating: ', 'fore': 'blue',  'style': 'bold'},
        {'text': path, 'fore': 'green',  'style': 'bold'}
    ])


# generates api scripts and adds them to an api route


def generate_api_scripts_and_add(inst, route_name, route_req_name, route_res_name, script, fetch_body_name, fetch_body_script):
    # generate the api scripts
    api_route_script, api_func_script = generate_api_scripts(
        route_name, route_req_name, route_res_name, script, fetch_body_name, fetch_body_script)
    # add the api route
    add_api_route(inst, route_name, api_route_script, api_func_script)
    # add route to interpreter
    inst.interpreter.routes[route_name] = api_route_script

# generates an api fetch snippet


def generate_fetch(path):
    return "await fetch('/api/" + path + "').then(res => res.json())"

# generates a set function for a state variable name


def generate_set_function(name):
    return f"set{name.capitalize()}"

# generates a safer set function


def generate_safe_set_function(inst, state_name):
    from js import parse
    set_function = generate_set_function(state_name)
    return f"{set_function}({state_name} => {{return {parse(inst, 0)}}})"


# adds to web imports


def try_add_web_import(inst, importItems=[], import_only=False):
    # for each import item
    for is_default, importName, importLocation in importItems:
        # if the import item is not in the web imports
        if (importName, importLocation) not in inst.interpreter.web_imports:
            # add the import item to the web imports
            inst.interpreter.web_imports.add((importName, importLocation))
            # insert the import
            insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
                                  (f"import {'{' if not is_default else ''}{importName}{'}' if not is_default else ''} from '{importLocation}';") if not import_only else f"import {importName}", check_for_dups=True)

# generates a serialized state name


def generate_serialized_state():
    global states_serial
    states_serial += 1
    state_name = f"msn2_generated_state_{states_serial}"
    return f"[{state_name}, {generate_set_function(state_name)}]", state_name

# adds a state to the list of states


def add_state(inst, name, default_value_or_new_value):
    from msnint2 import Var
    # if this state is already defined
    if name in inst.interpreter.states:
        return generate_set_function(name) + f"({name} => " + " {return " + str(default_value_or_new_value) + "})\n"
    # add state to list of states
    inst.interpreter.states[name] = Var(name, default_value_or_new_value)


def f_op_else():
    return "<msnint2 class>"




def f_ai_ai(inter, line, args, **kwargs):
    import os
    import openai
    import tiktoken

    global models
    # verify existence of openai api key
    if not openai.api_key:
        try:
            openai.api_key = os.environ["OPENAI_API_KEY"]
        except:
            # msn2 error, no OPENAI env var
            inter.err(
                f"OpenAI API key not found. Please set your OPENAI_API_KEY environment variable to your OpenAI API key.",
                True,
                "",
                kwargs["lines_ran"],
            )
    # if models not defined, define them
    if not models:
        # determines if a model needs chatcompletion
        def needs_completion(model):
            return (
                model.startswith("text")
                or model == "gpt-3.5-turbo-instruct"
            )

        # determines if arguments for 'model' are str and in models
        def check_model(model):
            # model must be str
            inter.type_err([(model, (str,))], line, kwargs["lines_ran"])
            # model must exist
            if model not in models:
                inter.err(
                    "Model not found",
                    f'Model {model} not found. Available models are {", ".join(models.keys())}',
                    line,
                    kwargs["lines_ran"],
                )
        # gets responses from the models

        def response(model, prompt):
            # enforce model as str and prompt as str
            inter.type_err(
                [(model, (str,)), (prompt, (str,))], line, kwargs["lines_ran"]
            )
            if needs_completion(model):
                # return v1/completions endpoint
                return (
                    openai.Completion.create(
                        model=model,
                        prompt=prompt,
                        temperature=0.5,
                        max_tokens=models[model]["max_tokens"] // 2,
                    )
                    .choices[0]
                    .text
                )
            else:
                return openai.ChatCompletion.create(
                    model=model,
                    messages=[{"role": "user", "content": prompt}],
                    temperature=0.5,
                    max_tokens=models[model]["max_tokens"] // 2,
                )
        # available models for use by the query
        return {
            "gpt-3.5-turbo-instruct": {
                "max_tokens": 4097,
                # compute $0.0015 / 1K tokens
                "price_per_token": 0.0015 / 1000,
            },
            "gpt-3.5-turbo": {
                "max_tokens": 4097,
                "price_per_token": 0.0015 / 1000,
            },
            "gpt-3.5-turbo-16k": {
                "max_tokens": 16384,
                "price_per_token": 0.003 / 1000,
            },
            "text-davinci-003": {
                "max_tokens": 4097,
                # turbo * 10, as stated by OPENAI model pricing documentation
                "price_per_token": (0.0015 / 1000) * 10,
            },
            # gets responses from these models
            "response": response,
            "needs_completion": needs_completion,
            "check_model": check_model,
        }

    return "<msnint2 class>"


def f_ai_models(inter, line, args, **kwargs):
    return f_ai_ai(inter, line, args, **kwargs)


def f_ai_max_tokens(inter, line, args, **kwargs):
    # prepare
    f_ai_ai(inter, line, args, **kwargs)
    # get model
    model = inter.parse(0, line, args)[2]
    # check model
    models["check_model"](model)
    # return max tokens
    return models[model]["max_tokens"]


def f_ai_price_per_token(inter, line, args, **kwargs):
    # prepare
    f_ai_ai(inter, line, args, **kwargs)
    # get the model
    model = inter.parse(0, line, args)[2]
    # check the model
    models["check_model"](model)
    # return price per token
    return models[model]["price_per_token"]


def f_ai_basic(inter, line, args, **kwargs):
    # generates an ai response with the basic model
    return models["response"](
        inter.parse(0, line, args)[2],
        inter.parse(1, line, args)[2],
    )


def f_ai_advanced(inter, line, args, **kwargs):
    models = f_ai_ai(inter, line, args, **kwargs)
    return models["response"](
        "gpt-3.5-turbo-16k", inter.parse(0, line, args)[2]
    )


def f_ai_query(inter, line, args, **kwargs):
    import openai
    # model to use
    model = inter.parse(0, line, args)[2]
    # check model
    models["check_model"](model)
    # messages
    messages = inter.parse(1, line, args)[2]
    inter.type_err([(messages, (list, str))], line, kwargs["lines_ran"])
    # temperature
    temperature = inter.parse(2, line, args)[2]
    # temp must be int or float
    inter.type_err([(temperature, (int, float))], line, kwargs["lines_ran"])
    # max_tokens
    max_tokens = inter.parse(3, line, args)[2]
    # max_tokens must be int
    inter.type_err([(max_tokens, (int,))], line, kwargs["lines_ran"])
    # top_p
    top_p = inter.parse(4, line, args)[2]
    # top_p must be int or float
    inter.type_err([(top_p, (int, float))], line, kwargs["lines_ran"])
    # frequency_penalty
    frequency_penalty = inter.parse(5, line, args)[2]
    # frequency_penalty must be int or float
    inter.type_err([(frequency_penalty, (int, float))],
                   line, kwargs["lines_ran"])
    # presence_penalty
    presence_penalty = inter.parse(6, line, args)[2]
    # presence_penalty must be int or float
    inter.type_err([(presence_penalty, (int, float))],
                   line, kwargs["lines_ran"])
    # stop
    stop = inter.parse(7, line, args)[2]
    # stop must be str
    inter.type_err([(stop, (str,))], line, kwargs["lines_ran"])
    # if model is standard
    if models["needs_completion"](model):
        return (
            openai.Completion.create(
                model=model,
                prompt=messages,
                temperature=temperature,
                max_tokens=max_tokens,
                top_p=top_p,
                frequency_penalty=frequency_penalty,
                presence_penalty=presence_penalty,
                stop=stop,
            )
            .choices[0]
            .text
        )
    else:
        return openai.ChatCompletion.create(
            model=model,
            messages=messages,
            temperature=temperature,
            top_p=top_p,
            frequency_penalty=frequency_penalty,
            presence_penalty=presence_penalty,
            stop=stop,
        )


def f_ai_tokens(inter, line, args, **kwargs):
    import tiktoken
    # string to check
    prompt = inter.parse(0, line, args)[2]
    # prompt must be str
    inter.type_err([(prompt, (str,))], line, kwargs["lines_ran"])
    # model to check
    model_name = inter.parse(1, line, args)[2]
    # check model
    models["check_model"](model_name)
    # get the encoding
    return len(
        tiktoken.encoding_for_model(model_name).encode(prompt)
    )


def f_ai_split_string(inter, line, args, **kwargs):
    # need langchain
    from langchain.text_splitter import TokenTextSplitter
    # model_name
    model_name = inter.parse(0, line, args)[2]
    # check model
    models["check_model"](model_name)
    # chunk size
    chunk_size = inter.parse(1, line, args)[2]
    # chunk size must be int
    inter.type_err([(chunk_size, (int,))], line, kwargs["lines_ran"])
    # string to split
    string = inter.parse(2, line, args)[2]
    # string must be str
    inter.type_err([(string, (str,))], line, kwargs["lines_ran"])
    # get the splitter, split by sentence
    splitter = TokenTextSplitter(
        chunk_size=chunk_size,
        chunk_overlap=0,
        model_name=model_name,
    )
    # split the string
    return splitter.split_text(string)


def f_ai_else():
    return "<msnint2 class>"


def f_var_equals(inter, line, args, **kwargs):
    firstvar = inter.parse(0, line, args)[2]
    return all(
        firstvar == inter.parse(i, line, args)[2]
        for i in range(1, len(args))
    )


def f_var_else(inter, line, args, **kwargs):
    return "<msnint2 class>"


def f_file_create(inter, line, args, **kwargs):
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    open(path, "w").close()
    kwargs["lock"].release()
    return True


def f_file_read(inter, line, args, **kwargs):
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    file = open(path, "r", encoding="utf-8")
    contents = file.read()
    file.close()
    kwargs["lock"].release()
    return contents


def f_file_write(inter, line, args, **kwargs):
    from functions import file_write
    return file_write(kwargs["inst"], kwargs["lock"], kwargs["lines_ran"])


def f_file_writemsn(inter, line, args, **kwargs):
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    file = open(path, "w")
    towrite = args[1][0]
    file.write(towrite)
    file.close()
    kwargs["lock"].release()
    return towrite


def f_file_clear(inter, line, args, **kwargs):
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    file = open(path, "w")
    file.write("")
    file.close()
    kwargs["lock"].release()
    return True


def f_file_append(inter, line, args, **kwargs):
    from functions import file_append
    return file_append(kwargs["inst"], kwargs["lock"], kwargs["lines_ran"])


def f_file_delete(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    deleting = inter.parse(0, line, args)[2]
    # deleting must be str
    inter.type_err([(deleting, (str,))], line, kwargs["lines_ran"])
    try:
        os.remove(deleting)
    except:
        None
    kwargs["lock"].release()
    return deleting


def f_file_rename(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    old = inter.parse(0, line, args)[2]
    # old must be str
    inter.type_err([(old, (str,))], line, kwargs["lines_ran"])
    new = inter.parse(1, line, args)[2]
    # new must be str
    inter.type_err([(new, (str,))], line, kwargs["lines_ran"])
    os.rename(old, new)
    kwargs["lock"].release()
    return new


def f_file_copy(inter, line, args, **kwargs):
    import shutil
    kwargs["lock"].acquire()
    old = inter.parse(0, line, args)[2]
    # old must be str
    inter.type_err([(old, (str,))], line, kwargs["lines_ran"])
    new = inter.parse(1, line, args)[2]
    # new must be str
    inter.type_err([(new, (str,))], line, kwargs["lines_ran"])
    shutil.copy2(old, new)
    kwargs["lock"].release()
    return new


def f_file_copy2(inter, line, args, **kwargs):
    import shutil
    kwargs["lock"].acquire()
    old = inter.parse(0, line, args)[2]
    # old must be str
    inter.type_err([(old, (str,))], line, kwargs["lines_ran"])
    new = inter.parse(1, line, args)[2]
    # new must be str
    inter.type_err([(new, (str,))], line, kwargs["lines_ran"])
    shutil.copy2(old, new)
    kwargs["lock"].release()
    return new


def f_file_copyfile(inter, line, args, **kwargs):
    import shutil
    kwargs["lock"].acquire()
    old = inter.parse(0, line, args)[2]
    # old must be str
    inter.type_err([(old, (str,))], line, kwargs["lines_ran"])
    new = inter.parse(1, line, args)[2]
    # new must be str
    inter.type_err([(new, (str,))], line, kwargs["lines_ran"])
    shutil.copyfile(old, new)
    kwargs["lock"].release()
    return new


def f_file_fullpath(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    fullpath = os.path.abspath(path)
    kwargs["lock"].release()
    return fullpath


def f_file_move(inter, line, args, **kwargs):
    import shutil
    kwargs["lock"].acquire()
    old = inter.parse(0, line, args)[2]
    # old must be str
    inter.type_err([(old, (str,))], line, kwargs["lines_ran"])
    new = inter.parse(1, line, args)[2]
    # new must be str
    inter.type_err([(new, (str,))], line, kwargs["lines_ran"])
    shutil.move(old, new)
    kwargs["lock"].release()
    return new


def f_file_exists(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    exists = os.path.exists(path)
    kwargs["lock"].release()
    return exists


def f_file_isdir(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    isdir = os.path.isdir(path)
    kwargs["lock"].release()
    return isdir


def f_file_isfile(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    isfile = os.path.isfile(path)
    kwargs["lock"].release()
    return isfile


def f_file_listdir(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    try:
        listdir = os.listdir(path)
        kwargs["lock"].release()
        return listdir
    except FileNotFoundError:
        # directory doesn't exist
        kwargs["lock"].release()
        return None


def f_file_mkdir(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    try:
        made = os.mkdir(path)
        kwargs["lock"].release()
        return made
    except FileExistsError:
        kwargs["lock"].release()
        return False


def f_file_rmdir(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    try:
        rm = os.rmdir(path)
        kwargs["lock"].release()
        return rm
    except OSError:
        kwargs["lock"].release()
        return None


def f_file_getcwd(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    cwd = os.getcwd()
    kwargs["lock"].release()
    return cwd


def f_file_getsize(inter, line, args, **kwargs):
    import os
    kwargs["lock"].acquire()
    # get filename
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    size = os.path.getsize(path)
    kwargs["lock"].release()
    return size


def f_file_emptydir(inter, line, args, **kwargs):
    import os
    import shutil
    kwargs["lock"].acquire()
    directory = inter.parse(0, line, args)[2]
    # directory must be str
    inter.type_err([(directory, (str,))], line, kwargs["lines_ran"])
    try:
        for file in os.listdir(directory):
            try:
                os.remove(os.path.join(directory, file))
            except:
                shutil.rmtree(
                    os.path.join(directory, file),
                    ignore_errors=True,
                )
        kwargs["lock"].release()
        return directory
    except FileNotFoundError:
        # directory doesn't exist
        kwargs["lock"].release()
        return None


def f_auto_largest(inter, line, args, **kwargs):
    elements = inter.parse(0, line, args)[2]
    # elements must be iterable
    inter.check_iterable(elements, line)
    if not elements:
        return elements
    largest = elements[0]
    for element in elements:
        try:
            # element has width and height
            if (
                element.width() > largest.width()
                and element.height() > largest.height()
            ):
                largest = element
        except:
            # element does not have width and height
            return element
    return largest


def f_auto_file(inter, line, args, **kwargs):
    # imports
    import os
    from tkinter import Tk
    from tkinter.filedialog import askopenfilename

    Tk().withdraw()
    # you can only run .msn2 scripts
    return askopenfilename(
        initialdir=os.getcwd(),
        filetypes=[("MSN2 Script", "*.msn2")],
    )


def f_auto_else():
    return "<msnint2 class>"


def _try_math(inter, func, line, **kwargs):
    try:
        return func()
    except Exception as e:
        return inter.err(
            "Error in math class",
            f"Unable to perform computation\n{e}",
            line,
            kwargs["lines_ran"],
        )


def f_math_add(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] + inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_subtract(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] - inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_multiply(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] * inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_divide(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] / inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_power(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] ** inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_root(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2]
        ** (1 / inter.parse(1, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_sqrt(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] ** 0.5,
        line,
        **kwargs,
    )


def f_math_mod(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: inter.parse(0, line, args)[2] % inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )


def f_math_floor(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.floor(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_ceil(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.ceil(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_round(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: round(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_abs(inter, line, args, **kwargs):
    return _try_math(
        inter,
        lambda: abs(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_sin(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.sin(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_cos(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.cos(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_tan(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.tan(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_asin(inter, line, args, **kwargs):
    import math
    return _try_math(
        inter,
        lambda: math.asin(inter.parse(0, line, args)[2]),
        line,
        **kwargs,
    )


def f_math_else():
    return "<msnint2 class>"


def f_quickcond(inter, line, args, **kwargs):
    kwargs["func"] = kwargs["func"][1:]
    ret = None
    if inter.interpret(kwargs["func"]):
        ret = inter.interpret(args[0][0])
    else:
        # else block is optional
        try:
            ret = inter.interpret(args[1][0])
        except:
            None
    return ret

# if the function is an integer


def f_int_loop(inter, line, args, **kwargs):
    ret = None
    for _ in range(kwargs["_i"]):
        ret = inter.parse(0, line, args)[2]
    return ret

# if the function is a variable name, this is for variable name based loops


def f_varname_loop(inter, line, args, **kwargs):
    # value
    val = inter.vars[kwargs["func"]].value
    # if the variable is an integer,
    # run the arguments as blocks inside
    # that many times
    if isinstance(val, int):
        ret = None
        for i in range(val):
            ret = inter.parse(0, line, args)[2]
        return ret
    # otherwise return the value
    return val


def f_pointer_getpos(inter, line, args, **kwargs):
    import win32api
    return win32api.GetCursorPos()


def f_pointer_move(inter, line, args, **kwargs):
    from pywinauto import mouse
    return mouse.move(
        coords=(
            inter.parse(0, line, args)[2],
            inter.parse(1, line, args)[2],
        )
    )


def f_pointer_click(inter, line, args, **kwargs):
    from pywinauto import mouse
    if len(args) == 2:
        return mouse.click(
            coords=(
                inter.parse(0, line, args)[2],
                inter.parse(1, line, args)[2],
            )
        )
    else:
        import win32api
        return mouse.click(coords=win32api.GetCursorPos())


def f_pointer_right_click(inter, line, args, **kwargs):
    # if args are provided
    if len(args) == 2:
        from pywinauto import mouse
        start = inter.parse(0, line, args)[2]
        # start must be int
        inter.type_err([(start, (int,))], line, kwargs["lines_ran"])
        end = inter.parse(1, line, args)[2]
        # end must be int
        inter.type_err([(end, (int,))], line, kwargs["lines_ran"])
        return mouse.right_click(coords=(start, end))
    # if no args are provided
    else:
        import win32api
        return mouse.right_click(coords=win32api.GetCursorPos())


def f_pointer_double_click(inter, line, args, **kwargs):
    # if args are provided
    if len(args) == 2:
        from pywinauto import mouse
        start = inter.parse(0, line, args)[2]
        # start must be int
        inter.type_err([(start, (int,))], line, kwargs["lines_ran"])
        end = inter.parse(1, line, args)[2]
        # end must be int
        inter.type_err([(end, (int,))], line, kwargs["lines_ran"])
        return mouse.double_click(coords=(start, end))
    # if no args are provided
    else:
        import win32api
        return mouse.double_click(coords=win32api.GetCursorPos())


def f_pointer_scroll_bottom(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    return mouse.scroll(
        wheel_dist=9999999, coords=win32api.GetCursorPos()
    )


def f_pointer_scroll_top(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    return mouse.scroll(
        wheel_dist=-9999999, coords=win32api.GetCursorPos()
    )


def f_pointer_scroll(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    dist = inter.parse(0, line, args)[2]
    # dist must be int
    inter.type_err([(dist, (int,))], line, kwargs["lines_ran"])
    return mouse.scroll(
        wheel_dist=dist, coords=win32api.GetCursorPos()
    )


def f_pointer_left_down(inter, line, args, **kwargs):
    import win32api
    return win32api.GetKeyState(0x01) < 0


def f_pointer_right_down(inter, line, args, **kwargs):
    import win32api
    return win32api.GetKeyState(0x02) < 0


def f_pointer_wait_left(inter, line, args, **kwargs):
    import win32api
    while True:
        if win32api.GetKeyState(0x01) < 0:
            break
    return True


def f_pointer_wait_right(inter, line, args, **kwargs):
    import win32api
    while True:
        if win32api.GetKeyState(0x02) < 0:
            break
    return True


def f_pointer_wait_left_click(inter, line, args, **kwargs):
    import win32api
    while True:
        if win32api.GetKeyState(0x01) < 0:
            break
    while True:
        if win32api.GetKeyState(0x01) >= 0:
            break
    return True


def f_pointer_wait_right_click(inter, line, args, **kwargs):
    import win32api
    while True:
        if win32api.GetKeyState(0x02) < 0:
            break
    while True:
        if win32api.GetKeyState(0x02) >= 0:
            break
    return True


def f_pointer_down(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    curr_x, curr_y = win32api.GetCursorPos()
    moving = inter.parse(0, line, args)[2]
    # moving must be int
    inter.type_err([(moving, (int,))], line, kwargs["lines_ran"])
    return mouse.move(coords=(curr_x, curr_y + moving))


def f_pointer_up(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    curr_x, curr_y = win32api.GetCursorPos()
    moving = inter.parse(0, line, args)[2]
    # moving must be int
    inter.type_err([(moving, (int,))], line, kwargs["lines_ran"])
    return mouse.move(coords=(curr_x, curr_y - moving))


def f_pointer_left(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    curr_x, curr_y = win32api.GetCursorPos()
    moving = inter.parse(0, line, args)[2]
    # moving must be int
    inter.type_err([(moving, (int,))], line, kwargs["lines_ran"])
    return mouse.move(coords=(curr_x - moving, curr_y))


def f_pointer_right(inter, line, args, **kwargs):
    import win32api
    from pywinauto import mouse
    curr_x, curr_y = win32api.GetCursorPos()
    moving = inter.parse(0, line, args)[2]
    # moving must be int
    inter.type_err([(moving, (int,))], line, kwargs["lines_ran"])
    return mouse.move(coords=(curr_x + moving, curr_y))


def f_pointer_drag(inter, line, args, **kwargs):
    import time
    from pywinauto import mouse
    start = (
        inter.parse(0, line, args)[2],
        inter.parse(1, line, args)[2],
    )
    # start[1] and [2] must be int
    inter.type_err(
        [(start[0], (int,)), (start[1], (int,))], line, kwargs["lines_ran"]
    )
    end = (
        inter.parse(2, line, args)[2],
        inter.parse(3, line, args)[2],
    )
    # end[1] and [2] must be int
    inter.type_err(
        [(end[0], (int,)), (end[1], (int,))], line, kwargs["lines_ran"]
    )
    # presses the mouse down at the coordinates
    mouse.press(coords=start)
    # slowly moves the mouse to the end coordinates
    # this is to prevent the mouse from moving too fast
    # and not dragging the object
    # the farther the distance, the longer it takes
    # to move the mouse
    speed = 50
    if len(args) == 5:
        speed = inter.parse(4, line, args)[2]
        # speed must be int
        inter.type_err([(speed, (int,))], line, kwargs["lines_ran"])
    # reverse the speed, so a speed of 50 gives
    # end_range of 50, and a speed of 75 gives
    # end_range of 25
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
    return True


# function dispatch
# function dispatch
FUNCTION_DISPATCH = {
    **REDIRECTS_DISPATCH,
    **FUNCTION_BASED_DISPATCH,
    **VARS_DISPATCH,
    **MATH_DISPATCH,
    **STRINGS_DISPATCH,
    **NUMBERS_DISPATCH,
    **TYPE_TESTING_DISPATCH,
    **ITERABLES_DISPATCH,
    **ASSERTIONS_DISPATCH,
    **SYSTEM_DISPATCH,
    **OBJECT_GENERAL_DISPATCH,
    **SYNTAX_DISPATCH,
    **LOGICAL_DISPATCH,
    **DOMAINS_DISPATCH,
    **IN_OUT_DISPATCH,
    **STDOUT_DISPATCH,
    **JS_DISPATCH,
    **TIME_DISPATCH,
    **CONTEXTS_DISPATCH,
    **MULTIPROGRAMMING_DISPATCH,
    **POINTER_DISPATCH,
    **API_DISPATCH,
    **MISC_DISPATCH,
    **INSERTIONS_DISPATCH,
    **LANG_DISPATCH,
    **WIN_AUTO_DISPATCH,
    **EXCEL_DISPATCH,
    **CAST_DISPATCH,
    **CONDITIONALS_DISPATCH,

    # special calls
    "special": {
        **SPECIAL_LOOPS_DISPATCH,
    },

    # msn2 classes
    "obj": {
        "general": {
            **OBJ_GENERAL_DESTRUCTIVE_DISPATCH,

            # integers, floats, and complex numbers
            "number": {
                **OBJ_GENERAL_NUMBER_COMPARISONS_DISPATCH,
                **OBJ_GENERAL_NUMBER_OPS_DISPATCH,
                **OBJ_GENERAL_NUMBER_OPS_IP_DISPATCH,
            },
            "set": {
                **OBJ_GENERAL_SET_BASIC_DISPATCH,
            },
            "list": {
                **OBJ_GENERAL_LIST_ACCESS_DISPATCH,
                **OBJ_GENERAL_LIST_MODIFY_DISPATCH,
            },
            "str": {
                **OBJ_GENERAL_STR_ACCESS_DISPATCH,
                **OBJ_GENERAL_STR_MODIFY_DISPATCH,
            },
            "dict": {
                **OBJ_GENERAL_DICT_ACCESS_DISPATCH,
                **OBJ_GENERAL_DICT_MODIFY_DISPATCH,
            },

            "class_based": {
                **OBJ_GENERAL_CLASS_BASED_REQUESTS_HTML_HTMLSession_DISPATCH,
                **OBJ_GENERAL_CLASS_BASED_REQUESTS_HTML_HTML_DISPATCH,
            },


            "default": {
                **OBJ_GENERAL_DEFAULT_CHAINED_DISPATCH,
                **OBJ_GENERAL_DEFAULT_OPS_DISPATCH,
                **OBJ_GENERAL_DEFAULT_STRINGS_DISPATCH,
                **OBJ_GENERAL_DEFAULT_GENERAL_DISPATCH,
                **OBJ_GENERAL_DEFAULT_PROPERTIES_DISPATCH,
                **OBJ_GENERAL_DEFAULT_CAST_DISPATCH,
            }
        },
        "instance": {
            **OBJ_INSTANCE_CREATION_DISPATCH,
        },
        "trace": {
            **OBJ_TRACE_GENERAL_DISPATCH,
        },
        "py": {
            **OBJ_PY_ACCESS_DISPATCH,
            **OBJ_PY_RUN_DISPATCH,
            **OBJ_PY_DEFAULT_DISPATCH,
        },
        "op": {
            **OBJ_OP_BASIC_DISPATCH,
            **OBJ_OP_DEFAULT_DISPATCH
        },
        "function": {
            **OBJ_FUNCTION_ACCESS_DISPATCH,
            **OBJ_FUNCTION_MODIFY_DISPATCH,
            **OBJ_FUNCTION_RUN_DISPATCH,
            **OBJ_FUNCTION_DEFAULT_DISPATCH,
        },
        "html": {
            **OBJ_HTML_BASIC_DISPATCH,
            **OBJ_HTML_DEFAULT_DISPATCH,
        },
        "ai": {
            "models": f_ai_models,
            "max_tokens": f_ai_max_tokens,
            "price_per_token": f_ai_price_per_token,
            "basic": f_ai_basic,
            "advanced": f_ai_advanced,
            "query": f_ai_query,
            "tokens": f_ai_tokens,
            "split_string": f_ai_split_string,
            "else": f_ai_else,
        },
        "var": {
            "equals": f_var_equals,
            "else": f_var_else,
        },
        "file": {
            "create": f_file_create,
            "read": f_file_read,
            "write": f_file_write,
            "writemsn": f_file_writemsn,
            "clear": f_file_clear,
            "append": f_file_append,
            "delete": f_file_delete,
            "rename": f_file_rename,
            "copy": f_file_copy,
            "copy2": f_file_copy2,
            "copyfile": f_file_copyfile,
            "fullpath": f_file_fullpath,
            "move": f_file_move,
            "exists": f_file_exists,
            "isdir": f_file_isdir,
            "isfile": f_file_isfile,
            "listdir": f_file_listdir,
            "mkdir": f_file_mkdir,
            "rmdir": f_file_rmdir,
            "getcwd": f_file_getcwd,
            "getsize": f_file_getsize,
            "emptydir": f_file_emptydir,
        },
        "auto": {
            "largest": f_auto_largest,
            "file": f_auto_file,
            "else": f_auto_else,
        },
        "math": {
            "add": f_math_add,
            "subtract": f_math_subtract,
            "multiply": f_math_multiply,
            "divide": f_math_divide,
            "power": f_math_power,
            "root": f_math_root,
            "sqrt": f_math_sqrt,
            "mod": f_math_mod,
            "floor": f_math_floor,
            "ceil": f_math_ceil,
            "round": f_math_round,
            "abs": f_math_abs,
            "sin": f_math_sin,
            "cos": f_math_cos,
            "tan": f_math_tan,
            "asin": f_math_asin,
            "else": f_math_else,
        },
        "pointer": {
            "right_click": f_pointer_right_click,
            "double_click": f_pointer_double_click,
            "scroll_bottom": f_pointer_scroll_bottom,
            "scroll_top": f_pointer_scroll_top,
            "scroll": f_pointer_scroll,
            "left_down": f_pointer_left_down,
            "right_down": f_pointer_right_down,
            "wait_left": f_pointer_wait_left,
            "wait_right": f_pointer_wait_right,
            "wait_left_click": f_pointer_wait_left_click,
            "wait_right_click": f_pointer_wait_right_click,
            "down": f_pointer_down,
            "up": f_pointer_up,
            "left": f_pointer_left,
            "right": f_pointer_right,
            "drag": f_pointer_drag,
            **aliases(f_pointer_getpos, ("getpos", "pos", "position")),
            **aliases(f_pointer_move, ("move", "hover")),
            **aliases(f_pointer_click, ("click", "left_click")),
        }
    }
}
