"""Joint functions and dispatch table."""


# utilties
from core.common import aliases

# common
from core.obj.ai.default import OBJ_AI_DEFAULT_DISPATCH
from core.obj.ai.info import OBJ_AI_INFO_DISPATCH
from core.obj.ai.querying import OBJ_AI_QUERYING_DISPATCH
from core.obj.auto.basic import OBJ_AUTO_BASIC_DISPATCH
from core.obj.auto.default import OBJ_AUTO_DEFAULT_DISPATCH
from core.obj.file.access import OBJ_FILE_ACCESS_DISPATCH
from core.obj.file.modify import OBJ_FILE_MODIFY_DISPATCH
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
from core.obj.int_var.basic import OBJ_INT_VAR_BASIC_DISPATCH
from core.obj.int_var.default import OBJ_INT_VAR_DEFAULT_DISPATCH
from core.obj.math.advanced import OBJ_MATH_ADVANCED_DISPATCH
from core.obj.math.basic import OBJ_MATH_BASIC_DISPATCH
from core.obj.math.default import OBJ_MATH_DEFAULT_DISPATCH
from core.obj.math.trig import OBJ_MATH_TRIG_DISPATCH
from core.obj.obj_instance.creation import OBJ_INSTANCE_CREATION_DISPATCH
from core.obj.op.basic import OBJ_OP_BASIC_DISPATCH
from core.obj.op.default import OBJ_OP_DEFAULT_DISPATCH
from core.obj.pointer.buttons import OBJ_POINTER_BUTTONS_DISPATCH
from core.obj.pointer.position import OBJ_POINTER_POSITION_DISPATCH
from core.obj.pointer.scrolling import OBJ_POINTER_SCROLLING_DISPATCH
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
            **OBJ_AI_INFO_DISPATCH,
            **OBJ_AI_QUERYING_DISPATCH,
            **OBJ_AI_DEFAULT_DISPATCH,
        },
        "var": {
            **OBJ_INT_VAR_BASIC_DISPATCH,
            **OBJ_INT_VAR_DEFAULT_DISPATCH,
        },
        "file": {
            **OBJ_FILE_ACCESS_DISPATCH,
            **OBJ_FILE_MODIFY_DISPATCH,
        },
        "auto": {
            **OBJ_AUTO_BASIC_DISPATCH,
            **OBJ_AUTO_DEFAULT_DISPATCH,
        },
        "math": {
            **OBJ_MATH_BASIC_DISPATCH,
            **OBJ_MATH_ADVANCED_DISPATCH,
            **OBJ_MATH_TRIG_DISPATCH,
            **OBJ_MATH_DEFAULT_DISPATCH,
        },
        "pointer": {
            **OBJ_POINTER_BUTTONS_DISPATCH,
            **OBJ_POINTER_POSITION_DISPATCH,
            **OBJ_POINTER_SCROLLING_DISPATCH,
        }
    }
}
