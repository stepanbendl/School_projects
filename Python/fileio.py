
# 1.
outFile = open('input.txt','w')
outFile.write('hey')
outFile.close()

# 2.
with open('output2.txt','w') as outFile:
    outFile.Write('Ahoj')

# 3.
inFile = open('output.txt', 'r')
for line in inFile:
        print(line, end="")