package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.exceptions.GameAlreadyCreatedException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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

    @FXML
    private void onCreateButtonClick() {

        String numberOfPlayersText = Integer.toString(numberPlayers);

        String nickname = nicknameCreateField.getText();
        if(numberOfPlayersText.equals("0") || nickname.isEmpty())
        {
            errorLabel.setText("Error: Number of players and/or nickname cannot be empty.");
        }
        else{
            errorLabel.setText("");
            try{
                clientController.CreateGame(Integer.parseInt(numberOfPlayersText), nickname);
            } catch(NumberFormatException e){
                errorLabel.setText("Error: First input must be a number");
            }
        }


        /*
        // Implementa la logica per inviare il comando generico
        Stri

        if (command.isEmpty()) {
            errorLabel.setText("Error:");
            errorLabelDesc.setText("Command cannot be empty.");
        } else {
            String[] inputArray = command.split("\\s+");
            String command1 = inputArray[0];

            if(command1.equals("CreateGame"))
                 clientController.CreateGame(Integer.parseInt(inputArray[1]), inputArray[2]);
        }*/
    }
    @FXML
    private void onJoinButtonClick() {
         String nickname = nicknameJoinField.getText();
         if(nickname.isEmpty()){
             errorLabel.setText("Error: Nickname cannot be empty.");
         }
         else
            clientController.JoinGame(nickname);
    }
    @FXML
    private void onRejoinButtonClick() {
        String nickname =nicknameRejoinField.getText();
        if(nickname.isEmpty()){
            errorLabel.setText("Error: Nickname cannot be empty.");
        }
        clientController.RejoinGame(nickname);
    }
    @Override
    public void postNotification(String title, String desc) {
        // Implementa la visualizzazione di notifiche
    }


    public void initialize() {
        // Adding event listeners to the ToggleButtons
        CreateGame.setOnAction(event -> handleToggleSelection(CreateGame));
        JoinGame.setOnAction(event -> handleToggleSelection(JoinGame));
        RejoinGame.setOnAction(event -> handleToggleSelection(RejoinGame));

        doneCreate.setOnAction(event -> onCreateButtonClick());
        doneJoin.setOnAction(event -> onJoinButtonClick());
        doneRejoin.setOnAction(event -> onRejoinButtonClick());


        numPlayers.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Controlla quale ToggleButton è stato selezionato e imposta il valore di numberOfPlayers di conseguenza
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

    private void handleToggleSelection(ToggleButton selectedToggle) {
        // Show or hide VBox based on which ToggleButton is selected
        CreateBox.setVisible(selectedToggle == CreateGame);
        JoinBox.setVisible(selectedToggle == JoinGame);
        RejoinBox.setVisible(selectedToggle == RejoinGame);

        // Ensure only the selected ToggleButton remains selected
        if (selectedToggle != CreateGame) CreateGame.setSelected(false);
        if (selectedToggle != JoinGame) JoinGame.setSelected(false);
        if (selectedToggle != RejoinGame) RejoinGame.setSelected(false);
    }

}
