package oop.project;

public class Board {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private final char[][] grid;

    public Board() {
        grid = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    public int getRows() { return ROWS; }
    public int getCols() { return COLS; }

    public int dropPiece(int col, char symbol) {
        if (col < 0 || col >= COLS || isColumnFull(col)) {
            return -1;
        }
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == ' ') {
                grid[row][col] = symbol;
                return row;
            }
        }
        return -1;
    }

    public boolean isColumnFull(int col) {
        return grid[0][col] != ' ';
    }

    public boolean isFull() {
        for (int j = 0; j < COLS; j++) {
            if (!isColumnFull(j)) return false;
        }
        return true;
    }

    public boolean checkWin(char symbol) {
        // horizontal
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (grid[r][c] == symbol && grid[r][c+1] == symbol &&
                    grid[r][c+2] == symbol && grid[r][c+3] == symbol) {
                    return true;
                }
            }
        }
        // vertical
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r <= ROWS - 4; r++) {
                if (grid[r][c] == symbol && grid[r+1][c] == symbol &&
                    grid[r+2][c] == symbol && grid[r+3][c] == symbol) {
                    return true;
                }
            }
        }
        // diagonal down-right
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = 0; c <= COLS - 4; c++) {
                if (grid[r][c] == symbol && grid[r+1][c+1] == symbol &&
                    grid[r+2][c+2] == symbol && grid[r+3][c+3] == symbol) {
                    return true;
                }
            }
        }
        // diagonal down-left
        for (int r = 0; r <= ROWS - 4; r++) {
            for (int c = COLS - 1; c >= 3; c--) {
                if (grid[r][c] == symbol && grid[r+1][c-1] == symbol &&
                    grid[r+2][c-2] == symbol && grid[r+3][c-3] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    public void display() {
        System.out.println("\n 1 2 3 4 5 6 7");
        for (int i = 0; i < ROWS; i++) {
            System.out.print("|");
            for (int j = 0; j < COLS; j++) {
                System.out.print(grid[i][j] + "|");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    public char[][] getGrid() {
        char[][] gridCopy = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(this.grid[i], 0, gridCopy[i], 0, COLS);
        }
        return gridCopy;
    }

    public void reset() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = ' ';
            }
        }
    }
}
