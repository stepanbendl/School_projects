class Node:
    def __init__(self, nextNode=None, prevNode=None, data=None):
        self.data = data
        self.nextNode = nextNode
        self.prevNode = prevNode


class LinkedList:
    def __init__(self):
        self.head = None


class Car:
    def __init__(self, identification, name, brand, price, active):
        self.identification = identification
        self.name = name
        self.brand = brand
        self.price = price
        self.active = active


db = LinkedList()


def init(cars):
    for i in range(len(cars)):
        add(cars[i])


def add(car):
    reference = db.head
    newNode = Node(None, None, car)
    if newNode.data is None:
        return None
    if db.head is None:
        db.head = newNode
    elif newNode.data.price < db.head.data.price:
        db.head = newNode
        newNode.nextNode = reference
        reference.prevNode = newNode
        newNode.prevNode = None
    else:
        while reference.nextNode is not None and car.price >= reference.nextNode.data.price:
            reference = reference.nextNode
        newNode.nextNode = reference.nextNode
        ref = reference.nextNode
        if reference.nextNode is not None:
            ref.prevNode = newNode
        newNode.prevNode = reference
        reference.nextNode = newNode


def updateName(identification, name):
    reference = db.head
    while reference is not None:
        if reference.data.identification == identification:
            reference.data.name = name
            break
        else:
            reference = reference.nextNode
    if reference is None:
        return None


def updateBrand(identification, brand):
    reference = db.head
    while reference:
        if reference.data.identification == identification:
            reference.data.brand = brand
            break
        else:
            reference = reference.nextNode
    if reference is None:
        return None


def activateCar(identification):
    reference = db.head
    while reference:
        if reference.data.identification == identification:
            reference.data.active = True
            break
        else:
            reference = reference.nextNode
    if reference is None:
        return None


def deactivateCar(identification):
    reference = db.head
    while reference:
        if reference.data.identification == identification:
            reference.data.active = False
            break
        else:
            reference = reference.nextNode
    if reference is None:
        return None


def getDatabaseHead():
    return db.head


def getDatabase():
    return db


def calculateCarPrice():
    reference = db.head
    overallPrice = 0
    while True:
        if reference.data.active:
            overallPrice += reference.data.price
        if reference.nextNode is None:
            return overallPrice
        reference = reference.nextNode


def clean():
    db.head = None
    db.nextNode = None
    db.prevNode = None


"""
car1 = Car(1, "Octavia", "Skoda", 123000, True)
car2 = Car(23, "Felicia", "Skoda", 5000, True)
car3 = Car(11, "Superb", "Skoda", 54000, True)
seznam = [car1,car2,car3]
init(seznam)
updateName(23, "Octavia2")
updateName(1, "Octavia2015")
print(db.head.data.name)
print(db.head.nextNode.nextNode.data.name)
"""