# Interpreters MSNScript 2.0
# Author : Mason Marker
# Date : 09/15/2022

from http.client import CONTINUE
import os
import math
import openai
import webbrowser
import time
import threading
import time
import requests


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

<<<<<<< HEAD
=======
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
    calledmethod2 = None
    current_line = 0

    # threading implementation
    thread = None

    # ai implementation
    openaikey = None
    tokens = 100

    # browser specific variables
    browser_path = None
>>>>>>> e004a12156b221b8b9e6a66eeba226fc3693873d

    # initializer
    def __init__(self):
        self.version = 2.0
        self.lines = []
        self.out = ''
        self.log = ''
        self.errors = []

        self.vars = {}
        self.methods = {}
        self.loggedmethod = []
        self.objects = {}
        self.calledmethod = None

        self.current_line = 0
        self.imports = set()

        self.thread = None
        self.threads = {}
        self.parent = None

        self.openaikey = None
        self.tokens = 100
        self.browser_path = None
        self.serial_1 = 0

    # executes stored script
    def execute(self, script):

        # convert script to lines
        self.lines = list(filter(None, script.split("\n")))

        # for aggregate syntax support !{}
        inmultiline = False
        multiline = ''

        # for block syntax
        inblock = 0
        all_ins_s = ''

        for line in self.lines:
            line = line.strip()
            if line.startswith("::") or line.startswith("#"):
                self.current_line += 1
                continue
            else:

                # aggregate syntax !{}
                if line.startswith('!{') and line.endswith('}'):
                    multiline = line[2:-1]
                    self.interpret(multiline)
                    multiline = ''
                elif not inmultiline and line.startswith("!{"):
                    inmultiline = True
                    multiline += line[2:]
                elif inmultiline and line.endswith("}"):
                    inmultiline = False
                    multiline += line[0:len(line) - 1]
                    self.interpret(multiline)
                    multiline = ''
                elif inmultiline:
                    multiline += line
                else:
                    self.interpret(line)

                # block syntax
                

            self.current_line += 1
        return self.out

    def replace_vars(self, line):
        boo = line 
        varnames = list(self.vars.keys())
        varnames = sorted(varnames, key=len, reverse=True)
        for varname in varnames:
            try:
                boo = boo.replace(varname, str(self.get_var(eval('"' + varname + '"'))))
            except:
                None
        return boo


    # interprets a line    
    def interpret(self, line, block=None):
        try:
            line = line.strip()
        except:
            return
        l = len(line)
        cont = False
        if line == '':
            return

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

        if line[0] == '*':
            line = self.replace_vars(line[1:])
            return self.interpret(line)

        if line[0] == '^':
            self.calledmethod = line[1:]
            cont = True
        if line[0] == '<' and line[-1] == '>':
            return '"' + line[1:len(line) - 1] + '"'

        # try base literal
        try:
            return eval(line)
        except:
            None

        # # || function replacement
        # # parse all text in the line for text surrounded by %
        # funccalls = []
        # infunc = False
        # func = ''
        # for i in range(0, l):
        #     if line[i] == '|' and not infunc:
        #         infunc = True
        #     elif line[i] == '|' and infunc:
        #         infunc = False
        #         funccalls.append(func)
        #         func = ''
        #     elif infunc:
        #         func += line[i]
        # line = line[1:]
        # for function in funccalls:
        #     print (function)
        #     line = line.replace('|' + function + '|', str(self.interpret(function)))
        # print (line)
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
                    return self.loggedmethod[-1]
            
            # method-specific line reached
            elif c=='-' and line[i + 1] == '-':
                line = line[i + 2:]
                try:
                    if not self.methods[self.loggedmethod[-1]].ended:
                        self.methods[self.loggedmethod[-1]].add_body(line)
                except:
                    None
                return

            # interpreting a function
            elif c == '(':

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

                # clean function for handling
                func = func.strip()

                # splits the first argument by the second argument
                if func == 'split':
                    to_split = self.parse(0, line, f, sp, args)[2]               
                    splitting_by = self.parse(1, line, f, sp, args)[2]     
                    return to_split.split(splitting_by)
                    
                # creates / sets a variable
                if func == 'var':
                    varname_ins = args[0][0]
                    value_ins = args[1][0]

                    # extract varname
                    line, varname = self.convert_arg(varname_ins, line, f, sp, args)

                    # extract value
                    line, value = self.convert_arg(value_ins, line, f, sp, args)
                    
                    # add / set variable
                    self.vars[varname] = Var(varname, value)
                    return value

                # gets the first argument at the second argument
                elif func == 'get':
                    getting_from = self.parse(0, line, f, sp, args)[2]
                    index = self.parse(1, line, f, sp, args)[2]
                    return eval(getting_from[index])

                # determines if a variable exists or not
                elif func == 'exists':
                    return self.parse(0, line, f, sp, args)[2] in self.vars

                # gets the length of the first argument
                elif func == 'len':
                    line, as_s, arg = self.parse(0, line, f, sp, args)
                    return len(arg)

                # asserts each argument is True, prints and logs assertion error
                elif func == 'assert':
                    for i in range(len(args)):
                        arguments = args[i]
                        line, assertion = self.convert_arg(arguments[0], line, f, sp, args)
                        if not assertion:
                            err = self.err("assertion error", "", args[0][0])
                            self.logg(err, line)
                            print (err)
                            return False

                    return True

                # conditional logic
                elif func == 'if':
                    # if condition and blocks arguments              
                    ifcond_s = args[0][0]
                    true_block_s = args[1][0]

                    # false block is optional
                    try:
                        false_block_s = args[2][0]
                    except:
                        false_block_s = None
                    
                    # extract and replacing the interpretation result of the if condition
                    line, ifcond = self.convert_arg(ifcond_s, line, f, sp, args)
                    
                    # if condition is true
                    if (ifcond):
                        line, ret = self.convert_arg(true_block_s, line, f, sp, args)
                        return ret

                    # otherwise false block is executed
                    line, ret = self.convert_arg(false_block_s, line, f, sp, args)
                    return ret    

                # while logic WIPWIPWIPWIPWIP
                elif func == 'while':

                    # while arguments as strings
                    whilecond_s = args[0][0]
                    while_block_s = args[1][0]

                    while (self.interpret(whilecond_s)):
                        self.interpret(while_block_s)
                    return True

                 # iteration
                elif func == 'for':

                    # block to execute
                    inside = args[3][0]

                    # times to loop
                    line, as_s, start = self.parse(0, line, f, sp, args)
                    line, as_s, end = self.parse(1, line, f, sp, args)
                    line, as_s, loopvar = self.parse(2, line, f, sp, args)

                    # regular iteration
                    if start < end:
                        for i in range(start, end):
                            if loopvar in self.vars and self.vars[loopvar].value >= end:
                                break
                            self.vars[loopvar] = Var(loopvar, i)
                            self.interpret(inside)
                    
                    # reversed if requested
                    elif start > end:
                        for i in reversed(range(end, start)):
                            if loopvar in self.vars and self.vars[loopvar].value < end:
                                break
                            self.vars[loopvar] = Var(loopvar, i) 
                            self.interpret(inside)
                    return self.vars[loopvar].value

                # executes a block of code for each element in an array
                elif func == 'each':
                    
                    # get array argument
                    line, as_s, array = self.parse(0, line, f, sp, args)
            
                    # get element variable name
                    line, as_s, element_name = self.parse(1, line, f, sp, args)
                    block_s = args[2][0]

                    # prepare each element
                    self.vars[element_name] = Var(element_name, 0)

                    # execute block for each element
                    for i in range(len(array)):
                        self.vars[element_name].value = array[i]
                        self.interpret(block_s)
                    return array

                # the following provide efficient variable arithmetic
                elif func == 'add':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value += second
                    return self.vars[first].value
                elif func == 'sub':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value -= second
                    return self.vars[first].value
                elif func == 'mult':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value *= second
                    return self.vars[first].value
                elif func == 'div':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value /= second
                    return self.vars[first].value

                # deletes a variable
                elif func == 'del':
                    for i in range(len(args)):
                        line, as_s, first = self.parse(i, line, f, sp, args)
                        del self.vars[first]
                    return True

                # determines equality of all arguments
                elif func == 'equals':
                    line, as_s, arg1 = self.parse(0, line, f, sp, args)
                    for i in range(1, len(args)):
                        line, as_s, curr_arg = self.parse(i, line, f, sp, args)
                        if curr_arg != arg1:
                            return False
                    return True

                # performs not on each argument
                # returns 'not arg1 and not arg2 and not arg3...'
                elif func == 'not':
                    return not self.parse(0, line, f, sp, args)[2]


                # inline function, takes any amount of instructions
                # returns the result of the last instruction
                elif func == "=>":
                    ret = None
                    for i in range(len(args)):
                        arguments = args[i]

                        # current instruction
                        ins_s = arguments[0]

                        # extract while condition literal
                        line, ret = self.convert_arg(ins_s, line, f, sp, args)
                    return ret

                # data structure for holding multiple items
                elif func == 'class':
                    # new interpreter
                    inter = Interpreter()

                    # extract class name
                    name = self.parse(0, line, f, sp, args)[2]

                    # block at which the class exists
                    block_s = args[1][0]

                    # execute the block in the private environment
                    inter.execute(block_s)
                    
                    # creates a variable out of the new interpreters resources
                    obj_to_add = {}
                    for varname in inter.vars.keys():
                        val = inter.vars[varname].value
                        obj_to_add[varname] = Var(varname, val)
                    
                    for methodname, method in inter.methods.keys():
                        obj_to_add[methodname] = method
                    
                    self.vars[name] = Var(name, obj_to_add)
                    return obj_to_add

                # gets the first argument at the second argument
                elif func == 'get':
                    iterable = self.parse(0, line, f, sp, args)[2]
                    index = self.parse(1, line, f, sp, args)[2]
                    return iterable[index]

                # imports resources from another location
                elif func == 'import' or func == 'launch' or func == 'include' or func == 'using':
                    
                    line, as_s, path = self.parse(0, line, f, sp, args)
                    if path in self.imports:
                        continue
                    self.imports.add(path)
                    contents = ''
                    with open(path) as f:
                        contents = f.readlines()
                        script = ''
                        for line in contents:
                            script += line
                        self.logg("importing library", str(args[0][0]))
                        self.execute(script)
                    return

                # interpreter printing mechanism
                elif func == 'prnt':
                    for i in range(len(args)):
                        arguments = args[i]
                        first = self.interpret(arguments[0])
                        srep = str(first)
                        line = line[:f + sp + arguments[1] + 1] + srep + line[f + sp + arguments[2]+ 1:]
                        if i != len(args) - 1:
                            self.out += srep + ' '
                        else:
                            self.out += srep + '\n'
                    return first

                # sleeps the thread for the first argument amount of seconds
                elif func == "sleep":
                    return time.sleep(self.parse(0, line, f, sp, args)[2])
                    

                # provides a representation of the current environment
                elif func == 'env':
                    should_print_s = args[0][0]
                    line, should_print = self.convert_arg(should_print_s, line, f, sp, args)
                    strenv = ''                        
                    strenv += "--------- environment ---------"
                    strenv += "\nout:\n" + self.out
                    strenv += "\nvariables:\n"
                    for varname, v in self.vars.items():
                        try:
                            strenv += "\t" + varname + " = " + str(v.value) + '\n'
                        except:
                            None

                    strenv += "\nmethods:\n"
                    # printing methods
                    for methodname, Method in self.methods.items():
                        strenv += "\t" + methodname + "\n"

                    strenv += "\nlog:\n" + self.log
                    strenv +="\n-------------------------------"
                    if should_print:
                        print(strenv)
                    return strenv

                # executes MSNScript2 from its string representation
                elif func == '-':
                    line, as_s, string = self.parse(0, line, f, sp, args)
                    return self.interpret(string)

                # gets the current time
                elif func == 'now':
                    return time.time()

                # creates a private execution enviroment
                # private block will have read access to the enclosing Interpreter's
                # variables and methods
                elif func == 'private':
                    block_s = args[0][0]
                    inter = Interpreter()
                    inter.parent = self
                    for vname, entry in self.vars.items():
                        inter.vars[vname] = entry
                    for mname, entry in self.methods.items():
                        inter.methods[mname] = entry
                    inter.execute(block_s)
                    return inter

                # creates a new execution environment
                # new env is executed by a fresh interpreter
                # (no resource inheritance)
                elif func == 'new':
                    inter = Interpreter()
                    inter.parent = self
                    return inter.execute(args[0][0])

                # creates a new thread to execute the block, thread
                # starts on the same interpreter
                elif func == "thread":
                    name = self.parse(0, line, f, sp, args)[2]
                    thread = threading.Thread(target=self.interpret, args=(args[1][0],))
                    thread.name = name
                    self.threads[name] = [thread, self]
                    thread.start()
                    return True

                # joins the current working thread with the thread name specified
                elif func == 'join':
                    self.thread_by_name(self.parse(0, line, f, sp, args)[2]).join()
                    return True
                
                # tries the first argument, if it fails, code falls to the catch/except block
                # there is no finally implementation
                elif func == 'try':
                    ret = None
                    try:
                        ret = self.interpret(args[0][0])
                    except:
                        ret = self.interpret(args[0][0])
                    return ret
                    
                # waits for a certain condition to be true
                elif func == 'wait':

                    # no block per tick provided
                    if (len(args)) == 1:
                        while not self.interpret(args[0][0]):
                            None

                    # block provided
                    else:
                        while not self.interpret(args[0][0]):
                            self.interpret(args[1][0])
                    return True

                # kills a thread by name
                elif func == 'tkill':
                    killing_s = self.parse(0, line, f, sp, args)[2]
                    if killing_s == 'self':
                        threading.current_thread()

                    thread = self.thread_by_name(killing_s)
                    thread.kill()
                    return True


                # exports a variable from the working context to the parent context,
                # ex private context -> boot context
                elif func == 'export':
                    varname = self.parse(0, line, f, sp, args)[2]
                    self.parent.vars[varname] = self.vars[varname]
                    return self.vars[varname].value

                # sends a command to the console, console type depends on
                # executors software of course
                elif func == 'console':
                    ins_s = args[0][0]
                    line, to_run = self.convert_arg(ins_s, line, f, sp, args)
                    os.system(to_run)
                    return True

                # simulates returning of the function currently running
                elif func == 'return':
                    method = self.methods[self.loggedmethod[-1]]
                    
                    # evaluate returning literal
                    line, as_s, ret = self.parse(0, line, f, sp, args)

                    # set return variable
                    ret_name = method.returns[0]

                    # if not a variable
                    if ret_name not in self.vars:
                        self.vars[ret_name] = Var(ret_name, None)

                    self.vars[ret_name].value = ret
                    return ret

                # starts an http server
                elif func == 'server':
                    None

                # simulates function closure
                elif func == 'end':
                    method = self.methods[self.loggedmethod[-1]]
                    self.loggedmethod.pop()
                    method.ended = True
                    return True

                # user method execution requested
                elif func in self.methods.keys():
                    method = self.methods[func]

                    # create func args 
                    func_args = []    

                    try:                
                        for i in range(len(args)):
                            arguments = args[i]
                            line, as_s, arg = self.parse(i, line, f, sp, args)
                            func_args.append(arg)
                            meth_argname = method.args[i]
                            self.vars[meth_argname] = Var(meth_argname, arg)
                    except:
                        l = len(method.args)
                        if l != 0:
                            self.err("bad arg count", f"correct arg count is {l}", line)

                    # create return variable
                    ret_name = method.returns[0]
                    self.vars[ret_name] = Var(ret_name, None)

                    # execute method
                    method.run(func_args)
                    try: 
                        return eval(str(self.vars[method.returns[0]].value))
                    except:
                        return str(self.vars[method.returns[0]].value)

                # object instance requested
                elif func in self.vars:

                    # get classname to create
                    classname = func

                    # template Var obj to create from
                    var_obj = self.vars[classname].value

                    # new class instance
                    instance = {}

                    curr_arg_num = 0

                    # attributes to apply
                    for name in var_obj:

                        # value can be None
                        try:
                            instance[name] = self.parse(curr_arg_num, line, f, sp, args)[2]
                            curr_arg_num += 1

                        # if not specified, field is default value
                        except:
                            instance[name] = var_obj.value[name].copy()

                    return instance

                # gets an attribute of an instance of a class
                elif func == 'getattr':
                    
                    # name of the object to pull from
                    name = self.parse(0, line, f, sp, args)[2]

                    # current working object
                    obj = self.vars[name].value

                    # get attribute to pull
                    attr = self.parse(1, line, f, sp, args)[2]
                    return obj[attr]

                # sets an attribute of an instance of a class
                elif func == 'setattr':
                    
                    # name of the object to set at
                    name = self.parse(0, line, f, sp, args)[2]

                    # current working object
                    obj = self.vars[name].value

                    # name of attribute to set
                    attr = self.parse(1, line, f, sp, args)[2]

                    # value to set
                    val = self.parse(2, line, f, sp, args)[2]

                    # set the value
                    obj[attr] = val
                    return val


                # functional syntax I decided to add to make loops a tiny bit faster,
                # cannot receive non literal arguments
                # syntax:     3|5|i (prnt(i))
                # prnts 3\n4\n5
                elif func.count('|') == 2:
                    loop_args = func.split('|')
                    start = eval(loop_args[0])
                    end = eval(loop_args[1])
                    loopvar = loop_args[2]

                    # prepare loop variable
                    self.vars[loopvar] = Var(loopvar, start)

                    # obtain loop block
                    block_s = args[0][0]

                    if start < end:
                        for i in range(start, end):
                            self.vars[loopvar].value = i
                            self.interpret(block_s)

                    # reversed iteration
                    else:
                        for i in reversed(range(end, start)):
                            self.vars[loopvar].value = i
                            self.interpret(block_s)
                    return
                
                # quicker conditional operator as functional prefix
                elif func[0] == '?':
                    func = func[1:]
                    ret = None
                    if self.interpret(func):
                        ret = self.interpret(args[0][0])
                    else:

                        # else block is optional
                        try:
                            ret = self.interpret(args[1][0])
                        except:
                            None
                    return ret

                # fallback
                else:
                    try:
                        line = self.replace_vars2(line)
                        # python piggyback attempt
                        return eval(line)
                    except:
                        # maybe its a variable?
                        try:
                            return self.vars[line].value
                        except:
                            # ok hopefully its a string lol
                            return line
                    # for i in range(len(evals)):
                    #     if i != len(evals) - 1:
                    #         self.out +=  (evals[i] + " ")
                    #     else:
                    #         self.out +=  (evals[i] + "\n")
                    #         return evals[i]





                     #   current_eval_str = str(self.interpret(arg[0]))
                    #    evals.append(current_eval_str)
                




















                # reverse evals to match the order of the arguments
                evals.reverse()

                if func == "import":
                    for path in evals:
                        if path in self.imports:
                            continue
                        self.imports.add(path)
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
                        try:
                            self.vars[varname].value[key] = value
                        except:
                            found = eval(varname)
                            found[key] = value
                            return found
                        return self.vars[varname].value
                                   
                if func == "assert" or func == "bool":
                    arg = eval(evals[0])
                    if not arg:
                        self.errors.append(Err(1))
                        if func == 'assert':
                            self.err("assertion failed", "", str(args[0][0]))
                        return False
                    return True

                # interprets a function
                if obj == "script":
                    return self.interpret(evals[0])

                # private execution block
                if func == "private":
                    inter = Interpreter()
                    block = self.calledmethod
                    for vname, entry in self.vars.items():
                        inter.vars[vname] = entry
                    for mname, entry in self.methods.items():
                        inter.methods[mname] = entry
                    inter.execute(block)
                    return inter

                if func == 'log':
                    return self.log
                
                # obtains the current environment variables
                if func == 'variables':
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

