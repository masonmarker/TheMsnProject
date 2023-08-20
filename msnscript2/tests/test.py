import math
def sum_list(list):    
    sum = 0
    for i in list:     
        sum += i       
    return sum
def product_list(list):
    product = 1        
    for i in list:     
        product *= i   
    return product   
  
for lst in [[1, 2, 3], [4, 5, 6], [7, 8, 9]]:

    # test sum_list()
    total = sum_list(lst)
    expected_sum = 0
    if len(lst) > 0:
        expected_sum = sum(lst)
    # test product_list()
    product = product_list(lst)
    expected_product = 1
    if len(lst) > 0:
        expected_product = math.prod(lst)
    # print the results
    print('list: ' + str(lst))
    print('sum: ' + str(total) + ' (expected: ' + str(expected_sum) + ')')
    print('product: ' + str(product) + ' (expected: ' + str(expected_product) + ')')
    print('')