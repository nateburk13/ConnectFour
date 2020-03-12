import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;


public class Settings extends BorderPane {

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // PROPERTIES TO CONTROL IF MUSIC IS PLAYING IN THE BACKGROUND
    private BooleanProperty settingsMusic = new SimpleBooleanProperty();
    private IntegerProperty InitPlayGameProp = new SimpleIntegerProperty(0);

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // VBOX FOR THE CENTER PANE
    private VBox centerVBox = new VBox();

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // Default constructor
    Settings() {
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    Settings(BooleanProperty settingsMusic/*, IntegerProperty InitPlayGameProp*/) {
        this.InitPlayGameProp = InitPlayGameProp;
        this.settingsMusic = settingsMusic;
        setStyle("-fx-background-color: #3333FF");
        createSettingsPage();
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // THIS CREATES THE SETTINGS PANE. IT INCLUDES:
    //      + OPTIONS FOR SETTING PLAYER COLORS
    //      + OPTION FOR PLAYING MUSIC IN THE BACKGROUND
    private void createSettingsPage() {
        Button instructionsBtn = new Button("Game Instructions");
        // STYLE BUTTON HERE AND POSITION

        gamePlayInstructions(instructionsBtn);

        CheckBox musicCB = new CheckBox("Music On");
        // STYLE THE CHECKBOX HERE

        musicOn(musicCB);

        // PUT ALL NODES ON VBOX
        centerVBox.getChildren().addAll(instructionsBtn,musicCB);

    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // RETURN IF MUSIC IS SELECTED TO PLAY
    private void musicOn(CheckBox musicCB) {
        if (musicCB.isSelected()) {
            settingsMusic.setValue(true);
        }
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // SHOW GAME INSTRUCTIONS
    private void gamePlayInstructions(Button InstructionsBTN) {
        ScrollPane InstructionsSP = new ScrollPane();
        InstructionsSP.fitToWidthProperty().set(true);
        InstructionsSP.fitToHeightProperty().set(true);

        String myInstructions = "  OBJECT:\n" +
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
        Instructions.setFont(Font.font("Arial", FontPosture.REGULAR, 12));
        Instructions.setLineSpacing(1.0);
        Instructions.setTextAlignment(TextAlignment.LEFT);
        InstructionsSP.setContent(Instructions);

        // BACK BUTTON - RETURN TO MAIN SETTINGS(SHOW CENTERVBOX)
        Button backBTN = new Button("back");
        backBTN.layoutXProperty().bind(widthProperty().divide(2));
        backBTN.layoutYProperty().bind(heightProperty().divide(2));

        // RETURN TO GAME BUTTON
//        Button returnToGameBtn = new Button();

        // INSTRUCTIONS LABEL AT TOP OF BORDER PANE
        Label InstructionLB = new Label("Instructions");
        InstructionLB.setFont(Font.font("Arial",FontWeight.BOLD,FontPosture.REGULAR,20));
        InstructionLB.setTextFill(Color.BLACK);

        // LISTENERS FOR INSTRUCTIONBTN AND BACKBTN
        InstructionsBTN.setOnMouseClicked(event -> {
            setTop(InstructionLB);
            setCenter(InstructionsSP);
            setBottom(backBTN);
            backBTN.setOnMouseClicked(e -> {
                setTop(null);
                setCenter(centerVBox);
                setBottom(null);
            });
        });
    }
}
