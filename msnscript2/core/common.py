"""Common utilities."""


def multi_lined(inst):
    ret = None
    for i in range(len(inst.args)):
        ret = inst.parse(i)
    return ret

# creates aliases for a function name


def aliases(func, aliases):
    return {alias: func for alias in aliases}


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
