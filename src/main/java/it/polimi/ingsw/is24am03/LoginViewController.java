package it.polimi.ingsw.is24am03;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController extends GUIController {
    @FXML
    private TextField commandField;
    @FXML
    private Label errorLabel;

    @FXML
    private Label errorLabelDesc;

    @FXML
    private void onSendButtonClick() {
        // Implementa la logica per inviare il comando generico
        String command = commandField.getText();

        if (command.isEmpty()) {
            errorLabel.setText("Error:");
            errorLabelDesc.setText("Command cannot be empty.");
        } else {
            String[] inputArray = command.split("\\s+");
            String command1 = inputArray[0];

            if(command1.equals("CreateGame"))
                 clientController.CreateGame(Integer.parseInt(inputArray[1]), inputArray[2]);
        }
    }

    @Override
    public void postNotification(String title, String desc) {
        // Implementa la visualizzazione di notifiche
    }
}
