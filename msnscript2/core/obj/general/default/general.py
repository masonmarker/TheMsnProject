




from core.classes.var import Var


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

OBJ_GENERAL_DEFAULT_GENERAL_DISPATCH = {
    # misc operations
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
}
