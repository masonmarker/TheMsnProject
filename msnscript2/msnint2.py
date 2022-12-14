# Interpreters MSNScript 2.0
# Author : Mason Marker
# Date : 09/15/2022

import os
import math
import shutil
import openai
import random
import time
import threading
import time
import requests
from flask import Flask, request
from flask_restful import Resource, Api
import logging
import socket
import sys
import subprocess

# web scraping
from bs4 import BeautifulSoup 


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




    
# global vars
lock = threading.Lock()
auxlock = threading.Lock()

# user defined syntax
syntax = {}

# current lines
lines_ran = []

# user defined macros
macros = {}

# user defined post macros
# aka macros that are defined that the end of a line
postmacros = {}


# interprets MSNScript2, should create a new interpreter for each execution iteration
class Interpreter:

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
        self.breaking = False
        self.imports = set()

        self.thread = None
        self.threads = {}
        self.parent = None

        self.openaikey = None
        self.tokens = 100
        self.browser_path = None
        self.serial_1 = 0
        
        self.endpoints = {}
        self.endpoint_datas = {}
        self.endpoint_path = 'demos/practical/apidata/apitestdata.csv'
        
        self.processes = {}

    # executes stored script
    def execute(self, script):

        # convert script to lines
        self.lines = list(filter(None, script.split("\n")))

        # for aggregate syntax support !{}
        inmultiline = False
        multiline = ''

        # for block syntax
        inblock = False
        p = 0

        for line in self.lines:
            line = line.strip()
            lines_ran.append(line)
            if line.startswith("::") or line.startswith("#"):
                self.current_line += 1
                continue
        
            else:

                # aggregate syntax !{} (not recommended for most cases)
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

                # block syntax (recommended for most cases)
                elif not inblock and line.endswith('('):
                    for c in line:
                        if c == '(':
                            p += 1
                        if c == ')':
                            p -= 1
                        multiline += c
                    inblock = True
                elif inblock:
                    for i in range(len(line)):
                        c = line[i]
                        if c == '(':
                            p += 1
                        if c == ')':
                            p -= 1

                        # end of syntax met                        
                        if p == 0:
                            multiline += c
                            inter = multiline
                            multiline = ''
                            inblock = False
                            self.interpret(inter)
                            break
                        multiline += c
                else:
                    self.interpret(line)
                

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
        global lock
        global auxlock
        if self.breaking:
            return
        try:
            line = line.strip()
        except:
            return
        l = len(line)
        cont = False
        if line == '':
            return

        if line.startswith('<<'):

            # parse all text in the line for text surrounded by %
            funccalls = []
            infunc = False
            func = ''
            for i in range(0, line.rindex('>>')):
                if line[i] == '|' and not infunc:
                    infunc = True
                elif line[i] == '|' and infunc:
                    infunc = False
                    funccalls.append(func)
                    func = ''
                elif infunc:
                    func += line[i]
            for function in funccalls:
                ret = self.interpret(function)
                if isinstance(ret, str):
                    line = line.replace('|' + function + '|', '"' + str(ret) + '"')
                else:
                    line = line.replace('|' + function + '|', str(ret))
            line = line[2:-2]
            try:
                return eval(line)
            except:
                return line

        # user defined syntax
        for key in syntax:
            if line.startswith(key):
                return self.run_syntax(key, line)
            
        # msn1 fallback
        if line[0] == '@':
            line = line[1:]
            return self.interpret_msnscript_1(line)

        # user defined macro
        for token in macros:
            if line.startswith(token):
                
                # variable name
                varname = macros[token][1]
                
                # function to execute
                function = macros[token][2]
                
                val = line[len(token):]
                                
                # store extended for user defined syntax
                self.vars[varname] = Var(varname, val)

                # if the macro returns a value instead of executing a function
                if len(macros[token]) == 4:
                    return self.interpret(macros[token][3])
                    
                # execute function
                return self.interpret(function)
        
        # user defined post macro
        for token in postmacros:
            if line.endswith(token):

                # if the macro returns a value instead of executing a function
                if len(postmacros[token]) == 4:
                    return postmacros[token][3]
                varname = postmacros[token][1]
                function = postmacros[token][2]
                val = line[0:len(line) - len(token)]
                self.vars[varname] = Var(varname, val)
                return self.interpret(function)

        if line[0] == '*':
            line = self.replace_vars(line[1:])
            return self.interpret(line)

        # try base literal
        try:
            if not line.startswith('--'):
                return eval(line)
        except:
            None

        func = ''
        objfunc = ''
        obj = ''
        s = 0
        sp = 0
        for i in range(l):
            if cont:
                continue
            try:
                c = line[i]
            except:
                break
            if c == ' ' and s == 0:
                sp += 1
                continue
        

            if c == '.':
                obj = func
                func = ''
                objfunc = ''
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
                    return self.loggedmethod[-1]
            
            # method-specific line reached
            elif line.startswith('--'):
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

                # clean function for handling
                func = func.strip()
                objfunc = objfunc.strip()
                # class attribute / method access
                if obj in self.vars:
                    vname = obj
                    try:
                        var = self.get_var(vname)
                    except:
                        var = self.vars[vname]
                    try:
                        object = self.vars[obj].value
                    except:
                        object = self.vars[obj]
                    if objfunc in object:
                        if args[0][0] == '':
                            return object[objfunc]
                        
                        # parameter provided, wants to set attribute
                        param = self.parse(0, line, f, sp, args)[2]

                        self.vars[obj].value[objfunc] = param
                        return param
                    
                    elif objfunc == 'copy':
                        return object.copy()
                
                    # literal specific methods
                    # the isinstance branches below indicate DESCTRUCTIVE methods!
                    
                    # array based functions
                    elif isinstance(object, list):

                        # adds all arguments to the first argument which should be a variable name
                        # as a string
                        if objfunc == 'push':
                            for i in range(len(args)):
                                self.vars[vname].value.append(self.parse(i, line, f, sp, args)[2])
                            return self.vars[vname].value

                        # gets the average of this array
                        elif objfunc == 'avg':
                            return sum(self.vars[vname].value) / len(self.vars[vname].value)

                        # inserts all values at an index
                        if objfunc == 'insert':
                            
                            # index to insert
                            index = self.parse(0, line, f, sp, args)[2]
                            
                            # inserts the rest of the arguments, one at a time
                            for i in range(len(args)):
                                self.vars[vname].value.insert(index, self.parse(i, line, f, sp, args)[2])
                            return self.vars[vname].value
                        
                        # removes a certain amount of all arguments supplied
                        if objfunc == 'removen':
                            count = self.parse(0, line, f, sp, args)[2]
                            
                            # removes count amount of the rest of the arguments from the object
                            for i in range(1, len(args)):
                                for j in range(count):
                                    del var[var.index(self.parse(i, line, f, sp, args)[2])]
                            return object

                        # removes all occurances of each argument from the list
                        if objfunc == 'remove':
                            for i in range(len(args)):
                                while self.parse(i, line, f, sp, args)[2] in var:
                                    del var[var.index(self.parse(i, line, f, sp, args)[2])]
                            return object
                    
                        # gets a sorted copy of this array
                        if objfunc == 'sorted':
                            return sorted(self.vars[vname].value)
                        
                        # sorts this array
                        if objfunc == 'sort':
                            self.vars[vname].value.sort()
                            return self.vars[vname].value
                    
                    # if the object is a string
                    elif isinstance(object, str):
                        
                        if objfunc == 'add':
                            for i in range(len(args)):
                                self.vars[vname].value += self.parse(i, line, f, sp, args)[2]
                            return self.vars[vname].value
                        
                        if objfunc == 'split':
                            arg = self.parse(0, line, f, sp, args)[2]
                            try:
                                return self.vars[vname].value.split(arg)
                            except:
                                return self.vars[vname].split(arg)

                        # replaces all instances of the first argument with the second argument
                        if objfunc == 'replace':
                            
                            # what to replace
                            replacing = self.parse(0, line, f, sp, args)[2]
                            
                            # replacing with
                            wth = self.parse(1, line, f, sp, args)[2]
                            
                            # replaces all instances of replacing with wth
                            self.vars[vname].value = self.vars[vname].value.replace(replacing, wth)
                            
                            # returns the new string
                            return self.vars[vname].value
                           
                        # strips the value at the variable name 
                        if objfunc == 'strip':
                            self.vars[vname].value = self.vars[vname].value.strip()
                            return self.vars[vname].value

                        # obtains a stripped version of itself
                        if objfunc == 'stripped':
                            return self.vars[vname].value.strip()

                        if objfunc == 'self':
                            try:
                                return self.vars[vname].value
                            except:
                                return self.vars[vname]

                    # if the object is a dictionary
                    elif isinstance(object, dict):
                        
                        # sets a dictionary at an index
                        if objfunc == 'set':
                            index = self.parse(0, line, f, sp, args)[2]
                            self.vars[vname].value[index] = self.parse(1, line, f, sp, args)[2]
                            return self.vars[vname].value

                # splits the first argument by the second argument
                if func == 'split':
                    to_split = self.parse(0, line, f, sp, args)[2]               
                    splitting_by = self.parse(1, line, f, sp, args)[2]     
                    return to_split.split(splitting_by)
                
                # obtains text between the first argument of the second argument
                if func == 'between':
                    
                    # surrounding token
                    surrounding = self.parse(0, line, f, sp, args)[2]
                    
                    # string to analyze
                    string = self.parse(1, line, f, sp, args)[2]
                    
                    funccalls = []
                    try:
                        while string.count(surrounding) > 1:
                            string = string[string.index(surrounding) + len(surrounding):]
                            funccalls.append(string[:string.index(surrounding)])
                            string = string[string.index(surrounding) + len(surrounding):]
                    except: 
                        None
                        
                    return funccalls
                
                # creates / sets a variable
                if func == 'var':
  
                    # extract varname
                    varname = self.parse(0, line, f, sp ,args)[2]

                    # extract value
                    value = self.parse(1, line, f, sp ,args)[2]
                                        
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
                            failed = ''
                            for arg in args:
                                failed += str(arg[0]) + ' '
                            err = self.err("assertion error", "", failed)
                            self.logg(err, line)
                            return False

                    return True

                # gets an amount of lines executed before the working line
                elif func == 'before':
                    return lines_ran[len(lines_ran) - self.parse(0, line, f, sp, args)[2]:]

                # trace capabilities
                elif obj == 'trace':
                    if objfunc == 'before':
                        return lines_ran[len(lines_ran) - self.parse(0, line, f, sp, args)[2]:]
                    if objfunc == 'this':
                        return lines_ran[-1]
                    return '<msnint2 class>'


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
                    
                    ifcond = self.parse(0, line, f, sp ,args)[2]                    
                    
                    # if condition is true
                    if (ifcond):
                        return self.parse(1, line, f, sp, args)[2]

                    # otherwise false block is executed
                    if false_block_s:
                        return self.parse(2, line, f, sp, args)[2]
                    return False

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

                    # case array
                    if isinstance(self.vars[first].value, list):
                        self.vars[first].value.append(second)

                    # case string or number
                    else:
                        self.vars[first].value += second
                    return self.vars[first].value
                
                elif func == 'sub':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value -= second
                    return self.vars[first].value
                elif func == 'mul':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value *= second
                    return self.vars[first].value
                elif func == 'div':
                    line, as_s, first = self.parse(0, line, f, sp, args)
                    line, as_s, second = self.parse(1, line, f, sp, args)
                    self.vars[first].value /= second
                    return self.vars[first].value
                
                # appends to an array variable
                elif func == 'append':        
                    
                    # varname
                    varname = self.parse(0, line, f, sp, args)[2]    
                    
                    # value to append
                    value = self.parse(1, line, f, sp, args)[2]
                    
                    self.vars[varname].value.append(value)
                    return value    
                    
                # gets the MSNScript version of this interpreter
                elif func == 'version':
                    return self.version

                # random capabilities
                elif func == 'random':
                    # gets a random number between 0 and 1
                    if len(args) == 1:

                        arg = self.parse(0, line, f, sp, args)[2]

                        return random.random()

                    # random number in range
                    elif len(args) == 2:
                        arg = self.parse(0, line, f, sp, args)[2]
                        arg2 = self.parse(1, line, f, sp, args)[2]

                        return (random.random() * (arg2 - arg)) + arg

                    # random int in range
                    elif len(args) == 3:
                        arg = self.parse(0, line, f, sp, args)[2]
                        arg2 = self.parse(1, line, f, sp, args)[2]

                        return math.floor((random.random() * (arg2 - arg)) + arg)


                    return '<msnint2 class>'

                # html parsing simplified
                elif obj == 'html':
                    
                    url = self.parse(0, line, f, sp, args)[2]
                
                    # creates a BeautifulSoup object of a url
                    if objfunc == 'soup':
                        
                        response = requests.get(url)
                        return BeautifulSoup(response.content, 'html5lib')
                    
                    # scrapes all html elements from a url
                    if objfunc == 'scrape':
                        all_elem = self.html_all_elements(url)
                    
                        
                        return None
                
                
                
                # performs math functions
                elif obj == 'math':

                    # extract argument
                    line, as_s, arg = self.parse(0, line, f, sp, args)

                    # perform function
                    if objfunc == 'abs':
                        return abs(arg)
                    elif objfunc == 'ceil':
                        return math.ceil(arg)
                    elif objfunc == 'floor':
                        return math.floor(arg)
                    elif objfunc == 'round':
                        return round(arg)
                    elif objfunc == 'sqrt':
                        return math.sqrt(arg)
                    elif objfunc == 'sin':
                        return math.sin(arg)
                    elif objfunc == 'cos':
                        return math.cos(arg)
                    elif objfunc == 'tan':
                        return math.tan(arg)
                    elif objfunc == 'asin':
                        return math.asin(arg)
                    elif objfunc == 'acos':
                        return math.acos(arg)
                    elif objfunc == 'atan':
                        return math.atan(arg)
                    elif objfunc == 'log':
                        return math.log(arg)
                    elif objfunc == 'log10':
                        return math.log10(arg)
                    elif objfunc == 'log2':
                        return math.log2(arg)
                    elif objfunc == 'exp':
                        return math.exp(arg)
                    elif objfunc == 'pow':
                        return math.pow(arg, self.parse(1, line, f, sp, args)[2])
                    elif objfunc == 'factorial':
                        return math.factorial(arg)
                    elif objfunc == 'e':
                        return math.e
                    elif objfunc == 'pi':
                        return math.pi
                    return '<msnint2 class>'
                   
                   
                    
                # defines new syntax, see tests/validator.msn2 for documentation
                elif func == 'syntax':
                    
                    # gets the syntax token
                    token = self.parse(0, line, f, sp, args)[2]
                    
                    # gets the variable name of the between
                    between = self.parse(1, line, f, sp, args)[2]
                    
                    # function that should be executed when the syntax is found
                    function = args[2][0]
                    
                    return self.add_syntax(token, between, function)
                
                # defines a new macro
                elif func == 'macro':
                    
                    token = self.parse(0, line, f, sp, args)[2]
                    
                    varname = self.parse(1, line, f, sp, args)[2]
                    
                    code = args[2][0]
                    
                    macros[token] = [token, varname, code]
                    
                    # 4th argument offered as a return value from that macro
                    # as opposed to a block of code
                    if len(args) == 4:
                        macros[token].append(self.parse(3, line, f, sp, args)[2])
                        
                    return macros[token]
                
                # creates a macro that should be declared at the end of a line
                elif func == 'postmacro':
                    
                    token = self.parse(0, line, f, sp, args)[2]
                    
                    varname = self.parse(1, line, f, sp, args)[2]
                    
                    code = args[2][0]
                    
                    postmacros[token] = [token, varname, code]
                        
                    # same as macro
                    if len(args) == 4:
                        postmacros[token].append(self.parse(3, line, f, sp, args)[2])

                    return postmacros[token]
                    
                
                # obtains the args of the first argument passed as if it were an 
                elif func == 'args':
                    None
                  
                # performs object based operations
                elif obj == 'var':
                    
                    # determines if all variables passed are equal
                    if objfunc == 'equals':
                        firstvar = self.vars[self.parse(0, line, f, sp, args)[2]].value
                        for i in range(1, len(args)):
                            if firstvar != self.vars[self.parse(i, line, f, sp, args)[2]].value:
                                return False
                        return True
                    return '<msnint2 class>'
                
                # gets the value of a variable
                elif func == 'val':
                    
                    # gets the variable name
                    varname = self.parse(0, line, f, sp, args)[2]
                    
                    try:
                        return self.vars[varname].value
                    except:
                        return self.vars[varname]
                    
                # gets a sorted version of the array 
                elif func == 'sorted':
                    return sorted(self.parse(0, line, f, sp, args)[2])
                    
                    
                # performs file-specific operations
                elif obj == 'file':
                    
                    # creates a file
                    if objfunc == 'create':
                        lock.acquire()
                        line, as_s, filename = self.parse(0, line, f, sp, args)
                        open(filename, 'w').close()
                        lock.release()
                        return True
                    
                    # reads text from a file
                    if objfunc == 'read':
                        lock.acquire()
                        file = open(self.parse(0, line, f, sp, args)[2], "r")
                        contents = file.read()
                        file.close()
                        lock.release()
                        return contents
                    
                    # writes to a file
                    if objfunc == 'write':
                        lock.acquire()
                        file = open(self.parse(0, line, f, sp, args)[2], "w")
                        towrite = self.parse(1, line, f, sp, args)[2]
                        file.write(towrite)
                        file.close()
                        lock.release()
                        return towrite
                    
                    # writes the argument as code
                    if objfunc == 'writemsn':
                        lock.acquire()
                        file = open(self.parse(0, line, f, sp, args)[2], "w")
                        towrite = args[1][0]
                        file.write(towrite)
                        lock.release()
                        return towrite
                    
                    # clears a file of all text
                    if objfunc == 'clear':
                        lock.acquire()
                        file = open(self.parse(0, line, f, sp, args)[2], "w")
                        file.write("")
                        file.close()
                        lock.release()
                        return True
                    
                    # appends to a file 
                    if objfunc == 'append':
                        lock.acquire()
                        file = open(self.parse(0, line, f, sp, args)[2], "a")
                        towrite = self.parse(1, line, f, sp, args)[2]
                        file.write(towrite)
                        file.close()
                        lock.release()
                        return towrite
                    
                    # deletes a file
                    if objfunc == 'delete':
                        lock.acquire()
                        deleting = self.parse(0, line, f, sp, args)[2]
                        try:
                            os.remove(deleting)
                        except:
                            None
                        lock.release()
                        return deleting
                    
                    # renames a file
                    if objfunc == 'rename':
                        lock.acquire()
                        old = self.parse(0, line, f, sp, args)[2]
                        new = self.parse(1, line, f, sp, args)[2]
                        os.rename(old, new)
                        lock.release()
                        return new
                    
                    # copies a file
                    if objfunc == 'copy':
                        lock.acquire()
                        old = self.parse(0, line, f, sp, args)[2]
                        new = self.parse(1, line, f, sp, args)[2]
                        shutil.copy(old, new)
                        lock.release()
                        return new
                    
                    # moves a file
                    if objfunc == 'move':
                        lock.acquire()
                        old = self.parse(0, line, f, sp, args)[2]
                        new = self.parse(1, line, f, sp, args)[2]
                        shutil.move(old, new)
                        lock.release()
                        return new
                    
                    # determines if a file exists
                    if objfunc == 'exists':
                        lock.acquire()
                        exists = os.path.exists(self.parse(0, line, f, sp, args)[2])
                        lock.release()
                        return exists
                    
                    # determines if a file is a directory
                    if objfunc == 'isdir':
                        lock.acquire()
                        isdir = os.path.isdir(self.parse(0, line, f, sp, args)[2])
                        lock.release()
                        return isdir
                    
                    # determines if a file is a file
                    if objfunc == 'isfile':
                        lock.acquire()
                        isfile = os.path.isfile(self.parse(0, line, f, sp, args)[2])
                        lock.release()
                        return isfile
                    
                    # lists files in a directory
                    if objfunc == 'listdir':
                        lock.acquire()
                        try:
                            listdir = os.listdir(self.parse(0, line, f, sp, args)[2])
                            lock.release()
                            return listdir
                        except FileNotFoundError:
                            
                            # directory doesn't exist
                            lock.release()
                            return None
                    
                    # makes a directory
                    if objfunc == 'mkdir':
                        lock.acquire()
                        try:
                            made = os.mkdir(self.parse(0, line, f, sp, args)[2])
                            lock.release()
                            return made
                        except FileExistsError:
                            lock.release()
                            return False
                    
                    # removes a directory
                    if objfunc == 'rmdir':
                        lock.acquire()
                        try:
                            rm = os.rmdir(self.parse(0, line, f, sp, args)[2])
                            lock.release()
                            return rm
                        except OSError:
                            lock.release()
                            return None
                                        
                    # gets the current working directory
                    if objfunc == 'getcwd':
                        lock.acquire()
                        cwd = os.getcwd()
                        lock.release()
                        return cwd
                    
                    # gets the size of a file
                    if objfunc == 'getsize':
                        lock.acquire()
                        size = os.path.getsize(self.parse(0, line, f, sp, args)[2])
                        lock.release()
                        return size
                        
                    # deletes all files and directories within a directory
                    if objfunc == 'emptydir':
                        lock.acquire()
                        directory = self.parse(0, line, f, sp, args)[2]
                        try:
                            for file in os.listdir(directory):
                                try:
                                    os.remove(os.path.join(directory, file))
                                except:
                                    shutil.rmtree(os.path.join(directory, file), ignore_errors=True)
                            lock.release()
                            return directory
                        except FileNotFoundError:
                            
                            # directory doesn't exist
                            lock.release()
                            return None

                # performs math operations
                elif obj == 'math':
                    if objfunc == 'add':
                        return self.parse(0, line, f, sp, args)[2] + self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'subtract':
                        return self.parse(0, line, f, sp, args)[2] - self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'multiply':
                        return self.parse(0, line, f, sp, args)[2] * self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'divide':
                        return self.parse(0, line, f, sp, args)[2] / self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'power':
                        return self.parse(0, line, f, sp, args)[2] ** self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'root':
                        return self.parse(0, line, f, sp, args)[2] ** (1 / self.parse(1, line, f, sp, args)[2])
                    
                    if objfunc == 'mod':
                        return self.parse(0, line, f, sp, args)[2] % self.parse(1, line, f, sp, args)[2]
                    
                    if objfunc == 'floor':
                        return math.floor(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'ceil':
                        return math.ceil(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'round':
                        return round(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'abs':
                        return abs(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'sin':
                        return math.sin(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'cos':
                        return math.cos(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'tan':
                        return math.tan(self.parse(0, line, f, sp, args)[2])
                    
                    if objfunc == 'asin':
                        return math.asin(self.parse(0, line, f, sp, args)[2])
                    return '<msnint2 class>'
                    
                # gets the type of the first argument passed
                elif func == 'type':
                    return type(self.parse(0, line, f, sp, args)[2])
                 
                # gets the parent context
                elif func == 'parent':
                    return self.parent
                
                # gets the booting context
                elif func == 'boot':
                    while self.parent != None:
                        self = self.parent
                    return self
                
                # sets an index of an array
                # first argument is the variable to modify
                # second is the index to modify
                # third argument is the value to set
                elif func == 'set':

                    # obtain varname of array
                    varname = self.parse(0, line, f, sp, args)[2]

                    # index to set at
                    ind = self.parse(1, line, f, sp, args)[2]

                    # value to set
                    val = self.parse(2, line, f, sp, args)[2]
                    
                    # perform set operation                                                      
                    self.vars[varname].value[ind] = val
                        
                    return val

                # deletes a variable
                elif func == 'del':
                    for i in range(len(args)):
                        line, as_s, first = self.parse(i, line, f, sp, args)
                        del self.vars[first]
                    return True

                # concatinates two strings
                elif func == 'cat':
                    
                    # first argument (required)
                    first = self.parse(0, line, f, sp, args)[2]

                    # second argument (required)
                    second = self.parse(1, line, f, sp ,args)[2]
                    
                    cat = str(first) + str(second)
                    
                    # concatinate rest of arguments
                    for i in range(2, len(args)):
                        cat += str(self.parse(i, line, f, sp, args)[2])

                    return cat

                # determines equality of all arguments
                elif func == 'equals':
                    line, as_s, arg1 = self.parse(0, line, f, sp, args)
                    for i in range(1, len(args)):
                        line, as_s, curr_arg = self.parse(i, line, f, sp, args)
                        if curr_arg != arg1:
                            return False
                    return True

                # nots a bool
                elif func == 'not':
                    return not self.parse(0, line, f, sp, args)[2]

                # ands two bools
                elif func == 'and':
                    first = self.parse(0, line, f, sp, args)[2]
                    for i in range(1, len(args)):
                        if not first and self.parse(i, line, f, sp ,args)[2]:
                            return False
                    return True

                # ors two bools
                elif func == 'or':
                    return self.parse(0, line, f, sp, args)[2] or self.parse(1, line, f, sp ,args)[2]

                # inline function, takes any amount of instructions
                # returns the result of the last instruction
                elif func == "=>":
                    ret = None
                    for i in range(len(args)):
                        arguments = args[i]

                        # current instruction
                        ins_s = arguments[0]

                        line, ret = self.convert_arg(ins_s, line, f, sp, args)
                    return ret

                # data structure for holding multiple items
                elif func == 'class':
                    # new interpreter
                    inter = Interpreter()
                    
                    # log self
                    inter.parent = self
                    
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
                    
                    for methodname in inter.methods:
                        obj_to_add[methodname] = Var(methodname + "#method", inter.methods[methodname])
                    
                    self.vars[name] = Var(name, obj_to_add)
                    return obj_to_add

                # gets the first argument at the second argument
                elif func == 'get':
                    iterable = self.parse(0, line, f, sp, args)[2]
                    index = self.parse(1, line, f, sp, args)[2]
                    return iterable[index]

                # get the keys of the first argument
                elif func == 'keys':
                    return self.parse(0, line, f, sp, args)[2].keys()

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

                # python print
                elif func == 'print':
                    ret = None
                    for i in range(len(args)):
                        ret = self.parse(i, line, f, sp, args)[2]
                        if i != len(args) - 1:
                            print(ret, end=" ", flush=True)
                        else:
                            print(ret, flush=True)
                    return ret

                # sleeps the thread for the first argument amount of seconds
                elif func == "sleep":
                    return time.sleep(self.parse(0, line, f, sp, args)[2])

                # returns this interpreter
                elif func == 'me':
                    return self.me()

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
                        strenv += "\t" + methodname + "("
                        for i in range(len(Method.args)):
                            arg = Method.args[i]
                            if i != len(Method.args) - 1:
                                strenv += "" + arg + ", "
                            else:
                                strenv += "" + arg
                        strenv += ")\n"
                        

                    strenv += "\nlog:\n" + self.log
                    strenv +="\n-------------------------------"
                    if should_print:
                        print(strenv)
                    return strenv

                # executes MSNScript2 from its string representation
                elif func == '-':
                    line, as_s, string = self.parse(0, line, f, sp, args)
                    return self.interpret(string)

                # returns the MSNScript2 passed as a string
                elif func == 'async':
                    return args[0][0]

                # gets the current time
                elif func == 'now':
                    return time.time()

                # creates a private execution enviroment
                # private block will have read access to the enclosing Interpreter's
                # variables and methods
                elif func == 'private' or func == 'inherit:all':
                    block_s = args[0][0]
                    inter = Interpreter()
                    inter.parent = self
                    for vname, entry in self.vars.items():
                        inter.vars[vname] = entry
                    for mname, entry in self.methods.items():
                        inter.methods[mname] = entry
                    inter.execute(block_s)
                    return inter

                # breaks out of the working context
                elif func == 'break':
                    self.breaking = True
                    return

                # inherits methods only from the parent context
                elif func == 'inherit:methods':
                    for methodname in self.parent.methods:
                        self.methods[methodname] = self.parent.methods[methodname]
                    return True

                # inherits variables from parent context
                elif func == 'inherit:vars':
                    for varname in self.parent.vars:
                        self.vars[varname] = self.parent.vars[varname]
                    return True

                # creates a new execution environment
                # new env is executed by a fresh interpreter
                # nothing is inherited from parent context
                elif func == 'new' or func == 'inherit:none':
                    inter = Interpreter()
                    inter.parent = self
                    return inter.execute(args[0][0])

                # starts a new process with the first argument as the target
                elif func == 'process':
                    
                    # path to the process to run
                    path = self.parse(0, line, f, sp, args)[2]
                
                    # if windows:
                    if os.name == 'nt':
                    
                        # runs the process
                        sub = subprocess.run (args=['python', 'msn2.py', path], shell=True)
                        self.processes[path] = sub 
                        return sub

                    # if linux
                    elif os.name == 'posix':
                        
                        return None
                    return None


                # gets the pid of the working process
                elif func == 'pid':
                    return os.getpid()
                
                # creates a new thread to execute the block, thread
                # starts on the same interpreter
                elif func == "thread":
                    name = self.parse(0, line, f, sp, args)[2]
                    thread = threading.Thread(target=self.interpret, args=(args[1][0],))
                    thread.name = name
                    self.threads[name] = [thread, self]
                    thread.start()
                    return True

                # acquires the global lock
                elif func == 'acquire':
                    return auxlock.acquire()
                
                # releases the global lock
                elif func == 'release':
                    return auxlock.release()
                    

                # joins the current working thread with the thread name specified
                elif func == 'join':
                    
                    for i in range(len(args)):
                        name = self.parse(i, line, f, sp, args)[2]
                        thread = self.thread_by_name(name)
                        while thread == None:
                            None
                        thread.join()
                    return True
                
                # exits the working thread
                elif func == 'stop':
                    
                    return os._exit(0)
                            
                # tries the first argument, if it fails, code falls to the catch/except block
                # there is no finally implementation
                elif func == 'try':
                    ret = None
                    try:
                        ret = self.interpret(args[0][0])
                    except:
                        try:
                            catch_block = args[1][0]
                            ret = self.interpret(catch_block)
                        except:
                            None
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

                # exports a quantity of variables or methods from the working context to the parent context,
                # ex private context -> boot context
                elif func == 'export':
                    for i in range(len(args)):
                        varname = self.parse(i, line, f, sp, args)[2]
                        if varname in self.vars:
                            self.parent.vars[varname] = self.vars[varname]
                        elif varname in self.methods:
                            self.parent.methods[varname] = self.methods[varname]
                    return True

                # exports a single value as the variable name
                # first argument is the new variable name
                # second is the value to export
                elif func == 'exportas':
                    
                    # variable name
                    varname = self.parse(0, line, f, sp, args)[2]

                    # value
                    val = self.parse(1, line, f, sp, args)[2]      

                    # export to parent context
                    self.parent.vars[varname] = Var(varname, val)
                    return val

                # exports all variables and methods to the parent context
                elif func == 'exportall':
                    for varname in self.vars:
                        self.parent.vars[varname] = self.vars[varname]
                    for methodname in self.methods:
                        self.parent.methods[methodname] = self.methods[methodname]
                    return True

                # sends a command to the console, console type depends on
                # executors software of course
                elif func == 'console':
                    ins_s = args[0][0]
                    line, to_run = self.convert_arg(ins_s, line, f, sp, args)
                    os.system(to_run)
                    return True

                # performs a get request to an http server
                # first parameter is the URL
                # second parameter is a map of parameters to sent as a request
                elif func == 'request':
                    
                    # get URL to request from
                    url = self.parse(0, line, f, sp, args)[2]

                    try:
                        # get parameters
                        params = self.parse(1, line, f, sp, args)[2]
                    except:
                        params = None

                    response = requests.get(url=url, params=params)
                    
                    # return response
                    return response.json()
                
                # requires thread-safe context, see /demos/protected.msn2
                # simulates returning of the function currently running
                # should be used cautiously, if you dont know whether to use return() or var()
                # to return a value, use var()
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

                # gets the public IP address of the machine
                elif func == 'pubip':
                    
                    # asks an api server for this address
                    return requests.get('https://api.ipify.org').text

                # gets the private ips of this machine
                elif func == 'privips':
                    return socket.gethostbyname_ex(socket.gethostname())[2]

                # starts an api endpoint
                elif func == 'ENDPOINT':
                    
                    # initial API endpoint data
                    path = None
                    init_data = {}
                    port = 5000
                    host = '127.0.0.1'
                    
                    # 1 argument, defaults to 127.0.0.1:5000/path = {}
                    if len(args) == 1:
                        
                        # path to endpoint
                        path = self.parse(0, line, f, sp, args)[2]

                    # 2 arguments, defaults to 127.0.0.1:5000/path = init_data
                    elif len(args) == 2:
                    
                        # path to endpoint
                        path = self.parse(0, line, f, sp, args)[2]
                    
                        # json to initialize at the endpoint
                        init_data = self.parse(1, line, f, sp, args)[2]
                    
                    # 3 arguments, defaults to host:port/path = init_data
                    elif len(args) == 4:
                        
                        # host to endpoint as first argument
                        host = self.parse(0, line, f, sp, args)[2]
                        
                        # port to endpoint as second argument
                        port = self.parse(1, line, f, sp, args)[2]
                        
                        # path to endpoint
                        path = self.parse(2, line, f, sp, args)[2]
                        
                        # json to initialize at the endpoint
                        init_data = self.parse(3, line, f, sp, args)[2]                        
                        
                    
                    # prepare endpoint
                    print('serving on http://' + host +':' + str(port) + path)
                    app = Flask(__name__)
                    
                    # disable flask messages that aren't error-related
                    log = logging.getLogger('werkzeug')
                    log.disabled = True
                    app.logger.disabled = True
                    
                    # gets Flask Api
                    api = Api(app)
                    
                    # makes a response to itself, carrying data to endpoint as a response
                    curr_endpoint = self.EndPoint.make_api(init_data)
                    
                    # logs newly created endpoint
                    self.endpoints[path] = api
                    
                    # adds class EndPoint as a Resource to the Api with the specific path
                    # passes arg2 alongside
                    api.add_resource(curr_endpoint, path)
                    
                    # starting flask server
                    try:
                        
                        # if internal
                        app.run(host=host, port=port, debug=False, use_reloader=False)
                        
                    except:
                        # if external
                        try:
                            if __name__ == '__main__':
                                app.run(host='0.0.0.0', port=port, debug=False, use_reloader=False)
                        except:
                            None
                    return api

                # posts to an api endpoint
                elif func == 'POST':
                    
                    # url to post to, defaults to localhost
                    host = self.parse(0, line, f, sp, args)[2]
                    
                    # port to post to
                    port = self.parse(1, line, f, sp, args)[2]
                    
                    # path after url
                    path = self.parse(2, line, f, sp, args)[2]
                    
                    # data to post
                    data = self.parse(3, line, f, sp, args)[2]

                    # if local network
                    if host == '0.0.0.0':
                        response = requests.post(url=('http://127.0.0.1:' + str(port) + path), json=data)
                    
                    # if localhost
                    else:
                        # post to endpoint
                        response = requests.post(url=('http://' + host + ':' + str(port) + path), json=data)
                    
                    # get response
                    return response.json()
                    
                # gets from an api endpoint
                elif func == 'GET':
                        
                    # url to get from, defaults to localhost
                    host = self.parse(0, line, f, sp, args)[2]
                        
                    # port to get from
                    port = self.parse(1, line, f, sp, args)[2]
                        
                    # path after url
                    path = self.parse(2, line, f, sp, args)[2]
                                            
                    # if local network
                    if host == '0.0.0.0':
                        return requests.get(url=('http://127.0.0.1:' + str(port) + path)).json()
                    
                    # if localhost
                    else:
                        return requests.get(url=('http://' + host + ':' + str(port) + path)).json()

                            
                # deletes from an api endpoint
                elif func == 'DELETE':
                    
                    # url to delete from, defaults to localhost
                    host = self.parse(0, line, f, sp, args)[2]
                    
                    # port to delete from
                    port = self.parse(1, line, f, sp, args)[2]
                    
                    # path after url
                    path = self.parse(2, line, f, sp, args)[2]
                    
                    if host == '0.0.0.0':
                        response = requests.delete(url=('http://127.0.0.1:' + str(port) + path))
                    else:
                        # delete from endpoint
                        response = requests.delete(url=('http://' + host + ':' + str(port) + path))
                    
                    return response.json()
                    

                # determines if the system is windows or not
                elif func == 'windows':
                    return os.name == 'nt'
                
                # determines if system is linux    
                elif func == 'linux':
                    return os.name == 'posix'
                
                # determines if system is mac
                elif func == 'mac':
                    return sys.platform == 'darwin'
                
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

                    if ret_name not in self.vars:
                        self.vars[ret_name] = Var(ret_name, None)

                    # execute method
                    method.run(func_args, self)
                    
                    try: 
                        return eval(str(self.vars[method.returns[0]].value))
                    except:
                        try:
                            return str(self.vars[method.returns[0]].value)
                        except:
                            return str(self.vars[method.returns[0]])
                
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

                        # if not specified, field is default value
                        except:
                            try:
                                instance[name] = var_obj.value[name].copy()
                            except:
                                var = var_obj[name]
                                instance[name] = var.value
                        curr_arg_num += 1

                    return instance

                # gets an attribute of an instance of a class
                elif func == 'getattr':
                    
                    # name of the object to pull from
                    name = self.parse(0, line, f, sp, args)[2]

                    # current working object
                    o = self.vars[name].value

                    # get attribute to pull
                    attr = self.parse(1, line, f, sp, args)[2]
                    return o[attr]

                # sets an attribute of an instance of a class
                elif func == 'setattr':
                    
                    # name of the object to set at
                    name = self.parse(0, line, f, sp, args)[2]

                    # current working object
                    o = self.vars[name].value

                    # name of attribute to set
                    attr = self.parse(1, line, f, sp, args)[2]

                    # value to set
                    val = self.parse(2, line, f, sp, args)[2]

                    # set the value
                    o[attr] = val
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
                elif len(func) > 0 and func[0] == '?':
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
  
            if obj != '':
                objfunc += c
            else:
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

    # adds a new program wide syntax
    def add_syntax(self, token, between, function):                    
        syntax[token] = [between, function]
        return [token, between, function]

    def run_syntax(self, key, line):
                # get everything between after syntax and before next index of syntax
                # or end of line
                inside = line[len(key):line.rindex(key)]
                
                # variable name
                invarname = syntax[key][0]
                
                # function to be run
                function = syntax[key][1]
                
                # store the in between for the user function
                self.vars[invarname] = Var(invarname, inside)
                
                return self.interpret(function)



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
        try:
            # thread exists
            return self.env_by_name(name)[0]
        except:
            
            # thread does not exist (yet)
            return None

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
        print (errmsg)
        return errmsg
    
    def __del__(self):
        None
    
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

    # splits a process
    def process_split(self, line):
        inter = Interpreter()
        return inter.interpret(line)

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
        indouble = False

        s2 = 0
        insingle = False

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

            if not self.in_string(s, s2):
                if c == '{':
                    b += 1
                if c == '}':
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

            # print(line)
            # print (f"""
            # c: {c}
            # p: {p}
            # a: {a}
            # s: {s}
            # s2: {s2}
            
            # """)

            if c == ',' and s == 0 and s2 == 0 and  p == 0 and a == 0 and b == 0:
                args.append([arg, start, start + len(arg)])
                start = i + 1
                arg = ''
                continue
            elif i == l:
                args.append([arg, start, start + len(arg)])
            arg += c
        return args

    def in_string(self, s, s2):
        return s > 0 or s2 > 0

    def interpret_msnscript_1(self, line):
        
        # parse all text in the line for text surrounded by %
        funccalls = []
        infunc = False
        func = ''
        for i in range(0, len(line)):
            if line[i] == '|' and not infunc:
                infunc = True
            elif line[i] == '|' and infunc:
                infunc = False
                funccalls.append(func)
                func = ''
            elif infunc:
                func += line[i]
            
        # for each instance of an msn2 reference    
        for call in funccalls:
            line = line.replace('|' + call + '|', str(self.interpret(call)))
        element = ''
        variable = ''

        for i in range(0, len(line)):
            c = line[i]
            if c != ' ':
                if c == '+' and line[i + 1] == '=':
                    variable = element
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
                    variable = element
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value -= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '*' and line[i + 1] == '=':
                    variable = element
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value *= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '/' and line[i + 1] == '=':
                    variable = element
                    element = ''
                    for j in range(i + 2, len(line)):
                        element += line[j]
                    self.vars[variable].value /= self.evaluate(element, 'number')
                    return self.vars[variable].value
                elif c == '=':
                    variable = element
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

    # scrapes all html elements from a URL
    def html_all_elements(self, url):
        
        # obtains a response from the URL
        response = requests.get(url)
        
        soup = BeautifulSoup(response.content, 'html5lib')
        
        # obtains all html elements
        return soup.find_all()
        
        

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
        try:
            return eval(new)
        except:
            None
        if type == 'number':
            new = self.replace_vars(new) 
            try:
                return eval(new)
            except:
                return eval(str(new))
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

    def me(self):
        return str(self).replace(' ', '').replace('<', '').replace('>', '').replace('Interpreter', '')


    def string(self, string):
        strn = ''
        isv = False
        try:
            string = self.vars[string]
            isv = True
        except KeyError:
            None

        # if isv and '"' not in string:
        #     string = '"' + string + '"'
        try:
            strn = eval(string);
        except:
            None
        try:
            for var in self.vars:
                strn = strn.replace("{" + var + "}", str(self.vars[var].value))
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

            def run(self, args, inter):
                for i in range(len(self.args)):
                    try:
                        inter.vars[self.args[i]] = inter.vars[args[i]]
                    except:
                        try: 
                            inter.vars[self.args[i]] = args[i]
                        except:
                            inter.vars[self.args[i]] = args[i]
                for line in self.body:
                    inter.interpret(line)
                    
                    
                    
    class EndPoint(Resource):
        
        @classmethod
        def make_api(cls, response):
            cls.response = response
            return cls

        # GET
        def get(self):
            return self.response

        # POST
        def post(self):
            
            # obtains current endpoint data
            current_data = self.response   
            
            # updates current data with data to post         
            current_data.update(request.get_json())
            
            # updates next response
            self.make_api(current_data)
            return current_data

        # DELETE
        def delete(self):
            self.make_api({})
            return self.response

