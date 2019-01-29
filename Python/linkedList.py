class Node:
    def __init__(self, data, next):
        self.data = data
        self.next = next


class Queue:
    def __init__(self):
        self.head = None

    def pushNode(self,item):
        if self.head == None:
            self.head = Node(item, None)
        else:
            current = self.head
            while current.next != None:
                current = current.next
            current.next = Node(item, None)


    def popNode(self):
        if self.head == None: return None
        current = self.head
        self.head = self.head.next
        return current.data


class Car:
    def __init__(self, brand, rep):
        self.brand = brand
        self.rep = rep


    def __str__(self):
        return self.brand + " " + str(self.rep)



linkedList = Queue()

linkedList.pushNode(Car("Audi", 848500))
linkedList.pushNode(Car("BMW", 951500))
linkedList.pushNode(Car("Mercedes", 1951500))
print(linkedList.popNode())
print(linkedList.popNode())
print(linkedList.popNode())
print(linkedList.popNode())
