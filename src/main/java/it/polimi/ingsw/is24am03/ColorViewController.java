package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.server.model.exceptions.ColorAlreadyPickedException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ColorViewController extends GUIController {

    @FXML
    private Button redButton;

    @FXML
    private Button blueButton;

    @FXML
    private Button yellowButton;

    @FXML
    private Button greenButton;

    @FXML
    private void onClickRedButton(){
            clientController.PickColor("RED");
    }
    @FXML
    private void onClickBlueButton(){
            clientController.PickColor("BLUE");
    }

    @FXML
    private void onClickGreenButton(){
            clientController.PickColor("GREEN");
    }

    @FXML
    private void onClickYellowButton(){
        clientController.PickColor("YELLOW");
    }

    @Override
    public void postNotification(String title, String desc) {

    }
}
