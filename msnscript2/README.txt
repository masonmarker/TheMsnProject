installing dependencies

there are only a few pip installs tbh, just for flask servers and such,
all of them are in dependencies.txt



starting a .msn2 script:

    - you can only run programs from /msnscript2

       if it works you can use: $ msn2 script.msn2 script2.msn2
       else: $ python msn2.py script.msn2 script2.msn2

it is suggested that when editing a .msn2 file that you switch VS Code's Language Mode to CoffeeScript!!
to do this, under VS Code's language mode tab, configure file association of .msn2 as CoffeeScript


    see /TUTORIAL for the SUGGESTED syntax that should be used in a .msn2 program.
    other syntax likely exists outside of the suggested syntax, however the syntax works in very
    specific scenarios, and are not recommended to be used. 

    the suggested syntax is the most updated, and safest way to code with complex types.

see /demos for demonstrations
see /tests for syntax specific usage (find the most recent validator in /tests)
see /projects for larger demonstrations


file-based code depends on the user's current directory after executing a .msn2 program (ex imports),
verify that file paths in written code are compatible with the directory at which the program is launched





