
package it.polimi.ingsw.is24am03.client.view.GUI;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.*;


public class GameViewController extends GUIController implements Initializable {

    private Map<Button, String> chatButtons=new HashMap<>();
    private Map <String, Integer> points;
    private Map<String,Pane> boards;
    private double scaleValue=1.0;
    private final double scaleIncrement=0.1;
    private final double maxScale = 3.0;

    private Map<Integer, ArrayList<Integer>> coords;
    private Map<String, ImageView> pawns;

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView redPawn;

    @FXML
    private ImageView greenPawn;

    @FXML
    private ImageView yellowPawn;

    @FXML
    private ImageView bluePawn;

    @FXML
    private ImageView plank;

    @FXML
    private Pane zoom;

    @FXML
    private Pane p1;

    @FXML
    private Pane p2;

    @FXML
    private Pane p3;

    @FXML
    private Pane p4;

    @FXML
    private ImageView resourceDeck;

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
    private Button sendChatP1;

    @FXML
    private Button openChatP1;
    @FXML
    private Button closeChatP1;

    @FXML
    private TextField chatP1;

    @FXML
    private ScrollPane scrollChatP1;

    @FXML
    private SplitPane splitChatP1;

    @FXML
    private VBox messagesP1;

    @FXML
    private Button sendChatP2;
    @FXML
    private TextField chatP2;

    @FXML
    private Button openChatP2;
    @FXML
    private Button closeChatP2;

    @FXML
    private ScrollPane scrollChatP2;

    @FXML
    private SplitPane splitChatP2;

    @FXML
    private VBox messagesP2;

    @FXML
    private Button sendChatP3;

    @FXML
    private Button openChatP3;

    @FXML
    private Button closeChatP3;
    @FXML
    private TextField chatP3;
    @FXML
    private ScrollPane scrollChatP3;

    @FXML
    private SplitPane splitChatP3;

    @FXML
    private VBox messagesP3;


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
    private TextField color;
    @FXML
    private Button green;
    @FXML
    private Button blue;
    @FXML
    private Button yellow;
    @FXML
    private Button red;

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

    @FXML
    private ScrollPane scrollPane1;

    @FXML
    private ScrollPane scrollPane2;


    @FXML
    private ScrollPane scrollPane3;

    @FXML
    private ScrollPane scrollPane4;

