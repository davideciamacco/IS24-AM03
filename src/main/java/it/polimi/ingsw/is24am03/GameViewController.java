
package it.polimi.ingsw.is24am03;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class GameViewController extends GUIController {


    @Override
    public void postNotification(String title, String desc) {

    }

    @FXML
    private ImageView resourceDeck;

    @FXML
    private ImageView goldDeck;

    @FXML
    private ImageView table0;

    @FXML
    private ImageView table1;
    @FXML
    private ImageView table2;
    @FXML
    private ImageView table3;

    @FXML
    private ImageView personalFront1;

    @FXML
    private ImageView personalBack1;
    @FXML
    private ImageView personalFront2;
    @FXML
    private ImageView personalBack2;
    @FXML
    private ImageView personalFront3;
    @FXML
    private ImageView personalBack3;

    @FXML
    private ImageView startingCardFront;

    @FXML
    private ImageView startingCardBack;


    @FXML
    private ImageView objective1;

    @FXML
    private ImageView objective2;


    @FXML
    private VBox container;




    @FXML
    private void onClickResourceDeck(MouseEvent mouseEvent){
        clientController.DrawResource();

    }

    @FXML
    private void onClickGoldDeck(MouseEvent mouseEvent){
        clientController.DrawGold();

    }

    @FXML
    private void onClickTable0(MouseEvent mouseEvent){
        clientController.DrawTable(1);

    }

    @FXML
    private void onClickTable1(MouseEvent mouseEvent){
        clientController.DrawTable(2);

    }

    @FXML
    private void onClickTable2(MouseEvent mouseEvent){
        clientController.DrawTable(3);

    }
    @FXML
    private void onClickTable3(MouseEvent mouseEvent){
        clientController.DrawTable(4);

    }



}

