installing dependencies:

you may need to run a few pip installs before executing .msn2 programs.
your program may or may not use the dependencies listed
in the dependencies.txt file.

you can install all of these dependencies at once via 'python install_deps.py'

these dependencies can also be re-installed via the msn2 help pages if necessary
as of 2.0.385. find these pages and much more with 'python msn2.py help'

starting a .msn2 script:

    - you can only run programs from /msnscript2

       if it works you can use: $ msn2 script.msn2 script2.msn2
       else: $ python msn2.py script.msn2 script2.msn2

       the in() call retrieves arguments to the command line
       in the msn2 environment

it is suggested that when editing a .msn2 file that you switch VS Code's Language Mode to CoffeeScript!!
to do this, under VS Code's language mode tab, configure file association of .msn2 as CoffeeScript

    see the msn2 help pages via 'python msn2.py help' too see the most updated syntax and usage.
    see /TUTORIAL for the msn2 source code for this demonstration.
    other syntax likely exists outside of the suggested syntax, however outside syntax works in very
    specific scenarios, and are not recommended to be used. 

    the suggested syntax is the most updated, and safest way to code.

--- ISSUES
if running a script doesn't work for any reason (other than a code-based error),
try reinstalling dependencies via 'python install_deps.py'
---

see /demos for demonstrations
see /tests for syntax specific usage (find the most recent validator in /tests)
see /projects for larger demonstrations
see /msn2 for a portable MSNScript2 Interpreter package that can be copied 
    into your project directories for launching .msn2 programs anywhere.
see /problems for popular programming problems solved in msn2.
see /system for system related operations in msn2.

run 'python msn2.py verify' to run the validator for msn2 integrity.

file-based code depends on the user's current directory after executing a .msn2 program (ex imports),
verify that file paths in written code are compatible with the directory at which the program is launched

again, run 'python msn2.py help' for more information on the msn2 interpreter and its usage.