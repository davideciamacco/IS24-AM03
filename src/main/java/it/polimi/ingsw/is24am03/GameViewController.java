
package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;


public class GameViewController extends GUIController implements Initializable {


    @Override
    public void postNotification(String title, String desc) {

    }

    @FXML
    private Pane zoom;

    @FXML
    private ImageView resourceDeck;

    @FXML
    private TextField choicheObjective;

    @FXML
    private TextField indexI;

    @FXML

    private TextField indexJ;

    @FXML
    private Button placeCard;

    @FXML
    private TextField cardSide;

    @FXML
    private TextField cardNumber;

    @FXML
    private Button objectiveChoice;

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
    private TextField finalOb;

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
    private GridPane grid;

    @FXML
    private TextField notifications;
    @FXML
    private ImageView objective1;

    @FXML
    private ImageView objective2;

    @FXML
    private ImageView commonOb1;

    @FXML
    private ImageView commonOb2;


    @FXML
    private TextField state;

    @FXML
    private TextField current;

    @FXML
    private TextField turnOrder;

    @FXML
    private Pane board;

    @FXML
    private void onClickPlaceCard(MouseEvent mouseEvent){
        try{
            clientController.PlaceCard(Integer.parseInt(String.valueOf(this.cardNumber)), Integer.parseInt(String.valueOf(this.indexI)), Integer.parseInt(String.valueOf(this.indexJ)), String.valueOf(this.cardSide));
        }catch (Exception e){
            this.drawNotifications("Missing Arguments");
        }
    }
    @FXML
    private void onClickResourceDeck(MouseEvent mouseEvent) {
        clientController.DrawResource();

    }

    @FXML
    private void onClickGoldDeck(MouseEvent mouseEvent) {
        clientController.DrawGold();

    }

    @FXML
    private void onClickTable0(MouseEvent mouseEvent) {
        clientController.DrawTable(1);

    }

    @FXML
    private void onClickTable1(MouseEvent mouseEvent) {
        clientController.DrawTable(2);

    }

    @FXML
    private void onClickTable2(MouseEvent mouseEvent) {
        clientController.DrawTable(3);

    }

    @FXML
    private void onClickTable3(MouseEvent mouseEvent) {
        clientController.DrawTable(4);

    }



    @FXML
    private void onClickObjective1(MouseEvent mouseEvent){
        clientController.ChooseObjectiveCard(1);
    }

    @FXML
    private void onClickObjective2(MouseEvent mouseEvent){
        clientController.ChooseObjectiveCard(2);
    }


    public void drawHand(ArrayList<ResourceCard> hand) {
        //devo scorrere le carte in hand e per ciascuna devo trovare sia fronte che retro

        //distinguo il caso in cui la hand è solo di due e invece quando è di tre carte
        if (hand.size() == 3) {
            //fronte
            Image card = new Image(getClass().getResource(findFrontUrl(hand.get(0).getId())).toExternalForm());
            personalFront1.setImage(card);
            //retro
            Image card1 = new Image(getClass().getResource(findBackUrl(hand.get(0).getId())).toExternalForm());
            personalBack1.setImage(card1);

            Image card2 = new Image(getClass().getResource(findFrontUrl(hand.get(1).getId())).toExternalForm());
            personalFront2.setImage(card2);
            Image card22 = new Image(getClass().getResource(findBackUrl(hand.get(1).getId())).toExternalForm());
            personalBack2.setImage(card22);

            Image card3 = new Image(getClass().getResource(findFrontUrl(hand.get(2).getId())).toExternalForm());
            personalFront3.setImage(card3);
            Image card33 = new Image(getClass().getResource(findBackUrl(hand.get(2).getId())).toExternalForm());
            personalBack3.setImage(card33);


        } else if (hand.size() == 2) {
            Image card = new Image(getClass().getResource(findFrontUrl(hand.get(0).getId())).toExternalForm());
            personalFront1.setImage(card);
            //retro
            Image card1 = new Image(getClass().getResource(findBackUrl(hand.get(0).getId())).toExternalForm());
            personalBack1.setImage(card1);

            Image card2 = new Image(getClass().getResource(findFrontUrl(hand.get(1).getId())).toExternalForm());
            personalFront2.setImage(card2);
            Image card22 = new Image(getClass().getResource(findBackUrl(hand.get(1).getId())).toExternalForm());
            personalBack2.setImage(card22);

            Image card3 = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            personalFront3.setImage(card3);
            Image card33 = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            personalBack3.setImage(card33);


        }
    }

