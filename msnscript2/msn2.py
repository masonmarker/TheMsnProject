
# prepare msn2 interpreter
import msnint2

# cmd argument support
import sys

# filename is the first argument
filename = sys.argv[1]


# verify file type
if filename[filename.index('.'):] != '.msn2':
    print("Error: file type not supported")
    exit()


# read script from file
with open(filename, 'r') as f:
    script = f.read()



interpreter = msnint2.Interpreter()
interpreter.execute(script)

print(interpreter.out)

