def writeTextToFile(zadany_text):
    STATICKÝ_TEXT = "This is my static text which must be added to file." \
                    " It is very long text and I do not know what they want to do with this terrible text."
    finalni_text = STATICKÝ_TEXT + " " + str(zadany_text)
    outFile = open('input.txt', 'w')
    outFile.write(finalni_text)
    outFile.close()
    return (input)


writeTextToFile("Ahoj")