    public void drawStarting(StartingCard startingCard) {
        Image card = new Image(getClass().getResource(findFrontUrl(startingCard.getId())).toExternalForm());
        startingCardFront.setImage(card);
        Image card1 = new Image(getClass().getResource(findBackUrl(startingCard.getId())).toExternalForm());
        startingCardBack.setImage(card1);
    }

    public void drawObjective1(ObjectiveCard objectiveCard) {
        Image card = new Image(getClass().getResource(findFrontUrl(objectiveCard.getId())).toExternalForm());
        objective1.setImage(card);
    }

    public void drawObjective2(ObjectiveCard objectiveCard) {
        Image card = new Image(getClass().getResource(findFrontUrl(objectiveCard.getId())).toExternalForm());
        objective2.setImage(card);
    }

    public void drawCommonObjective(ArrayList<ObjectiveCard> commons) {
        Image card1 = new Image(getClass().getResource(findFrontUrl(commons.get(0).getId())).toExternalForm());
        commonOb1.setImage(card1);
        Image card = new Image(getClass().getResource(findFrontUrl(commons.get(1).getId())).toExternalForm());
        commonOb2.setImage(card);

    }

    public void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3) {
        //deck risorsa
        if (resourceDeck == null) {
            //lo metto a empty
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.resourceDeck.setImage(card);
        } else {
            //se face ==true prendo il fronte

                Image card = new Image(getClass().getResource(findBackUrl(resourceDeck.getId())).toExternalForm());
                this.resourceDeck.setImage(card);


        }

        //deck oro
        if (goldDeck == null) {
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.goldDeck.setImage(card);

        } else {
                Image card = new Image(getClass().getResource(findBackUrl(goldDeck.getId())).toExternalForm());
                this.goldDeck.setImage(card);
            }




        //carta table0
        if (card0 == null) {
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.table0.setImage(card);
        } else {
            if (card0.getFace()) {
                Image card = new Image(getClass().getResource(findFrontUrl(card0.getId())).toExternalForm());
                this.table0.setImage(card);
            } else {
                Image card = new Image(findBackUrl(card0.getId()));
                this.table0.setImage(card);
            }


        }

        //carta table1
        if (card1 == null) {
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.table1.setImage(card);
        } else {
            if (card1.getFace()) {
                Image card = new Image(getClass().getResource(findFrontUrl(card1.getId())).toExternalForm());
                this.table1.setImage(card);
            } else {
                Image card = new Image(getClass().getResource(findBackUrl(card1.getId())).toExternalForm());
                this.table1.setImage(card);
            }

        }

        //carta table2
        if (card2 == null) {
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.table2.setImage(card);
        } else {
            if (card2.getFace()) {
                Image card = new Image(getClass().getResource(findFrontUrl(card2.getId())).toExternalForm());
                this.table2.setImage(card);
            } else {
                Image card = new Image(getClass().getResource(findBackUrl(card2.getId())).toExternalForm());
                this.table2.setImage(card);
            }

        }

        //carta table 3
        if (card3 == null) {
            Image card = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png");
            this.table3.setImage(card);

        } else {
            if (card3.getFace()) {
                Image card = new Image(getClass().getResource(findFrontUrl(card3.getId())).toExternalForm());
                this.table3.setImage(card);
            } else {
                Image card = new Image(getClass().getResource(findBackUrl(card3.getId())).toExternalForm());
                this.table3.setImage(card);
            }

        }
    }

    public void drawBoard(PlayableCard[][] board, String current, String player){

    }
    public void updateStarting(){
        this.startingCardBack.setVisible(false);
        this.startingCardFront.setVisible(false);
    }

    private String findFrontUrl(int id) {
        //metodo che mi restituisce l'url del fronte della carta corrispondente
        String url = null;
        if (id >= 0 && id <= 9) {
            //significa che ho una carta risorsa rossa
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + (id) + ".png";

        }
        if (id > 9 && id <= 19) {
            //risorsa verde
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }

        if (id > 19 && id <= 29) {
            //risorsa blu
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }

        if (id > 29 && id <= 39) {
            //risorsa viola
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 40 && id <= 49) {
            //oro rossa
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 50 && id <= 59) {
            //oro verde
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 60 && id <= 69) {
            //oro blu
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 70 && id <= 79) {
            //oro viola
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 80 && id <= 85) {
            //starting card
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 86 && id <= 101) {
            //carta obiettivo
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        return url;
    }

    private String findBackUrl(int id) {
        String url = null;
        if (id >= 0 && id <= 9) {
            //significa che ho una carta risorsa rossa
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_RED.png";

        }
        if (id > 9 && id <= 19) {
            //risorsa verde
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_GREEN.png";
        }

        if (id > 19 && id <= 29) {
            //risorsa blu
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_BLUE.png";
        }

        if (id > 29 && id <= 39) {
            //risorsa viola
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_PURPLE.png";
        }
        if (id >= 40 && id <= 49) {
            //oro rossa
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_RED.png";
        }
        if (id >= 50 && id <= 59) {
            //oro verde
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_GREEN.png";
        }
        if (id >= 60 && id <= 69) {
            //oro blu
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_BLUE.png";
        }
        if (id >= 70 && id <= 79) {
            //oro viola
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_PURPLE.png";
        }
        if (id >= 80 && id <= 85) {
            //starting card
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/" + id + ".png";
        }
        if (id >= 86 && id <= 101) {
            //carta obiettivo
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/OBJECTIVE.png";
        }
        return url;

    }

   @FXML

    private double scaleValue=1.0;
    private final double scaleIncrement=0.1;

    private final double minScale=0.5;


    @FXML
    private TextField color;



    @FXML
    private Button green;
    @FXML
    private Button blue;
    @FXML
    private Button yellow;
    @FXML
    private Button red;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoom.setOnScroll(this::handleZoom);
        this.finalOb.setVisible(false);
    }


    private void handleZoom(ScrollEvent event){
        double deltaY = event.getDeltaY();
        if (deltaY < 0) {
            scaleValue -= scaleIncrement;
        } else {
            scaleValue += scaleIncrement;
        }
        scaleValue = Math.max(minScale, scaleValue);

        Scale scale = new Scale(scaleValue, scaleValue, event.getX(), event.getY());
        zoom.getTransforms().setAll(scale);

        event.consume();
    }

    @FXML
    private void onClickStartingFront(MouseEvent mouseEvent){
        clientController.ChooseStartingCardSide("FRONT");
    }

    @FXML
    private void onClickStartingBack(MouseEvent mouseEvent){
        clientController.ChooseStartingCardSide("BACK");
    }

    public void drawFinalObjective(ObjectiveCard o){
        this.finalOb.setVisible(true);
        this.finalOb.setText("This is your secret objective");
        Image card = new Image(getClass().getResource(findFrontUrl(o.getId())).toExternalForm());
        this.objective2.setImage(card);
        this.objective1.setVisible(false);
    }

    public void drawState(State s){
        this.state.setText("Game state:" + s.toString());
    }
    public void drawTurnOrder(String s){
            this.turnOrder.setText(s);
    }
    public void drawCurrent(String s){
        this.current.setText("Current player is:" + s);
    }
    public void drawNotifications(String s){
        this.notifications.setText(s);
    }

    @FXML
    private void onClickRed(MouseEvent mouseEvent){
        clientController.PickColor("RED");
    }
    @FXML
    private void onClickBlue(MouseEvent mouseEvent){
        clientController.PickColor("BLUE");
    }
    @FXML
    private void onClickGreen(MouseEvent mouseEvent){
        clientController.PickColor("GREEN");
    }
    @FXML
    private void onClickYellow(MouseEvent mouseEvent){
        clientController.PickColor("YELLOW");
    }

}

