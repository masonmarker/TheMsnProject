



string = '))(())'
answer = 2

string2 = ')((())))'
answer2 = 2

string3 = ')((())'
answer3 = 2

string4 = '))(('
answer4 = 4

string5 = ')))((('
answer5 = 6

string6 = '(((((())'
answer6 = 4

string7 = '(()))))))'
answer7 = 5

# determines the missing amount of parentheses
def missing_count(str):
    
    # initialize variables
    left = 0
    right = 0
    missing = 0
    
    for c in str:
        
        if c == '(':
            left += 1
            
        if c == ')':
            right += 1
            
        if right > left:
            missing += 1
            right -= 1
            
    return missing + (left - right)



# make assertions    
def ass(actual, exp):
    if actual != exp:
        print(f"actual: {actual} != expected: {exp}")
        
ass(missing_count(string), answer)
ass(missing_count(string2), answer2)
ass(missing_count(string3), answer3)
ass(missing_count(string4), answer4)
ass(missing_count(string5), answer5)
ass(missing_count(string6), answer6)
ass(missing_count(string7), answer7)
