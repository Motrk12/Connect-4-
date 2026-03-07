package oop.project;

import java.util.Random;

public class EasyStrategy implements MoveStrategy {
    private final char aiSymbol;
    private final Random rand = new Random();

    public EasyStrategy(char aiSymbol) {
        this.aiSymbol = aiSymbol;
    }

    @Override
    public int getMove(Board board) {
        int col;
        do {
            col = rand.nextInt(board.getCols());
        } while (board.isColumnFull(col));
        return col;
    }
}
