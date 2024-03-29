# Converts MSN2 code to JavaScript.
#
# author : Mason Marker
# date : 12/15/2023
# version : 2.0.400

# available HTML elements
html_elements = (
    "nav", "h1", "h2", "h3", "h4", "h5", "h6", "div", "span", "p", "a", "b", "i", "u", "ul", "li", "ol", "br", "hr", "img", "input", "button", "form", "label", "select", "option", "textarea", "table", "thead", "tbody", "tr", "th", "td", "canvas", "audio", "video", "iframe", "script", "style", "link", "meta", "title", "head", "body", "html", "pre", "code"
)

# available React HTML attributes
html_attributes = (
    "onAbort", "onAbortCapture", "onAnimationEnd", "onAnimationEndCapture", "onAnimationIteration", "onAnimationIterationCapture", "onAnimationStart", "onAnimationStartCapture", "onAuxClick", "onAuxClickCapture", "onBlur", "onBlurCapture", "onCanPlay", "onCanPlayCapture", "onCanPlayThrough", "onCanPlayThroughCapture", "onChange", "onChangeCapture", "onClick", "onClickCapture", "onCompositionEnd", "onCompositionEndCapture", "onCompositionStart", "onCompositionStartCapture", "onCompositionUpdate", "onCompositionUpdateCapture", "onContextMenu", "onContextMenuCapture", "onCopy", "onCopyCapture", "onCut", "onCutCapture", "onDoubleClick", "onDoubleClickCapture", "onDrag", "onDragCapture", "onDragEnd", "onDragEndCapture", "onDragEnter", "onDragEnterCapture", "onDragExit", "onDragExitCapture", "onDragLeave", "onDragLeaveCapture", "onDragOver", "onDragOverCapture", "onDragStart", "onDragStartCapture", "onDrop", "onDropCapture", "onDurationChange", "onDurationChangeCapture", "onEmptied", "onEmptiedCapture", "onEncrypted", "onEncryptedCapture", "onEnded", "onEndedCapture", "onError", "onErrorCapture", "onFocus", "onFocusCapture", "onGotPointerCapture", "onGotPointerCaptureCapture", "onInput", "onInputCapture", "onInvalid", "onInvalidCapture", "onKeyDown", "onKeyDownCapture", "onKeyPress", "onKeyPressCapture", "onKeyUp", "onKeyUpCapture", "onLoad", "onLoadCapture", "onLoadStart", "onLoadStartCapture", "onLoadedData", "onLoadedDataCapture", "onLoadedMetadata", "onLoadedMetadataCapture", "onLostPointerCapture", "onLostPointerCaptureCapture", "onMouseDown", "onMouseDownCapture", "onMouseEnter", "onMouseLeave", "onMouseMove", "onMouseMoveCapture", "onMouseOut", "onMouseOutCapture", "onMouseOver", "onMouseOverCapture", "onMouseUp", "onMouseUpCapture", "onPaste", "onPasteCapture", "onPause", "onPauseCapture", "onPlay", "onPlayCapture", "onPlaying", "onPlayingCapture", "onPointerCancel", "onPointerCancelCapture", "onPointerDown", "onPointerDownCapture", "onPointerEnter", "onPointerEnterCapture", "onPointerLeave", "onPointerLeaveCapture", "onPointerMove", "onPointerMoveCapture", "onPointerOut", "onPointerOutCapture", "onPointerOver", "onPointerOverCapture", "onPointerUp", "onPointerUpCapture", "onProgress", "onProgressCapture", "onRateChange", "onRateChangeCapture", "onReset", "onResetCapture", "onResize", "onResizeCapture", "onScroll", "onScrollCapture", "onSeeked", "onSeekedCapture", "onSeeking", "onSeekingCapture", "onSelect", "onSelectCapture", "onStalled", "onStalledCapture", "onSubmit", "onSubmitCapture", "onSuspend", "onSuspendCapture", "onTimeUpdate", "onTimeUpdateCapture", "onToggle", "onToggleCapture", "onTouchCancel", "onTouchCancelCapture", "onTouchEnd", "onTouchEndCapture", "onTouchMove", "onTouchMoveCapture", "onTouchStart", "onTouchStartCapture", "onTransitionEnd", "onTransitionEndCapture", "onVolumeChange", "onVolumeChangeCapture", "onWaiting", "onWaitingCapture", "onWheel", "onWheelCapture",
    "accept", "acceptCharset", "accessKey", "action", "allowFullScreen", "allowTransparency", "alt", "async", "autoComplete", "autoFocus", "autoPlay", "capture", "cellPadding", "cellSpacing", "challenge", "charSet", "checked", "cite", "classID", "className", "colSpan", "cols", "content", "contentEditable", "contextMenu", "controls", "controlsList", "coords", "crossOrigin", "data", "dateTime", "default", "defer", "dir", "disabled", "download", "draggable", "encType", "form", "formAction", "formEncType", "formMethod", "formNoValidate", "formTarget", "frameBorder", "headers", "height", "hidden", "high", "href", "hrefLang", "htmlFor", "httpEquiv", "icon", "id", "inputMode", "integrity", "is", "keyParams", "keyType", "kind", "label", "lang", "list", "loop", "low", "manifest", "marginHeight", "marginWidth", "max", "maxLength", "media", "mediaGroup", "method", "min", "minLength", "multiple", "muted", "name", "noValidate", "nonce", "open", "optimum", "pattern", "placeholder", "poster", "preload", "profile", "radioGroup", "readOnly", "referrerPolicy", "rel", "required", "reversed", "role", "rowSpan", "rows", "sandbox", "scope", "scoped", "scrolling", "seamless", "selected", "shape", "size", "sizes", "span", "spellCheck", "src", "srcDoc", "srcLang", "srcSet", "start", "step", "style", "summary", "tabIndex", "target", "title", "type", "useMap", "value", "width", "wmode", "wrap"
)

