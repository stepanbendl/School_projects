lst = [15,	19,	10,	7,	17, 16]


def fixNode(index, array, maxIndex):
    leftChildIndex = 2 * index + 1
    rightChildIndex = 2 * index + 2

    for childToSwapIndex in [leftChildIndex, rightChildIndex]:
        if childToSwapIndex < maxIndex:
            childToSwap = array[childToSwapIndex]
            if childToSwap > array[index]:
                array[index], array[childToSwapIndex] = array[childToSwapIndex], array[index]
                fixNode(childToSwapIndex, array, maxIndex)


def heapify(array):
    for index in range((len(array) // 2)-1, -1, -1): #posledni rodic, do konce(hodnoty uplne nahore0, obracene
        fixNode(index, array, len(array)-1)


def heapSort(array):
        heapify(array)
        for i in range(len(array) - 1):
            maxIndex = len(array)- i - 1
            array[0], array[maxIndex] = array[maxIndex], array[0]
            fixNode(0,array,maxIndex)


heapSort(lst)
print(lst)




