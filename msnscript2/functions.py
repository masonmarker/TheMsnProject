"""Joint functions and dispatch table."""

from msnint2 import Var


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


def hyphen(inst):
    if len(inst.args) == 1:
        return inst.interpreter.interpret(inst.parse(0))
    # subtracts all arguments from the first argument
    else:
        ret = inst.parse(0)
        try:
            ret = ret.copy()
        except AttributeError:
            None
        for i in range(1, len(inst.args)):
            ret -= inst.parse(i)
        return ret

# defines a function


def define(inst, lines_ran):
    from method import Method
    # get the name of the new function
    name = inst.parse(0)
    # function name must be a string
    inst.type_err([(name, (str,))], lines_ran)
    # create the new function
    new_func = Method(name, inst.interpreter)
    func_args = []
    # __temp = self.Method('', self)
    __temp = Method('', inst.interpreter)
    # get the args for this function between the name and the body
    # parse all args between the name and the body
    for i in range(1, len(inst.args) - 1):
        # get the stripped string rep of this arg
        as_s = inst.args[i][0].strip()
        if as_s[0] == '&':
            func_args, meth_argname, _, ind = inst.interpreter.split_named_arg(
                as_s, __temp, func_args)
            # add argument with default value
            new_func.add_arg([meth_argname, func_args[ind]])
        else:
            # add this argument to the method
            new_func.add_arg(inst.parse(i))
            new_arg = inst.parse(i)
            # self.type_err([(new_arg, (str,))], line, lines_ran)
            inst.type_err([(new_arg, (str,))], lines_ran)
    # add the body
    new_func.add_body(f"ret('{name}',{inst.args[-1][0]})")
    # return buffer variable name
    r_name = f"{name}__return__"
    # if the return buffer doesn't exist, create it
    if r_name not in inst.interpreter.vars:
        from msnint2 import Var
        # create the return variable
        inst.interpreter.vars[r_name] = Var(r_name, None)
    # add the return variable
    new_func.add_return(r_name)
    # add the function to the methods
    inst.interpreter.methods[name] = new_func
    # return the name of the function
    return name

# runs a user function


# interprets a multi-lined function call


def multi_lined(inst):
    ret = None
    for i in range(len(inst.args)):
        ret = inst.parse(i)
    return ret

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


# general functions
def f_redirect(inter, line: str, args, **kwargs):
    inter.redirect_inside = []
    # creates redirect for this interpreter
    # check for type errors
    linevar = inter.parse(0, line, args)[2]
    inter.type_err([(linevar, (str,))], line, kwargs["lines_ran"])
    inter.redirect = [linevar, args[1][0]]
    inter.redirecting = True
    return inter.redirect


def f_stopredirect(inter, line: str, args, **kwargs):
    inter.redirecting = False
    return True


def f_startredirect(inter, line: str, args, **kwargs):
    ret = None
    for _ins in inter.redirect_inside:
        inter.vars[inter.redirect[0]] = Var(
            inter.redirect[0], _ins[1])
        ret = inter.interpret(_ins[0])
    return ret


def f_function(inter, line: str, args, **kwargs):
    from method import Method
    # obtain the name of the function
    fname = inter.parse(0, line, args)[2]
    # function name must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    # function arguments
    # create the new Method
    new_method = Method(fname, inter)
    # add the body
    new_method.add_body(args[1][0])
    new_method.add_return(f"{fname}__return__")
    # obtain the rest of the arguments as method args
    for i in range(2, len(args)):
        # adds variable name as an argument
        # if any function specific argument is None, break
        val = inter.parse(i, line, args)[2]
        # val must be a string
        inter.type_err([(val, (str,))], line, kwargs["lines_ran"])
        if val == None:
            break
        new_method.add_arg(val)
    inter.methods[fname] = new_method
    return fname


def f_def(inter, line: str, args, **kwargs):
    return define(kwargs["inst"], kwargs["lines_ran"])


def f_mod(inter, line: str, args, **kwargs):
    arg1 = inter.parse(0, line, args)[2]
    arg2 = inter.parse(1, line, args)[2]
    _allowed = (int, float, complex, str)
    # verify both arguments are either int or float or complex or str
    inter.type_err(
        [
            (arg1, _allowed),
            (arg2, _allowed),
        ],
        line,
        kwargs["lines_ran"],
    )
    # both types must be int or float
    return arg1 % arg2


def f_ret(inter, line: str, args, **kwargs):
    name = inter.parse(0, line, args)[2]
    # name must be a string
    inter.type_err([(name, (str,))], line, kwargs["lines_ran"])
    # function to return to
    vname = f"{name}__return__"
    # value
    value = inter.parse(1, line, args)[2]
    # if the variable does not exist, we should create it
    if vname not in inter.vars:
        inter.vars[vname] = Var(vname, None)
    inter.vars[vname].value = value
    return value


def f_arr(inter, line: str, args, **kwargs):
    if args[0][0] == "":
        return []
    return [inter.parse(i, line, args)[2] for i in range(len(args))]


def f_object(inter, line: str, args, **kwargs):
    d = {}
    if args[0][0] == "":
        return d
    # cannot have an odd number of arguments
    if len(args) % 2 == 1:
        inter.err(
            "Odd number of arguments in creating object",
            f"An even number of arguments required to have valid key-value pairs.\nYou said: {len(args)} arg(s)",
            line,
            kwargs["lines_ran"],
        )
    # step over arguments in steps of 2
    for i in range(0, len(args), 2):
        d[inter.parse(i, line, args)[2]] = inter.parse(i + 1, line, args)[2]
    return d


def f_split(inter, line: str, args, **kwargs):
    first = inter.parse(0, line, args)[2]
    second = inter.parse(1, line, args)[2]
    # first and second must be str
    inter.type_err([(first, (str,)), (second, (str,))],
                   line, kwargs["lines_ran"])
    return inter.parse(0, line, args)[2].split(
        inter.parse(1, line, args)[2]
    )


def f_lines(inter, line: str, args, **kwargs):
    return inter.parse(0, line, args)[2].split("\n")


