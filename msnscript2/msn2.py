# launches a .msn2 script


# prepare msn2 interpreter
from msn2python import run

# cmd argument support
import sys

# filenames to run
if len(sys.argv) <= 1:
    print('[-] at least one .msn2 file needs to be specified')
    exit(1)

# for each command line argument
for i in range(1, len(sys.argv)):
    
    # obtain the filename
    filename = sys.argv[i]
    
    # quick file access
    if filename == 'test':
        filename = 'tests/misc.msn2'
    elif filename == 'elements':
        filename = 'tests/practical/auto/clicked.msn2'
        
    # if the file does not end in .msn2, add it
    if not filename.endswith('.msn2'):
        filename += '.msn2'
    
    # run the script
    run(filename, None)