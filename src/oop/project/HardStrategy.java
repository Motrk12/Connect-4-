package oop.project;

import java.util.Random;

public class HardStrategy implements MoveStrategy {

    private final char aiSymbol;
    private final char opponentSymbol;
    private final Random rand = new Random();

    public HardStrategy(char aiSymbol) {
        this.aiSymbol = aiSymbol;
        this.opponentSymbol = (aiSymbol == 'X') ? 'O' : 'X';
    }

    @Override
    public int getMove(Board board) {
        // 1. Winning move
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.isColumnFull(col) && wouldWin(board, col, aiSymbol)) return col;
        }

        // 2. Block opponent
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.isColumnFull(col) && wouldWin(board, col, opponentSymbol)) return col;
        }

        // 3. Center
        int center = board.getCols() / 2;
        if (!board.isColumnFull(center)) return center;

        // 4. Random
        int col;
        do {
            col = rand.nextInt(board.getCols());
        } while (board.isColumnFull(col));
        return col;
    }

    private boolean wouldWin(Board original, int col, char symbol) {
        Board temp = copyBoard(original);
        temp.dropPiece(col, symbol);
        return temp.checkWin(symbol);
    }

    private Board copyBoard(Board b) {
        Board copy = new Board();
        char[][] oldGrid = b.getGrid();
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getCols(); j++) {
                if (oldGrid[i][j] != ' ') copy.dropPiece(j, oldGrid[i][j]);
            }
        }
        return copy;
    }
}