    @FXML
    private void onClickPlaceCard(MouseEvent mouseEvent){
        try{
            clientController.PlaceCard(Integer.parseInt(String.valueOf(this.cardNumber.getText())), Integer.parseInt(String.valueOf(this.indexI.getText())), Integer.parseInt(String.valueOf(this.indexJ.getText())), String.valueOf(this.cardSide.getText()));
            this.cardNumber.setText("");
            this.indexI.setText("");
            this.indexJ.setText("");
            this.cardSide.setText("");
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
        if (hand.size() == 3) {

            Image card = new Image(getClass().getResource(findFrontUrl(hand.get(0).getId())).toExternalForm());
            personalFront1.setImage(card);

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

            Image card1 = new Image(getClass().getResource(findBackUrl(hand.get(0).getId())).toExternalForm());
            personalBack1.setImage(card1);

            Image card2 = new Image(getClass().getResource(findFrontUrl(hand.get(1).getId())).toExternalForm());
            personalFront2.setImage(card2);
            Image card22 = new Image(getClass().getResource(findBackUrl(hand.get(1).getId())).toExternalForm());
            personalBack2.setImage(card22);

            Image card3 = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
            personalFront3.setImage(card3);
            Image card33 = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
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

        if (resourceDeck == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
            this.resourceDeck.setImage(card);
        } else {
                Image card = new Image(getClass().getResource(findBackUrl(resourceDeck.getId())).toExternalForm());
                this.resourceDeck.setImage(card);
        }
        if (goldDeck == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
            this.goldDeck.setImage(card);
        } else {
                Image card = new Image(getClass().getResource(findBackUrl(goldDeck.getId())).toExternalForm());
                this.goldDeck.setImage(card);
        }
        if (card0 == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
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
        if (card1 == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
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
        if (card2 == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
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
        if (card3 == null) {
            Image card = new Image(getClass().getResource("/it/polimi/ingsw/is24am03/Cards/Backs/EMPTY.png").toExternalForm());
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
        String url = null;
        if (id >= 0 && id <= 9) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + (id) + ".png";

        }
        if (id > 9 && id <= 19) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }

        if (id > 19 && id <= 29) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }

        if (id > 29 && id <= 39) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 40 && id <= 49) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 50 && id <= 59) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 60 && id <= 69) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 70 && id <= 79) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 80 && id <= 85) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        if (id >= 86 && id <= 101) {
            url = "/it/polimi/ingsw/is24am03/Cards/Fronts/" + id + ".png";
        }
        return url;
    }

    private String findBackUrl(int id) {
        String url = null;
        if (id >= 0 && id <= 9) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_RED.png";

        }
        if (id > 9 && id <= 19) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_GREEN.png";
        }

        if (id > 19 && id <= 29) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_BLUE.png";
        }

        if (id > 29 && id <= 39) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/RES_PURPLE.png";
        }
        if (id >= 40 && id <= 49) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_RED.png";
        }
        if (id >= 50 && id <= 59) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_GREEN.png";
        }
        if (id >= 60 && id <= 69) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_BLUE.png";
        }
        if (id >= 70 && id <= 79) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/GOLD_PURPLE.png";
        }
        if (id >= 80 && id <= 85) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/" + id + ".png";
        }
        if (id >= 86 && id <= 101) {
            url = "/it/polimi/ingsw/is24am03/Cards/Backs/OBJECTIVE.png";
        }
        return url;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        zoom.setOnScroll(this::handleZoom);
        this.coords=new HashMap<>();
        this.points=new HashMap<>();
        this.plank.setVisible(true);
        this.pawns=new HashMap<>();
        this.initializeCoord();
        p1.setOnScroll(this::handleZoom);
        p2.setOnScroll(this::handleZoom);
        p3.setOnScroll(this::handleZoom);
        p4.setOnScroll(this::handleZoom);
        this.finalOb.setVisible(false);
        this.points=new HashMap<>();
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
        this.scrollPane1.setVisible(false);
        this.p2.setVisible(false);
        this.scrollPane2.setVisible(false);
        this.p3.setVisible(false);
        this.scrollPane3.setVisible(false);
        this.p4.setVisible(false);
        this.scrollPane4.setVisible(false);
        this.goBack1.setVisible(false);
        this.goBack2.setVisible(false);
        this.goBack3.setVisible(false);
        this.goBack4.setVisible(false);
        this.boards=new HashMap<>();
        this.chat.setVisible(false);
        this.bluePawn.setVisible(false);
        this.yellowPawn.setVisible(false);
        this.greenPawn.setVisible(false);
        this.redPawn.setVisible(false);
        this.closeGroupChat.setVisible(false);
        this.openChatP1.setVisible(false);
        this.openChatP2.setVisible(false);
        this.openChatP3.setVisible(false);
        this.splitChatP1.setVisible(false);
        this.splitChatP2.setVisible(false);
        this.splitChatP3.setVisible(false);
    }


    private void handleZoom(ScrollEvent event){
        double deltaY = event.getDeltaY();
        if (deltaY < 0) {
            scaleValue -= scaleIncrement;
        } else {
            scaleValue += scaleIncrement;
        }
        scaleValue = Math.max(1.0, Math.min(scaleValue, maxScale));

        Scale scale = new Scale(scaleValue, scaleValue, event.getX(), event.getY());
        Pane zoomPane = (Pane) event.getSource();
        zoomPane.getTransforms().setAll(scale);
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
        this.scrollPane1.setVisible(false);
        this.p2.setVisible(false);
        this.scrollPane2.setVisible(false);
        this.p3.setVisible(false);
        this.scrollPane3.setVisible(false);
        this.p4.setVisible(false);
        this.scrollPane4.setVisible(false);
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
                this.bluePawn.setVisible(true);
                this.points.put(players.get(i), 0);
                this.pawns.put(players.get(i), bluePawn);
            }
            if(colors.get(players.get(i)).equals(Color.GREEN)){
                this.green.setVisible(true);
                this.green.setDisable(true);
                this.green.setText(players.get(i));
                this.greenPawn.setVisible(true);
                this.points.put(players.get(i), 0);
                this.pawns.put(players.get(i), greenPawn);
            }
            if(colors.get(players.get(i)).equals(Color.YELLOW)){
                this.yellow.setVisible(true);
                this.yellow.setDisable(true);
                this.yellow.setText(players.get(i));
                this.yellowPawn.setVisible(true);
                this.points.put(players.get(i), 0);
                this.pawns.put(players.get(i),yellowPawn);
            }
            if(colors.get(players.get(i)).equals(Color.RED)){
                this.red.setVisible(true);
                this.red.setDisable(true);
                this.red.setText(players.get(i));
                this.redPawn.setVisible(true);
                this.points.put(players.get(i), 0);
                this.pawns.put(players.get(i), redPawn);
            }
        }
    }

    public void drawPoints(String player, int points){
        this.points.put(player, points);
        ArrayList<Integer> coord =this.coords.get(points);
        this.pawns.get(player).setLayoutX(coord.get(0));
        this.pawns.get(player).setLayoutY(coord.get(1));

    }

    public void drawBoard(PlayableCard[][] board, String player) {
        double startX = 960;
        double startY = 540;
        final int CARD_WIDTH = 50;
        final double CARD_HEIGHT = 38.5;
        final double PADDING_X = 39.25;
        final double PADDING_Y = 22;
        final double LABEL_OFFSET_DIAGONAL = 12; // Offset diagonale per i label
        final double LABEL_GLOBAL_OFFSET_X = -8.5; // Offset globale per x
        final double LABEL_GLOBAL_OFFSET_Y = -8.5; // Offset globale per y
        int centerX = board.length / 2;
        int centerY = board[0].length / 2;
        Set<Position> occupiedPositions = new HashSet<>();
        Set<Position> validPositions = new HashSet<>();
        Map<Position, Label> positionLabels = new HashMap<>();
    int z;
        // Aggiunta dei label per gli angoli visibili
        for (int level = 0; level <= Math.max(centerX, centerY); level++) {
            for (int x = centerX - level; x <= centerX + level; x++) {
                for (int y = centerY - level; y <= centerY + level; y++) {
                    if (x >= 0 && x < board.length && y >= 0 && y < board[0].length && (Math.abs(x - centerX) == level || Math.abs(y - centerY) == level)) {
                        PlayableCard card = board[x][y];
                        if (card != null) {

                            ArrayList<Boolean> visibleCorners = new ArrayList<>();
                            if (card.getFace()) {
                                for (z = 0; z < 4; z++) {
                                    visibleCorners.add(card.getFrontCorner(z).isVisible());
                                }
                            } else {
                                for ( z = 0; z < 4; z++) {
                                    visibleCorners.add(card.getBackCorner(z).isVisible());
                                }
                            }

                            double posX = startX + (y - centerY) * PADDING_X;
                            double posY = startY + (x - centerX) * PADDING_Y;
                            for (z = 0; z < 4; z++) {
                                if (visibleCorners.get(z)) {

                                    int labelX = x;
                                    int labelY = y;

                                    if (z == 0) {
                                        labelX--;
                                        labelY--;
                                    } else if (z == 1) {
                                        labelX--;
                                        labelY++;
                                    } else if (z == 2) {
                                        labelX++;
                                        labelY++;
                                    } else if (z == 3) {
                                        labelX++;
                                        labelY--;
                                    }

                                    // Verifica se l'angolo è visibile prima di aggiungere il label
                                    if (labelX >= 0 && labelX < board.length && labelY >= 0 && labelY < board[0].length) {
                                        String cornerLabel = "(" + labelX + ", " + labelY + ")";
                                        Label label = new Label(cornerLabel);
                                        label.setStyle("-fx-font-size: 8px;");

                                        double labelPosX = posX + LABEL_GLOBAL_OFFSET_X;
                                        double labelPosY = posY + LABEL_GLOBAL_OFFSET_Y;

                                        // Calcola l'offset per posizionare il label vicino all'angolo
                                        if (z == 0) {
                                            labelPosX -= CARD_WIDTH / 2 + LABEL_OFFSET_DIAGONAL;
                                            labelPosY -= CARD_HEIGHT / 2 + LABEL_OFFSET_DIAGONAL;
                                        } else if (z == 1) {
                                            labelPosX += CARD_WIDTH / 2 + LABEL_OFFSET_DIAGONAL;
                                            labelPosY -= CARD_HEIGHT / 2 + LABEL_OFFSET_DIAGONAL;
                                        } else if (z == 2) { // Angolo in basso a DX
                                            labelPosX += CARD_WIDTH / 2 + LABEL_OFFSET_DIAGONAL;
                                            labelPosY += CARD_HEIGHT / 2 + LABEL_OFFSET_DIAGONAL;
                                        } else if (z == 3) { // Angolo in basso a sx
                                            labelPosX -= CARD_WIDTH / 2 + LABEL_OFFSET_DIAGONAL;
                                            labelPosY += CARD_HEIGHT / 2 + LABEL_OFFSET_DIAGONAL;
                                        }

                                        label.setLayoutX(labelPosX);
                                        label.setLayoutY(labelPosY);

                                        // Aggiungi il label alla giusta area giocatore
                                        if (player.equals(player1.getText())) {
                                            this.p1.getChildren().add(label);
                                        } else if (player.equals(player2.getText())) {
                                            this.p2.getChildren().add(label);
                                        } else if (player.equals(player3.getText())) {
                                            this.p3.getChildren().add(label);
                                        } else if (player.equals(player4.getText())) {
                                            this.p4.getChildren().add(label);
                                        }

                                        positionLabels.put(new Position(x, y), label);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int level = 0; level <= Math.max(centerX, centerY); level++) {
            for (int x = centerX - level; x <= centerX + level; x++) {
                for (int y = centerY - level; y <= centerY + level; y++) {
                    if (x >= 0 && x < board.length && y >= 0 && y < board[0].length && (Math.abs(x - centerX) == level || Math.abs(y - centerY) == level)) {
                        PlayableCard card = board[x][y];
                        if (card != null) {
                            Image cardImage;
                            if (card.getFace()) {
                                cardImage = new Image(getClass().getResource(findFrontUrl(card.getId())).toExternalForm());
                            } else {
                                cardImage = new Image(getClass().getResource(findBackUrl(card.getId())).toExternalForm());
                            }
                            ImageView imageView = new ImageView(cardImage);
                            double posX = startX + (y - centerY) * PADDING_X - (CARD_WIDTH / 2);
                            double posY = startY + (x - centerX) * PADDING_Y - (CARD_HEIGHT / 2);
                            imageView.setLayoutX(posX);
                            imageView.setLayoutY(posY);
                            imageView.setFitHeight(CARD_HEIGHT);
                            imageView.setFitWidth(CARD_WIDTH);

                            // Aggiungi l'immagine alla giusta area giocatore
                            if (player.equals(player1.getText())) {
                                this.p1.getChildren().add(imageView);
                            } else if (player.equals(player2.getText())) {
                                this.p2.getChildren().add(imageView);
                            } else if (player.equals(player3.getText())) {
                                this.p3.getChildren().add(imageView);
                            } else if (player.equals(player4.getText())) {
                                this.p4.getChildren().add(imageView);
                            }

                            Position pos = new Position(x, y);
                            occupiedPositions.add(pos);
                        }
                    }
                }
            }
        }

    }


    private static class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }











    @FXML
    private void onClickPlayer1(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p1.setPrefHeight(1080);
        this.p1.setPrefWidth(1920);
        this.p1.setVisible(true);
        this.scrollPane1.setVisible(true);
        this.goBack1.setVisible(true);
    }

    @FXML
    private void onClickPlayer2(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p2.setPrefHeight(1080);
        this.p2.setPrefWidth(1920);
        this.p2.setVisible(true);
        this.scrollPane2.setVisible(true);
        this.goBack2.setVisible(true);
    }
    @FXML
    private void onClickPlayer3(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p3.setPrefHeight(1080);
        this.p3.setPrefWidth(1920);
        this.p3.setVisible(true);
        this.scrollPane3.setVisible(true);
        this.goBack3.setVisible(true);
    }
    @FXML
    private void onClickPlayer4(MouseEvent mouseEvent){
        this.game.setVisible(false);
        this.p4.setPrefHeight(1080);
        this.p4.setPrefWidth(1920);
        this.p4.setVisible(true);
        this.scrollPane4.setVisible(true);
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

    @FXML
    private void onClickOpenPrivateChat(MouseEvent mouseEvent){
        if(mouseEvent.getSource() ==openChatP1){
            this.splitChatP1.setVisible(true);
        }
        else if(mouseEvent.getSource()==openChatP2){
            this.splitChatP2.setVisible(true);
        }
        else{
            this.splitChatP3.setVisible(true);
        }
    }

    @FXML
    private void onClickClosePrivateChat(MouseEvent mouseEvent){
        if(mouseEvent.getSource()==closeChatP1){
            this.splitChatP1.setVisible(false);
        }
        else if(mouseEvent.getSource()==closeChatP2){
            this.splitChatP2.setVisible(false);
        }
        else{
            this.splitChatP3.setVisible(false);
        }
    }

    @FXML
    private void onClickSendGroupText(MouseEvent mouseEvent){
        try{
            clientController.sendGroupText(groupText.getText());
            this.groupText.setText("");
        }catch (Exception e){
            this.drawNotifications("Missing arguments");
        }
    }

    @FXML
    private void onClickSendPrivateChat(MouseEvent mouseEvent){

            if(mouseEvent.getSource()== sendChatP1){
                try{
                    clientController.sendPrivateText(chatButtons.get(sendChatP1), chatP1.getText());
                }catch (Exception e){
                    this.drawNotifications("Missing arguments");
                }
            }
            else if(mouseEvent.getSource()==sendChatP2){
                try{
                    clientController.sendPrivateText(chatButtons.get(sendChatP2), chatP2.getText());
                }catch (Exception e){
                    this.drawNotifications("Missing arguments");
                }
            }
            else{
                try{
                    clientController.sendPrivateText(chatButtons.get(sendChatP3), chatP3.getText());
                }catch (Exception e){
                    this.drawNotifications("Missing arguments");
                }
            }

    }

    public void drawChat(ArrayList<Text> chat, String player){
        if(chat.get(0).getRecipient()==null){
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
                this.drawNotifications("New group text, check below!");
            }
            groupChat.layout();
            groupChat.setVvalue(1.0);

        }
        else{
            if(chat.get(0).getSender().equals(player)){
                    String recipient=chat.get(0).getRecipient();
                   Label text=new Label(chat.get(0).getMex());
                   text.setText("YOU: "+ chat.get(0).getMex());
                   text.setWrapText(true);
                    if(messagesP1.getId().equals(recipient)){
                        messagesP1.getChildren().add(text);
                        scrollChatP1.layout();
                        scrollChatP1.setVvalue(1.0);
                    }
                    else if(messagesP2.getId().equals(recipient)){
                        messagesP2.getChildren().add(text);
                        scrollChatP2.layout();
                        scrollChatP2.setVvalue(1.0);
                    }
                    else{
                        messagesP3.getChildren().add(text);
                        scrollChatP3.layout();
                        scrollChatP3.setVvalue(1.0);
                    }

            }

            if(chat.get(0).getRecipient().equals(player)) {
                this.drawNotifications("YOU HAVE A NEW TEXT FROM " + chat.get(0).getSender());
                String sender=chat.get(0).getSender();
                String mex=chat.get(0).getMex();
                Label text=new Label();
                text.setText(sender + " : " + mex);
                text.setWrapText(true);
                if(messagesP1.getId().equals(sender)){
                    messagesP1.getChildren().add(text);
                    scrollChatP1.layout();
                    scrollChatP1.setVvalue(1.0);
                }
                else if(messagesP2.getId().equals(sender)){
                    messagesP2.getChildren().add(text);
                    scrollChatP2.layout();
                    scrollChatP2.setVvalue(1.0);
                }
                else{
                    messagesP3.getChildren().add(text);
                    scrollChatP3.layout();
                    scrollChatP3.setVvalue(1.0);
                }
                }

            }
        }

    public void drawButtons(ArrayList<String> players, String player){
        this.boards=new HashMap<>();
        ArrayList<String> nicknames=new ArrayList<>();
        for(String s: players){
            if(!s.equals(player)){
                nicknames.add(s);
            }
        }
        if(players.size()==2){
            this.player1.setText(players.get(0));
            this.player2.setText(players.get(1));
            boards.put(players.get(0), p1);
            boards.put(players.get(1), p2);
            this.openChatP1.setText("Chat with " +nicknames.get(0));
            chatButtons.put(sendChatP1, nicknames.get(0));
            this.messagesP1.setId(nicknames.get(0));
            this.scrollChatP1.setId(nicknames.get(0));
            this.openChatP1.setVisible(true);
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
            this.openChatP1.setText("Chat with " +nicknames.get(0));
            chatButtons.put(sendChatP1,nicknames.get(0));
            this.messagesP1.setId(nicknames.get(0));
            this.scrollChatP1.setId(nicknames.get(0));
            this.openChatP2.setText("Chat with "+nicknames.get(1));
            chatButtons.put( sendChatP2,nicknames.get(1));
            this.messagesP2.setId(nicknames.get(1));
            this.scrollChatP2.setId(nicknames.get(1));
            this.openChatP1.setVisible(true);
            this.openChatP2.setVisible(true);
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
            this.openChatP1.setText("Chat with " +nicknames.get(0));
            chatButtons.put( sendChatP1,nicknames.get(0));
            this.messagesP1.setId(nicknames.get(0));
            this.scrollChatP1.setId(nicknames.get(0));
            this.openChatP2.setText("Chat with "+nicknames.get(1));
            chatButtons.put(sendChatP2,nicknames.get(1));
            this.messagesP2.setId(nicknames.get(1));
            this.scrollChatP2.setId(nicknames.get(1));
            this.openChatP3.setText("Chat with "+nicknames.get(2));
            chatButtons.put( sendChatP3,nicknames.get(2));
            this.messagesP3.setId(nicknames.get(2));
            this.scrollChatP3.setId(nicknames.get(2));
            this.openChatP1.setVisible(true);
            this.openChatP2.setVisible(true);
            this.openChatP3.setVisible(true);
            this.player1.setVisible(true);
            this.player2.setVisible(true);
            this.player3.setVisible(true);
            this.player4.setVisible(true);
        }
    }

    public void restoreChat(ArrayList<Text> chat, String player){
        for(int i=chat.size()-1; i>=0; i--){
            if(chat.get(i).getRecipient()==null){
                if(chat.get(i).getSender().equals(player)){
                    Label text=new Label(chat.get(i).getMex());
                    text.setText("YOU: " + chat.get(i).getMex());
                    text.setWrapText(true);
                    messages.getChildren().add(text);

                }
                else{
                    Label text=new Label(chat.get(i).getMex());
                    text.setText(chat.get(i).getSender()+" : " + chat.get(i).getMex());
                    text.setWrapText(true);
                    messages.getChildren().add(text);

                }
                groupChat.layout();
                groupChat.setVvalue(1.0);

            }

            else{
                if(chat.get(i).getRecipient().equals(messagesP1.getId()) && chat.get(i).getSender().equals(player)
                || chat.get(i).getRecipient().equals(player) && chat.get(i).getSender().equals(messagesP1.getId())){
                    if(chat.get(i).getSender().equals(player)){
                        Label text=new Label(chat.get(i).getMex());
                        text.setText("YOU: "+ chat.get(i).getMex());
                        text.setWrapText(true);
                            messagesP1.getChildren().add(text);
                            scrollChatP1.layout();
                            scrollChatP1.setVvalue(1.0);

                    }

                    else {

                        String sender=chat.get(i).getSender();
                        String mex=chat.get(i).getMex();
                        Label text=new Label();
                        text.setText(sender + " : " + mex);
                        text.setWrapText(true);
                            messagesP1.getChildren().add(text);
                            scrollChatP1.layout();
                            scrollChatP1.setVvalue(1.0);
                    }

                }
                else if(chat.get(i).getRecipient().equals(messagesP2.getId()) && chat.get(i).getSender().equals(player)
                        || chat.get(i).getRecipient().equals(player) && chat.get(i).getSender().equals(messagesP2.getId())){
                    if(chat.get(i).getSender().equals(player)){
                        Label text=new Label(chat.get(i).getMex());
                        text.setText("YOU: "+ chat.get(i).getMex());
                        text.setWrapText(true);
                        messagesP2.getChildren().add(text);
                        scrollChatP2.layout();
                        scrollChatP2.setVvalue(1.0);

                    }
                    else{
                        String sender=chat.get(i).getSender();
                        String mex=chat.get(i).getMex();
                        Label text=new Label();
                        text.setText(sender + " : " + mex);
                        text.setWrapText(true);
                        messagesP2.getChildren().add(text);
                        scrollChatP2.layout();
                        scrollChatP2.setVvalue(1.0);
                    }
                }
                else{
                    if(chat.get(i).getSender().equals(player)){
                        Label text=new Label(chat.get(i).getMex());
                        text.setText("YOU: "+ chat.get(i).getMex());
                        text.setWrapText(true);
                        messagesP3.getChildren().add(text);
                        scrollChatP3.layout();
                        scrollChatP3.setVvalue(1.0);
                    }

                    else{
                        String sender=chat.get(i).getSender();
                        String mex=chat.get(i).getMex();
                        Label text=new Label();
                        text.setText(sender + " : " + mex);
                        text.setWrapText(true);
                        messagesP3.getChildren().add(text);
                        scrollChatP3.layout();
                        scrollChatP3.setVvalue(1.0);

                    }

                }

            }

        }
    }

    private void initializeCoord(){
        this.coords.put(0, Points.ZERO.coordPoints());
       this.coords.put(1,Points.ONE.coordPoints());
       this.coords.put(2, Points.TWO.coordPoints());
        this.coords.put(3, Points.THREE.coordPoints());
        this.coords.put(4, Points.FOUR.coordPoints());
        this.coords.put(5, Points.FIVE.coordPoints());
        this.coords.put(6, Points.SIX.coordPoints());
        this.coords.put(7, Points.SEVEN.coordPoints());
        this.coords.put(8, Points.EIGHT.coordPoints());
        this.coords.put(9, Points.NINE.coordPoints());
        this.coords.put(10, Points.TEN.coordPoints());
        this.coords.put(11, Points.ELEVEN.coordPoints());
        this.coords.put(12, Points.TWELVE.coordPoints());
        this.coords.put(13, Points.THIRTEEN.coordPoints());
        this.coords.put(14, Points.FOURTEEN.coordPoints());
        this.coords.put(15, Points.FIFTEEN.coordPoints());
        this.coords.put(16, Points.SIXTEEN.coordPoints());
        this.coords.put(17, Points.SEVENTEEN.coordPoints());
        this.coords.put(18, Points.EIGHTEEN.coordPoints());
        this.coords.put(19, Points.NINETEEN.coordPoints());
        this.coords.put(20, Points.TWENTY.coordPoints());
        this.coords.put(21, Points.TWENTY_ONE.coordPoints());
        this.coords.put(22, Points.TWENTY_TWO.coordPoints());
        this.coords.put(23, Points.TWENTY_THREE.coordPoints());
        this.coords.put(24, Points.TWENTY_FOUR.coordPoints());
        this.coords.put(25, Points.TWENTY_FIVE.coordPoints());
        this.coords.put(26, Points.TWENTY_SIX.coordPoints());
        this.coords.put(27, Points.TWENTY_SEVEN.coordPoints());
        this.coords.put(28, Points.TWENTY_EIGHT.coordPoints());
        this.coords.put(29, Points.TWENTY_NINE.coordPoints());
    }

    @Override
    public void postNotification(String title, String desc) {

    }
}




