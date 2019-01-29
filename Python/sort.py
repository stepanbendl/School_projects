def sortNumbers(weights, condition):
    for k in range(len(weights) - 1):
        for i in range(len(weights) - k - 1):
            if weights[i] > weights[i + 1]:
                weights[i], weights[i + 1] = weights[i + 1], weights[i]
    if condition == "DESC":
        weights.reverse()
        return weights
    elif condition == "ASC":
        return weights


def sortData(weights, data, condition):
    if len(weights) != len(data):
        raise ValueError("Invalid input data")
    #result = weights[:]
    #copy = data[:]
    if condition == "ASC":
        for k in range(len(weights) - 1):
            for i in range(len(weights) - k - 1):
                if weights[i] > weights[i + 1]:
                    weights[i], weights[i + 1] = weights[i + 1], weights[i]
                    data[i], data[i + 1] = data[i + 1], data[i]
        return data
    if condition == "DESC":
        for h in range(len(weights) - 1):
            for l in range(len(weights) - h - 1):
                if weights[l] < weights[l + 1]:
                    weights[l], weights[l + 1] = weights[l + 1], weights[l]
                    data[l], data[l + 1] = data[l + 1], data[l]
        return data
