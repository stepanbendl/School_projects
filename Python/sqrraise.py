import math

number = float(input("Insert a number: "))
if number < 0:
    raise ValueError("This operation is not supported for given input parameters")
#    print("Input must be a number and the number can't be negative. "

print(math.sqrt(number))