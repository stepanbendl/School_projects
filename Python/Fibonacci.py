def fibonacci(x):
    list = [1, 1]
    z = 2
    if x == 1:
        return [1]
    elif x == 2:
        return [1, 1]
    elif x >= 3:
        while z < x:
            list.append(list[z-2] + list[z-1])
            z += 1
        return list


print(fibonacci(1))