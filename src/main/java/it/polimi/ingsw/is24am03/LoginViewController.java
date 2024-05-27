package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.exceptions.GameAlreadyCreatedException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController extends GUIController {
    @FXML
    private Label errorLabel;

    @FXML
    private Label errorLabelDesc;
    @FXML
    private TextField numbersOfPlayersField;
    @FXML
    private TextField nicknameCreateField;
    @FXML
    private TextField nicknameJoinField;
    @FXML
    private TextField nicknameRejoinField;

    @FXML
    private void onCreateButtonClick() {

        String numberOfPlayersText = numbersOfPlayersField.getText();
        String nickname = nicknameCreateField.getText();
        if(numberOfPlayersText.isEmpty() || nickname.isEmpty())
        {
            errorLabel.setText("Error: ");
            errorLabelDesc.setText("Number of players and/or nickname cannot be empty.");
        }
        else{
            try{
                clientController.CreateGame(Integer.parseInt(numberOfPlayersText), nickname);
            } catch(NumberFormatException e){
                errorLabel.setText("Error: ");
                errorLabelDesc.setText("First input must be a number");
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
             errorLabel.setText("Error: ");
             errorLabelDesc.setText("Nickname cannot be empty.");
         }
         else
            clientController.JoinGame(nickname);
    }
    @FXML
    private void onRejoinButtonClick() {
        String nickname =nicknameRejoinField.getText();
        if(nickname.isEmpty()){
            errorLabel.setText("Error: ");
            errorLabelDesc.setText("Nickname cannot be empty.");
        }
        clientController.RejoinGame(nickname);
    }
    @Override
    public void postNotification(String title, String desc) {
        // Implementa la visualizzazione di notifiche
    }
}
