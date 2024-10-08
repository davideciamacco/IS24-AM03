package it.polimi.ingsw.is24am03.client.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 *GUIController that handles the first scene shown when a player connects to the game
 */
public class LoginViewController extends GUIController {

    @FXML
    private ToggleButton JoinGame;
    @FXML
    private ToggleButton CreateGame;
    @FXML
    private ToggleButton RejoinGame;
    @FXML
    private ToggleGroup action;
    @FXML
    private ToggleButton ply2;
    @FXML
    private ToggleButton ply3;
    @FXML
    private ToggleButton ply4;

    @FXML
    private ToggleGroup numPlayers;
    @FXML
    private VBox CreateBox;
    @FXML
    private VBox JoinBox;
    @FXML
    private VBox RejoinBox;
    @FXML
    private Button doneCreate;
    @FXML
    private Button doneJoin;
    @FXML
    private Button doneRejoin;
    private int numberPlayers = 0;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField nicknameCreateField;
    @FXML
    private TextField nicknameJoinField;
    @FXML
    private TextField nicknameRejoinField;


    /**
     * Handles the creation of a game
     */
    @FXML
    private void onCreateButtonClick() {

        String numberOfPlayersText = Integer.toString(numberPlayers);

        String nickname = nicknameCreateField.getText();
        if(numberOfPlayersText.equals("0") || nickname.isEmpty())
        {
            showAlert("Error", "Number of players and/or nickname cannot be empty.");
        }
        else{
            errorLabel.setText("");
            try{
                clientController.CreateGame(Integer.parseInt(numberOfPlayersText), nickname);
            } catch(NumberFormatException e){
                showAlert("Error", "First input must be a number");
            }
        }
    }

    /**
     * Handles the joining of the game
     */
    @FXML
    private void onJoinButtonClick() {
        String nickname = nicknameJoinField.getText();
        if(nickname.isEmpty()){
            showAlert("Error", "Nickname cannot be empty.");
        }
        else
            clientController.JoinGame(nickname);
    }

    /**
     * Handles the rejoin in the game
     */
    @FXML
    private void onRejoinButtonClick() {
        String nickname =nicknameRejoinField.getText();
        if(nickname.isEmpty()){
            showAlert("Error", "Nickname cannot be empty.");
        }
        clientController.RejoinGame(nickname);
    }

    /**
     * Initializes the scene and assigns each button to a specific action
     */
    public void initialize() {
        CreateGame.setOnAction(event -> handleToggleSelection(CreateGame));
        JoinGame.setOnAction(event -> handleToggleSelection(JoinGame));
        RejoinGame.setOnAction(event -> handleToggleSelection(RejoinGame));
        doneCreate.setOnAction(event -> onCreateButtonClick());
        doneJoin.setOnAction(event -> onJoinButtonClick());
        doneRejoin.setOnAction(event -> onRejoinButtonClick());

        numPlayers.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == ply2) {
                    numberPlayers = 2;
                } else if (newValue == ply3) {
                    numberPlayers = 3;
                } else if (newValue == ply4) {
                    numberPlayers = 4;
                }
            }
        });
    }

    /**
     * Handles the selection on a certain button
     * @param selectedToggle refers to the button pressed
     */
    private void handleToggleSelection(ToggleButton selectedToggle) {
        CreateBox.setVisible(selectedToggle == CreateGame);
        JoinBox.setVisible(selectedToggle == JoinGame);
        RejoinBox.setVisible(selectedToggle == RejoinGame);

        if (selectedToggle != CreateGame) CreateGame.setSelected(false);
        if (selectedToggle != JoinGame) JoinGame.setSelected(false);
        if (selectedToggle != RejoinGame) RejoinGame.setSelected(false);
    }

}
