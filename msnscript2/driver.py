import msnint2

f = open("tests/validator.msn2", "r")
script = f.read()


interpreter = msnint2.Interpreter()
interpreter.execute(script)

print(interpreter.out)

print("\n\tvariables:")
# printing variables
for varname, Var in interpreter.vars.items():
    try:
        print (varname + " = " + str(Var.value))
    except:
        None

print("\n\tmethods:")
# printing methods
for methodname, Method in interpreter.methods.items():
    print (methodname + ":")

print("\nlog:")
print (interpreter.log)