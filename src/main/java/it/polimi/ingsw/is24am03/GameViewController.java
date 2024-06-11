
package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
            clientController.PlaceCard(Integer.parseInt(String.valueOf(this.cardNumber.getText())), Integer.parseInt(String.valueOf(this.indexI.getText())), Integer.parseInt(String.valueOf(this.indexJ.getText())), String.valueOf(this.cardSide.getText()));
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
        this.color.setVisible(false);
        this.green.setVisible(false);
        this.blue.setVisible(false);
        this.red.setVisible(false);
        this.yellow.setVisible(false);
        this.player1.setVisible(false);
        this.player2.setVisible(false);
        this.player3.setVisible(false);
        this.player4.setVisible(false);
        this.p1.setVisible(false);
        this.p2.setVisible(false);
        this.p3.setVisible(false);
        this.p4.setVisible(false);
        this.goBack1.setVisible(false);
        this.goBack2.setVisible(false);
        this.goBack3.setVisible(false);
        this.goBack4.setVisible(false);
        this.boards=new HashMap<>();
        this.chat.setVisible(false);
        this.closeGroupChat.setVisible(false);
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
        this.objective2.setDisable(true);
        this.notifications.clear();
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

    private void setInvisible(){
        this.blue.setVisible(false);
        this.red.setVisible(false);
        this.yellow.setVisible(false);
        this.green.setVisible(false);
    }
    @FXML
    private void onClickRed(MouseEvent mouseEvent){
        clientController.PickColor("RED");
        setInvisible();
        this.notifications.clear();

    }
    @FXML
    private void onClickBlue(MouseEvent mouseEvent){
        clientController.PickColor("BLUE");
        setInvisible();
        this.notifications.clear();
    }
    @FXML
    private void onClickGreen(MouseEvent mouseEvent){
        clientController.PickColor("GREEN");
        setInvisible();
        this.notifications.clear();
    }
    @FXML
    private void onClickYellow(MouseEvent mouseEvent){
        clientController.PickColor("YELLOW");
        setInvisible();
        this.notifications.clear();
    }

    @FXML
    private void onClickGoBack(MouseEvent mouseEvent){
        this.p1.setVisible(false);
        this.p2.setVisible(false);
        this.p3.setVisible(false);
        this.p4.setVisible(false);
        this.game.setVisible(true);
        this.goBack1.setVisible(false);
        this.goBack2.setVisible(false);
        this.goBack3.setVisible(false);
        this.goBack4.setVisible(false);
        this.chat.setVisible(false);
        this.closeGroupChat.setVisible(false);

    }
    public void drawAvailableColors(ArrayList<Color> colors){
        this.color.setText("Pick a color");

        for(int i=0; i<colors.size(); i++){
            if(colors.get(i).equals(Color.BLUE)){
                this.blue.setVisible(true);
                this.blue.setText("");
            }
            if(colors.get(i).equals(Color.GREEN)){
                this.green.setVisible(true);
                this.green.setText("");
            }
            if(colors.get(i).equals(Color.YELLOW)){
                this.yellow.setVisible(true);
                this.yellow.setText("");
            }
            if(colors.get(i).equals(Color.RED)){
                this.red.setVisible(true);
                this.red.setText("");
            }
        }
    }

    public void drawFinalColors(Map<String,Color> colors, ArrayList<String> players){
        this.color.setText("Final colors");
        this.blue.setVisible(false);
        this.green.setVisible(false);
        this.red.setVisible(false);
        this.yellow.setVisible(false);
        for(int i =0; i<players.size(); i++){
            if(colors.get(players.get(i)).equals(Color.BLUE)){
                this.blue.setVisible(true);
                this.blue.setDisable(true);
                this.blue.setText(players.get(i));
            }
            if(colors.get(players.get(i)).equals(Color.GREEN)){
                this.green.setVisible(true);
                this.green.setDisable(true);
                this.green.setText(players.get(i));
            }
            if(colors.get(players.get(i)).equals(Color.YELLOW)){
                this.yellow.setVisible(true);
                this.yellow.setDisable(true);
                this.yellow.setText(players.get(i));
            }
            if(colors.get(players.get(i)).equals(Color.RED)){
                this.red.setVisible(true);
                this.red.setDisable(true);
                this.red.setText(players.get(i));
            }
        }
    }

    @FXML
    private Button player1;
    @FXML
    private Button player2;
    @FXML
    private Button player3;
    @FXML
    private Button player4;
