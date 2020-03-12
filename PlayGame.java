import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.shape.*;

import javafx.scene.text.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class PlayGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(PlayGame(), Color.web("3333FF")));
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    // --------------------------------------------------------------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------------------------------------------------------------

    // PROPERTIES TO CONTROL THE COLOR OF THE PIECES AND PLAYER NAMES
    private StringProperty settingsColor1Prop = new SimpleStringProperty("RED");
    private StringProperty settingsColor2Prop = new SimpleStringProperty("YELLOW");

    // PROPERTIES TO CONTROL THE PLAYER NAMES
    private StringProperty Player1NameProp = new SimpleStringProperty("PLAYER 1");
    private StringProperty Player2NameProp = new SimpleStringProperty("PLAYER 2");

    // PROPERTY TO CONTROL ACCESS TO GAMESETUP
    private BooleanProperty GameSetupProp = new SimpleBooleanProperty(false);
    private IntegerProperty InitGameSetUpProp = new SimpleIntegerProperty(0);
    private IntegerProperty FinlGameSetUpProp = new SimpleIntegerProperty(1);


    // PROPERTY TO CONTROL IF MUSIC IS PLAYING IN THE BACKGROUND
    private BooleanProperty settingsMusic = new SimpleBooleanProperty(false);

    // PROPERTY TO CONTROL IF GAME IS READY TO BE PLAYED
    private BooleanProperty PlayGameProp = new SimpleBooleanProperty(false);
    private IntegerProperty InitPlayGameProp = new SimpleIntegerProperty(0);
    private IntegerProperty FinalPlayGameProp = new SimpleIntegerProperty(1);

    // PROPERTY TO CONTROL ACCESS TO Settings
    private BooleanProperty SettingsProp = new SimpleBooleanProperty(false);
    private IntegerProperty InitSettingsProp = new SimpleIntegerProperty(0);
    private IntegerProperty FinalSettingsProp = new SimpleIntegerProperty(1);

    // --------------------------------------------------------------------------------------------------------
    // FIELDS
    // --------------------------------------------------------------------------------------------------------

    private BorderPane borderpane = new BorderPane();

    // --------------------------------------------------------------------------------------------------------
    // DISPLAY TITLE AND PLAYER NAMES
    // --------------------------------------------------------------------------------------------------------

    private Pane topPaneGamePlay() {
        // set up top pane - player 1 on left, player2 on right, "CONNECT FOUR" in middle
        Pane topPane = new Pane();
        topPane.setStyle("-fx-background-color: #FFFFFF");
        topPane.setMinHeight(80);
        Text titleText = new Text("Connect Four");
        titleText.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 30));
        titleText.layoutXProperty().bind(topPane.widthProperty().divide(2).subtract(25));
        titleText.layoutYProperty().bind(topPane.heightProperty().divide(2));
        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(60);
        openingLight.setAzimuth(280);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(6);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);
        titleText.setEffect(openingLighting);


        // ADD PLAYER NAMES HERE
        Label player1NameLb = new Label();
        Glow LbGlow = new Glow(0.1);
        player1NameLb.setEffect(LbGlow);
        player1NameLb.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 16));
        player1NameLb.layoutXProperty().bind(topPane.widthProperty().divide(4));
        player1NameLb.layoutYProperty().bind(topPane.heightProperty().divide(2));
