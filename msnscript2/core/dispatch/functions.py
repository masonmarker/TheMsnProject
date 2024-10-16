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
from core.classes.var import Var

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


def user_function_exec(inst, lines_ran):
    from core.classes.var import Var
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
