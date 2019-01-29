class Car:
    def __init__(self, brand, rep):
        self.brand = brand
        self.rep = rep


    #def printClass(self):
    #   print(self.brand, self.rep)


    def __str__(self):
        return self.brand + " " + str(self.rep)


    def newBrand (self, newBrand):
        self.brand = newBrand



car = Car('BMW', 1)
print(car)