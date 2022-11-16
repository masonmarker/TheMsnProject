starting a .msn2 script:

    - you can only run programs from /msnscript2

       if functional: $ msn2 script.msn2
       else: $ python msn2.py script.msn2

it is suggested that when editing a .msn2 file that you switch VS Code's Language Mode to CoffeeScript!!


executing user programs:

command line:
    python msn2.py location_of_.msn2_file


    
see /demos for demonstrations
see /tests for syntax specific usage (find the most recent validator in /tests)

file-based code depends on the user's current directory after executing a .msn2 program (ex imports),
verify that file paths in written code are compatible with the directory at which the program is launched





