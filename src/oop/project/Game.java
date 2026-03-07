package oop.project;

import com.connect4.mavenproject10.User;

public class Game {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.board = new Board();
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    // Update in-memory score (GUI will call UserManager.updateScore)
    public void updateScore(Player winner) {
        if (winner instanceof HumanPlayer) {
            HumanPlayer human = (HumanPlayer) winner;
            User user = human.getUser();
            if (user != null) {
                user.addScore(1);
            }
        }
    }
    // In Game.java (console mode compatible)
public void startGame() {
    board.reset();
    currentPlayer = player1;

    board.display();

    while (true) {
        int col = currentPlayer.chooseColumn(board); // Human or AI
        int row = board.dropPiece(col, currentPlayer.getSymbol());
        if (row == -1) continue; // column full

        board.display();

        if (board.checkWin(currentPlayer.getSymbol())) {
            System.out.println("\nWinner: " + getPlayerName(currentPlayer));
            updateScore(currentPlayer);
            break;
        }

        if (board.isFull()) {
            System.out.println("\nIt's a draw!");
            break;
        }

        switchPlayer();
    }
}

// Helper to get name
private String getPlayerName(Player player) {
    if (player instanceof HumanPlayer) {
        return ((HumanPlayer) player).getUser().getUsername();
    }
    return "AI";
}

}
