package oop.project;

public class AIPlayer extends Player {
    private final MoveStrategy strategy;

    public AIPlayer(MoveStrategy strategy, char symbol) {
        super(symbol);
        this.strategy = strategy;
    }

    @Override
    public int chooseColumn(Board board) {
        return strategy.getMove(board);
    }
}
