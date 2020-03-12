import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Connect4 extends BorderPane {
    // PROPERTIES TO CONTROL THE COLOR OF THE PIECES
    private StringProperty settingsColor1Prop = new SimpleStringProperty();
    private StringProperty settingsColor2Prop = new SimpleStringProperty();

    // PROPERTY TO CONTROL PLAY AGAIN
    private BooleanProperty PlayAgainProp = new SimpleBooleanProperty(false);

    // CONSTRUCTOR
    Connect4(StringProperty settingsColor1Prop, StringProperty settingsColor2Prop) {
        this.settingsColor1Prop = settingsColor1Prop;
        this.settingsColor2Prop = settingsColor2Prop;
        playGame();
    }

    // Track whose turn it is
    private boolean Player1 = true;
    // Create an array to hold all the game pieces
    private GamePiece[][] piece = new GamePiece[7][6];
    // Create a pane for the game
    private Pane gamePane = new Pane();
    // Colors
//    private Color color1 = Color.web(String.valueOf(settingsColor1Prop.getValue()).substring(2));
//    private Color color2 = Color.web(String.valueOf(settingsColor2Prop.getValue()).substring(2));

    private List<Point2D> fourVertically;
    private List<Point2D> fourHorizontal;
    private List<Point2D> diagFromLeft;
    private List<Point2D> diagFromRight;

    private boolean win4Vertcally = false;
    private boolean win4Horizontally = false;
    private boolean win4Diagfromleft = false;
    private boolean win4DiagfromRight = false;

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void playGame() {
        Pane playGamePane = new Pane();
        playGamePane.setPadding(new Insets(10,10,10,10));
        playGamePane.getChildren().add(gamePane);
        Shape gridShape = makeGameBoard();
        playGamePane.getChildren().add(gridShape);
        playGamePane.getChildren().addAll(createColumns());
        setCenter(playGamePane);
        setAlignment(playGamePane, Pos.CENTER);
    }

    // Create the Board for the game
    private Shape makeGameBoard() {
        // create a shape for the background
        Shape gameBoard = new Rectangle(640, 560);

        // Add circles
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                Circle circle = new Circle(40);
                circle.setCenterX(40);
                circle.setCenterY(40);
                circle.setTranslateX(j * 85 + 20);
                circle.setTranslateY(i * 85 + 20);
                gameBoard = Shape.subtract(gameBoard, circle);
            }
        }
        gameBoard.setFill(Color.BLUE);
        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(20);
        openingLight.setAzimuth(10);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(3);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);
        gameBoard.setEffect(openingLighting);

        return gameBoard;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    // Create the columns for the game
    private List<Rectangle> createColumns() {
        List<Rectangle> columnList = new ArrayList<>();

        // create 6 columns
        for (int COL = 0; COL < 7; COL++) {
            Rectangle columnRectangle = new Rectangle(80, 560);
            columnRectangle.setTranslateX(COL * 85 + 20);
            columnRectangle.setFill(Color.TRANSPARENT);

            final int column = COL;

            // listener to put a game piece in a column if the column is clicked on
            columnRectangle.setOnMouseClicked(e -> playGamePiece(new GamePiece(Player1), column));

            columnList.add(columnRectangle);
        }
        return columnList;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    // this essentially plays the game
    private void playGamePiece(GamePiece playerPiece, int column) {
        // Check each row to see if there is a piece that has been placed in it
        int row = 5;
        while (row >= 0) {
            if (!getGamePiece(column, row).isPresent()) // If no piece is present break
                break;
            row--; // if a piece is there check next row up
        }

        if (row < 0) return;    // if all the rows are filled, don't do anything

        // If there isn't a piece in the chosen spot create one and add to board
        piece[column][row] = playerPiece; // add piece to my 2d array of pieces
        gamePane.getChildren().add(playerPiece);
        playerPiece.setTranslateX(column * 85 + 20);

        final int currentRow = row;

        // animate piece and check to see if there are 4 pieces in a row, if so end game else continue playing
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), playerPiece);
        animation.setToY(row * 85 + 20);
        animation.setOnFinished(e -> {
            if (gameEnded(column, currentRow)) {
                String winner1 = gameOver();

                // flash pieces and display name of winner
                if (winner1.equals("Player1")) {
                    if (win4Vertcally) Player1Wins(fourVertically);
                    if (win4Horizontally) Player1Wins(fourHorizontal);
                    if (win4Diagfromleft) Player1Wins(diagFromLeft);
                    if (win4DiagfromRight) Player1Wins(diagFromRight);
                } else {
                    if (win4Vertcally) Player2Wins(fourVertically);
                    if (win4Horizontally) Player2Wins(fourHorizontal);
                    if (win4Diagfromleft) Player2Wins(diagFromLeft);
                    if (win4DiagfromRight) Player2Wins(diagFromRight);
                }
            }
            Player1 = !Player1;
        });
        animation.play();

        // check to see if the board is full
        TiedGame();
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private boolean gameEnded(int column, int row) {
        // Create a list of 4 points that are vertical
        fourVertically = IntStream.rangeClosed(row - 3, row + 3).mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        // create a list of 4 points that are horizontal
        fourHorizontal = IntStream.rangeClosed(column - 3, column + 3).mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        // create a list of 4 points that are diagonal from left to right
        Point2D topLeft = new Point2D(column - 3, row - 3);
        diagFromLeft = IntStream.rangeClosed(0, 6).mapToObj(i -> topLeft.add(i, i)).collect(Collectors.toList());

        // create a lit of points that are diagonal from right to left
        Point2D bottomLeft = new Point2D(column - 3, row + 3);
        diagFromRight = IntStream.rangeClosed(0, 6).mapToObj(i -> bottomLeft.add(i, -i)).collect(Collectors.toList());

        if (checkForWinner(fourVertically)) {
            win4Vertcally = true;
            return true;
        } else if (checkForWinner(fourHorizontal)) {
            win4Horizontally = true;
            return true;
        } else if (checkForWinner(diagFromLeft)) {
            win4Diagfromleft = true;
            return true;
        } else if (checkForWinner(diagFromRight)) {
            win4DiagfromRight = true;
            return true;
        } else return false;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private boolean checkForWinner(List<Point2D> spots) {
        // count for 4 in a row
        int fourInRow = 0;

        for (Point2D p : spots) {
            int column = (int) p.getX();
            int row = (int) p.getY();
            GamePiece playerPiece = getGamePiece(column, row).orElse(new GamePiece(!Player1));

            if (playerPiece.playerOne == Player1) {
                fourInRow++;
                if (fourInRow == 4) {
                    return true;
                }
            } else {
                fourInRow = 0;
            }
        }
        return false;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private String gameOver() {
        String winner = (Player1 ? "Player1" : "Player2");
        // report no winner
        System.out.println("Winner: " + (Player1 ? "RED" : "YELLOW")); // +++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        gameOverFlag = true;
        return winner;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private Optional<GamePiece> getGamePiece(int column, int row) {
        if (column < 0 || column >= 7 || row < 0 || row >= 6) {
            return Optional.empty();
        }
        return Optional.ofNullable(piece[column][row]);
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private /*static*/ class GamePiece extends Circle {
        private final boolean playerOne;

        private GamePiece(boolean playerOne) {
            // override super class Circle, needs a radius and a fill color
            super(40, playerOne ? Color.web(String.valueOf(settingsColor1Prop.getValue()).substring(2))
                    : Color.web(String.valueOf(settingsColor2Prop.getValue()).substring(2)));
            this.playerOne = playerOne;
            setCenterX(40);
            setCenterY(40);
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void PieceFlash(GamePiece playerPiece, Color color1, Color color2) {
        FillTransition animationRedFlash = new FillTransition(Duration.millis(500), playerPiece, color1, color2);
        animationRedFlash.setCycleCount(100);
        animationRedFlash.setAutoReverse(true);
        animationRedFlash.play();
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void Player1Wins(List<Point2D> playerPieces) {
        // Create playerPieces from List
        for (Point2D p : playerPieces) {
            int column = (int) p.getX();
            int row = (int) p.getY();
            GamePiece playerPiece = getGamePiece(column, row).orElse(new GamePiece(!Player1));

            // Flash red pieces
            PieceFlash(playerPiece, Color.web(String.valueOf(settingsColor1Prop.getValue()).substring(2)),Color.GOLD);
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void Player2Wins(List<Point2D> playerPieces) {
        // Create playerPieces from List
        for (Point2D p : playerPieces) {
            int column = (int) p.getX();
            int row = (int) p.getY();
            GamePiece playerPiece = getGamePiece(column, row).orElse(new GamePiece(!Player1));

            // Flash red pieces
            PieceFlash(playerPiece, Color.web(String.valueOf(settingsColor2Prop.getValue()).substring(2)),Color.GOLD);
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private void TiedGame() {
        boolean openSpot = false;
        for (int col = 0; col < 7; col++) { // 7 columns
            for (int row = 0; row < 6; row++) { // 6 rows
                if (piece[col][row] == null) {
                    openSpot = true;
                }
            }
        }
        if (!openSpot) {
            // Game is a Tie
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

}

// --------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------

//                while(gameEndedFlag){
//                // Create a button to play again???
//                    Button playAgain = new Button("Play Again!");
//                    playAgain.setFont(new Font("Serif",30));
//                    playAgain.setStyle("-fx-background-color: #ecebe9");        // change the color ------------------------------------------------------------------
//                    playAgain.setStyle("-fx-background-insets: 0,9 9 8 9,9,10,11");
//                    playAgain.setStyle("-fx-background-radius: 50;\n" +
//                            "    -fx-padding: 15 30 15 30;\n" +
//                            "-fx-font-size: 18px;\n" + "-fx-text-fill: #311c09;\n" +
//                            "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.1) , 2, 0.0 , 0 , 1);");

//                    rightPane.getChildren().add(playAgain);
//
//                    // add a loop that only listens to the button input
////                    playAgain.setOnMouseClicked(e->{
//                        // How to I recall main/start

//                            gameEndedFlag = false;
////                    });
//                }
