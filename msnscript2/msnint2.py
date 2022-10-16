# Interpreters MSNScript 2.0
# Author : Mason Marker
# Date : 09/15/2022

import os
import math
import openai
import webbrowser
import time
import threading
import sys


class Err:
    def __init__(self, errorcode):
        self.errorcode = errorcode


class Var:

    # constructs a new Var
    def __init__(self, name, value):
        self.name = name
        self.value = value

    # determines equality of another Var
    def __eq__(self, other):
        if isinstance(other, Var):
            return other.name == self.name


# interprets MSNScript2, should create a new interpreter for each execution iteration
class Interpreter:

    # code-specific variables
    version = 2.0
    lines = []
    out = ''
    log = ''
    errors = []
    vars = {}
    methods = {}
    loggedmethod = []
    calledmethod = None
    current_line = 0

    # threading implementation
    thread = None

    # ai implementation
    openaikey = None
    tokens = 100

    # browser specific variables
    browser_path = None

    # initializer
    def __init__(self):
        None

    # executes stored script
    def execute(self, script):

        # convert script to lines
        self.lines = list(filter(None, script.split("\n")))

        # proceeds to interpret MSNScript2 line by line
        inmultiline = False
        multiline = ''
        for line in self.lines:
            line = line.strip()
            if line.startswith("::"):
                self.current_line += 1
                continue
            else:
                if not inmultiline and line.startswith("$$"):
                    inmultiline = True
                    multiline += line[2:]
                elif inmultiline and line.endswith("$$"):
                    inmultiline = False
                    multiline += line[0:len(line) - 2]
                    self.interpret(multiline)
                    multiline = ''
                elif inmultiline:
                    multiline += line
                else:
                    self.interpret(line)
            self.current_line += 1
        return self.out

    def replace_vars(self, line):
        boo = line 
        varnames = self.var_names()
        varnames = sorted(varnames, key=len, reverse=True)
        for varname in varnames:
            try:
                boo = boo.replace(varname[1:len(varname) - 1], str(self.get_var('"' + eval(varname) + '"')))
            except:
                None
        return boo

    def contains_boolop(self, line):
        return line.find("==") != -1 or line.find("!=") != -1 or line.find("<=") != -1 or line.find(">=") != -1 or line.find("and") != -1 or line.find("or") != -1 or line.find("not") != -1

    # interprets a line    
    def interpret(self, line):
        line = line.strip()
        l = len(line)
        cont = False
        if line == '':
            return

        if line[0] == '@':
            # parse all text in the line for text surrounded by @
            # if the text is a variable, replace it with the variable value
            
            funccalls = []
            infunc = False
            func = ''
            for i in range(0, l):
                if line[i] == '?' and not infunc:
                    infunc = True
                elif line[i] == '?' and infunc:
                    infunc = False
                    funccalls.append(func)
                    func = ''
                elif infunc:
                    func += line[i]
            line = line[1:]
            
            # replace all variables in funccall
            for funccall in funccalls:
                line = line.replace("?"+funccall+"?", str(self.get_var('"' + funccall + '"')))


            return line
        if line[0] == '&':
            # parse all text in the line for text surrounded by %
            
            funccalls = []
            infunc = False
            func = ''
            for i in range(0, l):
                if line[i] == '|' and not infunc:
                    infunc = True
                elif line[i] == '|' and infunc:
                    infunc = False
                    funccalls.append(func)
                    func = ''
                elif infunc:
                    func += line[i]
            line = line[1:]
            for function in funccalls:
                line = line.replace('|' + function + '|', str(self.interpret(function)))
            return line
        # base literal reached
        if line[0] == '*':
            try: 
                return self.get_var('"' + line[1:] + '"')
            except:
                try:
                    return self.get_var(self.interpret(line[1:]))
                except:
                    # replace vars
                    line = self.replace_vars(line)
                    try:
                        return eval(line[1:])
                    except:
                        return self.interpret(line[1:])
        if line[0] == '^':
            self.calledmethod = line[1:]
            cont = True
        if line[0] == '<' and line[len(line) - 1] == '>':
            return '"' + line[1:len(line) - 1] + '"'
        if line[0] == '[' and line[len(line) - 1] == ']':
            return eval(line)
        try:
            return eval(line)
        except:
            None
        if self.contains_boolop(line) and not self.calledmethod:
            line = self.replace_vars(line)
            try:
                return eval(line)
            except:
                None
        evaluation = None
        func = ''
        obj = ''
        s = 0
        sp = 0
        for i in range(l):
            if cont:
                continue

            c = line[i]
            if c == ' ' and s == 0:
                sp += 1
                continue
            
            if c == '<':
                s += 1
            elif c == '>':
                s -= 1

            if c == '.':
                obj = func
                func = ''
                continue

            # method creation
            if c == '~':
                    returnvariable = ''
                    self.loggedmethod.append('')
                    for j in range(i + 1, len(line)):
                        if line[j] != ' ':
                            if line[j] == '(':
                                args = self.method_args(line, j)
                                for k in range(args[1], len(line)):
                                    if line[k] != ' ':
                                        if line[k] == '-' and line[k + 1] == '>':
                                            for l in range(k + 2, len(line)):
                                                if line[l] != ' ':
                                                    returnvariable += line[l]
                                break
                            self.loggedmethod[-1] += line[j]
                    if self.loggedmethod[-1] not in self.methods.keys():
                        self.methods[self.loggedmethod[-1]] = self.Method(self.loggedmethod[-1], self)
                    else:
                        break
                    for arg in args[0]:
                        if arg != '':
                            self.vars[arg] = None
                            self.methods[self.loggedmethod[-1]].add_arg(arg)
                    self.methods[self.loggedmethod[-1]].add_return(returnvariable)
                    self.methods[self.loggedmethod[-1]].update_interpreter(self)
                    break

            elif c=='>':
                line = line[i + 1:]
                try:
                    if not self.methods[self.loggedmethod[-1]].ended:
                        self.methods[self.loggedmethod[-1]].add_body(line)
                except:
                    None
                break
            # recursively interpreting a function
            if c == '(':

                mergedargs = ''
                p = 1
                l = len(line)
                for j in range(i + 1, l - 1):
                    c2 = line[j]
                    if p == 0:
                        break
                    if c2 == '(':
                        p += 1
                    if c2 == ')':
                        p -= 1
                    mergedargs += c2
                args = self.get_args(mergedargs)
                f = len(func)

                # recursively replace the line with the evaluated arguments
                evals = []

                for i in reversed(range(len(args))):
                    arg = args[i]
                    current_eval_str = str(self.interpret(arg[0]))
                    evals.append(current_eval_str)
                    line = line[:f + sp + arg[1] + 1] + current_eval_str + line[f + sp + arg[2]+ 1:] 
                
                # reverse evals to match the order of the arguments
                evals.reverse()

                if func == "throw":
                    print ("error thrown : " + str(args[0][0]))
                    exit()
                    break
                if func == "import":
                    for path in evals:
                        contents = ''
                        with open(eval(path)) as f:
                            contents = f.readlines()
                            script = ''
                            for line in contents:
                                script += line
                            self.logg("importing library", str(args[0][0]))
                            self.execute(script)
                    break
                # allows for continuation of logic flow
                if func == 'continue':
                    continue;

                if func == 'break':
                    break;

                if func == "while":
                    whilecond = args[0][0]
                    waitfunc = args[1][0]
                    while (self.interpret(whilecond[1:])):
                        self.interpret(waitfunc[1:])
                    return None
                    

                # AI features
                if obj == 'ai':
                    if func == 'setkey':
                        openai.api_key = evals[0]
                        self.openaikey = evals[0]
                        return evals[0]
                    if func == 'settokens':
                        self.tokens = int(evals[0])
                        return self.tokens
                    if func == 'ask':
                        response = openai.Completion.create(model="text-davinci-002", prompt=evals[0], temperature=0, max_tokens=self.tokens)
                        return response.choices[0].text

                if obj == 'browser':
                    if func == 'setpath':
                        self.browser_path = evals[0]
                        return self.browser_path

                    if func == 'open':
                        if self.browser_path:
                            webbrowser.register('chrome', None, webbrowser.BackgroundBrowser(self.browser_path))
                            webbrowser.get('chrome').open(evals[0])
                        else:
                            webbrowser.open(evals[0])
                        return evals[0]

                if obj == 'file':
                    if func == 'read':
                        f = open(evals[0], "r")
                        return f.read()
                    if func == 'write':
                        f = open(evals[0], "w")
                        f.write(evals[1])
                        return evals[1]
                    if func == 'append':
                        f = open(evals[0], "a")
                        f.write(evals[1])
                        return evals[1]
                    if func == 'delete':
                        os.remove(evals[0])
                        return evals[0]
                    if func == 'create':
                        f = open(evals[0], "w")
                        f.write("")
                        return evals[0]
                    if func == 'exists':
                        return os.path.exists(evals[0])
                    if func == 'rename':
                        os.rename(evals[0], evals[1])
                        return evals[1]
                    if func == 'list':
                        return os.listdir(evals[0])
                    if func == 'isdir':
                        return os.path.isdir(evals[0])
                    if func == 'isfile':
                        return os.path.isfile(evals[0])
                    if func == 'path':
                        return os.path.abspath(evals[0])
                    if func == 'size':
                        return os.path.getsize(evals[0])
                    if func == 'created':
                        return os.path.getctime(evals[0])
                    if func == 'modified':
                        return os.path.getmtime(evals[0])

                # performs an each function on evals[0]
                if func == 'each':
                    function = self.calledmethod
                    evals[0] = eval(evals[0])
                    for i in range(len(evals[0])):
                        self.vars[evals[1]] = Var(evals[1], evals[0][i])
                        self.interpret(function)

                    return evals[0]

                # printing
                if func == "prnt":
                    for i in range(len(evals)):
                        if i != len(evals) - 1:
                            self.out +=  (evals[i] + " ")
                        else:
                            self.out +=  (evals[i] + "\n")
                            return evals[i]

                if func == "stringify":
                    if not self.is_py_str(evals[0]):
                        return '"' + evals[0] + '"'
                    return evals[0]
                if func == "destringify":
                    if self.is_py_str(evals[0]):
                        return evals[0][1:len(evals[0]) - 1]
                    return evals
                
                if obj == 'math':
                    if func == 'sin':
                        return math.sin(float(evals[0]))
                    if func == 'cos':
                        return math.cos(float(evals[0]))
                    if func == 'tan':
                        return math.tan(float(evals[0]))
                    if func == 'asin':
                        return math.asin(float(evals[0]))
                    if func == 'acos':
                        return math.acos(float(evals[0]))
                    if func == 'atan':
                        return math.atan(float(evals[0]))
                    if func == 'atan2':
                        return math.atan2(float(evals[0]), float(evals[1]))
                    if func == 'sinh':
                        return math.sinh(float(evals[0]))
                    if func == 'cosh':
                        return math.cosh(float(evals[0]))
                    if func == 'tanh':
                        return math.tanh(float(evals[0]))
                    if func == 'asinh':
                        return math.asinh(float(evals[0]))
                    if func == 'acosh':
                        return math.acosh(float(evals[0]))
                    if func == 'atanh':
                        return math.atanh(float(evals[0]))
                    if func == 'degrees':
                        return math.degrees(float(evals[0]))
                    if func == 'radians':
                        return math.radians(float(evals[0]))
                    if func == 'exp':
                        return math.exp(float(evals[0]))
                    if func == 'expm1':
                        return math.expm1(float(evals[0]))
                    if func == 'log':
                        return math.log(float(evals[0]))
                    if func == 'log1p':
                        return math.log1p(float(evals[0]))
                    if func == 'log2':
                        return math.log2(float(evals[0]))
                    if func == 'log10':
                        return math.log10(float(evals[0]))
                    if func == 'pow':
                        return math.pow(float(evals[0]), float(evals[1]))
                    if func == 'sqrt':
                        return math.sqrt(float(evals[0]))
                    if func == 'ceil':
                        return math.ceil(float(evals[0]))
                    if func == 'floor':
                        return math.floor(float(evals[0]))
                    if func == 'fabs':
                        return math.fabs(float(evals[0]))
                    if func == 'factorial':
                        return math.factorial(float(evals[0]))
                    
                # evaluating variable functions
                if obj == "var":
                    if func == "set":
                        result = evals[1]
                        varname = evals[0]
                        ev = eval(varname)

                        # in case the variable name is empty
                        if ev == '':
                            return result
                        if self.is_str(evals[0]):
                            varname = evals[0][1:len(evals[0]) - 1]
                        else:
                            try:
                                return self.get_var(ev)
                            except:
                                None
                        try:
                            self.vars[varname] = Var(varname, eval(result))
                        except:
                            self.vars[varname] = Var(varname, result)
                        return result
                    
                    # retrieve the variable passed
                    elif func == "get":
                        varname = evals[0]
                        try:
                            value = self.get_var(varname)
                        except:
                            value = eval(varname)
                        if len(evals) > 1:
                            return value[eval(evals[1])]
                        else:
                            return value
                    
                    elif func == 'sort':
                        varname = evals[0]
                        var = self.get_var(varname)
                        sorted = var.copy()
                        sorted.sort()
                        return sorted
                    elif func == 'add':
                        varname = evals[0]
                        try:
                            value = self.get_var(evals[1])
                        except:
                            try:
                                value = eval(evals[1])
                            except:
                                value = evals[1]
                        try:
                            self.vars[varname].value.append(value)
                        except:
                            try:
                                self.vars[varname].value += value
                            except:
                                try:
                                    added = eval(varname)
                                except:
                                    added = varname
                                try:
                                    added.append(value)
                                except:
                                    try:
                                        added += str(value)
                                    except:
                                        added += value
                                return added
                        return self.vars[varname].value
                    elif func == 'remove' or func == 'delete':
                        varname = evals[0]
                        try:
                            value = self.get_var(evals[1])
                        except:
                            value = eval(evals[1])
                        try:
                            # if array
                            self.vars[varname].value.remove(value)
                        except:
                            # if string
                            try:
                                self.vars[varname].value = self.vars[varname].value.replace(value, '')
                            except:
                                return eval(varname.replace(value, ''))
                        return self.vars[varname].value
                    # test the equality of a variable and another value
                    elif func == 'equals?':
                        varname = evals[0]
                        try:
                            value = self.get_var(evals[1])
                        except:
                            value = eval(evals[1])
                        try:
                            return self.vars[varname].value == value
                        except: 
                            return str(varname) == str(value) 
                    # find out if vars contains the evals passed
                    elif func == 'exists?':
                        return self.var_exists(evals[0])
                
                    # length of first argument
                    elif func == 'length':
                        varname = evals[0]
                        try:
                            return len(self.vars[varname].value)
                        except:
                            return len(eval(varname))
                            
                    # adds an entry to an object
                    elif func == 'obj_add':
                        varname = evals[0]
                        key = eval(evals[1])
                        value = eval(evals[2])
                        self.vars[varname].value[key] = value
                        return self.vars[varname].value
                                   
                if func == "assert" or func == "bool" or func == 'if':
                    arg = eval(evals[0])
                    if func == 'if':
                        conditions = []
                        conditions.append(arg)
                        if conditions[0]:
                            return evals[1]
                        return evals[2]
                    if not arg:
                        self.errors.append(Err(1))
                        if func == 'assert':
                            self.err("assertion failed", "", str(args[0][0]))
                        return False
                    return True

                # interprets a function
                if obj == "script":
                    return self.interpret(evals[0])

                if func == 'log':
                    return self.log
                
                # obtains the current environment variables
                if func == 'variables':
                    vars = {}
                    for var in self.vars:
                        try:
                            vars[var] = self.vars[var].value
                        except:
                            None
                    return vars
                if func == 'env':
                    print("--------- environment ---------")
                    print("\n\tvariables:")
                    for varname, v in self.vars.items():
                        try:
                            print (varname + " = " + str(v.value))
                        except:
                            None

                    print("\n\tmethods:")
                    # printing methods
                    for methodname, Method in self.methods.items():
                        print (methodname + ":")

                    print("\nlog:")
                    print (self.log)
                    print("-------------------------------")
                    return True
                            
                # waits for the boolean expression to be true
                if func == "wait":
                    waitcond = self.calledmethod
                    while not self.interpret(waitcond):
                        None
                    return True

                # obtains the called method
                if func == "called":
                    return self.calledmethod

                # gets the current out
                if func == "out":
                    return self.out

                if func == "sleep":
                    sleeping = eval(evals[0])
                    time.sleep(sleeping)
                    return sleeping

                if func == "thread":
                    self.thread = threading.Thread(target=self.interpret, args=(self.calledmethod,))
                    self.thread.start()
                    return True
                # copy mechanism
                if func == 'copy':
                    try:
                        return eval(evals[0]).copy()
                    except:
                        return evals[0]
                # looping mechanism
                if func == 'check':
                    checker = eval(evals[0])
                    if checker:
                        return self.interpret(self.calledmethod)
                    return evals[2]
                if func == 'later':
                    return self.interpret(self.calledmethod)
                if func == 'for':
                    inside = self.calledmethod
                    # times to loop
                    start = eval(evals[0])
                    end = eval(evals[1])
                    loopvar =  evals[2]
                    if start < end:
                        for i in range(start, end):
                            if loopvar in self.vars and self.vars[loopvar].value >= end:
                                break
                            self.vars[loopvar] = Var(loopvar, i)
                            self.interpret(inside)
                    elif start > end:
                        for i in reversed(range(end, start)):
                            if loopvar in self.vars and self.vars[loopvar].value < end:
                                break
                            self.vars[loopvar] = Var(loopvar, i) 
                            self.interpret(inside)
                    return self.vars[loopvar].value
                         
                if func in self.methods.keys():
                    method = self.methods[func]
                    for i in range(len(method.args)):
                        name = '"' + method.args[i] + '"'
                        self.vars[name] = Var(name, eval(evals[i]))
                    converted = []

                    for ev in evals:
                        converted.append(eval(ev))

                    # execute method
                    method.run(converted)
                    
                    ret = None
                    # logging return value
                    
                    try: 
                        ret = eval(str(self.vars['"' + method.returns[0] + '"'].value))
                    except:
                        ret = str(self.vars['"' + method.returns[0] + '"'].value)

                    return ret
                if obj == "system":
                    if func == "exit":
                        exit(eval(evals[0]))
                    if func == "version":
                        return self.version
                    if func == "destroy":
                        
                        # remove all evals from vars
                        for i in range(len(evals)):
                            if evals[i].startswith('"__'):
                                removing = []
                                for varname in self.vars.keys():
                                    if varname.startswith('"__'):
                                       removing.append(varname)
                                for varname in removing:
                                    del self.vars[varname]
                            else:
                                self.vars.pop(evals[i], None)
                        return True
                if obj == 'console':
                    if func == 'run':
                        for ev in evals:
                            os.system(str(eval(ev)))
                        return True
                elif func == "_":
                    return evals[0]
                elif func == 'strn':
                    return None
                elif func not in self.methods.keys():
                    try:
                        return eval(line)
                    except:
                        None
                break

            func += c 
        return evaluation

    def shell(self):
        ip = None
        while ip != 'exit':
            ip = input(">>> ")
            self.interpret(ip)

    def logg(self, msg, line):
        self.log += "[*] " + msg + " : " + line + "\n"

    def err(self, err, msg, line):
        if msg == '':
            errmsg = "[-] " + err + " : " + line
        else:
            errmsg =  "[-] " + err + " : " + msg + " : " + line
        self.out += errmsg + "\n"
        self.log += errmsg + "\n"
    
    def method_args(self, line, j):
        argstring = '' 
        instring = False
        for k in range(j + 1, len(line)):
            if line[k] == '"' and not instring:
                instring = True
            elif line[k] == '"' and instring:
                instring = False
            if not instring:
                if line[k] != ' ':
                    if line[k] == ')':
                        break
                    argstring += line[k]
            else:
                argstring += line[k]
        return argstring.split(','), k

    def thread_split(self, line):
        self.interpret(line)

    def var_exists(self, varname):
        if varname in self.vars:
            return True
        return False

    def var_names(self):
        names = []
        for key in self.vars:
            names.append(key)
        return names

    def is_py_str(self, line):
        try:
            return line[0] == '"' and line[len(line) - 1] == '"'
        except:
            return False

    def is_str(self, line):
        return line[0] == '<' and line[len(line) - 1] == '>'

    # gets the variable value from the variable name
    def get_var(self, name):
        return self.vars[name].value

    # extracts the argument lines from the merged arguments passed
    def get_args(self, line):
        args = []
        l = len(line)
        arg = ''
        start = 0
        p = 0
        a = 0
        s = 0
        b = 0
        for i in range(l + 1):
            c = ''
            try:
                c = line[i]
            except:
                None

            if c == '<':
                s += 1
            if c == ">":
                s -= 1
            
            if c == '[':
                a += 1
            if c == ']':
                a -= 1

            if c == '(':
                p += 1
            if c == ')':
                p -= 1

            if c == '{':
                b += 1
            if c == '}':
                b -= 1

            # print (f"""
            # c: {c}
            # p: {p}
            # a: {a}
            # s: {s}
            
            # """)

            if c == ',' and p == 0 and s == 0 and a == 0 and b == 0:
                args.append([arg, start, start + len(arg)])
                start = i + 1
                arg = ''
                continue
            elif i == l:
                args.append([arg, start, start + len(arg)])
            arg += c
        return args


    class Method:
            def __init__(self, name, interpreter):
                self.name = name
                self.args = []
                self.body = []
                self.returns = []
                self.ended = False
                self.interpreter = interpreter

            def add_arg(self, arg):
                self.args.append(arg)

            def add_body(self, body):
                self.body.append(body)

            def add_return(self, ret):
                self.returns.append(ret)

            def update_interpreter(self, interpreter):
                self.interpreter = interpreter

            def run(self, args):
                for i in range(len(self.args)):
                    try:
                        self.interpreter.vars[self.args[i]] = self.interpreter.vars[args[i]]
                    except:
                        try: 
                            self.interpreter.vars[self.args[i]] = args[i]
                        except:
                            self.interpreter.vars[self.args[i]] = args[i]
                for line in self.body:
                    self.interpreter.interpret(line)
            
            def clean(self):
                for arg in self.args:
                    try:
                        del self.interpreter.vars['"' + arg + '"']
                    except:
                        None
