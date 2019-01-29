def factorial(x):
    result = 1
    for i in range(1,x+1):
        result *= i
    return result


def euler():
    sum = 1
    for i in range(1, 20):
        sum += (1/factorial(i))
    return sum


def euler1(num_iter):
    eulerNumber = 1
    factor = 1
    for i in range(1, num_iter+1):
        factor *= i
        eulerNumber += 1/factor
    return eulerNumber


print(euler())
print(factorial(0))
print(euler1(2000000))