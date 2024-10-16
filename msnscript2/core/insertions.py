
from core.common import aliases

def f_script(inter, line, args, **kwargs):
    # inserts key tokens
    return inter.msn2_replace(args[0][0])
def f_ls(inter, line, args, **kwargs):
    return inter.ls(args)

INSERTIONS_DISPATCH = {
    **aliases(f_script, ("script", "async", "HTML")),
    **aliases(f_ls, ("ls", "longstring")),
}