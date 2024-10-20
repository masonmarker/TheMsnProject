"""Varaible assigning with '=' and associated operators."""


from core.classes.var import Var


def var_assign_or_transform(inter, line):
    line = line[1:]
    element = ""
    variable = ""
    for i in range(0, len(line)):
        c = line[i]
        if c != " ":
            if c == "+" and line[i + 1] == "=":
                variable = element
                element = ""
                for j in range(i + 2, len(line)):
                    element += line[j]

                # if element is a number
                if isinstance(element, float) or isinstance(element, int):
                    inter.vars[variable].value += inter.interpret(element)
                # if element is a string
                elif isinstance(element, str):
                    try:
                        inter.vars[variable].value += inter.interpret(
                            element)
                    except:
                        inter.vars[variable].value += inter.interpret(
                            element)
                return inter.vars[variable].value
            elif c == "-" and line[i + 1] == "=":
                variable = element
                element = ""
                for j in range(i + 2, len(line)):
                    element += line[j]
                inter.vars[variable].value -= inter.interpret(element)
                return inter.vars[variable].value
            elif c == "*" and line[i + 1] == "=":
                variable = element
                element = ""
                for j in range(i + 2, len(line)):
                    element += line[j]
                inter.vars[variable].value *= inter.interpret(element)
                return inter.vars[variable].value
            elif c == "/" and line[i + 1] == "=":
                variable = element
                element = ""
                for j in range(i + 2, len(line)):
                    element += line[j]
                inter.vars[variable].value /= inter.interpret(element)
                return inter.vars[variable].value
            elif c == "=":
                variable = element
                element = ""
                string = False
                array = False
                for j in range(i + 1, len(line)):
                    if line[j] == '"':
                        string = True
                    if line[j] == "[":
                        array = True
                    element += line[j]
                inter.vars[variable] = Var(
                    variable, inter.interpret(element))
                return inter.vars[variable].value
            else:
                element += c