//        player1NameLb.textProperty().bind(Player1NameProp);c
        Player1NameProp.addListener((ChangeListener) (observable, oldValue, newValue) -> {
            System.out.println(newValue);
            player1NameLb.setText((String) newValue);
            System.out.println(player1NameLb);
            player1NameLb.setTextFill(Color.web(String.valueOf(Player1NameProp.getValue())));
            topPane.getChildren().add(player1NameLb);
        });

        Label player2NameLb = new Label();
        player2NameLb.setEffect(LbGlow);
        player2NameLb.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 16));
        player2NameLb.layoutXProperty().bind(topPane.widthProperty().divide(2).add(70));
        player2NameLb.layoutYProperty().bind(topPane.heightProperty().divide(2));
        player2NameLb.textProperty().bind(Player2NameProp);
        player2NameLb.textProperty().addListener((observable, oldValue, newValue) -> {
            player2NameLb.setText(newValue);
            player2NameLb.setTextFill(Color.web(String.valueOf(Player2NameProp.getValue()).substring(2)));
            topPane.getChildren().add(titleText);
            System.out.println(String.valueOf(Player2NameProp));
        });
        return topPane;
    }

    // --------------------------------------------------------------------------------------------------------
    // PARENT METHOD TO CONTROL GAME FLOW
    // --------------------------------------------------------------------------------------------------------

    private Parent PlayGame() {

        GameSetupProp.bind(InitGameSetUpProp.isEqualTo(FinlGameSetUpProp));
        GameSetupProp.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Pane gameSetUpPane = new GameSetup(settingsColor1Prop, settingsColor2Prop,
                        Player1NameProp, Player2NameProp, InitPlayGameProp);
                borderpane.setCenter(gameSetUpPane);
                borderpane.setLeft(leftPane());
                borderpane.setStyle("-fx-background-color: #3333FF");
            }
        });

        PlayGameProp.bind(InitPlayGameProp.isEqualTo(FinalPlayGameProp));
        PlayGameProp.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Pane topPane = topPaneGamePlay();
                Pane tempPane1 = new Pane();
                tempPane1.setStyle("-fx-background-color: #FFFFFF");
                Connect4 connect4 = new Connect4(settingsColor1Prop, settingsColor2Prop);
                connect4.setPadding(new Insets(15, 15, 15, 15));
                borderpane.setCenter(tempPane1);
                borderpane.setCenter(connect4);
                borderpane.setTop(topPane);
