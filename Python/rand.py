import random

def createRandList(len):
    list = []
    for i in range (len):
        list.append(random.randint(1, 1000))
    return list


def findMax(list):
    max = list[0]
    for v in list[1:]:
        if v > max:
            max = v
    return max


def find2ndMax(list):
    list.remove(findMax(list))
    return findMax(list)


lst = createRandList(10)
print(lst)
print(findMax(lst))

print(find2ndMax(lst))