# Converts MSN2 code to JavaScript.
#
# author : Mason Marker
# date : 12/15/2023
# version : 2.0.400


    

def convertToJS(
    line: str, 
    func: str, 
    objfunc: str, 
    args, interpreter) -> str:
    """
    Converts a 'line' of MSN2 code to JavaScript
    """
    
    # general print, JavaScript equivalent
    # is the notorious 'console.log'
    if func == "prnt" or func == "print":
        asJS = "console.log("
        for i in range(len(args)):
            asJS += f"{interpreter.parse(i, line, args)[2]}, "
        return asJS + ")"
    # otherwise, interpret the line normally
    # without using JS
    else:
        interpreter.usingJS = False
        ret = interpreter.interpret(line)
        interpreter.usingJS = True
        return ret