import math
try:
    number = float(input("Insert a number: "))
    print(number**(1/2))
    print(math.sqrt(number))
except ValueError as e:
    print("Input must be a number and the number can't be negative. " + str(e))
finally:
    print("End")
