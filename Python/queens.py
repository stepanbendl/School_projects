
dim = 4

board = [0, 0, 0, 0]


def allowedToPlace(row, col):
        if row == col:
            return False


def placeQueen(row):
    for col in range(dim):
        if allowedToPlace(row, col):
            board[row] = col
            if row < (dim - 1):
                placeQueen(row + 1)


def printBoard():
    for row in board:
        print()
        for j in range(dim):
            if board[row] == j:
                print("Q", end=",")
            else:
                print("_", end=",")



printBoard()
placeQueen(0)