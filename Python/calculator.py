def addition(x, y):
    return float(x)+float(y)


def subtraction(x, y):
    return float(x)-float(y)

def multiplication(x, y):
    return float(x)*float(y)

def division(x, y):
    if y == 0:
        raise ValueError("This operation is not supported for given input parameters")
    else:
        return float(x)/float(y)


def modulo(x, y):
    if x < y or y <= 0 :
        raise ValueError("This operation is not supported for given input parameters")
    else:
        return float(x)%float(y)

def secondPower(x):
    return x**2


def power(x, y):
    if y < 0:
        raise ValueError("This operation is not supported for given input parameters")
    else:
        return float(x)**float(y)


def secondRadix(x):
    if x <= 0:
        raise ValueError("This operation is not supported for given input parameters")
    else:
        return float(x)**(1/2)


def magic(x, y, z, k):
    l = float(x) + float(k)
    m = float(y) + float(z)
    if m == 0:
        raise ValueError("This operation is not supported for given input parameters")
    else:
        z = ((l/m) + 1)
        return float(z)

def control(a, x, y, z, k):
    if a == ("ADDITION"):
        return addition(x,y)
    elif a == ("SUBTRACTION"):
        return subtraction(x,y)
    elif a == ("MULTIPLICATION"):
        return multiplication(x,y)
    elif a == ("DIVISION"):
        return division(x,y)
    elif a == ("MOD"):
        return modulo(x,y)
    elif a == ("POWER"):
        return power(x,y)
    elif a == ("SECONDRADIX"):
       return secondRadix(x)
    elif a == ("MAGIC"):
        return magic(x,y,z,k)
    else:
        raise ValueError("This operation is not supported for given input parameters")