//                borderpane.setStyle("-fx-background-color: #3333FF");
            }
        });

        borderpane.setCenter(StartAnimation(FinlGameSetUpProp));

        return borderpane;
    }

    private Pane StartAnimation(IntegerProperty FinalGameSetupProp) {
        // -----------------------------------------------------------------------------------------------------
        // OPENING ANIMATION
        // -----------------------------------------------------------------------------------------------------

        Pane startPane = new Pane();
        startPane.setStyle("-fx-background-color: #3333FF");

        // SETUP LIGHTING
        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(60);
        openingLight.setAzimuth(280);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(6);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);

        // TEXT "CONNECT" - FADES IN ONE LETTER AT A TIME
        Text C = new Text("C");
        Text O = new Text("O");
        Text N = new Text("N");
        Text N1 = new Text("N");
        Text E = new Text("E");
        Text C1 = new Text("C");
        Text T = new Text("T");
        Text FOUR = new Text("4");

        C.setEffect(openingLighting);
        O.setEffect(openingLighting);
        N.setEffect(openingLighting);
        N1.setEffect(openingLighting);
        E.setEffect(openingLighting);
        C1.setEffect(openingLighting);
        T.setEffect(openingLighting);
        FOUR.setEffect(openingLighting);

        C.setFill(Color.RED);
        O.setFill(Color.RED);
        N.setFill(Color.RED);
        N1.setFill(Color.RED);
        E.setFill(Color.RED);
        C1.setFill(Color.RED);
        T.setFill(Color.RED);
        FOUR.setFill(Color.YELLOW);

        Font myfont = Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 100);
        C.setFont(myfont);
        O.setFont(myfont);
        N.setFont(myfont);
        N1.setFont(myfont);
        E.setFont(myfont);
        C1.setFont(myfont);
        T.setFont(myfont);
        FOUR.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 300));

        C.setStroke(Color.BLACK);
        O.setStroke(Color.BLACK);
        N.setStroke(Color.BLACK);
        N1.setStroke(Color.BLACK);
        E.setStroke(Color.BLACK);
        C1.setStroke(Color.BLACK);
        T.setStroke(Color.BLACK);
        FOUR.setStroke(Color.BLACK);

        C.setStrokeWidth(2);
        O.setStrokeWidth(2);
        N.setStrokeWidth(2);
        N1.setStrokeWidth(2);
        E.setStrokeWidth(2);
        C1.setStrokeWidth(2);
        T.setStrokeWidth(2);
        FOUR.setStrokeWidth(2);

        C.setOpacity(0);
        O.setOpacity(0);
        N.setOpacity(0);
        N1.setOpacity(0);
        E.setOpacity(0);
        C1.setOpacity(0);
        T.setOpacity(0);

        // POSITION LETTER HERE
        C.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(230));
        C.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        O.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(170));
        O.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        N.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(100));
        N.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        N1.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(30));
        N1.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        E.layoutXProperty().bind(startPane.widthProperty().divide(2).add(40));
        E.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        C1.layoutXProperty().bind(startPane.widthProperty().divide(2).add(90));
        C1.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        T.layoutXProperty().bind(startPane.widthProperty().divide(2).add(155));
        T.layoutYProperty().bind(startPane.heightProperty().divide(2).subtract(100));
        FOUR.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(90));

        // SIDE PIECES

        Circle R1 = new Circle(40);
        Circle R2 = new Circle(40);
        Circle R3 = new Circle(40);
        Circle R4 = new Circle(40);
        Circle Y1 = new Circle(40);
        Circle Y2 = new Circle(40);
        Circle Y3 = new Circle(40);
        Circle Y4 = new Circle(40);

        R1.setFill(Color.RED);
        R2.setFill(Color.RED);
        R3.setFill(Color.RED);
        R4.setFill(Color.RED);
        Y1.setFill(Color.YELLOW);
        Y2.setFill(Color.YELLOW);
        Y3.setFill(Color.YELLOW);
        Y4.setFill(Color.YELLOW);

        R1.setEffect(openingLighting);
        R2.setEffect(openingLighting);
        R3.setEffect(openingLighting);
        R4.setEffect(openingLighting);
        Y1.setEffect(openingLighting);
        Y2.setEffect(openingLighting);
        Y3.setEffect(openingLighting);
        Y4.setEffect(openingLighting);

        R1.layoutXProperty().bind(startPane.widthProperty().divide(4));
        R2.layoutXProperty().bind(startPane.widthProperty().divide(4));
        R3.layoutXProperty().bind(startPane.widthProperty().divide(4));
        R4.layoutXProperty().bind(startPane.widthProperty().divide(4));
        Y1.layoutXProperty().bind(startPane.widthProperty().divide(2).add(360));
        Y2.layoutXProperty().bind(startPane.widthProperty().divide(2).add(360));
        Y3.layoutXProperty().bind(startPane.widthProperty().divide(2).add(360));
        Y4.layoutXProperty().bind(startPane.widthProperty().divide(2).add(360));

        R1.setLayoutY(-50);
        R2.setLayoutY(-50);
        R3.setLayoutY(-50);
        R4.setLayoutY(-50);
        Y1.setLayoutY(-50);
        Y2.setLayoutY(-50);
        Y3.setLayoutY(-50);
        Y4.setLayoutY(-50);

        Text loadingText = new Text("LOADING...");
        loadingText.setFill(Color.WHITE);
        loadingText.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 50));
        loadingText.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(110));
        loadingText.layoutYProperty().bind(startPane.heightProperty().divide(2));
        startPane.getChildren().add(loadingText);
        FadeTransition loadFade = new FadeTransition();
        loadFade.setDelay(Duration.millis(8000));
        loadFade.setDuration(Duration.millis(100));
        loadFade.setFromValue(10);
        loadFade.setToValue(0);
        loadFade.setNode(loadingText);
        loadFade.play();

        startPane.getChildren().addAll(FOUR, C, O, N, N1, E, C1, T);

        loadFade.statusProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == Animation.Status.STOPPED) {
                // TRANSITION TEXT - "CONNECT" FADES IN ONE LETTER AT A TIME
                FadeTransition CT = new FadeTransition();
                CT.setDelay(Duration.millis(500));
                CT.setDuration(Duration.millis(1500));
                CT.setFromValue(0);
                CT.setToValue(10);
                CT.setNode(C);
                CT.play();

                FadeTransition OT = new FadeTransition();
                OT.setDelay(Duration.millis(700));
                OT.setDuration(Duration.millis(1500));
                OT.setFromValue(0);
                OT.setToValue(10);
                OT.setNode(O);
                OT.play();

                FadeTransition NT = new FadeTransition();
                NT.setDelay(Duration.millis(900));
                NT.setDuration(Duration.millis(1500));
                NT.setFromValue(0);
                NT.setToValue(10);
                NT.setNode(N);
                NT.play();

                FadeTransition N1T = new FadeTransition();
                N1T.setDelay(Duration.millis(1100));
                N1T.setDuration(Duration.millis(1500));
                N1T.setFromValue(0);
                N1T.setToValue(10);
                N1T.setNode(N1);
                N1T.play();

                FadeTransition ET = new FadeTransition();
                ET.setDelay(Duration.millis(1300));
                ET.setDuration(Duration.millis(1500));
                ET.setFromValue(0);
                ET.setToValue(10);
                ET.setNode(E);
                ET.play();

                FadeTransition C1T = new FadeTransition();
                C1T.setDelay(Duration.millis(1500));
                C1T.setDuration(Duration.millis(1500));
                C1T.setFromValue(0);
                C1T.setToValue(10);
                C1T.setNode(C1);
                C1T.play();

                FadeTransition TT = new FadeTransition();
                TT.setDelay(Duration.millis(1700));
                TT.setDuration(Duration.millis(1500));
                TT.setFromValue(0);
                TT.setToValue(10);
                TT.setNode(T);
                TT.play();

                TranslateTransition animation4 = new TranslateTransition(Duration.seconds(0.5), FOUR);
                animation4.setToY(620);
                TT.setOnFinished(e -> {
                    FOUR.setY(-50);
                    animation4.play();
                });

                TranslateTransition animationR1 = new TranslateTransition(Duration.seconds(0.5), R1);
                animationR1.setToY(700);
                TranslateTransition animationR2 = new TranslateTransition(Duration.seconds(0.5), R2);
                animationR2.setToY(600);
                TranslateTransition animationR3 = new TranslateTransition(Duration.seconds(0.5), R3);
                animationR3.setToY(500);
                TranslateTransition animationR4 = new TranslateTransition(Duration.seconds(0.5), R4);
                animationR4.setToY(400);
                TranslateTransition animationY1 = new TranslateTransition(Duration.seconds(0.5), Y1);
                animationY1.setToY(700);
                TranslateTransition animationY2 = new TranslateTransition(Duration.seconds(0.5), Y2);
                animationY2.setToY(600);
                TranslateTransition animationY3 = new TranslateTransition(Duration.seconds(0.5), Y3);
                animationY3.setToY(500);
                TranslateTransition animationY4 = new TranslateTransition(Duration.seconds(0.5), Y4);
                animationY4.setToY(400);

                animation4.setOnFinished(e -> {
                    // MOVE PIECES DOWN ONE AT A TIME FROM TOP OF PAGE
                    startPane.getChildren().addAll(R1, R2, R3, R4, Y1, Y2, Y3, Y4);

                    animationR1.play();
                    animationY1.play();

                    animationY1.setOnFinished(event -> {
                        animationR2.play();
                        animationY2.play();
                    });

                    animationY2.setOnFinished(event1 -> {
                        animationR3.play();
                        animationY3.play();
                    });

                    animationY3.setOnFinished(event2 -> {
                        animationR4.play();
                        animationY4.play();
                    });

                    animationY4.setOnFinished(event3 -> {
                        // DISPLAY START GAME BUTTON
                        Button startGameBtn = new Button("START GAME");
                        startGameBtn.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 30));
                        startGameBtn.setEffect(openingLighting);
                        // STYLE BUTTON HERE
                        startGameBtn.layoutXProperty().bind(startPane.widthProperty().divide(2).subtract(100));
                        startGameBtn.layoutYProperty().bind(startPane.heightProperty().divide(2).add(275));
                        startGameBtn.setOnMouseClicked(event4 -> {
                            InitGameSetUpProp.set(InitGameSetUpProp.get() + 1);
                        });
                        startPane.getChildren().add(startGameBtn);
                    });
                });
            }
        });
        return startPane;
    }

    private Pane leftPane() {
//        Pane leftPane = new Pane();
        VBox tempLeftpane = new VBox();
        tempLeftpane.setStyle("-fx-background-color: #3333FF");
        tempLeftpane.setMinWidth(450);
        tempLeftpane.setPadding(new Insets(5,5,5,5));
        tempLeftpane.setAlignment(Pos.CENTER);

        Button instructionsBtn = new Button("Instructions");
        instructionsBtn.setAlignment(Pos.TOP_CENTER);
//        instructionsBtn.layoutXProperty().bind(le);

        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(60);
        openingLight.setAzimuth(280);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(6);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);
        instructionsBtn.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 15));
        instructionsBtn.setEffect(openingLighting);
