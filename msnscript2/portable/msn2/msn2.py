# launches a .msn2 script


# prepare msn2 interpreter
import msnint2

# cmd argument support
import sys

# filenames to run
if len(sys.argv) <= 1:
    print('[-] at least one .msn2 file needs to be specified')
    exit(1)

for i in range(1, len(sys.argv)):
    filename = sys.argv[i]
    
    # pushes changes to GitHub
    if filename == 'push':
        filename = 'projects/console/git.msn2'
    elif filename == 'test':
        filename = 'tests/misc.msn2'
    
    
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