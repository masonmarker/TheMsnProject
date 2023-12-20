# Converts MSN2 code to JavaScript.
#
# author : Mason Marker
# date : 12/15/2023
# version : 2.0.400

# available HTML elements
html_elements = (
    "h1", "h2", "h3", "h4", "h5", "h6", "div", "span", "p", "a", "b", "i", "u", "ul", "li", "ol", "br", "hr", "img", "input", "button", "form", "label", "select", "option", "textarea", "table", "thead", "tbody", "tr", "th", "td", "canvas", "audio", "video", "iframe", "script", "style", "link", "meta", "title", "head", "body", "html", "pre", "code"
)

# available React HTML attributes
html_attributes = (
    "accept", "acceptCharset", "accessKey", "action", "allowFullScreen", "allowTransparency", "alt", "async", "autoComplete", "autoFocus", "autoPlay", "capture", "cellPadding", "cellSpacing", "challenge", "charSet", "checked", "cite", "classID", "className", "colSpan", "cols", "content", "contentEditable", "contextMenu", "controls", "controlsList", "coords", "crossOrigin", "data", "dateTime", "default", "defer", "dir", "disabled", "download", "draggable", "encType", "form", "formAction", "formEncType", "formMethod", "formNoValidate", "formTarget", "frameBorder", "headers", "height", "hidden", "high", "href", "hrefLang", "htmlFor", "httpEquiv", "icon", "id", "inputMode", "integrity", "is", "keyParams", "keyType", "kind", "label", "lang", "list", "loop", "low", "manifest", "marginHeight", "marginWidth", "max", "maxLength", "media", "mediaGroup", "method", "min", "minLength", "multiple", "muted", "name", "noValidate", "nonce", "open", "optimum", "pattern", "placeholder", "poster", "preload", "profile", "radioGroup", "readOnly", "referrerPolicy", "rel", "required", "reversed", "role", "rowSpan", "rows", "sandbox", "scope", "scoped", "scrolling", "seamless", "selected", "shape", "size", "sizes", "span", "spellCheck", "src", "srcDoc", "srcLang", "srcSet", "start", "step", "style", "summary", "tabIndex", "target", "title", "type", "useMap", "value", "width", "wmode", "wrap"
)

# map of all html_attributes to their default values
html_attribute_defaults = {
    "accept": "",
    "acceptCharset": "",
    "accessKey": "",
    "action": "",
    "allowFullScreen": False,
    "allowTransparency": False,
    "alt": "",
    "async": False,
    "autoComplete": "",
    "autoFocus": False,
    "autoPlay": False,
    "capture": False,
    "cellPadding": "",
    "cellSpacing": "",
    "challenge": "",
    "charSet": "",
    "checked": False,
    "cite": "",
    "classID": "",
    "className": "",
    "colSpan": "",
    "cols": "",
    "content": "",
    "contentEditable": "",
    "contextMenu": "",
    "controls": False,
    "controlsList": "",
    "coords": "",
    "crossOrigin": "",
    "data": "",
    "dateTime": "",
    "default": False,
    "defer": False,
    "dir": "",
    "disabled": False,
    "download": "",
    "draggable": False,
    "encType": "",
    "form": "",
    "formAction": "",
    "formEncType": "",
    "formMethod": "",
    "formNoValidate": False,
    "formTarget": "",
    "frameBorder": "",
    "headers": "",
    "height": "",
    "hidden": False,
    "high": "",
    "href": "",
    "hrefLang": "",
    "htmlFor": "",
    "httpEquiv": "",
    "icon": "",
    "id": "",
    "inputMode": "",
    "integrity": "",
    "is": "",
    "keyParams": "",
    "keyType": "",
    "kind": "",
    "label": "",
    "lang": "",
    "list": "",
    "loop": False,
    "low": "",
    "manifest": "",
    "marginHeight": "",
    "marginWidth": "",
    "max": "",
    "maxLength": "",
    "media": "",
    "mediaGroup": "",
    "method": "",
    "min": "",
    "minLength": "",
    "multiple": False,
    "muted": False,
    "name": "",
    "noValidate": False,
    "nonce": "",
    "open": False,
    "optimum": "",
    "pattern": "",
    "placeholder": "",
    "poster": "",
    "preload": "",
    "profile": "",
    "radioGroup": "",
    "readOnly": False,
    "referrerPolicy": "",
    "rel": "",
    "required": False,
    "reversed": False,
    "role": "",
    "rowSpan": "",
    "rows": "",
    "sandbox": "",
    "scope": "",
    "scoped": False,
    "scrolling": "",
    "seamless": False,
    "selected": False,
    "shape": "",
    "size": "",
    "sizes": "",
    "span": "",
    "spellCheck": False,
    "src": "",
    "srcDoc": "",
    "srcLang": "",
    "srcSet": "",
    "start": "",
    "step": "",
    "style": "",
    "summary": "",
    "tabIndex": "",
    "target": "",
    "title": "",
    "type": "",
    "useMap": "",
    "value": "",
    "width": "",
    "wmode": "",
    "wrap": ""
}