# map of all html_attributes to their default values
html_attribute_defaults = {"accept": "",    "acceptCharset": "",    "accessKey": "",    "action": "",    "allowFullScreen": False,    "allowTransparency": False,    "alt": "",    "async": False,    "autoComplete": "",    "autoFocus": False,    "autoPlay": False,    "capture": False,    "cellPadding": "",    "cellSpacing": "",    "challenge": "",    "charSet": "",    "checked": False,    "cite": "",    "classID": "",    "className": "",    "colSpan": "",    "cols": "",    "content": "",    "contentEditable": "",    "contextMenu": "",    "controls": False,    "controlsList": "",    "coords": "",    "crossOrigin": "",    "data": "",    "dateTime": "",    "default": False,    "defer": False,    "dir": "",    "disabled": False,    "download": "",    "draggable": False,    "encType": "",    "form": "",    "formAction": "",    "formEncType": "",    "formMethod": "",    "formNoValidate": False,    "formTarget": "",    "frameBorder": "",    "headers": "",    "height": "",    "hidden": False,    "high": "",    "href": "",    "hrefLang": "",    "htmlFor": "",    "httpEquiv": "",    "icon": "",    "id": "",    "inputMode": "",    "integrity": "",    "is": "",    "keyParams": "",    "keyType": "",    "kind": "",    "label": "",    "lang": "",    "list": "",    "loop": False,    "low": "",    "manifest": "",    "marginHeight": "",    "marginWidth": "",    "max": "",    "maxLength": "",    "media": "",    "mediaGroup": "",    "method": "",    "min": "",    "minLength": "",    "multiple": False,    "muted": False,    "name": "",    "noValidate": False,    "nonce": "",    "open": False,    "optimum": "",    "onAbort": "",    "onAbortCapture": "",    "onAnimationEnd": "",    "onAnimationEndCapture": "",    "onAnimationIteration": "",    "onAnimationIterationCapture": "",    "onAnimationStart": "",    "onAnimationStartCapture": "",    "onAuxClick": "",    "onAuxClickCapture": "",    "onBlur": "",    "onBlurCapture": "",    "onCanPlay": "",    "onCanPlayCapture": "",    "onCanPlayThrough": "",    "onCanPlayThroughCapture": "",    "onChange": "",    "onChangeCapture": "",    "onClick": "",    "onClickCapture": "",    "onCompositionEnd": "",    "onCompositionEndCapture": "",    "onCompositionStart": "",    "onCompositionStartCapture": "",    "onCompositionUpdate": "",    "onCompositionUpdateCapture": "",    "onContextMenu": "",    "onContextMenuCapture": "",    "onCopy": "",    "onCopyCapture": "",    "onCut": "",    "onCutCapture": "",    "onDoubleClick": "",    "onDoubleClickCapture": "",    "onDrag": "",    "onDragCapture": "",    "onDragEnd": "",    "onDragEndCapture": "",    "onDragEnter": "",    "onDragEnterCapture": "",    "onDragExit": "",    "onDragExitCapture": "",    "onDragLeave": "",    "onDragLeaveCapture": "",    "onDragOver": "",    "onDragOverCapture": "",    "onDragStart": "",    "onDragStartCapture": "",    "onDrop": "",    "onDropCapture": "",    "onDurationChange": "",    "onDurationChangeCapture": "",    "onEmptied": "",    "onEmptiedCapture": "",    "onEncrypted": "",    "onEncryptedCapture": "",    "onEnded": "",    "onEndedCapture": "",    "onError": "",    "onErrorCapture": "",    "onFocus": "",    "onFocusCapture": "",    "onGotPointerCapture": "",
                           "onGotPointerCaptureCapture": "",    "onInput": "",    "onInputCapture": "",    "onInvalid": "",    "onInvalidCapture": "",    "onKeyDown": "",    "onKeyDownCapture": "",    "onKeyPress": "",    "onKeyPressCapture": "",    "onKeyUp": "",    "onKeyUpCapture": "",    "onLoad": "",    "onLoadCapture": "",    "onLoadStart": "",    "onLoadStartCapture": "",    "onLoadedData": "",    "onLoadedDataCapture": "",    "onLoadedMetadata": "",    "onLoadedMetadataCapture": "",    "onLostPointerCapture": "",    "onLostPointerCaptureCapture": "",    "onMouseDown": "",    "onMouseDownCapture": "",    "onMouseEnter": "",    "onMouseLeave": "",    "onMouseMove": "",    "onMouseMoveCapture": "",    "onMouseOut": "",    "onMouseOutCapture": "",    "onMouseOver": "",    "onMouseOverCapture": "",    "onMouseUp": "",    "onMouseUpCapture": "",    "onPaste": "",    "onPasteCapture": "",    "onPause": "",    "onPauseCapture": "",    "onPlay": "",    "onPlayCapture": "",    "onPlaying": "",    "onPlayingCapture": "",    "onPointerCancel": "",    "onPointerCancelCapture": "",    "onPointerDown": "",    "onPointerDownCapture": "",    "onPointerEnter": "",    "onPointerEnterCapture": "",    "onPointerLeave": "",    "onPointerLeaveCapture": "",    "onPointerMove": "",    "onPointerMoveCapture": "",    "onPointerOut": "",    "onPointerOutCapture": "",    "onPointerOver": "",    "onPointerOverCapture": "",    "onPointerUp": "",    "onPointerUpCapture": "",    "onProgress": "",    "onProgressCapture": "",    "onRateChange": "",    "onRateChangeCapture": "",    "onReset": "",    "onResetCapture": "",    "onResize": "",    "onResizeCapture": "",    "onScroll": "",    "onScrollCapture": "",    "onSeeked": "",    "onSeekedCapture": "",    "onSeeking": "",    "onSeekingCapture": "",    "onSelect": "",    "onSelectCapture": "",    "onStalled": "",    "onStalledCapture": "",    "onSubmit": "",    "onSubmitCapture": "",    "onSuspend": "",    "onSuspendCapture": "",    "onTimeUpdate": "",    "onTimeUpdateCapture": "",    "onToggle": "",    "onToggleCapture": "",    "onTouchCancel": "",    "onTouchCancelCapture": "",    "onTouchEnd": "",    "onTouchEndCapture": "",    "onTouchMove": "",    "onTouchMoveCapture": "",    "onTouchStart": "",    "onTouchStartCapture": "",    "onTransitionEnd": "",    "onTransitionEndCapture": "",    "onVolumeChange": "",    "onVolumeChangeCapture": "",    "onWaiting": "",    "onWaitingCapture": "",    "onWheel": "",    "onWheelCapture": "",    "pattern": "",    "placeholder": "",    "poster": "",    "preload": "",    "profile": "",    "radioGroup": "",    "readOnly": False,    "referrerPolicy": "",    "rel": "",    "required": False,    "reversed": False,    "role": "",    "rowSpan": "",    "rows": "",    "sandbox": "",    "scope": "",    "scoped": False,    "scrolling": "",    "seamless": False,    "selected": False,    "shape": "",    "size": "",    "sizes": "",    "span": "",    "spellCheck": False,    "src": "",    "srcDoc": "",    "srcLang": "",    "srcSet": "",    "start": "",    "step": "",    "style": "",    "summary": "",    "tabIndex": "",    "target": "",    "title": "",    "type": "",    "useMap": "",    "value": "",    "width": "",    "wmode": "",    "wrap": ""}