//        instructionsBtn.layoutXProperty().bind(tempLeftpane.widthProperty().divide(2));
//        instructionsBtn.layoutYProperty().bind(tempLeftpane.heightProperty().divide(2).add(300));
        instructionsBtn.setOnMouseClicked(e -> {
            ScrollPane InstructionsSP = new ScrollPane();
            InstructionsSP.fitToWidthProperty().set(true);
            InstructionsSP.fitToHeightProperty().set(true);

            String myInstructions = "                    INSTRUCTIONS:\n" + "  OBJECT:\n" +
                    "To win Connect Four you must be the first player to get\n" +
                    "four of your colored checkers in a row either horizontally,\n" +
                    "vertically or diagonally.\n" +
                    "\n\n  GAME PLAY:\n" +
                    "Player 1 goes first. Each player alternates turns." +
                    "\n\n  PLAYER TURN:\n" +
                    "On a player's turn, the player must click on one column or \n" +
                    "cell to place their piece. Once their piece has been played,\n" +
                    "the player's turn is over and it is the next player's turn." +
                    "\n\n  WINNING THE GAME:\n" +
                    "When a player gets four game pieces in a row either horizontally,\n" +
                    "vertically or diagonally, the four game pieces will flash\n" +
                    "indicating the winner of the game.\n";
            Text Instructions = new Text(myInstructions);
            Instructions.setFill(Color.BLACK);
            Instructions.setFont(Font.font("Arial", FontPosture.REGULAR, 14));
            Instructions.setLineSpacing(1.0);
            Instructions.setTextAlignment(TextAlignment.LEFT);
            InstructionsSP.setContent(Instructions);
            tempLeftpane.getChildren().add(InstructionsSP);
            // BACK BUTTON - RETURN TO MAIN SETTINGS(SHOW CENTERVBOX)
            Button backBTN = new Button("back");
            backBTN.setAlignment(Pos.CENTER);
            tempLeftpane.getChildren().add(backBTN);
            tempLeftpane.getChildren().remove(instructionsBtn);
            backBTN.setOnMouseClicked(event -> {
                tempLeftpane.getChildren().removeAll(InstructionsSP, backBTN);
                tempLeftpane.getChildren().add(instructionsBtn);
            });
        });
        tempLeftpane.getChildren().add(instructionsBtn);
        return tempLeftpane;
    }

    // RIGHT PANE - MUSIC CONTROLLED HERE
    private Pane rightPane() {
        Pane rightPane = new Pane();


        return rightPane;
    }

    public static void main(String args[]) {
        launch(args);
    }
}