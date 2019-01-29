class Node:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None


class BinarySearchTree:
    def __init__(self):
        self.root = None
        self.counter = 0

    def insert(self, value):
        newNode = Node(value)
        if self.root is None:
            self.root = newNode
        else:
            if value < self.root.value:
                self.insertLeft(newNode, value)
            else:
                self.insertRight(newNode, value)

    def insertLeft(self, node, value):
        if value < self.root.value:
            if self.root.left is None:
                self.root.left = node
            else:
                current = self.root.left
                while True:
                    if value < current.value:
                        if current.left is None:
                            current.left = node
                            break
                        current = current.left
                    else:  # value >= current.value
                        if current.right is None:
                            current.right = node
                            break
                        current = current.right

    def insertRight(self,node, value):
        if self.root.right is None:
            self.root.right = node
        else:
            current = self.root.right
            while True:
                if value >= current.value:
                    if current.right is None:
                        current.right = node
                        break
                    current = current.right
                else:  # value >= current.value
                    if current.left is None:
                        current.left = node
                        break
                    current = current.left

    def fromArray(self, array):
        for i in range(len(array)):
            self.insert(array[i])

    def search(self, value):
        current = self.root
        self.counter = 1
        while value != current.value:
            self.counter += 1
            if value < current.value:
                current = current.left
                if current is None:
                    self.counter -= 1
                    return False
            else:
                current = current.right
                if current is None:
                    self.counter -= 1
                    return False
        return True

    def min(self):
        current = self.root
        self.counter = 1
        while current.left is not None:
            self.counter += 1
            current = current.left
        return current.value

    def max(self):
        current = self.root
        self.counter = 1
        while current.right is not None:
            self.counter += 1
            current = current.right
        return current.value

    def visitedNodes(self):
        return self.counter

