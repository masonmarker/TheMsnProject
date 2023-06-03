# Python script
#
# author : Mason Marker
# date : 6/2/2023

# Import run function from msn2python
# for .msn2 file execution
# msn2python module is 2 directories up from this one

# this file, alongside msnint2 has been 
# copied to this directory as an example
from msn2python import run


# does some random things
# before asking .msn2 for help


# declare array
array = [1, 2, 3, 4, 5, 6, 7]

# reverses the array
array = array[::-1]

# sending array
print('[python] sending input:', array)

# send the array to the .msn2 script
# to reverse it again
# run() returns the exported values from the script
# run() can return any amount of values, the first one
# is going to be the sorted (reversed again) array
array = run('TUTORIAL/python/do_msn2_things', array)[0]

# print the array
print('[python] received output:', array)

# assert correctness
assert(array == [1, 2, 3, 4, 5, 6, 7])
