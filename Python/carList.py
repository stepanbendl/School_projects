cars = ["Ford","Audi", "Alfa Romeo", "Skoda", "Toyota"]


def printCars(list):
    x = len(list) -1
    y = 0
    while y < x+1:
        print("Znacka " + list[y])
        y += 1


def inList(list, value):
    for i in range(len(list)):
        if value in list:
            return True
        else:
            return False


def inList1(list, value):
    return value in list


def listAdd(list, value):
    if value not in list:
        list.append(value)


def listDel(list,value):
    if value in list:
        list.remove(value)
    return list



#def inList2(list,value):
 #   return list.index(value)


print(listDel(cars,"Skoda"))