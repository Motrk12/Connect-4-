package ui;

import com.connect4.mavenproject10.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import oop.project.*;

public class MainApp extends Application {

    private Stage stage;
    private UserManager userManager = new UserManager();
    private User loggedUser;

    private Game game;
    private Board board;
    private GridPane grid;

    private Player player1;
    private Player player2;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Connect 4");
        showLogin();
        stage.show();
    }

    // ================= LOGIN / SIGN UP =================
    private void showLogin() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Connect 4");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label msg = new Label();

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");

        loginBtn.setOnAction(e -> {
            User u = userManager.login(username.getText(), password.getText());
            if (u != null) {
                loggedUser = u;
                showMode();
            } else {
                msg.setText("Invalid username or password");
            }
        });

        signupBtn.setOnAction(e -> {
            User u = userManager.signup(username.getText(), password.getText());
            if (u != null) {
                loggedUser = u;
                showMode();
            } else {
                msg.setText("User already exists");
            }
        });

        root.getChildren().addAll(title, username, password, loginBtn, signupBtn, msg);
        stage.setScene(new Scene(root, 320, 300));
    }

    // ================= MODE SELECTION =================
    private void showMode() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label label = new Label("Select Mode");

        Button pvp = new Button("Player vs Player");
        Button pve = new Button("Player vs AI");

        pvp.setOnAction(e -> showSecondPlayerLogin());
        pve.setOnAction(e -> showAIDifficultyChoice());

        root.getChildren().addAll(label, pvp, pve);
        stage.setScene(new Scene(root, 300, 250));
    }

    // ================= SECOND PLAYER LOGIN =================
    private void showSecondPlayerLogin() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Second Player Login / Sign Up");

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Label msg = new Label();

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");

        loginBtn.setOnAction(e -> {
            User u = userManager.login(username.getText(), password.getText());
            if (u != null) startGame(false, u);
            else msg.setText("Invalid username or password");
        });

        signupBtn.setOnAction(e -> {
            User u = userManager.signup(username.getText(), password.getText());
            if (u != null) startGame(false, u);
            else msg.setText("User already exists");
        });

        root.getChildren().addAll(title, username, password, loginBtn, signupBtn, msg);
        stage.setScene(new Scene(root, 320, 300));
    }

    // ================= AI DIFFICULTY SELECTION =================
    private void showAIDifficultyChoice() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label label = new Label("Select AI Difficulty");

        Button easyBtn = new Button("Easy");
        Button hardBtn = new Button("Hard");

        easyBtn.setOnAction(e -> startGame(true, new EasyStrategy('O')));
        hardBtn.setOnAction(e -> startGame(true, new HardStrategy('O')));

        root.getChildren().addAll(label, easyBtn, hardBtn);
        stage.setScene(new Scene(root, 300, 250));
    }

    // ================= START GAME =================
    private void startGame(boolean vsAI, Object aiOrSecondPlayer) {
        player1 = new HumanPlayer(loggedUser, 'X');

        if (vsAI) {
            player2 = new AIPlayer((MoveStrategy) aiOrSecondPlayer, 'O');
        } else {
            player2 = new HumanPlayer((User) aiOrSecondPlayer, 'O');
        }

        game = new Game(player1, player2);
        board = game.getBoard();

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        drawBoard();
        stage.setScene(new Scene(grid, 520, 450));
    }

    // ================= DRAW BOARD =================
    private void drawBoard() {
        grid.getChildren().clear();

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Circle cell = new Circle(30);
                cell.setFill(Color.LIGHTGRAY);

                int col = c;
                cell.setOnMouseClicked(e -> handleMove(col));

                grid.add(cell, c, r);
            }
        }
    }

    // ================= HANDLE MOVE =================
    private void handleMove(int col) {
        Player current = game.getCurrentPlayer();
        int row = board.dropPiece(col, current.getSymbol());
        if (row == -1) return;

        updateBoard();

        if (board.checkWin(current.getSymbol())) {

            String winnerName = "Player";
            if (current instanceof HumanPlayer) {
                winnerName = ((HumanPlayer) current).getUser().getUsername();
                userManager.updateScore(((HumanPlayer) current).getUser(), 1);
            } else {
                winnerName = "AI";
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Over");
            alert.setContentText("Winner: " + winnerName + " (" + current.getSymbol() + ")");
            alert.showAndWait();

            showMode();
            return;
        }

        if (board.isFull()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Over");
            alert.setContentText("Draw!");
            alert.showAndWait();
            showMode();
            return;
        }

        game.switchPlayer();

        // AI auto move
        if (game.getCurrentPlayer() instanceof AIPlayer) {
            AIPlayer ai = (AIPlayer) game.getCurrentPlayer();
            int aiCol = ai.chooseColumn(board);
            handleMove(aiCol);
        }
    }

    private void updateBoard() {
        char[][] g = board.getGrid();

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Circle cell = (Circle) grid.getChildren()
                        .get(r * board.getCols() + c);

                if (g[r][c] == 'X') cell.setFill(Color.RED);
                else if (g[r][c] == 'O') cell.setFill(Color.YELLOW);
                else cell.setFill(Color.LIGHTGRAY);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
