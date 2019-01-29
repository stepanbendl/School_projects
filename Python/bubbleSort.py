data = [542,256,11,258,99,156,260,453,51,156,252,1059,0]

def bubbleSort(list):
    for k in range(len(list)-1):
        for i in range(len(list)-k-1):
            if list[i] > list[i+1]:
                list[i], list[i+1] = list[i+1], list[i]
    return list


print(bubbleSort(data))
