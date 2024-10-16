

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

SPECIAL_LOOPS_DISPATCH = {
    "?": f_quickcond,
    "varloop": f_varname_loop,
    "intloop": f_int_loop,
}