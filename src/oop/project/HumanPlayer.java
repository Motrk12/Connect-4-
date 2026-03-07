package oop.project;

import com.connect4.mavenproject10.User;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private final User user;
    private static final Scanner scanner = new Scanner(System.in);

    public HumanPlayer(User user, char symbol) {
        super(symbol);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int chooseColumn(Board board) {
        // GUI clicks handled in MainApp
        return -1;
    }
}
