package com.connect4.connectfourgamenew;

import com.connect4.mavenproject10.User;
import com.connect4.mavenproject10.UserManager;
import oop.project.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectFourGameNew {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserManager userManager = new UserManager();

    public static void main(String[] args) {
        try {
            System.out.println("=== Welcome to Connect Four ===");
            User player1User = handleUserLoginSignup("Player 1");
            showGameModeMenu(player1User);
        } catch (Exception e) {
            System.err.println("\n--- CRITICAL SYSTEM ERROR ---");
            e.printStackTrace();
        } finally {
            userManager.saveUsers();
            System.out.println("\nApplication terminated. User data saved.");
        }
    }

    private static User handleUserLoginSignup(String playerLabel) {
        User playerUser = null;
        while (playerUser == null) {
            System.out.println("\n--- " + playerLabel + " Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Play as Guest");
            System.out.print("Choice: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 3) {
                    System.out.println("Starting as Guest.");
                    playerUser = new User("Guest", "", 0);
                    break;
                }
                if (choice != 1 && choice != 2) {
                    System.out.println("Invalid menu choice.");
                    continue;
                }
                System.out.print("Username: ");
                String user = scanner.nextLine();
                System.out.print("Password: ");
                String pass = scanner.nextLine();
                if (choice == 1) {
                    playerUser = userManager.login(user, pass);
                    if (playerUser == null) System.out.println("Login failed. Try again.");
                } else {
                    playerUser = userManager.signup(user, pass);
                    if (playerUser == null) System.out.println("Signup failed: Username may already exist.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return playerUser;
    }

    private static void showGameModeMenu(User player1User) {
        System.out.println("\n--- Game Mode Menu ---");
        System.out.println("1. Human vs. Human");
        System.out.println("2. Human vs. AI (Easy)");
        System.out.println("3. Human vs. AI (Hard)");
        System.out.print("Choose mode (1, 2, or 3): ");

        try {
            int modeChoice = scanner.nextInt();
            scanner.nextLine();

            Player player1 = new HumanPlayer(player1User, 'X');
            Player player2;

            switch (modeChoice) {
                case 1 -> {
                    // Second human player login/signup
                    User player2User = handleUserLoginSignup("Player 2");
                    player2 = new HumanPlayer(player2User, 'O');
                    System.out.println("Mode: Human vs. Human");
                }
                case 2 -> {
                    player2 = new AIPlayer(new EasyStrategy('O'), 'O');
                    System.out.println("Mode: Human vs. AI (Easy)");
                }
                case 3 -> {
                    player2 = new AIPlayer(new HardStrategy('O'), 'O');
                    System.out.println("Mode: Human vs. AI (Hard)");
                }
                default -> {
                    System.out.println("Invalid mode choice.");
                    return;
                }
            }

            Game game = new Game(player1, player2);
            game.startGame();

            // After game ends, update scores for human players
            if (player1 instanceof HumanPlayer) {
                userManager.updateScore(((HumanPlayer) player1).getUser(), ((HumanPlayer) player1).getUser().getScore());
            }
            if (player2 instanceof HumanPlayer) {
                userManager.updateScore(((HumanPlayer) player2).getUser(), ((HumanPlayer) player2).getUser().getScore());
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
    }
}
