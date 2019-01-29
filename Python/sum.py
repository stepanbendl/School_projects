def suma(min, max):
    result = min
    while min < max:
        min += 1
        result += min
    return result


def suma2(min, max):
    result = 0
    for i in range(min,max+1):
        result += i
    return result


def suma3(min, max):
    soucet = ((min + max) * (1+max-min))/2
    return soucet


print(suma(3, 6))
print(suma2(3, 6))
print(suma3(3, 6))