# converts an MSN2 instruction to JS

# returns the representation of parsed input


def parse(inst, i):
    from functions import is_jsx_element
    strp = inst.args[i][0].strip()
    if inst.interpreter.is_py_str(strp):
        return f"`{inst.parse(i)}`"
    ret = inst.parse(i)
    if ret == None:
        ret = strp
    return ret


def parse_string(inst, string):
    strp = string.strip()
    if inst.interpreter.is_py_str(strp):
        return f"`{inst.interpreter.interpret(string)}`"
    ret = inst.interpreter.interpret(string)
    if ret == None:
        ret = strp
    return string


def convert_to_js(inst, lock, lines_ran):
    """
    Converts a 'line' of MSN2 code to JavaScript
    """
    # user function execution requested
    if inst.func in inst.interpreter.methods:
        from functions import is_msn2_react_call, user_function_exec
        # execute the function
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
        raise NotImplementedError
    # if in inst.interpreter.states
    elif inst.obj in inst.interpreter.states:
        state_name = inst.obj
        # get the state value
        state_value = inst.interpreter.states[state_name].value
        if inst.objfunc == 'set':
            from functions import generate_safe_set_function
            return generate_safe_set_function(inst, state_name)
        # otherwise, return the state
        return state_name
    elif inst.func == "+":
        return f"({parse(inst, 0)} + {parse(inst, 1)})"
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
        from functions import is_react_code
        name = inst.parse(0)
        value = parse(inst, 1)
        # if value is react code
        # create variable in inst.interpreter.vars
        inst.interpreter.vars[name] = Var(
            name, name if not is_react_code else f"{{{name}}}")
        return f"const {name} = {value}\n"
    # creates a state
    elif inst.func == "state":
        from msnint2 import Var
        from functions import generate_set_function, try_add_web_import

        # # determine if useState has been imported
        # if (imp := ('useState', 'react')) not in inst.interpreter.web_imports:
        #     from functions import insert_line_at_marker
        #     # insert_line_at_marker()
        #     inst.interpreter.web_imports.add(imp)
        #     # insert the import
        #     insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
        #                           "import { useState } from 'react';", check_for_dups=True)
        #     # insert useEffect
        #     insert_line_at_marker(inst, inst.interpreter.next_entry_path, "imports",
        #                           "import { useEffect } from 'react';", check_for_dups=True)
        try_add_web_import(
            inst, [(False, 'useState', 'react'), (False, 'useEffect', 'react')])

        name = inst.parse(0)
        set_function = generate_set_function(name)
        # default value or new value
        default_value_or_new_value = parse(inst, 1)
        # if this state is already defined
        if name in inst.interpreter.states:
            return generate_set_function(name) + f"({name} => " + " {return " + str(default_value_or_new_value) + "})\n"
        # add state to list of states
        inst.interpreter.states[name] = Var(name, default_value_or_new_value)
        # create new variable
        return f"const [{name}, {set_function}] = useState({default_value_or_new_value})\nuseEffect(() => {{\n// {name} useEffect ::\n}}, [{name}])\n"
    # inserts a useEffect hook
    elif inst.func == "effect":
        from functions import use_effect
        return use_effect(inst)
    # map(iterable, element_varname, (opt) index_varname, js_script) function
    elif inst.func == "map":
        from msnint2 import Var
        # grab the iterable
        iterable = inst.parse(0)
        # grab the element variable name
        element_varname = inst.parse(1)
        # create element_varname as a variable
        inst.interpreter.vars[element_varname] = Var(
            element_varname, element_varname)
        # if len of args is 4, then there is an index variable name
        if len(inst.args) == 4:
            index_varname = inst.parse(2)
            # create index_varname as a variable
            inst.interpreter.vars[index_varname] = Var(
                index_varname, index_varname)
        else:
            index_varname = None

        return f"{{{iterable}.map(({element_varname}{', ' + index_varname if index_varname else ''}) => \u007breturn {parse(inst, -1)}\u007d)}}"

    # creates a /pages/api/*route*.js file for api interaction
    # also (creates and) appends to /api/functions.js file for api functions
    elif inst.func == "apiroute":
        from functions import add_api_route, generate_api_scripts_and_add
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
        # api_route_script = "export default async function " + \
        #     route_name + "(" + route_req_name + ", " + route_res_name + ")  \
        #     {\n" + script + "\n}"
        # # fetches the api route
        # api_func_script = "export async function " + route_name +  \
        #     "(" + fetch_body_name + ") {\nreturn " + fetch_body_script + "\n}"
        generate_api_scripts_and_add(inst, route_name, route_req_name,
                                     route_res_name, script, fetch_body_name, fetch_body_script)
        return ""
    # simpler apiroute
    elif inst.func == "defineapi":
        from functions import generate_api_scripts_and_add, generate_fetch
        # take route_name
        route_name = inst.parse(0)
        # take request variable name
        route_req_name = inst.parse(1)
        # take response variable name
        route_res_name = inst.parse(2)
        # take script
        script = parse(inst, 3)
        # create the api route function to place at the default export
        # function called when fetched at this route
        generate_api_scripts_and_add(inst, route_name, route_req_name,
                                     route_res_name, script, 'body', generate_fetch(route_name))
        return ""
    # gets from api:route
    elif inst.func == "apiget":
        from functions import generate_fetch
        # path to get from
        path = inst.parse(0)
        # request body
        return generate_fetch(inst.parse(0))
    # creates a component route
    elif inst.func == "route":
        from functions import add_route
        return add_route(inst, inst.parse(0), inst.parse(1))
    # link to a different page
    elif inst.func == "linkto":
        # import nextlink and useRouter
        from functions import try_add_web_import
        # get the page to navigate to
        page = inst.parse(0)
        # remove the first 2 args in inst.args
        # to prepare component
        inst.args = inst.args[1:]
        # add the imports
        try_add_web_import(inst, [(False, 'useRouter', 'next/router'),
                                  (True, 'Link', 'next/link')])
        from functions import component
        # return the link
        return component(inst, "Link", {'href': page})
    # grid
    # forces row and columns
    elif inst.func == "grid":
        # get number of rows and columns
        rows = inst.parse(0)
        cols = inst.parse(1)
        # remove the first 2 args in inst.args
        # to prepare component
        inst.args = inst.args[2:]
        # return component
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'grid',
            'gridTemplateRows': f"repeat({rows}, 1fr)",
            'gridTemplateColumns': f"repeat({cols}, 1fr)"
        }})
    # creates a page route to a script
    # HTML elements
    elif inst.func in html_elements:
        from functions import component
        return component(inst, inst.func)
    # general text
    elif inst.func == "text":
        from functions import component
        return component(inst, "span")
    # vcenter: centers an element vertically
    elif inst.func == "vcenter":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'alignItems': 'center',
            'textAlign': 'center'
        }})
    # hcenter: centers an element horizontally
    elif inst.func == "hcenter":
        from functions import component
        return component(inst, "div", {'style': {
            'height': "10rem",
            'display': 'flex',
            'justifyContent': 'center',
            'textAlign': 'center'
        }})
    # centers children both horizontally and vertically
    elif inst.func == "center":
        from functions import component
        return component(inst, "div", {'style': {
            'display': 'flex',
            'justifyContent': 'center',
            'alignItems': 'center',
            'textAlign': 'center'
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
    # imports from an msn2 file
    elif inst.func == "import":
        from js import parse_string
        from functions import get_root_dir
        import os
        # get the path of the file
        path = inst.parse(0)
        # get the root dir
        root_dir = get_root_dir(inst)
        # find file
        path = f"{root_dir}msn2/{path}"
        # add .msn2 extension if not already
        if not path.endswith(".msn2"):
            path += ".msn2"
        # get the lines of the file
        with open(path, 'r') as f:
            lines = f.readlines()
        strrep = ""
        for line in lines:
            line = line.strip()
            # if the line is a comment
            if inst.interpreter.is_comment(line):
                continue
            if line:
                strrep += line + "\n"
        # return the string representation of the file
        return inst.interpreter.interpret(strrep)
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
    # general unnamed function with possible arguments
    elif inst.func == "do":
        # if no arguments
        if inst.args[0][0] == "":
            return "async () => {}"
        # get arguments, aka all arguments except the last
        args = [inst.parse(i) for i in range(len(inst.args) - 1)]
        # get function body, aka the last argument
        body = parse(inst, -1)
        return f"async ({', '.join([str(arg) for arg in args if arg])}) => \u007b{body}\u007d"
    # awaits a function
    elif inst.func == "await":
        return f"await {parse(inst, 0)}"
    # detailed named function with arguments
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
        as_js += ") {\n"
        as_js += f"return {inst.parse(-1)}"
        as_js += "}"
        return as_js
    # creates and executes a function in one call
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
