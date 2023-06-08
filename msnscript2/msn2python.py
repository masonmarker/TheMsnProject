# Function for executing .msn2 scripts from a Python environment.
#
# Provides the ability to input values to the script, as well as receive
# an output value from the script.
#
# author : Mason Marker
# date : 6/2/2023

# provides a .msn2 file with input values, and receives the output value
#
# inputs: a list of values to be passed to the script
# returns the specified out value from the script,
# see the .msn2 file in this directory for how input
# values are read and output

# prepare msn2 interpreter
import msnint2

# executes the script
#
# filename: the path to the .msn2 file to be executed
# inputs: a list of values to be passed to the script
def run(filename, inputs):

    # opens the file for reading
    f = open(filename, "r")
    script = f.read()
    
    # create an Interpreter to run the script
    interpreter = msnint2.Interpreter()
    
    # reserved keyword for input
    _n = '_msn2_reserved_in__'
    
    # pass the inputs to the script
    interpreter.vars[_n] = msnint2.Var(_n, inputs)
    
    # execute the script
    interpreter.execute(script)

    # prints the prnt() out if specified
    if interpreter.out != '':
        print(interpreter.out)
        
    # returns the exported value from the script
    
    _n_out = '_msn2_reserved_out__'
    
    if _n_out in interpreter.vars:
        return interpreter.vars[_n_out].value
