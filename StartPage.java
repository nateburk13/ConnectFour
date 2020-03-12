import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class StartPage extends Pane {
    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // PROPERTIES TO CONTROL THE COLOR OF THE PIECES AND PLAYER NAMES
    private StringProperty settingsColor1Prop = new SimpleStringProperty();
    private StringProperty settingsColor2Prop = new SimpleStringProperty();

    // PROPERTIES TO CONTROL THE PLAYER NAMES
    private StringProperty Player1NameProp = new SimpleStringProperty();
    private StringProperty Player2NameProp = new SimpleStringProperty();

    // PROPERTY TO CONTROL ACCESS TO GAMESETUP
    private BooleanProperty GameSetupProp = new SimpleBooleanProperty();

    // PROPERTY TO CONTROL IF MUSIC IS PLAYING IN THE BACKGROUND
    private BooleanProperty settingsMusic = new SimpleBooleanProperty();

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // default constructor
//    StartPage(){}

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    StartPage(StringProperty settingsColor1Prop,StringProperty settingsColor2Prop,
              StringProperty PlayerName1Prop, StringProperty Player2NameProp,
              BooleanProperty GameSetupProp, BooleanProperty settingsMusic ) {
        this.settingsColor1Prop = settingsColor1Prop;
        this.settingsColor2Prop = settingsColor2Prop;
        this.Player1NameProp = PlayerName1Prop;
        this.Player2NameProp = Player2NameProp;
        this.GameSetupProp = GameSetupProp;
        this.settingsMusic = settingsMusic;
        createStartPage();

    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // THIS CREATES THE MAIN PAGE WHEN OPENED UP(AFTER ANIMATION). IT INCLUDES:
    //    + PLAY GAME BUTTON
    //    + SETTINGS BUTTON
    private void createStartPage() {
        // PLAY GAME BUTTON
        Button StartGameBtn = new Button("START GAME");
//        PlayGameBtn.setTextFill(Color.WHITE);
        StartGameBtnListener(StartGameBtn);

        // SETTINGS BUTTON
        Button SettingsBtn = new Button("settings");
//        SettingsBtn.setTextFill(Color.BLACK);
        SettingsBtnListener(SettingsBtn);


    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // GOT TO SETTINGS PAGE on new stage
    private void SettingsBtnListener(Button settingsBtn) {
        settingsBtn.setOnMouseClicked(e -> {
            Settings settings = new Settings(settingsMusic);
            Scene settingsScene = new Scene(settings, 500, 500);
            Stage settingsStage = new Stage();
            settingsStage.setTitle("SettingsStage");
            settingsStage.setScene(settingsScene);
            settingsStage.show();
        });
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------

    // GO TO GAME SETUP PAGE
    private void StartGameBtnListener(Button StartGameBtn) {
        // LISTENER TO ADJUST SCENE ON CONNECT4.JAVA CLASS PRIMARYSTAGE
        StartGameBtn.setOnMouseClicked(event -> {
            GameSetupProp.set(true);
           });
    }


}