@FXML
private Button goBack1;
    @FXML
    private Button goBack2;
    @FXML
    private Button goBack3;
    @FXML
    private Button goBack4;

    @FXML
    private ScrollPane game;
    private Map<String,Pane> boards;
    public void DrawBoard(PlayableCard[][] board, String player) {

//        // Posizioniamo la carta centrale al centro della finestra
        double startX = 960;
         double startY = 540;
         int cont=0;
//        // Usare una griglia virtuale per posizionare le carte
               for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++){
                if ((row + col) % 2 == 0) {
                    Image card1 = new Image(getClass().getResource(findFrontUrl(0)).toExternalForm());
                    ImageView imageView1 = new ImageView(card1);
                    // Calcolare la posizione della carta
                    double x = startX + (col - GRID_SIZE / 2) * PADDING_X;
                    double y = startY + (row - GRID_SIZE / 2) * PADDING_Y;
                    imageView1.setLayoutX(x);
                    imageView1.setLayoutY(y);
                    imageView1.setFitHeight(38.5);
                    imageView1.setFitWidth(50.0);
                    this.p1.getChildren().add(imageView1);
                }
                cont++;
    }
           }
    }
     private ImageView createCardImageView() {
         Image cardImage = new Image("/it/polimi/ingsw/is24am03/Cards/Backs/80.png"); // Percorso all'immagine della carta
         ImageView imageView = new ImageView(cardImage);
         imageView.setFitWidth(CARD_WIDTH);
         imageView.setFitHeight(CARD_HEIGHT);
         return imageView;
     }
    @FXML
    private void onClickPlayer1(MouseEvent mouseEvent){
        //metodo per mostrare pane giocatore
        this.game.setVisible(false);
        DrawBoard(null,null);
        this.p1.setPrefHeight(1080);
        this.p1.setPrefWidth(1920);
        this.p1.setVisible(true);
        this.goBack1.setVisible(true);
        //devo disegnare il pane di p1



    }

    @FXML
    private void onClickPlayer2(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p2.setPrefHeight(1080);
        this.p2.setPrefWidth(1920);
        this.p2.setVisible(true);
        this.goBack2.setVisible(true);

    }
    @FXML
    private void onClickPlayer3(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p3.setPrefHeight(1080);
        this.p3.setPrefWidth(1920);
        this.p3.setVisible(true);
        this.goBack3.setVisible(true);

    }
    @FXML
    private void onClickPlayer4(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p4.setPrefHeight(1080);
        this.p4.setPrefWidth(1920);
        this.p4.setVisible(true);
        this.goBack4.setVisible(true);

    }

    @FXML
    private void onClickShowGroupChat(MouseEvent mouseEvent){
        this.chat.setVisible(true);
        this.openGroupChat.setVisible(false);
        this.closeGroupChat.setVisible(true);
    }

    @FXML
    private void onClickCloseGroupChat(MouseEvent mouseEvent){
        this.chat.setVisible(false);
        this.openGroupChat.setVisible(true);
        this.closeGroupChat.setVisible(false);
    }


    private static final int GRID_SIZE = 20; // Numero massimo di carte in una direzione
    private static final int CARD_WIDTH = 50; // Larghezza della carta
    private static final double CARD_HEIGHT = 37.5; // Altezza della carta
    private static final double PADDING_X = 39.25; // Padding lungo x (orizzontale)
    private static final double PADDING_Y = 20.5;  // Padding lungo y (verticale)

    @FXML
    private Pane p1;

    @FXML
    private Pane p2;

    @FXML
    private Pane p3;

    @FXML
    private Pane p4;

    @FXML
    private ScrollPane groupChat;

    @FXML
    private SplitPane chat;

    @FXML
    private Button sendGroupChat;

    @FXML
    private Button openGroupChat;

    @FXML
    private Button closeGroupChat;

    @FXML
    private TextField groupText;

    @FXML
    private VBox messages;

    @FXML
    private void onClickSendGroupText(MouseEvent mouseEvent){
        try{
            clientController.sendGroupText(groupText.getText());
            this.groupText.setText("");
        }catch (Exception e){
            this.drawNotifications("Missing arguments");
        }
    }

    public void drawChat(ArrayList<Text> chat, String player){
        if(chat.get(0).getRecipient()==null){
            //stampo tutti i messaggi precedenti se ce ne sono, che abbiano receiver nullo

            if(chat.get(0).getSender().equals(player)){
                Label text=new Label(chat.get(0).getMex());
                text.setText("YOU: " + chat.get(0).getMex());
                text.setWrapText(true);
                messages.getChildren().add(text);
            }
            else{
                Label text=new Label(chat.get(0).getMex());
                text.setText(chat.get(0).getSender()+" : " + chat.get(0).getMex());
                text.setWrapText(true);
                messages.getChildren().add(text);
            }
            groupChat.layout();
            groupChat.setVvalue(1.0);

        }
    /*    else{
            if(chat.get(0).getSender().equals(player)){
                //trovo tutti i messaggi scambiati con il player destinatario
                System.out.println("YOU SENT A TEXT TO "+ chat.get(0).getRecipient() + "THIS IS YOUR PRIVATE CHAT WITH: " + chat.get(0).getRecipient());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messaggi che io ho mandato a lui
                    if(chat.get(i).getSender().equals(player) && chat.get(i).getRecipient().equals(chat.get(0).getRecipient())){
                        System.out.println("YOU " + " : " + chat.get(i).getMex() );}
                    //messaggi che lui ha mandato a me
                    else if(chat.get(i).getSender().equals(chat.get(0).getRecipient()) && chat.get(i).getRecipient().equals(player)){
                        System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex() );}
                }
            }

            if(chat.get(0).getRecipient().equals(player)) {
                //stampo tutti i loro vecchi messaggi
                System.out.println("YOU HAVE A NEW TEXT FROM " + chat.get(0).getSender() + " THIS IS YOUR PRIVATE CHAT WITH: " + chat.get(0).getSender());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messa che io ho mandato a lui
                    if(chat.get(i).getSender().equals(player) && chat.get(i).getRecipient().equals(chat.get(0).getSender())){
                        System.out.println("YOU " + " : " + chat.get(i).getMex());}
                    //mess che lui ha mandato a me
                    else if(chat.get(i).getSender().equals(chat.get(0).getSender()) && chat.get(i).getRecipient().equals(player)){
                        System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex());}
                }

            }
        }*/
    }



    public void drawButtons(ArrayList<String> players){
        //in questo metodo associo a ogni giocatore il suo pane corrispondente
        if(players.size()==2){
            this.player1.setText(players.get(0));
            this.player2.setText(players.get(1));
            boards.put(players.get(0), p1);
            boards.put(players.get(1), p2);
            this.player1.setVisible(true);
            this.player2.setVisible(true);

        }
        else if(players.size()==3){
            this.player1.setText(players.get(0));
            this.player2.setText(players.get(1));
            this.player3.setText(players.get(2));
            boards.put(players.get(0), p1);
            boards.put(players.get(1), p2);
            boards.put(players.get(2), p3);
            this.player1.setVisible(true);
            this.player2.setVisible(true);
            this.player3.setVisible(true);


        }
        else{
            this.player1.setText(players.get(0));
            this.player2.setText(players.get(1));
            this.player3.setText(players.get(2));
            this.player4.setText(players.get(3));
            boards.put(players.get(0), p1);
            boards.put(players.get(1), p2);
            boards.put(players.get(2), p3);
            boards.put(players.get(3), p4);
            this.player1.setVisible(true);
            this.player2.setVisible(true);
            this.player3.setVisible(true);
            this.player4.setVisible(true);

        }
    }

}

    /*public void drawButtons(ArrayList<String> players){
        for(int i=0; i<players.size(); i++){
            Button b=new Button(players.get(i));

        }

     */