<<<<<<< HEAD
                # current time
                if func == "now":
                    return time.time()
=======
>>>>>>> e004a12156b221b8b9e6a66eeba226fc3693873d

                # obtains the called method
                if func == "called":
                    return self.calledmethod

                # obtains the called method
                if func == "called2":
                    return self.calledmethod2

                if func == "variables":
                    return self.vars

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

                # checking mechanism
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
                        name = method.args[i]
                        self.vars[name] = Var(name, eval(evals[i]))
                    converted = []

                    for ev in evals:
                        converted.append(eval(ev))

                    # execute method
                    method.run(converted)
                    
                    ret = None
                    # logging return value
                    
                    try: 
                        ret = eval(str(self.vars[method.returns[0]].value))
                    except:
                        ret = str(self.vars[method.returns[0]].value)

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

                    # resets the current interpreter based on arguments
                    elif func == 'reset':
                        for arg in evals:
                            if arg == '"out"':
                                self.out = ''
                            if arg == '"variables"':
                                keys = list(self.vars.keys())
                                for varname in keys:
                                    del self.vars[varname]
                            if arg == '"methods"':
                                keys = list(self.methods.keys())
                                for methodname in keys:
                                    del self.methods[methodname]
                            if arg == '"log"':
                                self.log = ''    
                if obj == 'console':
                    if func == 'run':
                        for ev in evals:
                            os.system(str(eval(ev)))
                        return True
                elif func == "_":
                    return evals[0]
                elif func == "now":
                    return time.time()
                elif func == 'strn':
                    return None
                elif func == '-':
                    for ins in args:
                        self.interpret_msnscript_1(ins[0])
                    return True
                # msn1 primal looping mechanism                
                elif "{" in func and "}" in func:
                    if ":" not in func:
                        called = self.calledmethod
                        expression = func.replace("{", '').replace("}", '')
                        willrun = self.interpret(expression)
                        if willrun:
                            self.interpret(called)
                    else:
                        times = self.loop(func)
                        first = times[0]
                        last = times[1]
                        optvar = times[2]
                        try:
                            first = self.vars[first].value
                        except: 
                            None
                        try:
                            last = self.vars[last].value
                        except:
                            None
                        first = int(first)
                        last = int(last)
                        self.vars[optvar] = Var(optvar, first)
                        interpreting = line.replace(func, '')
                        called = self.calledmethod
                        if first <= last:
                            for i in range(first, last):
                                if optvar in self.vars:
                                    self.vars[optvar].value = i
                                self.interpret(called)
                        else:
                            for i in range(last, first):
                                if optvar != '':
                                    self.vars[optvar].value = i
                                self.interpret(called)
                    return True

                elif func not in self.methods.keys():
                    try:
                        return eval(line)
                    except:
                        None

                break

            func += c
        
        try:
            line = self.replace_vars2(line)
        except:
            None
        try:
            return eval(line)
        except:
            try:
                return eval(str(self.replace_vars(line)))
            except:
                return None

    def replace_vars2(self, line):
        for varname, var in self.vars.items():
                try:
                    val = var.value
                except:
                    try:
                        val = eval(str(var))
                    except:
                        val = str(var)
                if isinstance(val, str):
                    val = '"' + val + '"'
                line = line.replace('?'+varname+'?', str(val))
        return line

    def thread_by_name(self, name):
        return self.env_by_name(name)[0]

    def env_by_name(self, name):
        for threadname in self.threads.keys():
            if threadname == name:
                return self.threads[threadname]
        return None

    def parse(self, arg_number, line, f, sp, args):
        as_s = args[arg_number][0]
        line, ret = self.convert_arg(as_s, line, f, sp, args)
        return line, as_s, ret

    def convert_arg(self, ins_s, line, f, sp ,args):
        ret = self.interpret(ins_s)
        return line[:f + sp + args[0][1] + 1] + str(ret) + line[f + sp + args[0][2]+ 1:], ret

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
        return errmsg
    
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

            if c == ',' and p == 0 and a == 0 and b == 0:
                args.append([arg, start, start + len(arg)])
                start = i + 1
                arg = ''
                continue
            elif i == l:
                args.append([arg, start, start + len(arg)])
            arg += c
        return args

    def interpret_msnscript_1(self, line):
        element = ''
        variable = ''

        for i in range(0, len(line)):
            c = line[i]
            if c != ' ':
                if c == '+' and line[i + 1] == '=':
                    variable = '"' + element + '"'
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]

                    # if element is a number
                    if isinstance(element, float) or isinstance(element, int):
                        self.vars[variable].value += self.evaluate(element, 'number')
                    # if element is a string
                    elif isinstance(element, str):
                        try:
                            self.vars[variable].value += self.evaluate(element, 'string')
                        except:
                            self.vars[variable].value += self.evaluate(element, 'number')
                                
                    return self.vars[variable].value
                elif c == '-' and line[i + 1] == '=':
                    variable = '"' + element + '"'
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value -= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '*' and line[i + 1] == '=':
                    variable = '"' + element + '"'
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value *= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '/' and line[i + 1] == '=':
                    variable = '"' + element + '"'
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value /= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '=':
                    variable = '"' + element + '"'
                    element = ''
                    string = False
                    array = False
                    for j in range(i+1, len(line)):   
                        if line[j] == '"':
                            string = True
                        if line[j] == '[':
                            array = True                     
                        element += line[j]
                    if string:
                        self.vars[variable] = Var(variable, self.evaluate(element, 'string'))
                    elif array:
                        self.vars[variable] = Var(variable, self.evaluate(element, 'array'))
                    else:
                        self.vars[variable] = Var(variable, self.evaluate(element, 'number'))
                    return self.vars[variable].value
                
                elif "{" in element and "}" in element:
                    if ":" not in element:
                        expression = element.replace("{", '').replace("}", '')
                        willrun = self.boolean(expression)
                        
                        if willrun:
                            line = line[line.index("}") + 1:]
                            self.interpret(line)
                    else:
                        times = self.loop(element)
                        first = times[0]
                        last = times[1]
                        optvar = '"' + times[2] + '"'
                        try:
                            first = self.vars[first].value
                        except: 
                            None
                        try:
                            last = self.vars[last].value
                        except:
                            None
                        first = int(first)
                        last = int(last)
                        self.vars[optvar] = Var(optvar, first)
                        if first <= last:
                            for i in range(first, last):
                                if optvar in self.vars:
                                    self.vars[optvar].value = i
                                self.interpret_msnscript_1(line.replace(' ', '').replace(element, ''))
                        else:
                            for i in range(last, first):
                                if optvar != '':
                                    self.vars[optvar].value = i
                                self.interpret_msnscript_1(line.replace(' ', '').replace(element, ''))
                    break
                elif c == "&":
                    None

                else:
                    element += c

    def boolean(self, expression):
        first = ''
        last = ''
        op = ''
        for i in range(0, len(expression)):
            if  expression[i] == '&' and expression[i + 1] == '&' and expression[i + 2] != '-' and expression[i + 2] != '+':
                for j in range(i + 2, len(expression)):
                    last += expression[j]
                op = '==' 
                break
            elif expression[i] == '!' and expression[i + 1] == '&' and expression[i + 2] == '&'  and expression[i + 3] != '-' and expression[i + 3] != '+':
                for j in range(i + 3, len(expression)):
                    last += expression[j]
                op = '!=' 
                break
            elif expression[i] == '&' and expression[i + 1] == '&' and expression[i + 2] == '-' and expression[i + 3] != 'e':
                for j in range(i + 3, len(expression)):
                    last += expression[j]
                op = '<' 
                break
            elif expression[i] == '&' and expression[i + 1] == '&' and expression[i + 2] == '+' and expression[i + 3] != 'e':
                for j in range(i + 3, len(expression)):
                    last += expression[j]
                op = '>' 
                break
            elif expression[i] == '&' and expression[i + 1] == '&' and expression[i + 2] == '-' and expression[i + 3] == 'e':
                for j in range(i + 4, len(expression)):
                    last += expression[j]
                op = '<=' 
                break
            elif expression[i] == '&' and expression[i + 1] == '&' and expression[i + 2] == '+' and expression[i + 3] == 'e':
                for j in range(i + 4, len(expression)):
                    last += expression[j]
                op = '>=' 
                break
            else:
                first += expression[i]
        
        try:
            firsteval = self.vars[first].value
        except KeyError:
            firsteval = self.evaluate(first, 'string')

        try:
            lasteval = self.vars[last].value
        except KeyError:
            lasteval = self.evaluate(last, 'string')

        if op == '==':
            return firsteval == lasteval
        if op == '!=':
            return firsteval != lasteval
        if op == '<':
            return firsteval < lasteval
        if op == '>':
            return firsteval > lasteval
        if op == '<=':
            return firsteval <= lasteval
        if op == '>=':
            return firsteval >= lasteval
        return False

    def loop(self, section):
        first = ''
        last = ''
        optvar = ''
        optvarfound = False
        for i in range(1, len(section)):
            if section[i] == ':':
                for j in range(i + 1, len(section)):
                    if section[j] == ':':
                        for k in range(j + 1, len(section)):
                            if section[k] == '}':
                                optvarfound = True
                                break
                            optvar += section[k]    
                    if section[j] == '}':
                        break
                    if optvarfound:
                        break
                    last += section[j]
                break
            first += section[i]
        return first, last, optvar
        
    def evaluate(self, postop, type):
        new = postop
        
        if type == 'number':
            new = self.replace_vars(new) 
            try:
                return eval(new)
            except:
                return eval('"' + new + '"')
        elif type == 'string':
            return self.string(postop)
        elif type == 'array':
            return self.array(postop)
        elif type == 'unknown':
            new = self.replace_vars(new) 
            try:
                evaluation = eval(str(self.string(new)))
            except:
                try:
                    evaluation = self.array(self.vars[new])
                except:
                    try:
                        evaluation = self.vars[self.string(eval(new))]
                    except:
                        try:
                            evaluation = eval(new)
                        except:
                            evaluation = new
            return evaluation

    def string(self, string):
        strn = ''
        isv = False
        try:
            string = self.vars[string]
            isv = True
        except KeyError:
            None

        if isv and '"' not in string:
            string = '"' + string + '"'
        try:
            strn = eval(string);
        except:
            None
        try:
            for var in self.vars:
                strn = strn.replace("{" + var + "}", str(self.vars[var]))
            for method in self.methods.keys():
                toprint = ''
                body = self.methods[method].body
                for line in body:
                    toprint += line + '\\n'
                strn = strn.replace("{" + method + "}", toprint)
        except:
            None
        return strn
    def array(self, postop):
        array = []
        try:
            array = eval(postop)
        except:
            None
        return array

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


