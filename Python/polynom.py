def polyEval(poly, x):
    result = poly[0]
    for i in range(1, len(poly)):
        result += poly[i]*(x**i)
    return result


def polySum(poly1, poly2):
    result = []
    difference = len(poly1) - len(poly2)
    if difference < 0:
        difference *=-1
        for k in range (difference):
            poly1.append(0)
    if difference > 0:
        for j in range(difference):
            poly2.append(0)
    for i in range (len(poly1)):
        result.append(poly1[i] + poly2[i])
    for h in range(len(result)):
        length = len(result) - 1
        if result[length] == 0:
            del result[length]
    return result


def polyMultiply(poly1, poly2):
    result = []
    resultLength = len(poly1) + len(poly2) - 1
    for i in range(resultLength):
        result.append(0)
    for k in range(0, len(poly1)):
        for j in range(0, len(poly2)):
            result[j+k] += poly1[k] * poly2[j]
    return result
