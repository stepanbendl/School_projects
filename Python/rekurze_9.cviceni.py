def recursion(n):
    if n > 0:
        recursion(n-1)
        print("Hello World")


def factorialwo(x):
    factorial = 1
    if x == 0:
        return 1
    if x < 0:
        raise ValueError
    for i in range(1, x+1):
        factorial *= i
    return factorial


def factorialRecursion(x):
    if x <= 1:
        return 1
    if x < 0:
        raise ValueError
    return factorialRecursion(x-1) * x


def fibonacciRec(n):
    x0 = 0
    x1 = 1
    if n == 1:
        return x0
    if n == 2:
        return x1
    return (fibonacciRec(n-1) + fibonacciRec(n-2))


def reverse(array):
    if len(array) == 1:
        return array
    lst = []
    lst.append(array.pop())
    return lst + reverse(array[:len(array)])


def gcd(a, b):
    if a == 0:
        return b
    if b == 0:
        return a
    return gcd(b % a, a)


