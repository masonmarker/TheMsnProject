

# directory functions

# whether or not /api/functions has been created or not
api_functions_created = False
# serialized value for generating api routes
serialized_value = 0


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
    # get the name of the new function
    name = inst.parse(0)
    # function name must be a string
    inst.type_err([(name, (str,))], lines_ran)
    # create the new function
    new_func = inst.interpreter.Method(name, inst.interpreter)
    func_args = []
    # __temp = self.Method('', self)
    __temp = inst.interpreter.Method('', inst.interpreter)
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
        if not l.endswith('::\n'):
            continue
        l = l[2:-3].strip()
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

# parse prop


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
            props[key] = value if value else html_attribute_defaults[key]
    # return component with props
    return props

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
    from js import html_attributes, html_attribute_defaults
    merged = props.copy()
    parsed_props = parse_props(inst)

    # for each possible html attribute
    for attr in html_attributes:
        props_t = {key: value.strip() for key, value in props[attr].items(
        ) if value} if attr in props and props[attr] else {}
        parsed_props_t = {key: value.strip() for key, value in parsed_props[attr].items(
        ) if value} if attr in parsed_props and parsed_props[attr] and \
            type(parsed_props[attr]) != type(html_attribute_defaults[attr]) else {}
        merged[attr] = {**props_t, **parsed_props_t}
    # add rest of props
    for key, value in parsed_props.items():
        # append based on type
        if not merged[key]:
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
                       if not (as_s := inst.args[i][0].strip()) in inst.interpreter.states else
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
    inst.interpreter.styled_print([
        {'text': '[', 'fore': 'white', 'style': 'bold'}, 
        {'text': 'MSN2', 'fore': 'black',  'style': 'bold'},
        {'text': '] ', 'fore': 'white',  'style': 'bold'},
        {'text': 'updating: ', 'fore': 'blue',  'style': 'bold'},
        {'text': path, 'fore': 'green',  'style': 'bold'}
    ])
# prints finishing a file
def print_finished_file(inst, path):
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

# adds to web imports


def try_add_web_import(inst, importItems=[]):
    # for each import item
    for is_default, importName, importLocation in importItems:
        # if the import item is not in the web imports
        if (importName, importLocation) not in inst.interpreter.web_imports:
            # add the import item to the web imports
            inst.interpreter.web_imports.add((importName, importLocation))
            # insert the import
            insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
                                  f"import {'{' if not is_default else ''}{importName}{'}' if not is_default else ''} from '{importLocation}';", check_for_dups=True)