def f_eval(inter, line: str, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be a str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    return eval(arg)


def f_between(inter, line: str, args, **kwargs):
    # surrounding token
    surrounding = inter.parse(0, line, args)[2]
    # surrounding must be str
    inter.type_err([(surrounding, (str,))], line, kwargs["lines_ran"])
    # string to analyze
    string = inter.parse(1, line, args)[2]
    # string must be str
    inter.type_err([(string, (str,))], line, kwargs["lines_ran"])
    funccalls = []
    try:
        while string.count(surrounding) > 1:
            string = string[
                string.index(surrounding) + len(surrounding):
            ]
            funccalls.append(string[: string.index(surrounding)])
            string = string[
                string.index(surrounding) + len(surrounding):
            ]
    except:
        None
    return funccalls


def f_isstr(inter, line: str, args, **kwargs):
    return isinstance(inter.parse(0, line, args)[2], str)


def f_islist(inter, line: str, args, **kwargs):
    return isinstance(inter.parse(0, line, args)[2], list)


def f_isfloat(inter, line: str, args, **kwargs):
    return isinstance(inter.parse(0, line, args)[2], float)


def f_isint(inter, line: str, args, **kwargs):
    return isinstance(inter.parse(0, line, args)[2], int)


def f_isdict(inter, line: str, args, **kwargs):
    return isinstance(inter.parse(0, line, args)[2], dict)


def f_isinstance(inter, line: str, args, **kwargs):
    return isinstance(
        inter.parse(0, line, args)[2], inter.parse(1, line, args)[2]
    )


def f_sum(inter, line: str, args, **kwargs):
    total = 0
    for i in range(len(args)):
        try:
            total += sum(inter.parse(i, line, args)[2])
        except:
            try:
                total += inter.parse(i, line, args)[2]
            except Exception as e:
                inter.err("Error computing sum", e, line, kwargs["lines_ran"])
    return total


def f_var(inter, line: str, args, **kwargs):
    # extract varname
    varname = inter.parse(0, line, args)[2]
    # must be varname
    inter.check_varname(varname, line)
    # extract value
    value = inter.parse(1, line, args)[2]
    # add / set variable
    inter.vars[varname] = Var(varname, value)
    return value


def f_list(inter, line: str, args, **kwargs):
    try:
        return list(inter.parse(0, line, args)[2])
    except:
        return inter.err(
            "Casting error",
            "Could not cast arg to a list",
            line,
            kwargs["lines_ran"],
        )


def f_abs(inter, line: str, args, **kwargs):
    try:
        return abs(inter.parse(0, line, args)[2])
    except:
        return inter.err(
            "Error computing absolute value",
            f"Could not compute absolute value of arg\nConsider changing arg to a number",
            line,
            kwargs["lines_ran"],
        )


def f_zip(inter, line: str, args, **kwargs):
    first = inter.parse(0, line, args)[2]
    second = inter.parse(1, line, args)[2]
    # verify both are iterable
    inter.check_iterable(first, line)
    inter.check_iterable(second, line)
    return zip(first, second)


def f_next(inter, line: str, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be iterable
    inter.check_iterable(arg, line)
    return next(arg)


def f_iter(inter, line: str, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be iterable
    inter.check_iterable(arg, line)
    return iter(arg)


def f_exists(inter, line: str, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    return arg in inter.vars


def f_exists_function(inter, line: str, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    return arg in inter.methods


def f_len(inter, line: str, args, **kwargs):
    # get the first argument
    arg = inter.parse(0, line, args)[2]
    # arg must be iterable
    inter.check_iterable(arg, line)
    return len(arg)


def f_assert(inter, line: str, args, **kwargs):
    for i in range(len(args)):
        assertion = inter.parse(i, line, args)[2]
        if not assertion:
            failed = ""
            for arg in args:
                failed += f"{assertion} "
            inter.err(
                f"Assertion error in '{line}'",
                assertion,
                failed,
                kwargs["lines_ran"],
            )
    return True


def f_assert_err(inter, line: str, args, **kwargs):
    for i in range(len(args)):
        thrown = True
        try:
            # set inter.trying to True
            inter.trying = True
            # execute the line
            ret = inter.parse(i, line, args)[2]
            thrown = False
            inter.trying = False
        except:
            thrown = True
        if not thrown:
            inter.err(
                f"Assertion error, expected error",
                "No error thrown where one was expected",
                line,
                kwargs["lines_ran"],
            )
    return True


def f_settings(inter, line: str, args, **kwargs):
    return inter.settings


def f_trace_this(inter, line: str, args, **kwargs):
    return kwargs["lines_ran"][-1]


def f_trace_before(inter, line: str, args, **kwargs):
    if args[0][0] == "":
        # return all
        return kwargs["lines_ran"]
    numlines = inter.parse(0, line, args)[2]
    # numlines must be int
    inter.type_err([(numlines, (int,))], line, kwargs["lines_ran"])
    return kwargs["lines_ran"][len(kwargs["lines_ran"]) - numlines:]


def f_trace_len(inter, line: str, args, **kwargs):
    return kwargs["total_ints"]


def f_settings(inter, line: str, args, **kwargs):
    return inter.settings


def f_py_get(inter, line: str, args, **kwargs):
    vn = ""
    name = inter.parse(0, line, args)[2]
    # name must be str
    inter.type_err([(name, (str,))], line, kwargs["lines_ran"])
    try:
        return inter._locals[name]
    except KeyError:
        inter.no_var_err(vn, "local", "local", inter._locals, line)


def f_py_set(inter, line: str, args, **kwargs):
    # name
    vn = inter.parse(0, line, args)[2]
    # vn must be str
    inter.type_err([(vn, (str,))], line, kwargs["lines_ran"])
    value = inter.parse(1, line, args)[2]
    inter._locals[vn] = value
    return value


def f_py_locals(inter, line: str, args, **kwargs):
    return inter._locals


def f_py_local(inter, line: str, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be str
    inter.type_err([(vn, (str,))], line, kwargs["lines_ran"])
    try:
        return inter._locals[vn]
    except KeyError:
        inter.no_var_err(vn, "local", "local", inter._locals, line)


def f_py_globals(inter, line: str, args, **kwargs):
    return inter._globals


def f_py_global(inter, line: str, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be str
    inter.type_err([(vn, (str,))], line, kwargs["lines_ran"])
    try:
        return inter._globals[vn]
    except:
        inter.no_var_err(vn, "global", "global", inter._globals, line)


def f_py_run(inter, line: str, args, **kwargs):
    # get the script
    scr = inter.parse(0, line, args)[2]
    # remove all lines starting with '#'
    scr = "\n".join(
        [i for i in scr.split("\n") if not i.startswith("#")]
    )
    # scr must be str
    inter.type_err([(scr, (str,))], line, kwargs["lines_ran"])
    # execute the python and return
    # the snippet with arguments inserted
    return inter.exec_python(scr)


def f_py_else(inter, line: str, args, **kwargs):
    try:
        # check in locals
        return inter._locals[kwargs["objfunc"]]
    except KeyError:
        try:
            # check in globals
            return inter._globals[kwargs["objfunc"]]
        except Exception as e:
            print(e)
            # raise error
            inter.no_var_err(
                kwargs["objfunc"],
                "local or global",
                "local and global",
                inter._globals,
                line,
            )


def _try_cast(inter, func, line: str, **kwargs):
    try:
        return func()
    except Exception as e:
        inter.err(
            "Casting error",
            f"Could not cast arg to specified type\n{e}",
            line,
            kwargs["lines_ran"],
        )
# casting functions


def f_int(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: int(inter.parse(0, line, args)[2]), line, **kwargs)


def f_float(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: float(inter.parse(0, line, args)[2]), line, **kwargs)


def f_str(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: str(inter.parse(0, line, args)[2]), line, **kwargs)


def f_bool(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: bool(inter.parse(0, line, args)[2]), line, **kwargs)


def f_complex(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: complex(inter.parse(0, line, args)[2]), line, **kwargs)


def f_type(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: type(inter.parse(0, line, args)[2]), line, **kwargs)


def f_dir(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: dir(inter.parse(0, line, args)[2]), line, **kwargs)


def f_set(inter, line: str, args, **kwargs):
    if args[0][0] == "":
        return set()
    s = set()
    for i in range(len(args)):
        s.add(inter.parse(i, line, args)[2])
    return s


def f_dict(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: dict(inter.parse(0, line, args)[2]), line, **kwargs)


def f_tuple(inter, line: str, args, **kwargs):
    return _try_cast(inter, lambda: tuple(inter.parse(0, line, args)[2]), line, **kwargs)


# conditional logic
def f_if(inter, line: str, args, **kwargs):
    # false block is optional
    try:
        false_block_s = args[2][0]
    except:
        false_block_s = None
    ifcond = inter.parse(0, line, args)[2]
    # if condition is true
    if ifcond:
        return inter.parse(1, line, args)[2]
    # otherwise false block is executed
    if false_block_s:
        return inter.parse(2, line, args)[2]
    return False


def f_while(inter, line: str, args, **kwargs):
    while inter.interpret(args[0][0]):
        inter.interpret(args[1][0])
    return True


def f_for(inter, line: str, args, **kwargs):
    # times to loop
    start = inter.parse(0, line, args)[2]
    end = inter.parse(1, line, args)[2]
    loopvar = inter.parse(2, line, args)[2]
    # start must be int
    # end must be int
    # loopvar must be str
    inter.type_err(
        [(start, (int,)), (end, (int,)), (loopvar, (str,))],
        line,
        kwargs["lines_ran"],
    )
    inter.vars[loopvar] = Var(loopvar, start)
    # regular iteration
    if start < end:
        for i in range(start, end):
            if loopvar in inter.vars and inter.vars[loopvar].value >= end:
                break
            inter.vars[loopvar] = Var(loopvar, i)
            inter.interpret(args[3][0])
    # reversed if requested
    elif start > end:
        for i in reversed(range(end, start)):
            if loopvar in inter.vars and inter.vars[loopvar].value < end:
                break
            inter.vars[loopvar] = Var(loopvar, i)
            inter.interpret(args[3][0])
    return inter.vars[loopvar].value


def f_each(inter, line: str, args, **kwargs):
    # get array argument
    array = inter.parse(0, line, args)[2]
    # get element variable name
    element_name = inter.parse(1, line, args)[2]
    # prepare each element
    inter.vars[element_name] = Var(element_name, 0)
    # execute block for each element
    for i in range(len(array)):
        inter.vars[element_name].value = array[i]
        inter.interpret(args[2][0])
    return array


def f_sortby(inter, line: str, args, **kwargs):
    # iterable to sort
    iterable = inter.parse(0, line, args)[2]
    # iterable must be an iterable
    inter.check_iterable(iterable, line)
    # variable name
    varname = inter.parse(1, line, args)[2]
    # check variable name
    inter.check_varname(varname, line)
    # pairing elements to their interpretations
    pairing = []
    for i in range(len(iterable)):
        inter.vars[varname] = Var(varname, iterable[i])
        pairing.append((inter.interpret(args[2][0]), iterable[i]))
    # sort the pairing based on the first element of each pair
    pairing.sort(key=lambda x: x[0])
    # return the sorted array containing the second element
    # of each pair
    return [pair[1] for pair in pairing]


def f_comp(inter, line: str, args, **kwargs):
    lst = []
    # array to comprehend
    arr = inter.parse(0, line, args)[2]
    # should be an iterable
    inter.check_iterable(arr, line)
    # varname for the element
    varname = inter.parse(1, line, args)[2]
    # should be a varname
    inter.check_varname(varname, line)
    # block to perform
    block = args[2][0]
    # performs the list comprehension
    for v in arr:
        inter.vars[varname] = Var(varname, v)
        r = inter.interpret(block)
        if r != kwargs["msn2_none"]:
            lst.append(r)
    return lst


def f_do(inter, line: str, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    inter.interpret(args[1][0])
    return ret


def f_None(inter, line: str, args, **kwargs):
    return kwargs["msn2_none"]


def f_filter(inter, line: str, args, **kwargs):
    # iterable to filter
    iterable = inter.parse(0, line, args)[2]
    # check if iterable
    inter.check_iterable(iterable, line)
    # variable name
    varname = inter.parse(1, line, args)[2]
    # check variable name
    inter.check_varname(varname, line)
    # new array
    filtered = []
    # iterate through each element
    for v in iterable:
        # set the variable to the element
        inter.vars[varname] = Var(varname, v)
        # if the block returns true, add the element to the new array
        if inter.interpret(args[2][0]):
            filtered.append(v)
    return filtered


def f_unpack(inter, line: str, args, **kwargs):
    # iterable to unpack
    iterable = inter.parse(0, line, args)[2]
    # check if iterable
    inter.check_iterable(iterable, line)
    # variable names to unpack into
    for i in range(1, len(args)):
        varname = inter.parse(i, line, args)[2]
        inter.vars[varname] = Var(varname, iterable[i - 1])
    return iterable


def f_has(inter, line: str, args, **kwargs):
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    # optimized code:
    try:
        return all(
            inter.parse(i + 1, line, args)[2] in iterable
            for i in range(len(args) - 1)
        )
    except Exception as e:
        return inter.err("Error in has()", e, line, kwargs["lines_ran"])


def f_first(inter, line: str, args, **kwargs):
    try:
        return inter.parse(0, line, args)[2][0]
    except:
        return


def f_add(inter, line: str, args, **kwargs):
    first = inter.parse(0, line, args)[2]
    second = inter.parse(1, line, args)[2]
    # first must be a varname
    inter.check_varname(first, line)
    # case array
    if isinstance(inter.vars[first].value, list):
        inter.vars[first].value.append(second)
    # case string or number
    else:
        inter.vars[first].value += second
    return


def _try_op(inter, func, line, lines_ran):
    try:
        return func()
    except Exception as e:
        inter.err(
            "Error in op() class",
            f"Could not perform operation on arguments\n{e}",
            line,
            lines_ran,
        )


def f_op_append(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_append(inter, line, args), line, kwargs["lines_ran"])

def _f_op_append(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    if isinstance(arg1, list):
        for i in range(1, len(args)):
            arg1.append(inter.parse(i, line, args)[2])
        return arg1
    else:
        for i in range(1, len(args)):
            arg1 += inter.parse(i, line, args)[2]
        return arg1
def f_op_sub(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_sub(inter, line, args), line, kwargs["lines_ran"])

def _f_op_sub(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 -= inter.parse(i, line, args)[2]
    return arg1
def f_op_mul(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_mul(inter, line, args), line, kwargs["lines_ran"])

def _f_op_mul(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 *= inter.parse(i, line, args)[2]
    return arg1
def f_op_div(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_div(inter, line, args), line, kwargs["lines_ran"])

def _f_op_div(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 /= inter.parse(i, line, args)[2]
    return arg1
def f_op_idiv(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_idiv(inter, line, args), line, kwargs["lines_ran"])

def _f_op_idiv(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 //= inter.parse(i, line, args)[2]
    return arg1
def f_op_mod(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_mod(inter, line, args), line, kwargs["lines_ran"])

def _f_op_mod(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 %= inter.parse(i, line, args)[2]
    return arg1
def f_op_pow(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_pow(inter, line, args), line, kwargs["lines_ran"])

def _f_op_pow(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 **= inter.parse(i, line, args)[2]
    return arg1
def f_op_root(inter, line, args, **kwargs):
    return _try_op(inter, lambda: _f_op_root(inter, line, args), line, kwargs["lines_ran"])

def _f_op_root(inter, line, args):
    arg1 = inter.parse(0, line, args)[2]
    for i in range(1, len(args)):
        arg1 **= 1 / inter.parse(i, line, args)[2]
    return arg1
def f_op_else():
    return "<msnint2 class>"
def f_USD(inter, line, args, **kwargs):
    num = inter.parse(0, line, args)[2]
    # number must be int or float
    inter.type_err([(num, (int, float))], line, kwargs["lines_ran"])
    return f"${num:,.2f}"
def f_format(inter, line, args, **kwargs):
    num = inter.parse(0, line, args)[2]
    # number must be int or float
    inter.type_err([(num, (int, float))], line, kwargs["lines_ran"])
    places = inter.parse(1, line, args)[2]
    # places must be int
    inter.type_err([(places, (int,))], line, kwargs["lines_ran"])
    return f"{num:.{places}f}"
def f_round(inter, line, args, **kwargs):
    num = inter.parse(0, line, args)[2]
    # number must be int or float
    inter.type_err([(num, (int, float))], line, kwargs["lines_ran"])
    digits = inter.parse(1, line, args)[2]
    # digits must be int
    inter.type_err([(digits, (int,))], line, kwargs["lines_ran"])
    return round(num, digits)
def f_maximum(inter, line, args, **kwargs):
    try:
        maxval = (
            max(_f)
            if isinstance((_f := inter.parse(0, line, args)[2]), list)
            else _f
        )
        for i in range(1, len(args)):
            val = inter.parse(i, line, args)[2]
            if isinstance(val, list):
                maxval = max(maxval, max(val))
            else:
                maxval = max(maxval, val)
    except Exception as e:
        return inter.err("Error finding maximum value", e, line, kwargs["lines_ran"])
    return maxval
def f_minimum(inter, line, args, **kwargs):
    try:
        minval = (
            min(_f)
            if isinstance((_f := inter.parse(0, line, args)[2]), list)
            else _f
        )
        for i in range(1, len(args)):
            val = inter.parse(i, line, args)[2]
            if isinstance(val, list):
                minval = min(minval, min(val))
            else:
                minval = min(minval, val)
    except Exception as e:
        return inter.err("Error finding minimum value", e, line, kwargs["lines_ran"])
    return minval

def f_function_addbody(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    inter.methods[fname].add_body(inter.parse(1, line, args)[2])
    return fname
def f_function_addarg(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    arg = inter.parse(1, line, args)[2]
    # arg must be a string
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    inter.methods[fname].add_arg(arg)
    return fname
def f_function_addreturn(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    ret = inter.parse(1, line, args)[2]
    # ret must be a string
    inter.type_err([(ret, (str,))], line, kwargs["lines_ran"])
    inter.methods[fname].add_return(ret)
    return fname
def f_function_getbody(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    return inter.methods[fname].body
def f_function_getargs(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    return inter.methods[fname].args
def f_function_getreturn(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    return inter.methods[fname].returns[0]
def f_function_destroy(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    inter.methods.pop(fname)
    return fname
def f_function_run(inter, line, args, **kwargs):
    fname = inter.parse(0, line, args)[2]
    # fname must be a string
    inter.type_err([(fname, (str,))], line, kwargs["lines_ran"])
    # form a string that is msn2 of the user defined function
    args_str = ""
    for i in range(1, len(args)):
        arg = inter.parse(i, line, args)[2]
        if i != len(args) - 1:
            args_str += f"{arg},"
        else:
            args_str += str(arg)
    inst = f"{fname}({args_str})"
    return inter.interpret(inst)
def f_function_else():
    return "<msnint2 class>"

def f_sub(inter, line, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be a variable name
    inter.check_varname(vn, line)
    # gets the substring
    other = inter.parse(1, line, args)[2]
    inter.vars[vn].value -= other
    return inter.vars[vn].value
def f_mul(inter, line, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be a variable name
    inter.check_varname(vn, line)
    # gets the substring
    other = inter.parse(1, line, args)[2]
    inter.vars[vn].value *= other
    return inter.vars[vn].value
def f_div(inter, line, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be a variable name
    inter.check_varname(vn, line)
    # gets the substring
    other = inter.parse(1, line, args)[2]
    inter.vars[vn].value /= other
    return inter.vars[vn].value
def f_append(inter, line, args, **kwargs):
    # variable name
    vn = inter.parse(0, line, args)[2]
    # vn must be a variable name
    inter.check_varname(vn, line)
    other = inter.parse(1, line, args)[2]
    # appends to the array
    inter.vars[vn].value.append(other)
    return inter.vars[vn].value
def f_op_getarrow(inter, line, args, **kwargs):
    from method import Method
    indexable = inter.parse(0, line, args)[2]
    # must be indexable
    if not isinstance(indexable, (list, str, dict, tuple)):
        inter.err(
            "Error indexing with ->()",
            "First argument not indexable, indexable types are lists, strings, dicts, and tuples",
            line,
            kwargs["lines_ran"],
        )
    try:
        return indexable[inter.parse(1, line, args)[2]]
    except IndexError:
        return inter.raise_index_out_of_bounds(
            line, kwargs["lines_ran"], Method("->", inter)
        )
def f_op_else():
    return "<msnint2 class>"

def f_version(inter, line, args, **kwargs):
    return inter.version
def f_destroy(inter, line, args, **kwargs):
    for i in range(len(args)):
        varname = inter.parse(i, line, args)[2]
        # varname must be varname
        inter.check_varname(varname, line)
        # must be a varname
        # deletes all variables or methods that start with '__'
        if varname == "__":
            for key in list(inter.vars):
                if key.startswith("__"):
                    del inter.vars[key]
            return True
        if varname in inter.vars:
            del inter.vars[varname]
        elif varname in inter.methods:
            del inter.methods[varname]
    return True
def f_destroy_function(inter, line, args, **kwargs):
    fname = None
    for i in range(len(args)):
        fname = inter.parse(i, line, args)[2]
        # must be a varname
        inter.check_varname(fname, line)
        inter.methods.pop(fname)
    return fname
def f_range(inter, line, args, **kwargs):
    start = inter.parse(0, line, args)[2]
    # start must be int
    inter.type_err([(start, (int,))], line, kwargs["lines_ran"])
    # if one argument
    if len(args) == 1:
        return range(start)
    # get the end of the range
    end = inter.parse(1, line, args)[2]
    # end must be int
    inter.type_err([(end, (int,))], line, kwargs["lines_ran"])
    # if two arguments
    if len(args) == 2:
        return range(start, end)
    if len(args) == 3:
        step = inter.parse(2, line, args)[2]
        # step must be int
        inter.type_err([(step, (int,))], line, kwargs["lines_ran"])
        return range(start, end, step)
    return range()
def f_uuid4(inter, line, args, **kwargs):
    import uuid
    return str(uuid.uuid4())
def f_random(inter, line, args, **kwargs):
    import random
    # gets a random number between 0 and 1
    if len(args) == 1:
        return random.random()
    # random number in range
    elif len(args) == 2:
        arg = inter.parse(0, line, args)[2]
        # arg must be int
        inter.type_err([(arg, (int,))], line, kwargs["lines_ran"])
        arg2 = inter.parse(1, line, args)[2]
        # arg2 must be int
        inter.type_err([(arg2, (int,))], line, kwargs["lines_ran"])
        return (random.random() * (arg2 - arg)) + arg
    # random int in range
    elif len(args) == 3:
        # using math
        import math
        arg = inter.parse(0, line, args)[2]
        # arg must be int
        inter.type_err([(arg, (int,))], line, kwargs["lines_ran"])
        arg2 = inter.parse(1, line, args)[2]
        # arg must be int
        inter.type_err([(arg2, (int,))], line, kwargs["lines_ran"])
        return math.floor((random.random() * (arg2 - arg)) + arg)
    return "<msnint2 class>"

def f_html_soup(inter, line, args, **kwargs):
    from bs4 import BeautifulSoup
    import requests
    url = inter.parse(0, line, args)[2]
    # url must be str
    inter.type_err([(url, (str,))], line, kwargs["lines_ran"])
    response = requests.get(url)
    return BeautifulSoup(response.content, "html5lib")
def f_html_from(inter, line, args, **kwargs):
    from bs4 import BeautifulSoup
    url = inter.parse(0, line, args)[2]
    # url must be str
    inter.type_err([(url, (str,))], line, kwargs["lines_ran"])
    obj_to_add = []
    all_elem = inter.html_all_elements(url)
    for elem in all_elem:
        obj_to_add.append(
            {
                "tag": elem.name,
                "attrs": elem.attrs,
                "text": elem.text,
            }
        )
    return obj_to_add
def f_html_session(inter, line, args, **kwargs):
    from requests_html import HTMLSession
    return HTMLSession()
def f_html_else():
    return "<msnint2 class>"

def f_ai_ai(inter, line, args, **kwargs):
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
    inter.type_err([(frequency_penalty, (int, float))], line, kwargs["lines_ran"])
    # presence_penalty
    presence_penalty = inter.parse(6, line, args)[2]
    # presence_penalty must be int or float
    inter.type_err([(presence_penalty, (int, float))], line, kwargs["lines_ran"])
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

def f_merge(inter, line, args, **kwargs):
    # gets the first argument
    arg1 = inter.parse(0, line, args)[2]
    # arg must exist
    if arg1 is None:
        inter.err(
            "Error in merge()",
            "First argument must exist",
            line,
            kwargs["lines_ran"],
        )
    # gets the rest of the arguments
    for i in range(1, len(args)):
        _arg = inter.parse(i, line, args)[2]
        # arg must exist
        if _arg is None:
            inter.err(
                "Error in merge()",
                "Merging arguments must exist",
                line,
                kwargs["lines_ran"],
            )
        arg1 |= inter.parse(i, line, args)[2]
    return arg1
def f_exception(inter, line, args, **kwargs):
    # get the error
    error = inter.parse(0, line, args)[2]
    # error must be str
    inter.type_err([(error, (str,))], line, kwargs["lines_ran"])
    # get the message
    message = inter.parse(1, line, args)[2]
    # message must be str
    inter.type_err([(message, (str,))], line, kwargs["lines_ran"])
    # get the line
    l = inter.parse(2, line, args)[2]
    # line must be str
    inter.type_err([(l, (str,))], line, kwargs["lines_ran"])
    # set trying to False
    return inter.err(error, message, l, kwargs["lines_ran"])
def f_syntax(inter, line, args, **kwargs):
    synt = inter.parse(0, line, args)[2]
    # synt must be str
    inter.type_err([(synt, (str,))], line, kwargs["lines_ran"])
    # add the between
    between = inter.parse(1, line, args)[2]
    # between must be a varname
    inter.check_varname(between, line)
    return inter.add_syntax(synt, between, args[2][0])
def f_enclosed_syntax(inter, line, args, **kwargs):
    start = inter.parse(0, line, args)[2]
    # start must be str
    inter.type_err([(start, (str,))], line, kwargs["lines_ran"])
    end = inter.parse(1, line, args)[2]
    # end must be str
    inter.type_err([(end, (str,))], line, kwargs["lines_ran"])
    varname = inter.parse(2, line, args)[2]
    # varname must be a varname
    inter.check_varname(varname, line)
    index = f"{start}msnint2_reserved{end}"
    kwargs["enclosed"][index] = [start, end, varname, args[3][0]]
    if len(args) == 5:
        kwargs["enclosed"][index].append(inter.parse(4, line, args)[2])
    return kwargs["enclosed"][index]
def f_macro(inter, line, args, **kwargs):
    token = inter.parse(0, line, args)[2]
    # token must be str
    inter.type_err([(token, (str,))], line, kwargs["lines_ran"])
    # get the symbol
    symbol = inter.parse(1, line, args)[2]
    # symbol must be str
    inter.type_err([(symbol, (str,))], line, kwargs["lines_ran"])
    kwargs["macros"][token] = [token, symbol, args[2][0]]
    # 4th argument offered as a return value from that macro
    # as opposed to a block of code
    if len(args) == 4:
        kwargs["macros"][token].append(inter.parse(3, line, args)[2])
    return kwargs["macros"][token]
def f_postmacro(inter, line, args, **kwargs):
    token = inter.parse(0, line, args)[2]
    # token must be str
    inter.type_err([(token, (str,))], line, kwargs["lines_ran"])
    # get the symbol
    symbol = inter.parse(1, line, args)[2]
    # symbol must be str
    inter.type_err([(symbol, (str,))], line, kwargs["lines_ran"])
    kwargs["postmacros"][token] = [token, symbol, args[2][0]]
    # same as macro
    if len(args) == 4:
        kwargs["postmacros"][token].append(inter.parse(3, line, args)[2])
    return kwargs["postmacros"][token]
def f_var_equals(inter, line, args, **kwargs):
    firstvar = inter.parse(0, line, args)[2]
    return all(
        firstvar == inter.parse(i, line, args)[2]
        for i in range(1, len(args))
    )
def f_var_else(inter, line, args, **kwargs):
    return "<msnint2 class>"

def f_val(inter, line, args, **kwargs):
    varname = inter.parse(0, line, args)[2]
    # varname must be a varname
    inter.check_varname(varname, line)
    try:
        return inter.vars[varname].value
    except:
        return inter.vars[varname]
def f_sorted(inter, line, args, **kwargs):
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    return sorted(iterable)
def f_copy(inter, line, args, **kwargs):
    try:
        return inter.parse(0, line, args)[2].copy()
    except Exception as e:
        return inter.err("Error copying object", e, line, kwargs["lines_ran"])
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
def f_fileacquire(inter, line, args, **kwargs):
    kwargs["lock"].acquire()
    return True
def f_filerelease(inter, line, args, **kwargs):
    kwargs["lock"].release()
    return True
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

def f_map(inter, line, args, **kwargs):
    # iterable
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    # varname
    varname = inter.parse(1, line, args)[2]
    # varname must be a varname
    inter.check_varname(varname, line)
    # map the function to each element in the iterable
    for i, el in enumerate(iterable):
        inter.vars[varname] = Var(varname, el)
        iterable[i] = inter.interpret(args[2][0])
    return iterable
def f_insert(inter, line, args, **kwargs):
    # iterable
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    # index
    index = inter.parse(1, line, args)[2]
    # index must be int
    inter.type_err([(index, (int,))], line, kwargs["lines_ran"])
    # value
    value = inter.parse(2, line, args)[2]
    # insert the value into the iterable
    iterable.insert(index, value)
    return iterable
def f_type(inter, line, args, **kwargs):
    return type(inter.parse(0, line, args)[2])
def f_parent(inter, line, args, **kwargs):
    return inter.parent
def f_boot(inter, line, args, **kwargs):
    while inter.parent != None:
        inter = inter.parent
    return inter
def f_del(inter, line, args, **kwargs):
    for i in range(len(args)):
        first = inter.parse(i, line, args)[2]
        # first must be a varname
        inter.check_varname(first, line)
        del inter.vars[first]
    return True
def f_cat(inter, line, args, **kwargs):
    cat = str(inter.parse(0, line, args)[2])
    # concatinate rest of arguments
    for i in range(1, len(args)):
        cat += str(inter.parse(i, line, args)[2])
    return cat
def f_equals(inter, line, args, **kwargs):
    arg1 = inter.parse(0, line, args)[2]
    return all(
        inter.parse(i, line, args)[-1] == arg1
        for i in range(1, len(args))
    )
def f_not(inter, line, args, **kwargs):
    return not inter.parse(0, line, args)[2]
def f_and(inter, line, args, **kwargs):
    return all(inter.parse(i, line, args)[2] for i in range(len(args)))
def f_or(inter, line, args, **kwargs):
    return inter.parse(0, line, args)[2] or inter.parse(1, line, args)[2]

def _try_compare(inter, func, line, **kwargs):
    try:
        return func()
    except Exception as e:
        return inter.err(
            "Comparison error",
            f"Unable to compare values\n{e}",
            line,
            kwargs["lines_ran"],
        )
def f_greater(inter, line, args, **kwargs):
    return _try_compare(
        inter,
        lambda: inter.parse(0, line, args)[2] > inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )
def f_less(inter, line, args, **kwargs):
    return _try_compare(
        inter,
        lambda: inter.parse(0, line, args)[2] < inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )
def f_greaterequal(inter, line, args, **kwargs):
    return _try_compare(
        inter,
        lambda: inter.parse(0, line, args)[2] >= inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )
def f_lessequal(inter, line, args, **kwargs):
    return _try_compare(
        inter,
        lambda: inter.parse(0, line, args)[2] <= inter.parse(1, line, args)[2],
        line,
        **kwargs,
    )
def f_class(inter, line, args, **kwargs):
    # new interpreter
    new_int = inter.new_int()
    # extract class name
    name = inter.parse(0, line, args)[2]
    # name must be a varname
    inter.check_varname(name, line)
    # execute the block in the private environment
    new_int.execute(args[1][0])
    # creates a variable out of the new interpreters resources
    obj_to_add = {}
    for varname in new_int.vars:
        val = new_int.vars[varname].value
        obj_to_add[varname] = Var(varname, val)
    for methodname in new_int.methods:
        obj_to_add[methodname] = Var(
            f"{methodname}#method", new_int.methods[methodname]
        )
    inter.vars[name] = Var(name, obj_to_add)
    return obj_to_add

def f_get(inter, line, args, **kwargs):
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    index = inter.parse(1, line, args)[2]
    # index must be int or str
    inter.type_err([(index, (int, str))], line, kwargs["lines_ran"])
    try:
        return iterable[index]
    except IndexError:
        inter.err(
            "Index Error",
            f"Index out of bounds: {index} (you) > {str(len(iterable))} (max)",
            line,
            kwargs["lines_ran"],
        )
def f_getn(inter, line, args, **kwargs):
    # get iterable
    iterable = inter.parse(0, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    try:
        # must have at least one index
        ind1 = inter.parse(1, line, args)[2]
        # get at the index
        obj = iterable[ind1]
        # get the rest of the indices
        for i in range(2, len(args)):
            # get the index
            ind = inter.parse(i, line, args)[2]
            # get at the index
            obj = obj[ind]
        return obj
    except:
        inter.err(
            "Error in getn()",
            "Could not index the iterable",
            line,
            kwargs["lines_ran"],
        )
def f_keys(inter, line, args, **kwargs):
    arg = None
    try:
        return (arg := inter.parse(0, line, args)[2]).keys()
    except:
        inter.err(
            "Error getting keys",
            f"Argument must be a dictionary\nYou said: {arg}",
            line,
            kwargs["lines_ran"],
        )
def f_import(inter, line, args, **kwargs):
    # for each import
    for i in range(len(args)):
        if script := inter.imp(i, line, args, inter.imports):
            inter.execute(script)
    return
def f_domain(inter, line, args, **kwargs):
    # get the name of the domain to create
    domain_name = inter.parse(0, line, args)[2]
    # domain_name must be a varname
    inter.check_varname(domain_name, line)
    # domains cannot coexist
    if domain_name in inter.domains:
        inter.err(
            "Domain Error",
            f'Domain "{domain_name}" already exists',
            line,
            kwargs["lines_ran"],
        )
    # add the domain_name to the set
    inter.domains.add(domain_name)
    # interpret the block in a new interpreter
    # with the domain_name as the parent
    new_int = inter.new_int()
    new_int.execute(args[1][0])
    # throws a domain error

    def domain_err(name, object):
        inter.err(
            "Domain Error",
            f'Domain object name already claimed: "{name}", consider renaming the object',
            line,
            kwargs["lines_ran"],
        )
        
    # for each variable in the interpreter
    for varname in new_int.vars:
        name = f"{domain_name}:{varname}"
        # name cannot already exist
        if name in inter.vars:
            domain_err(name, inter.vars)
        inter.vars[name] = new_int.vars[varname]
    # do the same for methods
    for methodname in new_int.methods:
        name = f"{domain_name}:{methodname}"
        if name in inter.methods:
            domain_err(name, inter.methods)
        inter.methods[name] = new_int.methods[methodname]
    return domain_name
def d_domainfind(inter, line, args, **kwargs):
    # get the domain directory
    domain_dir = inter.parse(0, line, args)[2]
    # domain_dir must be a varname
    inter.check_varname(domain_dir, line)
    # get all variables in the domain
    # with the domain_dir chopped off
    domain_vars = {}
    # for each variable in the domain
    for varname in inter.vars:
        # if the variable is in the domain
        if varname.startswith(domain_dir):
            # get the variable name without the domain
            varname = varname[len(domain_dir):]
            # add the variable to the domain_vars
            domain_vars[varname] = inter.vars[varname]
    # return the domain_vars
    return domain_vars
def f_in(inter, line, args, **kwargs):
    reserved_name = "_msn2_reserved_in__"
    if reserved_name not in inter.vars:
        return None
    inval = inter.vars[reserved_name].value
    # if no arguments, return the value
    if args[0][0] == "":
        return inval
    # if 1 argument, get index of value from input
    elif len(args) == 1:
        ind = inter.parse(0, line, args)[2]
        # ind must be int
        inter.type_err([(ind, (int,))], line, kwargs["lines_ran"])
        return inval[ind]
    # if 2 arguments, get slice of input
    elif len(args) == 2:
        start = inter.parse(0, line, args)[2]
        # start must be int
        inter.type_err([(start, (int,))], line, kwargs["lines_ran"])
        end = inter.parse(1, line, args)[2]
        # end must be int
        inter.type_err([(end, (int,))], line, kwargs["lines_ran"])
        return inval[start:end]
    return inval
def f_out(inter, line, args, **kwargs):
    outting = [inter.parse(i, line, args)[2] for i in range(len(args))]
    reserved_name = "_msn2_reserved_out__"
    inter.vars[reserved_name] = Var(reserved_name, outting)
    return outting
def f_prnt(inter, line, args, **kwargs):
    srep = ""
    for i in range(len(args)):
        srep = str(inter.parse(i, line, args)[2])
        if i != len(args) - 1:
            inter.out += srep + " "
        else:
            inter.out += srep + "\n"
    return srep
def f_print(inter, line, args, **kwargs):
    ret = None
    for i in range(len(args)):
        ret = inter.parse(i, line, args)[2]
        if i != len(args) - 1:
            print(ret, end=" ", flush=True)
        else:
            print(ret, flush=True)
    return ret
def f_printbox(inter, line, args, **kwargs):
    ret = None
    for i in range(len(args)):
        ret = inter.parse(i, line, args)[2]
        if i != len(args) - 1:
            print(inter.bordered(str(ret)), end=" ", flush=True)
        else:
            print(inter.bordered(str(ret)), flush=True)
    return ret
def f_printcolor(inter, line, args, **kwargs):
    print_args = [
        inter.parse(i, line, args)[2] for i in range(len(args))
    ]
    return inter.styled_print(print_args)
def f_sleep(inter, line, args, **kwargs):
    import time

    delay = inter.parse(0, line, args)[2]
    # delay must be int or float
    inter.type_err([(delay, (int, float))], line, kwargs["lines_ran"])
    return time.sleep(delay)
def f_me(inter, line, args, **kwargs):
    return inter.me()
def f_next_entry_path(inter, line, args, **kwargs):
    import os
    # if no args
    if args[0][0] == "":
        return inter.next_entry_path
    # otherwise, we're setting it
    inter.next_entry_path = inter.parse(0, line, args)[2]
    # compute and set the next project path, it should be
    # two directories up from the next entry path
    inter.next_project_path = os.path.dirname(
        os.path.dirname(inter.next_entry_path)
    )
    return inter.next_entry_path
def f_next_project_path(inter, line, args, **kwargs):
    return inter.next_project_path
def f_env(inter, line, args, **kwargs):
    should_print = False
    if args[0][0] != "":
        should_print = True
    # first argument should be either string or integer
    first = inter.parse(0, line, args)[2]
    strenv = "--------- environment ---------"
    strenv += f"\nout:\n{inter.out}"
    strenv += "\nvariables:\n"
    for varname, v in inter.vars.items():
        try:
            strenv += f"\t{varname} = {inter.shortened(v.value)}\n"
        except:
            None
    strenv += "\nmethods:\n"
    # printing methods
    for methodname, Method in inter.methods.items():
        strenv += f"\t{methodname}("
        for i in range(len(Method.args)):
            arg = Method.args[i]
            if i != len(Method.args) - 1:
                strenv += f"{arg}, "
            else:
                strenv += str(arg)
        strenv += f") : {len(Method.body)} inst\n"
    # printing macros
    strenv += "\nmacros:\n\t"
    # adding regular macros
    if len(inter.macros) > 0:
        strenv += "premacros:\n\t\t"
        for macro in inter.macros:
            # strenv += macro + "\n\t\t"
            strenv += f"{macro}\n\t\t"
    if len(inter.postmacros) > 0:
        strenv += "\n\tpostmacros:\n\t\t"
        for macro in inter.postmacros:
            # strenv += macro + "\n\t\t"
            strenv += f"{macro}\n\t\t"
    if len(inter.syntax) > 0:
        strenv += "\n\tsyntax:\n\t\t"
        for macro in inter.syntax:
            strenv += f"{macro}\n\t\t"
    if len(inter.enclosed) > 0:
        strenv += "\n\tenclosedsyntax:\n\t\t"
        for macro in inter.enclosed:
            strenv += f"{macro}\n\t\t"
    strenv += f"\nlog:\n{inter.log}\n-------------------------------"
    if should_print:
        inter.styled_print(
            [{"text": strenv, "style": "bold", "fore": "blue"}]
        )
    return strenv
def f_envmaxchars(inter, line, args, **kwargs):
    # if no arguments, return the current maxchars
    if args[0][0] == "":
        return inter.env_max_chars
    # otherwise, a single variable was provided
    # this will change the max chars
    new_maxchars = inter.parse(0, line, args)[2]
    # check for type errors
    inter.type_err([(new_maxchars, (int,))], line, kwargs["lines_ran"])
    # alter the max chars
    inter.env_max_chars = new_maxchars
    return new_maxchars

def _try_func_math(inter, func, line, **kwargs):
    try:
        return func()
    except Exception as e:
        return inter.err(
            "Error", e, line, kwargs["lines_ran"]
        )
def f_symbolminus(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: hyphen(kwargs["inst"]),
        line,
        **kwargs,
    )
def _f_plussymbol(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret += inter.parse(i, line, args)[2]
    return ret
def f_symbolplus(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_plussymbol(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_symbolx(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret *= inter.parse(i, line, args)[2]
    return ret
def f_symbolx(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_symbolx(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_symboldiv(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret /= inter.parse(i, line, args)[2]
    return ret
def f_symboldiv(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_symboldiv(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_symboldivdiv(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret //= inter.parse(i, line, args)[2]
    return ret
def f_symboldivdiv(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_symboldivdiv(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_symbolmod(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret %= inter.parse(i, line, args)[2]
    return ret
def f_symbolmod(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_symbolmod(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_symbolpow(inter, line, args, **kwargs):
    ret = inter.parse(0, line, args)[2]
    try:
        ret = ret.copy()
    except AttributeError:
        None
    for i in range(1, len(args)):
        ret **= inter.parse(i, line, args)[2]
    return ret
def f_symbolpow(inter, line, args, **kwargs):
    return _try_func_math(
        inter,
        lambda: _f_symbolpow(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def f_isdigit(inter, line, args, **kwargs):
    return inter.parse(0, line, args)[2].isdigit()
def f_isalpha(inter, line, args, **kwargs):
    return inter.parse(0, line, args)[2].isalpha()
def f_as(inter, line, args, **kwargs):
    # temporary variable name
    varname = inter.parse(0, line, args)[2]
    # varname must be a varname
    inter.check_varname(varname, line)
    # block to execute
    block = args[2][0]
    # set the variable
    inter.vars[varname] = Var(varname, inter.parse(1, line, args)[2])
    # execute the block
    ret = inter.interpret(block)
    # delete the variable
    del inter.vars[varname]
    return ret
def f_startswith(inter, line, args, **kwargs):
    # arg
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    # prefix
    prefix = inter.parse(1, line, args)[2]
    # prefix must be str
    inter.type_err([(prefix, (str,))], line, kwargs["lines_ran"])
    return arg.startswith(prefix)
def f_endswith(inter, line, args, **kwargs):
    # arg
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    # suffix
    suffix = inter.parse(1, line, args)[2]
    # suffix must be str
    inter.type_err([(suffix, (str,))], line, kwargs["lines_ran"])
    return arg.endswith(suffix)
def f_strip(inter, line, args, **kwargs):
    try:
        return inter.parse(0, line, args)[2].strip()
    except Exception as e:
        return inter.err(
            "Error in strip()", e, line, kwargs["lines_ran"]
        )
def f_slice(inter, line, args, **kwargs):
    # first
    first = inter.parse(0, line, args)[2]
    # first must be slicable
    inter.check_iterable(first, line)
    # second
    second = inter.parse(1, line, args)[2]
    # second must be int
    inter.type_err([(second, (int,))], line, kwargs["lines_ran"])
    # third
    third = inter.parse(2, line, args)[2]
    # third must be int
    inter.type_err([(third, (int,))], line, kwargs["lines_ran"])
    return first[second:third]
def f_iterablejoin(inter, line, args, **kwargs):
    delimiter = inter.parse(0, line, args)[2]
    # delimiter must be str
    inter.type_err([(delimiter, (str,))], line, kwargs["lines_ran"])
    # iterable
    iterable = inter.parse(1, line, args)[2]
    # iterable must be iterable
    inter.check_iterable(iterable, line)
    return delimiter.join(iterable)
def f_script(inter, line, args, **kwargs):
    # inserts key tokens
    return inter.msn2_replace(args[0][0])
def f_ls(inter, line, args, **kwargs):
    return inter.ls(args)
def f_now(inter, line, args, **kwargs):
    import time
    return time.time()
def f_private(inter, line, args, **kwargs):
    new_int = inter.new_int()
    for vname, entry in inter.vars.items():
        try:
            new_int.vars[vname] = Var(vname, entry.value)
        except:
            new_int.vars[vname] = Var(vname, entry)
    for mname, entry in inter.methods.items():
        new_int.methods[mname] = entry
    ret = new_int.interpret(args[0][0])
    return ret
def f_break(inter, line, args, **kwargs):
    inter.breaking = True
    return
def f_reverse(inter, line, args, **kwargs):
    # arg
    arg = inter.parse(0, line, args)[2]
    # arg must be iterable
    inter.check_iterable(arg, line)
    return arg[::-1]
def f_upper(inter, line, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    return arg.upper()
def f_lower(inter, line, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    return arg.lower()
def f_title(inter, line, args, **kwargs):
    arg = inter.parse(0, line, args)[2]
    # arg must be str
    inter.type_err([(arg, (str,))], line, kwargs["lines_ran"])
    # return title
    return arg.title()
def f_inheritmethods(inter, line, args, **kwargs):
    for methodname in inter.parent.methods:
        inter.methods[methodname] = inter.parent.methods[methodname]
    return True
def f_inheritvars(inter, line, args, **kwargs):
    for varname in inter.parent.vars:
        inter.vars[varname] = inter.parent.vars[varname]
    return True
def f_inheritsingle(inter, line, args, **kwargs):
    name = inter.parse(0, line, args)[2]
    # name must be a varname
    inter.check_varname(name, line)
    if name in inter.parent.vars:
        inter.vars[name] = inter.parent.vars[name]
    elif name in inter.parent.methods:
        inter.methods[name] = inter.parent.methods[name]
    return True
def f_new(inter, line, args, **kwargs):
    new_int = inter.new_int()
    return new_int.interpret(args[0][0])
def f_alias(inter, line, args, **kwargs):
    global python_alias
    if args[0][0] == "":
        return python_alias
    new_al = inter.parse(0, line, args)[2]
    # new_al must be str
    inter.type_err([(new_al, (str,))], line, kwargs["lines_ran"])
    python_alias = new_al
    return python_alias
def f_process(inter, line, args, **kwargs):
    import os
    # path to the process to run
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    # if windows:
    if os.name == "nt":
        import subprocess
        # runs the process
        sub = subprocess.run(
            args=[kwargs["python_alias"], "msn2.py", path], shell=True
        )
        inter.processes[path] = sub
        return sub
    # if linux
    elif os.name == "posix":
        inter.err("POSIX not yet implemented", "", line, kwargs["lines_ran"])
        return None
    return None
def f_proc(inter, line, args, **kwargs):
    # import lib processes module if not imported
    if "lib/processes.msn2" not in inter.imports:
        inter.interpret('import("lib/processes")')
    # import the processes library and
    # create a new process
    name = inter.parse(0, line, args)[2]
    return inter.interpret(
        f"processes:fork('{name}',private(async({args[1][0]})))"
    )
def f_pid(inter, line, args, **kwargs):
    import os
    return os.getpid()
def f_thread(inter, line, args, **kwargs):
    import threading
    # name not provided
    if len(args) == 1:
        global thread_serial
        name = f"__msn2_thread_id_{thread_serial}"
        block = args[0][0]
    # name provided (2 arguments provided)
    else:
        name = str(inter.parse(0, line, args)[2])
        # name must be a varname
        inter.check_varname(name, line)
        block = args[1][0]
    thread = threading.Thread(target=inter.interpret, args=(block,))
    thread.name = name
    inter.threads[name] = [thread, inter]
    thread.start()
    return True
def f_threadpool(inter, line, args, **kwargs):
    import concurrent.futures
    
    # get the amount of threads to create
    max_workers = inter.parse(0, line, args)[2]
    # max_workers must be int
    inter.type_err([(max_workers, (int,))], line, kwargs["lines_ran"])
    # create the thread pool
    # submit the block to the pool
    concurrent.futures.ThreadPoolExecutor(max_workers).submit(
        inter.interpret, args[1][0]
    )
    return True
def f_tvar(inter, line, args, **kwargs):
    # thread name
    name = str(inter.parse(0, line, args)[2])
    # variable name
    varname = str(inter.parse(1, line, args)[2])
    # variable value
    val = inter.parse(2, line, args)[2]
    # thread var name
    tvarname = f"_msn2_tvar_{name}_{varname}"
    # sets a thread specific variable
    inter.vars[tvarname] = Var(varname, val)
    return val
def f_gettvar(inter, line, args, **kwargs):
    # gets the variable
    return inter.vars[
        f"_msn2_tvar_{inter.parse(0, line, args)[2]}_{inter.parse(1, line, args)[2]}"
    ].value
def f_tvarstr(inter, line, args, **kwargs):
    # returns the string
    return f"_msn2_tvar_{inter.parse(0, line, args)[2]}_{inter.parse(1, line, args)[2]}"
def f_varmethod(inter, line, args, **kwargs):
    # variable name
    return inter.interpret(
        f"{inter.parse(0, line, args)[2]}.{args[1][0]}"
    )
def f_acquire(inter, line, args, **kwargs):
    return kwargs["auxlock"].acquire()
def f_release(inter, line, args, **kwargs):
    return kwargs["auxlock"].release()
def f_acquirepointer(inter, line, args, **kwargs):
    return kwargs["pointer_lock"].acquire()
def f_releasepointer(inter, line, args, **kwargs):
    return kwargs["pointer_lock"].release()
def f_join(inter, line, args, **kwargs):
    for i in range(len(args)):
        name = inter.parse(i, line, args)[2]
        thread = inter.thread_by_name(name)
        while thread == None:
            thread = inter.thread_by_name(name)
        thread.join()
    return True
def f_stop(inter, line, args, **kwargs):
    import os
    return os._exit(0)
def f_try(inter, line, args, **kwargs):
    ret = None
    inter.trying = True
    try:
        ret = inter.interpret(args[0][0])
    except:
        inter.trying = False
        if len(args) == 2:
            ret = inter.interpret(args[1][0])
    inter.trying = False
    return ret
def f_wait(inter, line, args, **kwargs):
    ret = None
    if len(args) == 1:
        while not (ret := inter.interpret(args[0][0])):
            None
    elif len(args) == 2:
        while not (ret := inter.interpret(args[0][0])):
            inter.interpret(args[1][0])
    elif len(args) == 3:
        import time
        
        s = inter.parse(2, line, args)[2]
        inter.type_err([(s, (int, float))], line, kwargs["lines_ran"])
        while not (ret := inter.interpret(args[0][0])):
            inter.interpret(args[1][0])
            time.sleep(s)
    return ret
def f_interval(inter, line, args, **kwargs):
    import time
    
    # amount of seconds
    seconds = inter.parse(0, line, args)[2]
    # seconds must be int or float
    inter.type_err([(seconds, (int, float))], line, kwargs["lines_ran"])
    # if the interval should last for a certain amount of seconds
    # should account for the first argument to correctly wait
    if len(args) == 3:
        extra = inter.parse(2, line, args)[2]
        # extra must be int or float
        inter.type_err([(extra, (int, float))], line, kwargs["lines_ran"])
        # if time is negative, we set it to infinity
        if extra == -1:
            extra = float("inf")
        end = time.time() + extra
        while time.time() < end:
            time.sleep(seconds)
            inter.interpret(args[1][0])
    else:
        while True:
            time.sleep(seconds)
            inter.interpret(args[1][0])
    return True
def f_export(inter, line, args, **kwargs):
    # if last argument is True,
    # we add the variables to the parent context's variable
    last_arg = inter.parse(len(args) - 1, line, args)[2]
    for i in range(len(args)):
        varname = inter.parse(i, line, args)[2]
        # varname must be a varname
        inter.check_varname(varname, line)
        if varname in inter.vars:
            if isinstance(last_arg, bool):
                # if inter.vars[varname].value is any type of number
                if isinstance(
                    inter.vars[varname].value, (int, float, complex)
                ):
                    inter.parent.vars[varname].value += inter.vars[
                        varname
                    ].value
                # otherwise add every element to the parent context's variable
                elif isinstance(inter.vars[varname].value, list):
                    for element in inter.vars[varname].value:
                        inter.parent.vars[varname].value.append(element)
            else:
                inter.parent.vars[varname] = inter.vars[varname]
        elif varname in inter.methods:
            inter.parent.methods[varname] = inter.methods[varname]
    return True
def f_exportas(inter, line, args, **kwargs):
    # variable name
    varname = inter.parse(0, line, args)[2]
    # varname must be a varname
    inter.check_varname(varname, line)
    # value
    val = inter.parse(1, line, args)[2]
    # export to parent context
    inter.parent.vars[varname] = Var(varname, val)
    return val
def f_exportall(inter, line, args, **kwargs):
    for varname in inter.vars:
        inter.parent.vars[varname] = inter.vars[varname]
    for methodname in inter.methods:
        inter.parent.methods[methodname] = inter.methods[methodname]
    return True
def f_exportthread(inter, line, args, **kwargs):
    # thread name
    tname = inter.parse(0, line, args)[2]
    # thread must exist
    if not (thread := inter.thread_by_name(tname)):
        inter.err(
            "Thread does not exist",
            f'Thread name "{tname}" does not exist in this context',
            line,
            kwargs["lines_ran"],
        )
    # export the thread to the parent context
    inter.parent.threads[tname] = inter.threads[tname]
    return tname
def f_clearthreads(inter, line, args, **kwargs):
    inter.threads = {}
    return True
def f_console(inter, line, args, **kwargs):
    import os
    ret = None
    for i in range(len(args)):
        command = inter.parse(i, line, args)[2]
        # command must be str
        inter.type_err([(command, (str,))], line, kwargs["lines_ran"])
        ret = os.system(command)
    return ret
def f_consoleread(inter, line, args, **kwargs):
    import subprocess

    # returns the console output
    # of the last argument
    command = inter.parse(0, line, args)[2]
    # command must be str
    inter.type_err([(command, (str,))], line, kwargs["lines_ran"])
    process = subprocess.run(
        command, shell=True, capture_output=True, text=True
    )
    if process.returncode == 0:
        return process.stdout
    else:
        return process.stderr
def f_request(inter, line, args, **kwargs):
    import requests

    # get URL to request from
    url = inter.parse(0, line, args)[2]
    # url must be str
    inter.type_err([(url, (str,))], line, kwargs["lines_ran"])
    try:
        # get parameters
        params = inter.parse(1, line, args)[2]
    except:
        params = None
    r = requests.get(url=url, params=params)
    # return response
    try:
        return r.json()
    except:
        return r
def f_return(inter, line, args, **kwargs):
    method = inter.methods[inter.loggedmethod[-1]]
    # evaluate returning literal
    ret = inter.parse(0, line, args)[2]
    # set return variable
    ret_name = method.returns[0]
    # if not a variable
    if ret_name not in inter.vars:
        inter.vars[ret_name] = Var(ret_name, None)
    inter.vars[ret_name].value = ret
    return ret
def f_pubip(inter, line, args, **kwargs):
    import requests
    
    # asks an api server for this address
    return requests.get("https://api.ipify.org").text
def f_privip(inter, line, args, **kwargs):
    import socket
    
    # gets the private ips of this machine
    return socket.gethostbyname_ex(socket.gethostname())[2]
def f_endpoint(inter, line, args, **kwargs):
    # imports
    from flask import Flask
    from flask_restful import Api, Resource
    import logging
    
    # initial API endpoint data
    path = None
    init_data = {}
    port = 5000
    host = "127.0.0.1"
    last_arg = None
    # 1 argument, defaults to 127.0.0.1:5000/path = {}
    if len(args) == 1:
        # path to endpoint
        path = inter.parse(0, line, args)[2]
        # path should be str
        inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
        last_arg = path
    # 2 arguments, defaults to 127.0.0.1:5000/path = init_data
    if len(args) == 2:
        # path to endpoint
        path = inter.parse(0, line, args)[2]
        # path should be str
        inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
        # json to initialize at the endpoint
        init_data = inter.parse(1, line, args)[2]
        # init_data should be dict
        inter.type_err([(init_data, (dict,))], line, kwargs["lines_ran"])
        last_arg = init_data
    # 3 arguments, defaults to host:port/path = init_data
    else:
        # host to endpoint as first argument
        host = inter.parse(0, line, args)[2]
        # host should be str
        inter.type_err([(host, (str,))], line, kwargs["lines_ran"])
        # port to endpoint as second argument
        port = inter.parse(1, line, args)[2]
        # port should be int
        inter.type_err([(port, (int,))], line, kwargs["lines_ran"])
        # path to endpoint
        path = inter.parse(2, line, args)[2]
        # path should be str
        inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
        # json to initialize at the endpoint
        init_data = inter.parse(3, line, args)[2]
        # init_data should be dict
        inter.type_err([(init_data, (dict,))], line, kwargs["lines_ran"])
        last_arg = init_data
        if len(args) == 5:
            last_arg = inter.parse(4, line, args)[2]
    # prepare endpoint
    print("serving on http://" + host + ":" + str(port) + path)
    app = Flask(__name__)
    cors = False
    # if the last argument is a string with 'CORS' in it
    # then enable CORS
    if isinstance(last_arg, str) and "CORS" in last_arg:
        from flask_cors import CORS

        # enable CORS
        print("starting with cors")
        cors = True
        CORS(app)
    # disable flask messages that aren't error-related
    log = logging.getLogger("werkzeug")
    log.disabled = True
    app.logger.disabled = True
    # gets Flask Api
    api = Api(app)
    # api endpoint class
    
    class EndPoint(Resource):
        @classmethod
        def make_api(cls, response):
            cls.response = response
            return cls
        
        # GET
        def get(self):
            return self.response
        
        # POST
        def post(self):
            from flask import request
            
            # obtains current endpoint data
            current_data = self.response
            # updates current data with data to post
            current_data.update(request.get_json())
            # updates next response
            self.make_api(current_data)
            return current_data
        
        # DELETE
        def delete(self):
            self.make_api({})
            return self.response
        
    curr_endpoint = EndPoint.make_api(init_data)
    # logs newly created endpoint
    inter.endpoints[path] = api
    # adds class EndPoint as a Resource to the Api with the specific path
    # passes arg2 alongside
    api.add_resource(curr_endpoint, path)
    
    # starting flask server
    try:
        # if internal
        app.run(host=host, port=port, debug=False)
    except:
        # if external
        try:
            app.run(host="0.0.0.0", port=port, debug=False, use_reloader=False)
        except:
            None
    return api
def f_post(inter, line, args, **kwargs):
    import requests
    
    # url to post to, defaults to localhost
    host = inter.parse(0, line, args)[2]
    # host must be str
    inter.type_err([(host, (str,))], line, kwargs["lines_ran"])
    # port to post to
    port = inter.parse(1, line, args)[2]
    # port must be int
    inter.type_err([(port, (int,))], line, kwargs["lines_ran"])
    # path after url
    path = inter.parse(2, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    # data to post
    data = inter.parse(3, line, args)[2]
    # data must be dict
    inter.type_err([(data, (dict,))], line, kwargs["lines_ran"])
    # if local network
    if host == "0.0.0.0":
        response = requests.post(
            url=("http://127.0.0.1:" + str(port) + path), json=data
        )
    # if localhost
    else:
        # post to endpoint
        response = requests.post(
            url=("http://" + host + ":" + str(port) + path), json=data
        )
    # get response
    return response.json()
def f_get_api(inter, line, args, **kwargs):
    import requests
    
    # url to get from, defaults to localhost
    host = inter.parse(0, line, args)[2]
    # host must be str
    inter.type_err([(host, (str,))], line, kwargs["lines_ran"])
    # port to get from
    port = inter.parse(1, line, args)[2]
    # port must be int
    inter.type_err([(port, (int,))], line, kwargs["lines_ran"])
    # path after url
    path = inter.parse(2, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    # if local network
    if host == "0.0.0.0":
        return requests.get(
            url=("http://127.0.0.1:" + str(port) + path)
        ).json()
    # if localhost
    else:
        return requests.get(
            url=("http://" + host + ":" + str(port) + path)
        ).json()
def f_delete(inter, line, args, **kwargs):
    import requests
    
    # url to delete from, defaults to localhost
    host = inter.parse(0, line, args)[2]
    # host must be str
    inter.type_err([(host, (str,))], line, kwargs["lines_ran"])
    # port to delete from
    port = inter.parse(1, line, args)[2]
    # port must be int
    inter.type_err([(port, (int,))], line, kwargs["lines_ran"])
    # path after url
    path = inter.parse(2, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    if host == "0.0.0.0":
        response = requests.delete(
            url=("http://127.0.0.1:" + str(port) + path)
        )
    else:
        # delete from endpoint
        response = requests.delete(
            url=("http://" + host + ":" + str(port) + path)
        )
    return response.json()
def f_windows(inter, line, args, **kwargs):
    import os
    return os.name == "nt"
def f_linux(inter, line, args, **kwargs):
    import os
    return os.name == "posix"
def f_mac(inter, line, args, **kwargs):
    import sys
    
    return sys.platform == "darwin"
def f_end(inter, line, args, **kwargs):
    method = inter.methods[inter.loggedmethod[-1]]
    inter.loggedmethod.pop()
    method.ended = True
    return True
def f_static(inter, line, args, **kwargs):
    try:
        return inter.parse(0, line, args)[2].value
    except Exception as e:
        inter.err("Error in static()", e, line, kwargs["lines_ran"])
        
# seperate set of functions for creating object instances
def f_instance_new(inter, line, args, **kwargs):
    from method import Method
    classname = kwargs["func"]
    # template Var obj to create from
    var_obj = inter.vars[classname].value
    instance = {}
    curr_arg_num = 0
    # attributes to apply
    for name in var_obj:
        # if attribute is a method
        if isinstance(var_obj[name].value, Method):
            # add the method to the instance
            instance[name] = var_obj[name].value
            # if the method's name is 'const'
            if var_obj[name].value.name == "const":
                # run the function with the argument being
                # this instance
                var_obj[name].value.run(
                    [instance], inter, actual_args=args[1:]
                )
            continue
        # if attribute is a variable
        # value can be None
        try:
            instance[name] = inter.parse(curr_arg_num, line, args)[2]
            if instance[name] == None:
                instance[name] = var_obj[name].value
        # if not specified, field is default value
        except:
            try:
                instance[name] = var_obj.value[name].copy()
            except:
                instance[name] = var_obj[name].value
        curr_arg_num += 1
    return instance
def f_app(inter, line, args, **kwargs):
    import os
    global timings_set
    # set timings if not already set
    if not timings_set:
        from pywinauto import timings

        # pywinauto defaults
        timings.Timings.after_clickinput_wait = 0.001
        timings.Timings.after_setcursorpos_wait = 0.001
        timings.Timings.after_sendkeys_key_wait = 0.001
        timings.Timings.after_menu_wait = 0.001
    # get the path to the application
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    # if there is not second argument, we do not kill any
    # existing instances of the application
    name = None
    extension = None
    if len(args) == 1:
        # get the name and extension of the application
        _sp = path.split("\\")
        name = _sp[-1].split(".")[0]
        extension = _sp[-1].split(".")[1]
        # use taskkill to kill the application
        # taskkill should end the program by name, and should kill
        # all child processes forcefully, it should also not print
        # anything to the console
        os.system(f"taskkill /f /im {name}.{extension} >nul 2>&1")
    # creates an App variable
    return inter.App(path=path, name=name, extension=extension)
def f_connect(inter, line, args, **kwargs):
    from pywinauto.application import Application

    appl = inter.parse(0, line, args)[2]
    a = Application(backend="uia").connect(
        process=appl.application.process
    )
    # connect to the application
    return inter.App(path=appl.path)
def f_excel(inter, line, args, **kwargs):
    # automating Excel
    import openpyxl
    
    path = inter.parse(0, line, args)[2]
    # path must be str
    inter.type_err([(path, (str,))], line, kwargs["lines_ran"])
    # creates and returns a Workbook
    return inter.Workbook(openpyxl.load_workbook(path), path)
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
def f_getattr(inter, line, args, **kwargs):
    vn = inter.parse(0, line, args)[2]
    # vn must be a varname
    inter.check_varname(vn, line)
    # get the attribute
    return inter.vars[vn].value[inter.parse(1, line, args)[2]]
def f_setattr(inter, line, args, **kwargs):
    # current working object
    o = inter.vars[inter.parse(0, line, args)[2]].value
    # name of attribute to set
    attr = inter.parse(1, line, args)[2]
    # attr must be a string
    inter.type_err([(attr, (str,))], line, kwargs["lines_ran"])
    # value to set
    val = inter.parse(2, line, args)[2]
    # set the value
    o[attr] = val
    return val

# language functions
def f_C(inter, line, args, **kwargs):
    import os
    # get the C code
    c_code = inter.msn2_replace(args[0][0])
    # create a directory for the C code
    # if it does not exist
    exec_folder_path = "_exec"
    # if the folder does not exist, create it
    if not os.path.exists(exec_folder_path):
        os.mkdir(exec_folder_path)
    # create a file for the C code
    # and write the C code to it
    # get the amount of files in the directory
    # and use that as the file name
    file_num = len(os.listdir(exec_folder_path))
    file_name = f"{exec_folder_path}/c{file_num}.c"
    with open(file_name, "w") as f:
        f.write(c_code)
    # creates a new process
    
    def retrieve_c_environment(c_code):
        import subprocess

        # executable
        executable = f"{exec_folder_path}/c{file_num}.exe"
        # create a new process
        # and execute the C code
        compiled_code = subprocess.run(
            ["gcc", file_name, "-o", executable],
            # capture the output
            capture_output=True,
            text=True,
        )
        # if there's an error, print it
        if len(compiled_code.stderr) > 0:
            return {"out": "", "err": compiled_code.stderr}
        # run the executable
        compiled_code = subprocess.run(
            [executable],
            # capture the output
            capture_output=True,
            text=True,
        )
        # get the output and error
        out = compiled_code.stdout
        err = compiled_code.stderr
        # get the environment
        # env = out.split('\n')[-2]
        # env = env.replace('\'', '"')
        # env = json.loads(env)
        return {"out": out, "err": err}
    
    # execute the C code
    return retrieve_c_environment(c_code)
def f_JS(inter, line, args, **kwargs):
    import os
    # get the JavaScript code
    js_code = inter.msn2_replace(args[0][0])
    # create a directory for the JavaScript code
    # if it does not exist
    exec_folder_path = "_exec"
    # if the folder does not exist, create it
    if not os.path.exists(exec_folder_path):
        os.mkdir(exec_folder_path)
    # create a file for the JavaScript code
    # and write the JavaScript code to it
    # get the amount of files in the directory
    # and use that as the file name
    file_num = len(os.listdir(exec_folder_path))
    file_name = f"{exec_folder_path}/js{file_num}.js"
    # if JS() has two arguments, the second is the name of
    # the file, excluding .js
    if len(args) == 2:
        file_name = (
            f"{exec_folder_path}/{inter.parse(1, line, args)[2]}.js"
        )
    with open(file_name, "w") as f:
        f.write(js_code)
    # creates a new process
    # and executes the JavaScript code
    # returns the environment
    # including the out and variables
    
    def retrieve_js_environment(js_code):
        import subprocess

        # executable
        executable = file_name
        # create a new process
        # and execute the JavaScript code
        compiled_code = subprocess.run(
            ["node", file_name],
            # capture the output
            capture_output=True,
            text=True,
        )
        # get the output and error
        out = compiled_code.stdout
        err = compiled_code.stderr
        # # if there is an error, print it
        # if len(err) > 0:
        #     print(err)
        # remove a succeeding newline
        # if it exists
        if len(out) > 0 and out[-1] == "\n":
            out = out[:-1]
        return {"out": out, "err": err}
    
    # execute the JavaScript code
    return retrieve_js_environment(js_code)
def f_JAVA(inter, line, args, **kwargs):
    import os
    # get the Java code
    java_code = inter.msn2_replace(args[0][0])
    # create a directory for the Java code
    # if it does not exist
    exec_folder_path = "_exec"
    # if the folder does not exist, create it
    if not os.path.exists(exec_folder_path):
        os.mkdir(exec_folder_path)
    # create a file for the Java code
    # and write the Java code to it
    # get the amount of files in the directory
    # and use that as the file name
    file_num = len(os.listdir(exec_folder_path))
    file_name = f"{exec_folder_path}/java{file_num}.java"
    with open(file_name, "w") as f:
        f.write(java_code)
    # creates a new process
    # and executes the Java code
    # returns the environment
    # including the out and variables
    
    def retrieve_java_environment(java_code):
        import subprocess

        # executable
        executable = f"{exec_folder_path}/java{file_num}.class"
        # create a new process
        # and execute the Java code
        compiled_code = subprocess.run(
            ["javac", file_name],
            # capture the output
            capture_output=True,
            text=True,
        )
        # if there's an error, print it
        if len(compiled_code.stderr) > 0:
            return {"out": "", "err": compiled_code.stderr}
        # run the executable
        compiled_code = subprocess.run(
            ["java", executable],
            # capture the output
            capture_output=True,
            text=True,
        )
        # get the output and error
        out = compiled_code.stdout
        err = compiled_code.stderr
        return {"out": out, "err": err}
    
    # execute the Java code
    return retrieve_java_environment(java_code)

# multi-instruction function trigger
def f_multi_function(inter, line, args, **kwargs):
    return multi_lined(kwargs["inst"])

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
        
def f_clipboard(inter, line, args, **kwargs):
    import pyperclip
    # if no arguments
    if args[0][0] == "":
        return pyperclip.paste()
    # if one argument
    else:
        copying = str(inter.parse(0, line, args)[2])
        pyperclip.copy(copying)
        return copying
    


def f_obj_general_destructive(inter, line, args, **kwargs):
    # recreate the line to interpret without the '!'
    inter.vars[kwargs["vname"]].value = inter.interpret(
        f"{kwargs['vname']}.{kwargs['objfunc'][:-1]}({kwargs['mergedargs']})"
    )
    return inter.vars[kwargs["vname"]].value


def f_obj_default_copy(inter, line, args, **kwargs):
    if kwargs["objfunc"] == "copy":
        try:
            return kwargs["object"].copy()
        except:
            # no attribute copy
            inter.err(
                "Error copying object.",
                f'Object "{kwargs["obj"]}" does not have attribute "copy".',
                line,
                kwargs["lines_ran"],
            )
def f_obj_default_print(inter, line, args, **kwargs):
    # if no arguments
    if args[0][0] == "":
        print(kwargs["object"])
        return kwargs["object"]
    # if one argument
    elif len(args) == 1:
        # what to print
        to_print = f"{inter.parse(0, line, args)[2]}{kwargs['object']}"
        # print the object
        print(to_print)
        return to_print
    # if two arguments
    elif len(args) == 2:
        # what to print
        to_print = f"{inter.parse(0, line, args)[2]}{kwargs['object']}{inter.parse(1, line, args)[2]}"
        # print the object
        print(to_print)
        # return the printed object
        return to_print
    return kwargs["object"]
def f_obj_default_val(inter, line, args, **kwargs):
    return kwargs["object"]

def _try_obj_default_cast(inter, func, object, line, lines_ran):
    try:
        return func()
    except:
        # no attribute func
        inter.err(
            "Casting error",
            f"Could not cast object of type {type(object)} to the type specified.",
            line,
            lines_ran,
        )
def f_obj_default_type(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: type(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_len(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: len(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_str(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: str(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_int(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: int(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_float(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: float(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_complex(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: complex(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_bool(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: bool(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_dict(inter, line, args, **kwargs):
    return _try_obj_default_cast(
        inter,
        lambda: dict(kwargs["object"]),
        kwargs["object"],
        line,
        kwargs["lines_ran"],
    )
def f_obj_default_switch(inter, line, args, **kwargs):
    # other variable name
    other_varname = inter.parse(0, line, args)[2]
    # check variable name
    inter.check_varname(other_varname, line)
    # other_varname must exist in self.vars
    if other_varname not in inter.vars:
        inter.err(
            "Error switching variables.",
            f'Variable name "{other_varname}" does not exist in this context.',
            line,
            kwargs["lines_ran"],
        )
    # switch the variables
    inter.vars[kwargs["vname"]].value, inter.vars[other_varname].value = (
        inter.vars[other_varname].value,
        inter.vars[kwargs["vname"]].value,
    )
    # return the variable
    return inter.vars[kwargs["vname"]].value

def f_obj_default_rename(inter, line, args, **kwargs):
    # get the variable name
    varname = inter.parse(0, line, args)[2]
    # variable name must be a string
    inter.check_varname(varname, line)
    # rename the variable
    inter.vars[varname] = Var(varname, kwargs["object"])
    # delete the old entry
    del inter.vars[kwargs["vname"]]
    # return the variable
    return inter.vars[varname]
def f_obj_default_if(inter, line, args, **kwargs):
    # variable name
    varname = inter.parse(0, line, args)[2]
    # check varname
    inter.check_varname(varname, line)
    new_list = []
    # perform logic
    for el in kwargs["object"]:
        inter.vars[varname] = Var(varname, el)
        if inter.interpret(args[1][0]):
            new_list.append(el)
    return new_list
def f_obj_default_is(inter, line, args, **kwargs):
    return kwargs["object"] is inter.parse(0, line, args)[2]
def f_obj_default_equals(inter, line, args, **kwargs):
    for i in range(len(args)):
        if kwargs["object"] != inter.parse(i, line, args)[2]:
            return False
    return True
def f_obj_default_slice(inter, line, args, **kwargs):
    first = inter.parse(0, line, args)[2]
    second = inter.parse(1, line, args)[2]
    # ensure both arguments are integers or None
    inter.type_err(
        [(first, (int, type(None))), (second, (int, type(None)))],
        line,
        kwargs["lines_ran"],
    )
    return kwargs["object"][first:second]
def f_obj_default_index(inter, line, args, **kwargs):
    return kwargs["object"].index(inter.parse(0, line, args)[2])
def f_obj_default_export(inter, line, args, **kwargs):
    # if an argument is provided
    # export as name
    if args[0][0] != "":
        vname = inter.parse(0, line, args)[2]
        # check vname
        inter.check_varname(vname, line)
        # vname must exist in current context as a variable
        inter.export_err(vname, line)
    inter.parent.vars[vname] = Var(vname, kwargs["object"])
    return kwargs["object"]
def f_obj_default_noobjfunc(inter, line, args, **kwargs):
    ret = inter.vars[kwargs["vname"]].value
    # for each block
    for arg in args:
        ret = inter.interpret(f"{kwargs['vname']}.{arg[0]}")
    return ret
def f_obj_default_string_name(inter, line, args, **kwargs):
    return kwargs["vname"]
def f_obj_default_each(inter, line, args, **kwargs):
    # get the variable name
    varname = inter.parse(0, line, args)[2]
    # check varname
    inter.check_varname(varname, line)
    # try an indexable
    try:
        for i in range(len(kwargs["object"])):
            inter.vars[varname] = Var(varname, kwargs["object"][i])
            inter.interpret(args[1][0])
    except:
        # try a set
        for i in kwargs["object"]:
            inter.vars[varname] = Var(varname, i)
            inter.interpret(args[1][0])
    return kwargs["object"]
def f_obj_default_rfind(inter, line, args, **kwargs):
    return kwargs["object"].rfind(inter.parse(0, line, args)[2])
def f_obj_default_lfind(inter, line, args, **kwargs):
    return kwargs["object"].find(inter.parse(0, line, args)[2])
def f_obj_default_find(inter, line, args, **kwargs):
    return kwargs["object"].find(inter.parse(0, line, args)[2])
def f_obj_default_filter(inter, line, args, **kwargs):
    # get the variable name
    varname = inter.parse(0, line, args)[2]
    # check variable name
    inter.check_varname(varname, line)
    # filtered
    filtered = []
    # filter the iterable
    for el in kwargs["object"]:
        inter.vars[varname] = Var(varname, el)
        if inter.interpret(args[1][0]):
            filtered.append(el)
    # set the variable to the filtered list
    inter.vars[kwargs["vname"]].value = filtered
    # return the filtered list
    return inter.vars[kwargs["vname"]].value

def _try_obj_default_math(inter, func, line, **kwargs):
    try:
        return func()
    except:
        inter.raise_operation_err(kwargs["vname"], kwargs["objfunc"], kwargs["lines_ran"])


def _f_obj_default_add(inter, line, args, **kwargs):
    # basic arithmetic, non-destructive
    # takes any amount of arguments
    ret = kwargs["object"]
    for i in range(len(args)):
        ret += inter.parse(i, line, args)[2]
    return ret
def f_obj_default_add(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_add(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_sub(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret -= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_sub(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_sub(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_mul(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret *= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_mul(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_mul(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_div(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret /= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_div(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_div(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_mod(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret %= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_mod(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_mod(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_pow(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret **= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_pow(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_pow(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def _f_obj_default_idiv(inter, line, args, **kwargs):
    ret = kwargs["object"]
    for i in range(len(args)):
        ret //= inter.parse(i, line, args)[2]
    return ret
def f_obj_default_idiv(inter, line, args, **kwargs):
    return _try_obj_default_math(
        inter,
        lambda: _f_obj_default_idiv(inter, line, args, **kwargs),
        line,
        **kwargs,
    )
def f_obj_default_func(inter, line, args, **kwargs):
    ret = kwargs["object"]
    # apply the function to the object
    for arg in args:
        ret = inter.interpret(f"{arg[0]}({ret})")
    return ret
def f_obj_default_reverse(inter, line, args, **kwargs):
    # only types that can be reversed:
    # list, tuple, string
    # check types
    inter.type_err(
        [(kwargs["object"], (list, tuple, str))], line, kwargs["lines_ran"]
    )
    inter.vars[kwargs["vname"]].value = kwargs["object"][::-1]
    return inter.vars[kwargs["vname"]].value
def f_obj_default_in(inter, line, args, **kwargs):
    in_var = inter.parse(0, line, args)[2]
    # in_var must be of type iterable
    inter.check_iterable(in_var, line)
    # if object is of type string
    if isinstance(kwargs["object"], str):
        # in_var must be a string also
        inter.type_err([(in_var, (str,))], line, kwargs["lines_ran"])
        return kwargs["object"] in in_var
    else:
        # otherwise, in_var cannot be a string
        if isinstance(in_var, str):
            inter.err(
                "Type error",
                f'Cannot search for type {type(kwargs["object"])} in type {type(in_var)}\nConsider casting variable "{kwargs["vname"]}" to {str}, \nor change in() argument to a different iterable',
                line,
                kwargs["lines_ran"],
            )
    return kwargs["object"] in in_var

def f_obj_number_inc(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value += 1
    return inter.vars[kwargs["vname"]].value
def f_obj_number_dec(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value -= 1
    return inter.vars[kwargs["vname"]].value
def f_obj_number_even(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value % 2 == 0
def f_obj_number_odd(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value % 2 != 0

def _try_obj_number_math(inter, func, line, args, **kwargs):
    try:
        return func()
    except:
        inter.raise_operation_err(kwargs["vname"], kwargs["objfunc"], line)
def _f_obj_number_add(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value += inter.parse(i, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_number_add(inter, line, args, **kwargs):
    return _try_obj_number_math(
        inter,
        lambda: _f_obj_number_add(inter, line, args, **kwargs),
        line,
        args,
        **kwargs,
    )
def _f_obj_number_sub(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value -= inter.parse(i, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_number_sub(inter, line, args, **kwargs):
    return _try_obj_number_math(
        inter,
        lambda: _f_obj_number_sub(inter, line, args, **kwargs),
        line,
        args,
        **kwargs,
    )
def _f_obj_number_mul(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value *= inter.parse(i, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_number_mul(inter, line, args, **kwargs):
    return _try_obj_number_math(
        inter,
        lambda: _f_obj_number_mul(inter, line, args, **kwargs),
        line,
        args,
        **kwargs,
    )
def _f_obj_number_div(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value /= inter.parse(i, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_number_div(inter, line, args, **kwargs):
    return _try_obj_number_math(
        inter,
        lambda: _f_obj_number_div(inter, line, args, **kwargs),
        line,
        args,
        **kwargs,
    )
        
def f_obj_number_abs(inter, line, args, **kwargs):
    # can only be performed on numbers
    inter.vars[kwargs["vname"]].value = abs(inter.vars[kwargs["vname"]].value)
    return inter.vars[kwargs["vname"]].value
def f_obj_number_round(inter, line, args, **kwargs):
    # round to the nearest decimal place
    if args[0][0] != "":
        # decimal place to round to
        place = inter.parse(0, line, args)[2]
        # place type must be int
        inter.type_err([(place, (int,))], line, kwargs["lines_ran"])
        inter.vars[kwargs["vname"]].value = round(
            inter.vars[kwargs["vname"]].value, place
        )
    else:
        inter.vars[kwargs["vname"]].value = round(inter.vars[kwargs["vname"]].value)
    return inter.vars[kwargs["vname"]].value
def f_obj_number_floor(inter, line, args, **kwargs):
    # using math
    import math
    
    inter.vars[kwargs["vname"]].value = math.floor(inter.vars[kwargs["vname"]].value)
    return inter.vars[kwargs["vname"]].value
def f_obj_number_ceil(inter, line, args, **kwargs):
    # using math
    import math
    
    inter.vars[kwargs["vname"]].value = math.ceil(inter.vars[kwargs["vname"]].value)
    return inter.vars[kwargs["vname"]].value
def f_obj_number_neg(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value = -inter.vars[kwargs["vname"]].value
    return inter.vars[kwargs["vname"]].value
def _try_obj_number_compare(inter, func, line, args, **kwargs):
    try:
        return func()
    except:
        inter.raise_comp(kwargs["objfunc"], kwargs["vname"], line)
def f_obj_number_greater(inter, line, args, **kwargs):
    return _try_obj_number_compare(
        inter,
        lambda: all(
            inter.vars[kwargs["vname"]].value > inter.parse(i, line, args)[2]
            for i in range(len(args))
        ),
        line,
        args,
        **kwargs,
    )
def f_obj_number_less(inter, line, args, **kwargs):
    return _try_obj_number_compare(
        inter,
        lambda: all(
            inter.vars[kwargs["vname"]].value < inter.parse(i, line, args)[2]
            for i in range(len(args))
        ),
        line,
        args,
        **kwargs,
    )
def f_obj_number_greaterequal(inter, line, args, **kwargs):
    return _try_obj_number_compare(
        inter,
        lambda: all(
            inter.vars[kwargs["vname"]].value >= inter.parse(i, line, args)[2]
            for i in range(len(args))
        ),
        line,
        args,
        **kwargs,
    )
def f_obj_number_lessequal(inter, line, args, **kwargs):
    return _try_obj_number_compare(
        inter,
        lambda: all(
            inter.vars[kwargs["vname"]].value <= inter.parse(i, line, args)[2]
            for i in range(len(args))
        ),
        line,
        args,
        **kwargs,
    )

def f_obj_set_add(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value.add(inter.parse(i, line, args)[2])
    return inter.vars[kwargs["vname"]].value
def f_obj_set_pop(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.pop()
def f_obj_set_remove(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value.remove(inter.parse(i, line, args)[2])
    return inter.vars[kwargs["vname"]].value
def f_obj_set_list(inter, line, args, **kwargs):
    return list(inter.vars[kwargs["vname"]].value)
def f_obj_set_get(inter, line, args, **kwargs):
    # index to get at
    ind = inter.parse(0, line, args)[2]
    # index must be an int
    inter.type_err([(ind, (int,))], line, kwargs["lines_ran"])
    # get the index
    for i in inter.vars[kwargs["vname"]].value:
        if ind == 0:
            return i
        ind -= 1

def f_obj_list_push(inter, line, args, **kwargs):
    for i in range(len(args)):
        inter.vars[kwargs["vname"]].value.append(inter.parse(i, line, args)[2])
    return inter.vars[kwargs["vname"]].value
def f_obj_list_pop(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.pop()
def f_obj_list_get(inter, line, args, **kwargs):
    # index to get at
    ind = inter.parse(0, line, args)[2]
    # index must be an int
    inter.type_err([(ind, (int,))], line, kwargs["lines_ran"])
    # get the index
    return inter.vars[kwargs["vname"]].value[ind]
def f_obj_list_set(inter, line, args, **kwargs):
    ind = inter.parse(0, line, args)[2]
    # index must be an int
    inter.type_err([(ind, (int,))], line, kwargs["lines_ran"])
    inter.vars[kwargs["vname"]].value[ind] = inter.parse(1, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_list_avg(inter, line, args, **kwargs):
    try:
        return sum(inter.vars[kwargs["vname"]].value) / len(
            inter.vars[kwargs["vname"]].value
        )
    except:
        if not inter.vars[kwargs["vname"]].value:
            inter.raise_empty_array(line)
        else:
            inter.raise_avg(line)
def f_obj_list_insert(inter, line, args, **kwargs):
    for i in range(len(args)):
        ind = inter.parse(i, line, args)[2]
        # index must be an int
        inter.type_err([(ind, (int,))], line, kwargs["lines_ran"])
        val = inter.parse(i, line, args)[2]
        inter.vars[kwargs["vname"]].value.insert(ind, val)
    return inter.vars[kwargs["vname"]].value
def f_obj_list_removen(inter, line, args, **kwargs):
    count = inter.parse(0, line, args)[2]
    # count must be an int
    inter.type_err([(count, (int,))], line, kwargs["lines_ran"])
    for i in range(1, len(args)):
        for j in range(count):
            val = inter.parse(i, line, args)[2]
            try:
                del inter.vars[kwargs["vname"]].value[
                    (
                        _v := inter.vars[kwargs["vname"]].value.index(val)
                    )
                ]
            except ValueError:
                inter.raise_value(val, line)
    return inter.vars[kwargs["vname"]].value
def f_obj_list_remove(inter, line, args, **kwargs):
    for i in range(len(args)):
        while inter.parse(i, line, args)[2] in inter.vars[kwargs["vname"]].value:
            try:
                del inter.vars[kwargs["vname"]].value[
                    (
                        _v := inter.vars[kwargs["vname"]].value.index(
                            inter.parse(i, line, args)[2]
                        )
                    )
                ]
            except ValueError:
                inter.raise_value(_v, line)
    return inter.vars[kwargs["vname"]].value
def f_obj_list_sorted(inter, line, args, **kwargs):
    return sorted(inter.vars[kwargs["vname"]].value)
def f_obj_list_sort(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value.sort()
    return inter.vars[kwargs["vname"]].value
def f_obj_list_len(inter, line, args, **kwargs):
    return len(inter.vars[kwargs["vname"]].value)
def f_obj_list_empty(inter, line, args, **kwargs):
    return len(inter.vars[kwargs["vname"]].value) == 0
def f_obj_list_contains(inter, line, args, **kwargs):
    return inter.parse(0, line, args)[2] in inter.vars[kwargs["vname"]].value
def f_obj_list_find(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.find(inter.parse(0, line, args)[2])
def f_obj_list_shuffle(inter, line, args, **kwargs):
    import random
    
    random.shuffle(inter.vars[kwargs["vname"]].value)
    return inter.vars[kwargs["vname"]].value
def f_obj_list_map(inter, line, args, **kwargs):
    # get the variable name
    varname = inter.parse(0, line, args)[2]
    # check varname
    inter.check_varname(varname, line)
    for i in range(len(inter.vars[kwargs["vname"]].value)):
        inter.vars[varname] = Var(varname, inter.vars[kwargs["vname"]].value[i])
        inter.vars[kwargs["vname"]].value[i] = inter.interpret(args[1][0])
    del inter.vars[varname]
    return inter.vars[kwargs["vname"]].value
def f_obj_list_join(inter, line, args, **kwargs):
    delimiter = inter.parse(0, line, args)[2]
    # delimiter must be a string
    inter.type_err([(delimiter, (str,))], line, kwargs["lines_ran"])
    return delimiter.join(map(str, inter.vars[kwargs["vname"]].value))
def f_obj_list_toset(inter, line, args, **kwargs):
    return set(inter.vars[kwargs["vname"]].value)

def f_obj_str_add(inter, line, args, **kwargs):
    for i in range(len(args)):
        adding = inter.parse(i, line, args)[2]
        # adding must be a string
        inter.type_err([(adding, (str,))], line, kwargs["lines_ran"])
        inter.vars[kwargs["vname"]].value += adding
    return inter.vars[kwargs["vname"]].value
def f_obj_str_split(inter, line, args, **kwargs):
    splitting_by = inter.parse(0, line, args)[2]
    # splitting_by must be a string
    inter.type_err([(splitting_by, (str,))], line, kwargs["lines_ran"])
    return inter.vars[kwargs["vname"]].value.split(splitting_by)
def f_obj_str_lines(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.split("\n")
def f_obj_str_words(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.split(" ")
def f_obj_str_lwordremove(inter, line, args, **kwargs):
    # number of words to remove
    num = inter.parse(0, line, args)[2]
    # num must be an int
    inter.type_err([(num, (int,))], line, kwargs["lines_ran"])
    # number cannot be negative
    if num < 0:
        inter.err(
            "Value error",
            "Number of words to remove cannot be negative.",
            line,
            kwargs["lines_ran"],
        )
    # remove the words
    inter.vars[kwargs["vname"]].value = " ".join(
        inter.vars[kwargs["vname"]].value.split(" ")[num:]
    )
    return inter.vars[kwargs["vname"]].value
def f_obj_str_rwordremove(inter, line, args, **kwargs):
    # number of words to remove
    num = inter.parse(0, line, args)[2]
    # num must be an int
    inter.type_err([(num, (int,))], line, kwargs["lines_ran"])
    # number cannot be negative
    if num < 0:
        inter.err(
            "Value error",
            "Number of words to remove cannot be negative.",
            line,
            kwargs["lines_ran"],
        )
    # remove the words
    inter.vars[kwargs["vname"]].value = " ".join(
        inter.vars[kwargs["vname"]].value.split(" ")[:-num]
    )
    return inter.vars[kwargs["vname"]].value
def f_obj_str_chars(inter, line, args, **kwargs):
    return list(inter.vars[kwargs["vname"]].value)
def f_obj_str_isdigit(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.isdigit()
def f_obj_str_isalpha(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.isalpha()
def f_obj_str_replace(inter, line, args, **kwargs):
    # what to replace
    replacing = inter.parse(0, line, args)[2]
    wth = inter.parse(1, line, args)[2]
    # both must be strings
    inter.type_err(
        [(replacing, (str,)), (wth, (str,))], line, kwargs["lines_ran"]
    )
    # replacing with
    if len(args) == 2:
        # replaces all instances of replacing with wth
        inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value.replace(
            replacing, wth
        )
    elif len(args) == 3:
        third = inter.parse(2, line, args)[2]
        # third must be a string
        inter.type_err([(third, (str,))], line, kwargs["lines_ran"])
        inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value.replace(
            replacing, wth, third
        )
    # returns the new string
    return inter.vars[kwargs["vname"]].value
def f_obj_str_strip(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value.strip()
    return inter.vars[kwargs["vname"]].value
def f_obj_str_stripped(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.strip()
def f_obj_str_self(inter, line, args, **kwargs):
    try:
        return inter.vars[kwargs["vname"]].value
    except:
        return inter.vars[kwargs["vname"]]
def f_obj_str_set(inter, line, args, **kwargs):
    # index to set
    index = inter.parse(0, line, args)[2]
    # index must be an int
    inter.type_err([(index, (int,))], line, kwargs["lines_ran"])
    # create a new string with the new character
    inter.vars[kwargs["vname"]].value = (
        f"{inter.vars[kwargs['vname']].value[:index]}{inter.parse(1, line, args)[2]}{inter.vars[kwargs['vname']].value[index + 1:]}"
    )
    # returns the new string
    return inter.vars[kwargs["vname"]].value
def f_obj_str_get(inter, line, args, **kwargs):
    ind = inter.parse(0, line, args)[2]
    return inter.vars[kwargs["vname"]].value[ind]
def f_obj_str_upper(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value.upper()
    return inter.vars[kwargs["vname"]].value
def f_obj_str_lower(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value.lower()
def f_obj_str_cut(inter, line, args, **kwargs):
    start = inter.parse(0, line, args)[2]
    end = inter.parse(1, line, args)[2]
    # both start and end must be ints
    inter.type_err(
        [(start, (int,)), (end, (int,))], line, kwargs["lines_ran"]
    )
    inter.vars[kwargs["vname"]].value = inter.vars[kwargs["vname"]].value[start:end]
    return inter.vars[kwargs["vname"]].value
def f_obj_str_shove(inter, line, args, **kwargs):
    inserting = inter.parse(0, line, args)[2]
    index = inter.parse(1, line, args)[2]
    # index must be an int
    inter.type_err([(index, (int,))], line, kwargs["lines_ran"])
    inter.vars[kwargs["vname"]].value = (
        f"{inter.vars[kwargs['vname']].value[:index]}{inserting}{inter.vars[kwargs['vname']].value[index:]}"
    )
    return inter.vars[kwargs["vname"]].value
def f_obj_str_around(inter, line, args, **kwargs):
    # keyword to search for
    keyword = inter.parse(0, line, args)[2]
    # keyword must be a string
    inter.type_err([(keyword, (str,))], line, kwargs["lines_ran"])
    # get the index of the keyword
    index = inter.vars[kwargs["vname"]].value.find(keyword)
    # if not found
    if index == -1:
        # f"around(): Keyword '{keyword}' not found in string"
        # raise an msn2 error
        inter.err(
            f"around(): Keyword '{keyword}' not found in string",
            line,
            kwargs["lines_ran"],
            kwargs["f"],
        )
    # get the string
    return kwargs["object"][
        index
        - inter.parse(1, line, args)[2] : index
        + len(keyword)
        + inter.parse(2, line, args)[2]
    ]
def f_obj_str_startswith(inter, line, args, **kwargs):
    st = inter.parse(0, line, args)[2]
    # st must be a string
    inter.type_err([(st, (str,))], line, kwargs["lines_ran"])
    return inter.vars[kwargs["vname"]].value.startswith(st)
def f_obj_str_endswith(inter, line, args, **kwargs):
    st = inter.parse(0, line, args)[2]
    # st must be a string
    inter.type_err([(st, (str,))], line, kwargs["lines_ran"])
    return inter.vars[kwargs["vname"]].value.endswith(st)

def f_obj_html_all(inter, line, args, **kwargs):
    # gets information from a website
    return kwargs["object"].get(inter.parse(0, line, args)[2]).html
def f_obj_html_render(inter, line, args, **kwargs):
    # renders the html session
    return kwargs["object"].render(retries=3)
def f_obj_html_else(inter, line, args, **kwargs):
    return kwargs["object"]   

def f_obj_justhtml_gather(inter, line, args, **kwargs):
    # finds elements in the HTML
    if args[0][0] == "":
        return kwargs["object"].find()
    else:
        return kwargs["object"].find(inter.parse(0, line, args)[2])
def f_obj_justhtml_else(inter, line, args, **kwargs):
    return getattr(kwargs["object"], kwargs["objfunc"])

def f_obj_dict_set(inter, line, args, **kwargs):
    inter.vars[kwargs["vname"]].value[inter.parse(0, line, args)[2]] = inter.parse(1, line, args)[2]
    return inter.vars[kwargs["vname"]].value
def f_obj_dict_setn(inter, line, args, **kwargs):
    # what to set
    to_set = inter.parse(0, line, args)[2]
    # the rest of the arguments are the indices
    # example: dict.setn('im being set', 'index1', 'index2', 'index3', ...)
    # should equal: dict['index1']['index2']['index3'] = 'im being set'
    # the object to set
    obj = inter.vars[kwargs["vname"]].value
    # iterates through the indices
    for i in range(1, len(args)):
        # if the index is the last one
        if i == len(args) - 1:
            # sets the index to to_set
            obj[inter.parse(i, line, args)[2]] = to_set
        # if the index is not the last one
        else:
            # sets the object to the index
            obj = obj[inter.parse(i, line, args)[2]]
    # returns the object
    return inter.vars[kwargs["vname"]].value
def f_obj_dict_get(inter, line, args, **kwargs):
    # the object to get from
    obj = inter.vars[kwargs["vname"]].value
    # iterates through the indices
    for i in range(len(args)):
        ind = inter.parse(i, line, args)[2]
        try:
            # sets the object to the index
            obj = obj[ind]
        except KeyError:
            inter.raise_key(ind, line)
    # returns the object
    return obj
def f_obj_dict_keys(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.keys()
def f_obj_dict_values(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.values()
def f_obj_dict_items(inter, line, args, **kwargs):
    return inter.vars[kwargs["vname"]].value.items()
def f_obj_dict_foreach(inter, line, args, **kwargs):
    # variable name of the key
    keyname = inter.parse(0, line, args)[2]
    # variable name of the value
    valuename = inter.parse(1, line, args)[2]
    # check both keyname and valuename as varnames
    inter.check_varname(keyname, line)
    inter.check_varname(valuename, line)
    # function to execute
    function = args[2][0]
    # loop through the dictionary
    for key, value in inter.vars[kwargs["vname"]].value.items():
        # set the key and value variables
        inter.vars[keyname] = Var(keyname, key)
        inter.vars[valuename] = Var(valuename, value)
        # execute the function
        inter.interpret(function)
    # return the dictionary
    return inter.vars[kwargs["vname"]].value
def f_obj_dict_map(inter, line, args, **kwargs):
    # map arguments
    keyvarname = inter.parse(0, line, args)[2]
    valuevarname = inter.parse(1, line, args)[2]
    # check both for varnames
    inter.check_varname(keyvarname, line)
    inter.check_varname(valuevarname, line)
    function = args[2][0]
    new_dict = {}
    # loop through the objects items, assigning the key to the key and
    # value to the value
    for key, value in inter.vars[kwargs["vname"]].value.items():
        # log old key
        old_key = key
        # execute the function
        inter.vars[keyvarname] = Var(keyvarname, key)
        inter.vars[valuevarname] = Var(valuevarname, value)
        # run the function
        ret = inter.interpret(function)
        if inter.vars[keyvarname].value == old_key:
            new_dict[old_key] = inter.vars[valuevarname].value
        else:
            new_dict[inter.vars[keyvarname].value] = inter.vars[valuevarname].value
    inter.vars[kwargs["vname"]].value = new_dict
    return inter.vars[kwargs["vname"]].value



#  # allows for repetitive setting on a multiple indexed dictionary
#                         if objfunc == "set":
#                             self.vars[vname].value[self.parse(0, line, args)[2]] = (
#                                 self.parse(1, line, args)[2]
#                             )
#                             return self.vars[vname].value
#                         # first argument is what to set, should be called to_set
#                         # rest of the arguments are the indices at which to index the object and set to_set
#                         if objfunc == "setn":
#                             # what to set
#                             to_set = self.parse(0, line, args)[2]
#                             # the rest of the arguments are the indices
#                             # example: dict.setn('im being set', 'index1', 'index2', 'index3', ...)
#                             # should equal: dict['index1']['index2']['index3'] = 'im being set'
#                             # the object to set
#                             obj = self.vars[vname].value
#                             # iterates through the indices
#                             for i in range(1, len(args)):
#                                 # if the index is the last one
#                                 if i == len(args) - 1:
#                                     # sets the index to to_set
#                                     obj[self.parse(i, line, args)[2]] = to_set
#                                 # if the index is not the last one
#                                 else:
#                                     # sets the object to the index
#                                     obj = obj[self.parse(i, line, args)[2]]
#                             # returns the object
#                             return self.vars[vname].value
#                         # recursively gets a value in a dictionary
#                         if objfunc == "get":
#                             # the object to get from
#                             obj = self.vars[vname].value
#                             # iterates through the indices
#                             for i in range(len(args)):
#                                 ind = self.parse(i, line, args)[2]
#                                 try:
#                                     # sets the object to the index
#                                     obj = obj[ind]
#                                 except KeyError:
#                                     self.raise_key(ind, line)
#                             # returns the object
#                             return obj
#                         # gets the keys of this dictionary
#                         if objfunc == "keys":
#                             return self.vars[vname].value.keys()
#                         # gets the values of this dictionary
#                         if objfunc == "values":
#                             return self.vars[vname].value.values()
#                         # gets the items of this dictionary
#                         if objfunc == "items":
#                             return self.vars[vname].value.items()
#                         # executes a function for each key-value pair
#                         if objfunc == "foreach":
#                             # variable name of the key
#                             keyname = self.parse(0, line, args)[2]
#                             # variable name of the value
#                             valuename = self.parse(1, line, args)[2]
#                             # check both keyname and valuename as varnames
#                             self.check_varname(keyname, line)
#                             self.check_varname(valuename, line)
#                             # function to execute
#                             function = args[2][0]
#                             # loop through the dictionary
#                             for key, value in self.vars[vname].value.items():
#                                 # set the key and value variables
#                                 self.vars[keyname] = Var(keyname, key)
#                                 self.vars[valuename] = Var(valuename, value)
#                                 # execute the function
#                                 self.interpret(function)
#                             # return the dictionary
#                             return self.vars[vname].value
#                         # maps each value in the dictionary to the output of the function
#                         if objfunc == "map":
#                             # map arguments
#                             keyvarname = self.parse(0, line, args)[2]
#                             valuevarname = self.parse(1, line, args)[2]
#                             # check both for varnames
#                             self.check_varname(keyvarname, line)
#                             self.check_varname(valuevarname, line)
#                             function = args[2][0]
#                             new_dict = {}
#                             # loop through the objects items, assigning the key to the key and
#                             # value to the value
#                             for key, value in self.vars[vname].value.items():
#                                 # log old key
#                                 old_key = key
#                                 # execute the function
#                                 self.vars[keyvarname] = Var(keyvarname, key)
#                                 self.vars[valuevarname] = Var(valuevarname, value)
#                                 # run the function
#                                 ret = self.interpret(function)
#                                 if self.vars[keyvarname].value == old_key:
#                                     new_dict[old_key] = self.vars[valuevarname].value
#                                 else:
#                                     new_dict[self.vars[keyvarname].value] = self.vars[
#                                         valuevarname
#                                     ].value
#                             self.vars[vname].value = new_dict
#                             return self.vars[vname].value


# function dispatch
FUNCTION_DISPATCH = {
    "redirect": f_redirect,
    "stopredirect": f_stopredirect,
    "startredirect": f_startredirect,
    "function": f_function,
    "def": f_def,
    "mod": f_mod,
    "ret": f_ret,
    "arr": f_arr, "from": f_arr,
    "object": f_object, "dictfrom": f_object,
    "split": f_split,
    "lines": f_lines,
    "eval": f_eval,
    "between": f_between,
    "isstr": f_isstr,
    "islist": f_islist,
    "isfloat": f_isfloat,
    "isint": f_isint,
    "isdict": f_isdict,
    "isinstance": f_isinstance,
    "sum": f_sum,
    "var": f_var,
    "list": f_list,
    "abs": f_abs,
    "zip": f_zip,
    "next": f_next,
    "iter": f_iter,
    "exists": f_exists,
    "exists:function": f_exists_function,
    "len": f_len,
    "assert": f_assert,
    "assert:err": f_assert_err,
    "settings": f_settings,
    "sortby": f_sortby,
    "comp": f_comp,
    "do": f_do,
    "None": f_None,
    "filter": f_filter,
    "unpack": f_unpack,
    "has": f_has,
    "first": f_first, "head": f_first,
    "add": f_add,
    "USD": f_USD,
    "format": f_format,
    "round": f_round,
    "maximum": f_maximum,
    "minimum": f_minimum,
    "sub": f_sub,
    "mul": f_mul,
    "div": f_div,
    "append": f_append,
    "->": f_op_getarrow,
    "version": f_version,
    "destroy": f_destroy,
    "destroy:function": f_destroy_function,
    "range": f_range,
    "uuid4": f_uuid4,
    "random": f_random,
    "merge": f_merge,
    "exception": f_exception,
    "syntax": f_syntax,
    "enclosedsyntax": f_enclosed_syntax,
    "macro": f_macro,
    "postmacro": f_postmacro,
    "val": f_val,
    "sorted": f_sorted,
    "copy": f_copy,
    "fileacquire": f_fileacquire,
    "filerelease": f_filerelease,
    "map": f_map,
    "insert": f_insert,
    "type": f_type,
    "parent": f_parent,
    "boot": f_boot,
    "del": f_del,
    "cat": f_cat,
    "equals": f_equals,
    "not": f_not,
    "and": f_and,
    "or": f_or,
    "greater": f_greater,
    "less": f_less,
    "greaterequal": f_greaterequal,
    "lessequal": f_lessequal,
    "class": f_class,
    "get": f_get,
    "getn": f_getn,
    "keys": f_keys,
    "import": f_import, "launch": f_import, "include": f_import, "using": f_import,
    "domain": f_domain,
    "domain:find": d_domainfind,
    "in": f_in,
    "out": f_out,
    "prnt": f_prnt,
    "print": f_print,
    "print:box": f_printbox,
    "print:color": f_printcolor,
    "sleep": f_sleep,
    "me": f_me,
    "next_entry_path": f_next_entry_path,
    "next_project_path": f_next_project_path,
    "env": f_env,
    "env:maxchars": f_envmaxchars,
    "-": f_symbolminus,
    "+": f_symbolplus,
    "x": f_symbolx,
    "*": f_symbolx,
    "/": f_symboldiv,
    "//": f_symboldivdiv,
    "%": f_symbolmod,
    "^": f_symbolpow,
    "isdigit": f_isdigit,
    "isalpha": f_isalpha,
    "as": f_as,
    "startswith": f_startswith,
    "endswith": f_endswith,
    "strip": f_strip,
    "slice": f_slice,
    "iterable:join": f_iterablejoin,
    "script": f_script, "async": f_script, "HTML": f_script,
    "ls": f_ls, "longstring": f_ls,
    "now": f_now,
    "private": f_private, "inherit:all": f_private,
    "break": f_break,
    "reverse": f_reverse,
    "upper": f_upper,
    "lower": f_lower,
    "title": f_title,
    "inherit:methods": f_inheritmethods,
    "inherit:vars": f_inheritvars,
    "inherit:single": f_inheritsingle,
    "new": f_new,
    "alias": f_alias,
    "process": f_process,
    "proc": f_proc,
    "pid": f_pid,
    "thread": f_thread,
    "threadpool": f_threadpool,
    "tvar": f_tvar,
    "gettvar": f_gettvar,
    "tvarstr": f_tvarstr,
    "varmethod": f_varmethod,
    "acquire": f_acquire,
    "release": f_release,
    "acquire:pointer": f_acquirepointer,
    "release:pointer": f_releasepointer,
    "join": f_join,
    "stop": f_stop,
    "try": f_try,
    "wait": f_wait,
    "interval": f_interval,
    "export": f_export,
    "exportas": f_exportas, "export:as": f_exportas,
    "exportall": f_exportall, "export:all": f_exportall,
    "exportthread": f_exportthread, "export:thread": f_exportthread,
    "clearthreads": f_clearthreads,
    "console": f_console,
    "consoleread": f_consoleread,
    "request": f_request,
    "return": f_return,
    "pubip": f_pubip,
    "privip": f_privip,
    "ENDPOINT": f_endpoint,
    "POST": f_post,
    "DELETE": f_delete,
    "GET": f_get_api,
    "windows": f_windows,
    "linux": f_linux,
    "mac": f_mac,
    "end": f_end,
    "static": f_static,
    "getattr": f_getattr,
    "setattr": f_setattr,
    "=>": f_multi_function,
    "clipboard": f_clipboard,
    
    # language functions
    "C": f_C,
    "JS": f_JS,
    "JAVA": f_JAVA,
    
    # different
    "app": f_app,
    "connect": f_connect,
    "excel": f_excel,


    # casting functions
    "int": f_int,
    "float": f_float,
    "str": f_str,
    "bool": f_bool,
    "complex": f_complex,
    "type": f_type,
    "dir": f_dir,
    "set": f_set,
    "dict": f_dict,
    "tuple": f_tuple,

    # conditional logic
    "if": f_if,
    "while": f_while,
    "for": f_for,
    "each": f_each,

    # special calls
    "special": {
        "?": f_quickcond,
        "varloop": f_varname_loop,
        "intloop": f_int_loop,
    },

    # msn2 classes
    "obj": {        
        "general": {
            "!": f_obj_general_destructive,
            
            # integers, floats, and complex numbers
            "number": {
                
                # comparisons
                "greater": f_obj_number_greater, "greaterthan": f_obj_number_greater, "g": f_obj_number_greater,
                "less": f_obj_number_less, "lessthan": f_obj_number_less, "l": f_obj_number_less,
                "greaterequal": f_obj_number_greaterequal, "ge": f_obj_number_greaterequal,
                "lessequal": f_obj_number_lessequal, "le": f_obj_number_lessequal,
                
                # operations (not inplace)
                "++": f_obj_number_inc, "inc": f_obj_number_inc,
                "--": f_obj_number_dec, "dec": f_obj_number_dec,
                "even": f_obj_number_even,
                "odd": f_obj_number_odd,
                
                # inplace
                "add": f_obj_number_add,
                "sub": f_obj_number_sub,
                "mul": f_obj_number_mul,
                "div": f_obj_number_div,
                "abs": f_obj_number_abs,
                "round": f_obj_number_round,
                "floor": f_obj_number_floor,
                "ceil": f_obj_number_ceil,
                "neg": f_obj_number_neg,
            },
            "set": {
                "add": f_obj_set_add, "put": f_obj_set_add,
                "pop": f_obj_set_pop,
                "remove": f_obj_set_remove,
                "list": f_obj_set_list,
                "get": f_obj_set_get,
            },
            "list": {
                "push": f_obj_list_push, "append": f_obj_list_push, "add": f_obj_list_push,
                "pop": f_obj_list_pop, 
                "get": f_obj_list_get,
                "set": f_obj_list_set,
                "avg": f_obj_list_avg, "average": f_obj_list_avg,
                "insert": f_obj_list_insert,
                "removen": f_obj_list_removen,
                "remove": f_obj_list_remove,
                "sorted": f_obj_list_sorted,
                "sort": f_obj_list_sort,
                "len": f_obj_list_len,
                "empty": f_obj_list_empty,  
                "contains": f_obj_list_contains, "has": f_obj_list_contains, "includes": f_obj_list_contains,
                "find": f_obj_list_find,
                "shuffle": f_obj_list_shuffle,
                "map": f_obj_list_map,
                "join": f_obj_list_join, "delimit": f_obj_list_join,
                "toset": f_obj_list_toset,
            },
            "str": {
                "add": f_obj_str_add,
                "split": f_obj_str_split,
                "lines": f_obj_str_lines,
                "words": f_obj_str_words,
                "lwordremove": f_obj_str_lwordremove,
                "rwordremove": f_obj_str_rwordremove,
                "chars": f_obj_str_chars,
                "isdigit": f_obj_str_isdigit,
                "isalpha": f_obj_str_isalpha,
                "replace": f_obj_str_replace,
                "strip": f_obj_str_strip, "trim": f_obj_str_strip,
                "stripped": f_obj_str_stripped,
                "self": f_obj_str_self,
                "set": f_obj_str_set,
                "get": f_obj_str_get,
                "upper": f_obj_str_upper,
                "lower": f_obj_str_lower,
                "cut": f_obj_str_cut,
                "shove": f_obj_str_shove,
                "around": f_obj_str_around,
                "startswith": f_obj_str_startswith,
                "endswith": f_obj_str_endswith,
            },
            "dict": {
                "set": f_obj_dict_set,
                "setn": f_obj_dict_setn,
                "get": f_obj_dict_get,
                "keys": f_obj_dict_keys,
                "values": f_obj_dict_values,
                "items": f_obj_dict_items,
                "foreach": f_obj_dict_foreach,
                "map": f_obj_dict_map,   
            },
            
            "class_based": {
                "<class 'requests_html.HTMLSession'>": {
                    "all": f_obj_html_all,
                    "render": f_obj_html_render,    
                    "else": f_obj_html_else,
                },
                "<class 'requests_html.HTML'>": {
                    "gather": f_obj_justhtml_gather,
                    "else": f_obj_justhtml_else,
                }
            },
            
            
            "default": {
                # chained
                "": f_obj_default_noobjfunc,
                
                # basic operations
                "+": f_obj_default_add,
                "-": f_obj_default_sub,
                "*": f_obj_default_mul, "x": f_obj_default_mul,
                "/": f_obj_default_div,
                "%": f_obj_default_mod,
                "**": f_obj_default_pow,
                "//": f_obj_default_idiv,
                

                "string_name": f_obj_default_string_name,
                "each": f_obj_default_each,
                "rfind": f_obj_default_rfind,
                "lfind": f_obj_default_lfind,
                "find": f_obj_default_find,
                "filter": f_obj_default_filter,
                "func": f_obj_default_func,
                "reverse": f_obj_default_reverse,
                "in": f_obj_default_in,
                
                "copy": f_obj_default_copy,
                "print": f_obj_default_print,
                "switch": f_obj_default_switch,
                "rename": f_obj_default_rename,
                "if": f_obj_default_if,
                "is": f_obj_default_is,
                "equals": f_obj_default_equals,
                "slice": f_obj_default_slice,
                "index": f_obj_default_index,
                "export": f_obj_default_export,
                
                # properties
                "val": f_obj_default_val,
                "type": f_obj_default_type,
                "len": f_obj_default_len,
                
                # casting
                "str": f_obj_default_str,
                "int": f_obj_default_int,
                "float": f_obj_default_float,
                "complex": f_obj_default_complex,
                "bool": f_obj_default_bool,
                "dict": f_obj_default_dict,
            }
        },
        "instance": {
            "new": f_instance_new,
        },
        "trace": {
            "this": f_trace_this,
            "before": f_trace_before,
            "len": f_trace_len,
        },
        "py": {
            "get": f_py_get,
            "set": f_py_set,
            "locals": f_py_locals,
            "local": f_py_local,
            "globals": f_py_globals,
            "global": f_py_global,
            "run": f_py_run,
            "else": f_py_else,
        },
        "op": {
            "append": f_op_append, "push": f_op_append, "add": f_op_append, "plus": f_op_append, "+": f_op_append, "concat": f_op_append, "concatenate": f_op_append, "join": f_op_append, "merge": f_op_append, "sum": f_op_append,
            "sub": f_op_sub, "minus": f_op_sub, "subtract": f_op_sub, "-": f_op_sub,
            "mul": f_op_mul, "times": f_op_mul, "x": f_op_mul, "*": f_op_mul, "multiply": f_op_mul,
            "div": f_op_div, "divide": f_op_div, "over": f_op_div, "/": f_op_div,
            "idiv": f_op_idiv, "intdiv": f_op_idiv, "intdivide": f_op_idiv, "intover": f_op_idiv, "//": f_op_idiv, "÷÷": f_op_idiv,
            "mod": f_op_mod, "modulo": f_op_mod, "modulus": f_op_mod, "%": f_op_mod, "remainder": f_op_mod,
            "pow": f_op_pow, "power": f_op_pow, "exponent": f_op_pow, "**": f_op_pow,
            "root": f_op_root, "nthroot": f_op_root, "nthrt": f_op_root,
            "else": f_op_else,
        },
        "function": {
            "addbody": f_function_addbody,
            "addarg": f_function_addarg,
            "addreturn": f_function_addreturn,
            "getbody": f_function_getbody,
            "getargs": f_function_getargs,
            "getreturn": f_function_getreturn,
            "destroy": f_function_destroy,
            "run": f_function_run,
            "else": f_function_else,
        },
        "html": {
            "soup": f_html_soup,
            "from": f_html_from,
            "session": f_html_session,
            "else": f_html_else,
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
            "getpos": f_pointer_getpos, "pos": f_pointer_getpos, "position": f_pointer_getpos,
            "move": f_pointer_move, "hover": f_pointer_move,
            "click": f_pointer_click, "left_click": f_pointer_click,
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
        }
    }
}
