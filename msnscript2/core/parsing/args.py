"""Parsing arguments."""

from core.classes.instruction import Instruction

def consume(inter, line, i, l, obj, inst_tree, func, objfunc):
    mergedargs = ""
    p = 1
    for j in range(i + 1, l - 1):
        c2 = line[j]
        if p == 0:
            break
        if c2 == "(":
            p += 1
        if c2 == ")":
            p -= 1
        mergedargs += c2
    args = get_args(inter, mergedargs)
    # f = len(func)
    # clean function for handling
    func = func.strip()
    objfunc = objfunc.strip()
    # create an instruction from parsed data
    inst = Instruction(line, func, obj, objfunc,
                        args, inst_tree, inter)
    # return interpretations
    return mergedargs, args, func, objfunc, inst

def method_args(inter, line, j):
    argstring = ""
    instring = False
    for k in range(j + 1, len(line)):
        if line[k] == '"' and not instring:
            instring = True
        elif line[k] == '"' and instring:
            instring = False
        if not instring:
            if line[k] != " ":
                if line[k] == ")":
                    break
                argstring += line[k]
        else:
            argstring += line[k]
    return argstring.split(","), k

def get_args(inter, line):
    args = []
    l = len(line)
    arg = ""
    start = 0
    p = 0
    a = 0
    s = 0
    indouble = False
    s2 = 0
    insingle = False
    b = 0
    for i in range(l + 1):
        c = ""
        try:
            c = line[i]
        except:
            None
        if c == "[" and not s2 > 0 and not s > 0:
            a += 1
        if c == "]" and not s2 > 0 and not s > 0:
            a -= 1
        if c == "(" and not s2 > 0 and not s > 0:
            p += 1
        if c == ")" and not s2 > 0 and not s > 0:
            p -= 1
        if not inter.in_string(s, s2):
            if c == "{":
                b += 1
            if c == "}":
                b -= 1
        if not indouble and not s2 > 0 and c == '"':
            s += 1
            indouble = True
        elif indouble and c == '"':
            s -= 1
            indouble = False
        if not insingle and not s > 0 and c == "'":
            s2 += 1
            insingle = True
        elif insingle and c == "'":
            s2 -= 1
            insingle = False
        if c == "," and s == 0 and s2 == 0 and p == 0 and a == 0 and b == 0:
            args.append([arg, start, start + len(arg)])
            start = i + 1
            arg = ""
            continue
        elif i == l:
            args.append([arg, start, start + len(arg)])
        arg += c
    return args