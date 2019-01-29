def solver(a,b,c):
    assert a != 0
    return (c-b)/a

try:
    print(solver(0,2,3))
except ZeroDivisionError:
    print("Error")
except AssertionError:
    print("Error")