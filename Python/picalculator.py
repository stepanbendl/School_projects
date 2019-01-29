import math
def newtonPi(init):
    xk = init
    a = init
    xk1 = 0
    while True:
        xk1 = xk - (math.sin(xk)/math.cos(xk))
        if xk == xk1:
            return xk1
        else:
            xk = xk1
            a = xk1

def leibnizPi(iterations):
    divisor = 1
    count=1
    if iterations == 1:
        return float(4)
    else:
        overall = 4
        while count < iterations:
            divisor+=2
            if count % 2 == 1:
                overall += (4/-divisor)
                count +=1
            else:
                overall += (4/divisor)
                count +=1
        return overall


def nilakanthaPi(iterations):
    divisor = 2
    if iterations == 1:
        return float(3)
    else:
        overall = 3
        count = 2
        while (iterations + 1) > count:
            divisor += 2
            if count % 2 == 0:
                overall += 4/(divisor*(divisor-1)*(divisor-2))
                count += 1
            else:
                overall += -4/(divisor*(divisor-1)*(divisor-2))
                count += 1
        return overall

print(newtonPi(-3))
print(math.pi)