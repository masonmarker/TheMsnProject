# launches a .msn2 script


# prepare msn2 interpreter
import msnint2

# cmd argument support
import sys

# filename is the first argument
filename = sys.argv[1]

extension = filename[filename.rindex('.'):]

# verify file type
if extension != '.msn2':
    print("Error: file type not supported")
    exit()


f = open(filename, "r")
script = f.read()

interpreter = msnint2.Interpreter()
interpreter.execute(script)

if interpreter.out != '':
    print(interpreter.out)