package sudosolv;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Čumák
 */

class Coord {
  private int row;
  private int col;

  public Coord(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public void setCol(int col) {
    this.col = col;
  }  
}

class Rect {
  private int row1;
  private int col1;
  private int row2;
  private int col2;

  public Rect(int row1, int row2, int col1, int col2) {
    this.row1 = row1;
    this.col1 = col1;
    this.row2 = row2;
    this.col2 = col2;
  }

  public int getRow1() {
    return row1;
  }

  public int getCol1() {
    return col1;
  }

  public int getRow2() {
    return row2;
  }

  public int getCol2() {
    return col2;
  }

  public void setRow1(int row1) {
    this.row1 = row1;
  }

  public void setCol1(int col1) {
    this.col1 = col1;
  }  

  public void setRow2(int row2) {
    this.row2 = row2;
  }

  public void setCol2(int col2) {
    this.col2 = col2;
  }  
}

public class SudoSolver {
  private static final int CELL_COUNT = 81;
  private static final int BOARD_DIM = 9;
  private static final Set<Character> ALL_NUMS = new HashSet<>(
    Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9')
  );
  private char[] board;
  
  public SudoSolver(String board) {
    this.board = board.toCharArray();
  }
  
  private char getCellValue(int row, int col) {
    return board[row * BOARD_DIM + col];
  }
 
  private void setCellValue(int row, int col, char value) {
    board[row * BOARD_DIM + col] = value;
  } 
 
  private void delCellValue(int row, int col) {
    board[row * BOARD_DIM + col] = '0';
  }

  private Set<Character> getNumsInRow(int row) {
    Set<Character> nums = new HashSet<>();
    for (int col = 0; col < BOARD_DIM; ++col) {
      char num = getCellValue(row, col);
      if (num > '0') {
        nums.add(num);
      }
    }
    return nums;
  }
  
  private Set<Character> getNumsInCol(int col) {
    Set<Character> nums = new HashSet<>();
    for (int row = 0; row < BOARD_DIM; ++row) {
      char num = getCellValue(row, col);
      if (num > '0') {
        nums.add(num);
      }
    }
    return nums;
  }

  private Rect getRectRange(int row, int col) {
    int rowMin = 3 * (row / 3);
    int rowMax = rowMin + 2;
    int colMin = 3 * (col / 3);
    int colMax = colMin + 2;
    return new Rect(rowMin, rowMax, colMin, colMax);
  }
    
  private Set<Character> getNumsInRect(int row, int col) {
    Set<Character> nums = new HashSet<>();
    Rect ranges = getRectRange(row, col);
    for (row = ranges.getRow1(); row <= ranges.getRow2(); ++row) {
      for (col = ranges.getCol1(); col <= ranges.getCol2(); ++col) {
        char num = getCellValue(row, col);
        if (num > '0') {
          nums.add(num);
        }
      }
    }
    return nums;
  }

  private Set<Character> getAllowedNums(int row, int col) {
    Set<Character> nums = getNumsInRow(row);
    nums.addAll(getNumsInCol(col));
    nums.addAll(getNumsInRect(row, col));
    Set<Character> missingNums = new HashSet<>(ALL_NUMS);
    missingNums.removeAll(nums);
    return missingNums;
  }
  
  public void printState() {
    System.out.println("+-------+-------+-------+");
    for (int r = 0; r < BOARD_DIM; ++r) {
      System.out.print("| ");
      for (int c = 0; c < BOARD_DIM; ++c) {
        char v = getCellValue(r, c);
        System.out.print(v > '0' ? v : '_');
        System.out.print(' ');
        if ((c + 1) % 3 == 0) {
          System.out.print("| ");
        }
      }
      System.out.println("");
      if ((r + 1) % 3 == 0) {
        System.out.println("+-------+-------+-------+");
      }
    }
  }
  
  private Coord findFirstEmptyCell() {
    for (int r = 0; r < BOARD_DIM; ++r) {
      for (int c = 0; c < BOARD_DIM; ++c) {
        if (getCellValue(r, c) == '0') {
          return new Coord(r, c);
        }
      }
    }
    return new Coord(-1, -1);
  }

  public void solveBrute() {
    Coord empty = findFirstEmptyCell();
    if (empty.getRow() == -1) {
      printState();
    } else {
      int row = empty.getRow();
      int col = empty.getCol();
      for (char candidate : getAllowedNums(row, col)) {
        setCellValue(row, col, candidate);
        solveBrute();
        delCellValue(row, col);
      }
    }
  }
  
  public static void main(String[] args) {
    
    String sudoEasy1 = "914000037000673094073140050309250700700001306480036509042067900500904260096500073";
    // A Sudoku designed to work against the brute force algorithm
    // (https://en.wikipedia.org/wiki/Sudoku_solving_algorithms)
    String againstbf = "000000000000003085001020000000507000004000100090000000500000073002010000000040009"; // cca 1 h 10 min in Python
    
    SudoSolver ss = new SudoSolver(againstbf);
    ss.printState();
    long startTime = System.nanoTime();
    ss.solveBrute();
    long elapsedTime = System.nanoTime() - startTime;
    System.out.println("Execution time in seconds: " + elapsedTime / 1000000000.0);
  }  
}