# converts an MSN2 instruction to JS

# returns the representation of parsed input


def parse(inst, i):
    strp = inst.args[i][0].strip()
    if inst.interpreter.is_py_str(strp):
        return f"`{inst.parse(i)}`"
    ret = inst.parse(i)
    if ret == None:
        ret = strp
    return ret


def convert_to_js(inst, lock, lines_ran):
    """
    Converts a 'line' of MSN2 code to JavaScript
    """
    # user function execution requested
    if inst.func in inst.interpreter.methods:
        from functions import user_function_exec
        # # execute the function
        # user_function_exec(inst, lines_ran)
        as_js = f"{inst.func}("
        for i in range(len(inst.args)):
            if i != len(inst.args) - 1:
                as_js += f"{inst.parse(i)}, "
            else:
                as_js += f"{inst.parse(i)}"
        return as_js + ")"
    # if in inst.interpreter.routes
    elif inst.func in inst.interpreter.routes:
        from functions import insert_line_at_marker
        # import this route
        
        
    elif inst.func == "+":
        return f"({inst.parse(0)} + {inst.parse(1)})"
    elif inst.func == "-":
        from functions import hyphen
        return hyphen(inst)
    # default export code manipulation
    # prettifying code
    elif inst.func == "prettify":
        import jsbeautifier
        # get file path
        file_path = inst.parse(0)
        # get file contents
        with open(file_path, 'r') as f:
            contents = f.read()
        # prettify the contents
        contents = jsbeautifier.beautify(contents, {
            "indent_size": 2,
            "indent_char": " ",
            "indent_with_tabs": False,
            "e4x": True,
            "max_preserve_newlines": 2,
            "preserve_newlines": True,
            "jslint_happy": True
        })
        # write the contents back to the file
        with open(file_path, 'w') as f:
            f.write(contents)
        return ""
    # variables
    # creating a const
    elif inst.func == "const":
        from msnint2 import Var
        name = inst.parse(0)
        value = parse(inst, 1)
        # create variable in inst.interpreter.vars
        inst.interpreter.vars[name] = Var(name, value)
        return f"const {name} = {value}\n"
    # creates a state
    elif inst.func == "state":
        from msnint2 import Var
        # determine if useState has been imported
        if (imp := ('useState', 'react')) not in inst.interpreter.web_imports:
            from functions import insert_line_at_marker
            # insert_line_at_marker()
            inst.interpreter.web_imports.add(imp)
            # insert the import
            insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
                                  "import { useState } from 'react';")
        name = inst.parse(0)
        set_function = f"set{name.capitalize()}"
        default_value = parse(inst, 1)
        # add state to list of states
        inst.interpreter.states[name] = Var(name, default_value)
        # create new variable
        return f"const [{name}, {set_function}] = useState({default_value})\n"
    # inserts a useEffect hook
    elif inst.func == "effect":
        from functions import use_effect
        return use_effect(inst)
    # creates a /pages/api/*route*.js file for api interaction
    # also (creates and) appends to /api/functions.js file for api functions
    elif inst.func == "api:route":
        from functions import add_api_route
        # take input
        route_name = inst.parse(0)
        # route request variable name
        route_req_name = inst.parse(1)
        # route response variable name
        route_res_name = inst.parse(2)
        # generate script from 3rd argument
        script = parse(inst, 3)
        # function fetch body variable name
        fetch_body_name = inst.parse(4)
        # function fetch body script
        fetch_body_script = parse(inst, 5)
        # create the api route function to place at the default export
        # function called when fetched at this route
        api_route_script = "export default async function " + \
            route_name + "(" + route_req_name + ", " + route_res_name + ")  \
            {\n" + script + "\n}"
        # fetches the api route
        api_func_script = "export async function " + route_name +  \
        "(" + fetch_body_name + ") {\nreturn " + fetch_body_script + "\n}"
        # add the api route
        add_api_route(inst, route_name, api_route_script, api_func_script)
        # add route to interpreter
        inst.interpreter.routes[route_name] = api_route_script
        return ""
    # gets from api:route
    elif inst.func == "api:get":
        # path to get from
        path = inst.parse(0)
        # request body
        # body = str(parse(inst, 1))
        return "await fetch('/api/" + path + "').then(res => res.json())"
    # creates a component route
    elif inst.func == "route":
        from functions import add_route
        return add_route(inst, inst.parse(0), inst.parse(1))
    
    # creates a page route to a script
    # HTML elements
    elif inst.func in html_elements:
        from functions import component
        return component(inst, inst.func)
    # vcenter: centers an element vertically
    elif inst.func == "vcenter":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'alignItems': 'center',
        }})
    # hcenter: centers an element horizontally
    elif inst.func == "hcenter":
        from functions import component
        return component(inst, "div", {'style': {
            'height': "10rem",
            'display': 'flex',
            'justifyContent': 'center',
        }})
    # centers children both horizontally and vertically
    elif inst.func == "center":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'justifyContent': 'center',
            'alignItems': 'center',
        }})
    # vertical stack of components
    elif inst.func == "vstack":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'flexDirection': 'column',
        }})
    # horizontal stack of components
    elif inst.func == "hstack":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'flexDirection': 'row',
        }})
    # checking objfunc
    elif inst.obj == "file":
        if inst.objfunc == "write":
            from functions import file_write
            return file_write(inst, lock, lines_ran)
        elif inst.objfunc == "append":
            from functions import file_append
            return file_append(inst, lock, lines_ran)
    # creating a JS import
    elif inst.func == "import":
        from functions import insert_line_at_marker
        # component to import
        comp = inst.parse(0)
        # path to import from
        path = inst.parse(1)
        insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports", f"import {comp} from '{path}';")
        return ""
    # general print, JavaScript equivalent
    # is the notorious 'console.log'
    elif inst.func in ("prnt", "print"):
        from functions import stringify
        as_js = "console.log("
        for i in range(len(inst.args)):
            curr = parse(inst, i)
            if not curr:
                curr = inst.args[i][0].strip()
            if i != len(inst.args) - 1:
                as_js += f"{curr},"
            else:
                as_js += curr
        return as_js + ")"
    # creates a function
    elif inst.func == 'def':
        from functions import define
        # define the function so it may be recognized as a method
        # in future interpretation
        define(inst, lines_ran)
        as_js = f"function {inst.parse(0)}("
        # last argument is the function body
        for i in range(1, len(inst.args) - 1):
            if i != len(inst.args) - 2:
                as_js += f"{inst.parse(i)}, "
            else:
                as_js += f"{inst.parse(i)}"
        as_js += ") {\n// hooks ::\n"
        as_js += f"return {inst.parse(-1)}"
        as_js += "}"
        return as_js
    elif inst.func == "=>" or (inst.func == '' and inst.objfunc == ''):
        from functions import callback
        return callback(inst)
    # async function
    elif inst.func == "async":
        from functions import callback
        # does the same as the above condition
        # but with async
        return callback(inst, is_async=True)
    # exports a function
    elif inst.func == "export":
        # export the first argument as a string
        return f"export {inst.parse(0)}"
    elif inst.func == "exdefault":
        # export the first argument as a string
        return f"export default {inst.parse(0)}"
    # set interpreter using_js to False
    inst.interpreter.using_js = False
    ret = inst.interpret()
    inst.interpreter.using_js = True
    return ret
