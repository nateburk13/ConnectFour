import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//THIS REQUESTS PLAYER'S NAME AND SETS THEIR COLOR
public class GameSetup extends Pane {

    // PROPERTIES TO CONTROL THE COLOR OF THE PIECES AND PLAYER NAMES
    private StringProperty settingsColor1Prop = new SimpleStringProperty();
    private StringProperty settingsColor2Prop = new SimpleStringProperty();
    private StringProperty Player1NameProp = new SimpleStringProperty();
    private StringProperty Player2NameProp = new SimpleStringProperty();
    private IntegerProperty InitPlayGameProp;

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // DEFAULT CONSTRUCTOR
    GameSetup() {
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    GameSetup(StringProperty settingsColor1Prop, StringProperty settingsColor2Prop,
              StringProperty Player1NameProp, StringProperty Player2NameProp, IntegerProperty InitPlayGameProp) {
        this.settingsColor1Prop = settingsColor1Prop;
        this.settingsColor2Prop = settingsColor2Prop;
        this.Player1NameProp = Player1NameProp;
        this.Player2NameProp = Player2NameProp;
        this.InitPlayGameProp = InitPlayGameProp;
        setStyle("-fx-background-color: #3333FF");
        createGameSetUpPage();
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    private void createGameSetUpPage() {
        Label player1NameLB = new Label("Player 1:");
        labelStyle(player1NameLB);
        player1NameLB.layoutXProperty().bind(widthProperty().divide(4));
        player1NameLB.layoutYProperty().bind(heightProperty().divide(4));

        TextField player1NameTF = new TextField();
        player1NameTF.layoutXProperty().bind(widthProperty().divide(4).add(60));
        player1NameTF.layoutYProperty().bind(heightProperty().divide(4));
        player1NameTF.textProperty().addListener((observable, oldValue, newValue) -> setPlayer1Name(newValue));

        Label choosePlayer1ColorLB = new Label("Player 1 color:");
        choosePlayer1ColorLB.setTooltip(new Tooltip("click on a color"));
        labelStyle(choosePlayer1ColorLB);
        choosePlayer1ColorLB.layoutXProperty().bind(widthProperty().divide(4).add(220));
        choosePlayer1ColorLB.layoutYProperty().bind(heightProperty().divide(4));

        ColorPicker colorPicker1 = new ColorPicker();
        colorPicker1.setOnAction(e -> {
            settingsColor1Prop.set(String.valueOf(colorPicker1.getValue()));
            if (settingsColor1Prop.getValue().equals(settingsColor2Prop.getValue())) {
                Stage tempStage = new Stage();
                Pane tempPane = new Pane();
                Text text = new Text("Please choose different colors");
                text.setFill(Color.BLACK);
                text.setFont(Font.font(14));
                text.layoutXProperty().bind(tempPane.widthProperty().divide(6));
                text.layoutYProperty().bind(tempPane.heightProperty().divide(2));
                tempPane.getChildren().add(text);
                tempStage.setScene(new Scene(tempPane, 300, 150));
                tempStage.show();
                settingsColor1Prop.set("RED");
            }
        });
        colorPicker1.layoutXProperty().bind(widthProperty().divide(4).add(330));
        colorPicker1.layoutYProperty().bind(heightProperty().divide(4));

        Label player2NameLB = new Label("Player 2:");
        labelStyle(player2NameLB);
        player2NameLB.layoutXProperty().bind(widthProperty().divide(4));
        player2NameLB.layoutYProperty().bind(heightProperty().divide(4).add(100));

        TextField player2NameTF = new TextField();
        player2NameTF.layoutXProperty().bind(widthProperty().divide(4).add(60));
        player2NameTF.layoutYProperty().bind(heightProperty().divide(4).add(100));
        player2NameTF.textProperty().addListener((observable, oldValue, newValue) -> {
            setPlayer2Name(newValue);
        });

        Label choosePlayer2ColorLB = new Label("Player 2 color:");
        choosePlayer2ColorLB.setTooltip(new Tooltip("click on a color"));
        labelStyle(choosePlayer2ColorLB);
        choosePlayer2ColorLB.layoutXProperty().bind(widthProperty().divide(4).add(220));
        choosePlayer2ColorLB.layoutYProperty().bind(heightProperty().divide(4).add(100));

        ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setOnAction(e -> {
            settingsColor2Prop.set(String.valueOf(colorPicker2.getValue()));
            if (settingsColor1Prop.getValue().equals(settingsColor2Prop.getValue())) {
                Stage tempStage = new Stage();
                Pane tempPane = new Pane();
                Text text = new Text("Please choose different colors");
                text.setFill(Color.BLACK);
                text.setFont(Font.font(14));
                text.layoutXProperty().bind(tempPane.widthProperty().divide(6));
                text.layoutYProperty().bind(tempPane.heightProperty().divide(2));
                tempPane.getChildren().add(text);
                tempStage.setScene(new Scene(tempPane, 300, 150));
                tempStage.show();
                settingsColor1Prop.set("YELLOW");
            }
        });
        colorPicker2.layoutXProperty().bind(widthProperty().divide(4).add(330));
        colorPicker2.layoutYProperty().bind(heightProperty().divide(4).add(100));

        // BUTTON PLAY GAME
        Button playGameBtn = new Button("Play");
        buttonStyle(playGameBtn);
        playGameBtn.layoutXProperty().bind(widthProperty().divide(4).add(220));
        playGameBtn.layoutYProperty().bind(heightProperty().divide(4).add(200));
        playGameBtn.setOnMouseClicked(event -> InitPlayGameProp.set(InitPlayGameProp.get() + 1));

        getChildren().addAll(player1NameLB, player1NameTF, colorPicker1, choosePlayer1ColorLB,
                player2NameLB, player2NameTF, colorPicker2, choosePlayer2ColorLB, playGameBtn);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    private void buttonStyle(Button playGameBtn) {
        // SETUP LIGHTING
        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(60);
        openingLight.setAzimuth(280);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(6);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);
        playGameBtn.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 20));
        playGameBtn.setEffect(openingLighting);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    private void setPlayer2Name(String player2Name) {
        Player2NameProp.set(player2Name);
    }


    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    private void setPlayer1Name(String player1Name) {
        Player1NameProp.set(player1Name);
//        System.out.println(String.valueOf(Player1NameProp.getValue()));
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    private void labelStyle(Label label) {
        label.setTextFill(Color.WHITE);
        // SETUP LIGHTING
        Light.Distant openingLight = new Light.Distant();
        openingLight.setElevation(60);
        openingLight.setAzimuth(280);
        Lighting openingLighting = new Lighting();
        openingLighting.setSurfaceScale(6);
        openingLighting.setDiffuseConstant(1.1);
        openingLighting.setLight(openingLight);

        label.setFont(Font.font("Hemi Head", FontWeight.BOLD, FontPosture.REGULAR, 14));
        label.setEffect(openingLighting);
    }
}

