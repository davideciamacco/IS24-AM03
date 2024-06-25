package it.polimi.ingsw.is24am03.client.view.GUI;

import it.polimi.ingsw.is24am03.SceneType;
import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.client.connection.Client;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;
import java.io.IOException;
import java.util.*;

/**
 * Gui Application Class
 */
public class GUIView extends Application implements ViewInterface {
    private static Client controller;
    private GUIController guiController;
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Stage stage;
    private String connectionType;

    public static void setClient(Client client) {
        controller = client;
    }
    
    @Override
    public void start(Stage stage) {
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();
        controller.setGUI(this);

        this.stage = stage;

        this.stage.setFullScreen(true);
        this.stage.getIcons().add(new Image(GUIView.class.getResource("/it/polimi/ingsw/is24am03/logo.png").toExternalForm()));



        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

                var dockIcon = defaultToolkit.getImage(getClass().getResource("/it/polimi/ingsw/is24am03/logo.png"));

                taskbar.setIconImage(dockIcon);
            }
        }
        this.stage.setMinWidth(750);
        this.stage.setMinHeight(750);

        fxmlLoader = new FXMLLoader(GUIView.class.getResource("/it/polimi/ingsw/is24am03/login-view.fxml"));

        try {
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 1500, 750);
            guiController = fxmlLoader.getController();
            guiController.setConnectionHandler(controller);
            this.stage.setTitle("Codex Naturalis");
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e) {
        }

    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void drawScene(SceneType sceneType) {
        Platform.runLater(() -> {
            String fxmlPath = "";
            switch (sceneType) {
                case WAITING -> fxmlPath="/it/polimi/ingsw/is24am03/lobby-view.fxml";
                case GAME -> fxmlPath="/it/polimi/ingsw/is24am03/game-view.fxml";

            }
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(GUIView.class.getResource(fxmlPath));

            try {
                Parent newRoot = fxmlLoader.load();
                scene.setRoot(newRoot);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Loading Error", "Unable to load the FXML file.");
            }

            guiController = fxmlLoader.getController();
            guiController.setStage(stage);
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void drawBoard(PlayableCard[][] playableCards) {

    }

    public void drawBoard(PlayableCard[][] playableCards, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawBoard(playableCards,player);
        });
    }



    @Override
    public void drawHand(ArrayList<ResourceCard> hand) {
    Platform.runLater(()->{
        GameViewController gameViewController=fxmlLoader.getController();
        gameViewController.drawHand(hand);
    });

    }

    @Override
    public void drawObjective(ObjectiveCard o) {
    }

    private void drawObjective1(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawObjective1(o);
        });
    }
    private  void  drawObjective2(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawObjective2(o);
        });

    }

    @Override
    public void drawStarting(StartingCard startingCard) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawStarting(startingCard);
        });
    }

    @Override
    public void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawTable(playerPoints,resourceDeck,goldDeck,card0,card1,card2,card3);
        });
    }

    @Override
    public void drawAvailableColors(ArrayList<Color> colors) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawAvailableColors(colors);
        });
    }

    @Override
    public void notifyJoinedPlayer(String joinedPlayer) {
    }

    @Override
    public void notifyWinners(ArrayList<String> winners) {
        StringBuilder message = new StringBuilder();
        for(int i=0; i< winners.size()-1;i++){
            message.append(winners.get(i)).append("-");
        }
        message.append(winners.getLast());
        message= new StringBuilder("Winners:  " + message);
        this.printNotifications(message.toString());

    }

    @Override
    public void notifyTurnOrder(ArrayList<String> order, String player) {
        StringBuilder message = new StringBuilder();
        for(int i=0; i< order.size()-1;i++){
            message.append(order.get(i)).append("-");
        }
        message.append(order.getLast());
        message= new StringBuilder("Turn order is  " + message);
        this.drawTurnOrder(message.toString());
        this.drawButtons(order, player);

    }
    public void drawTurnOrder(String s){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawTurnOrder(s);
        });
    }
    public void drawButtons(ArrayList<String> order, String player){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawButtons(order, player);
        });
    }

    @Override
    public void notifyCurrentPlayer(String current, Map<String, PlayableCard[][]> boards, String player, ArrayList<ResourceCard> hand, State gamestate) {
        this.drawCurrent(current);

        if(current.equals(player)){
            if(gamestate.equals(State.PLAYING)){
                this.printNotifications("It's your turn, pick a card");
            }
            if(gamestate.equals(State.COLOR)){
                this.printNotifications("It's your turn, pick a color");
            }
            if(gamestate.equals(State.DRAWING)){
                this.printNotifications("Draw a card");
            }
            if(gamestate.equals(State.STARTING)){
                this.printNotifications("It's your turn, choose a side for your starting card");
            }
            if(gamestate.equals(State.OBJECTIVE)){
                this.printNotifications("It's your turn, choose your personal objective");
            }
        }
    }


    public void drawCurrent(String current){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawCurrent(current);
        });
    }

    @Override
    public void notifyCrashedPlayer(String username) {
        this.printNotifications(username+" has crashed");
    }

    @Override
    public void notifyChangeState(State gameState) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawState(gameState);
        });
    }

    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer) {
        this.printNotifications(rejoinedPlayer+" has rejoined the game");
    }

    @Override
    public void notifyChangePlayerBoard(String player, String nickname, Map<String, PlayableCard[][]> boards) {
        if(player.equals(nickname)){
            this.printNotifications("You placed a card successfully");
            this.updateStarting();
            this.drawBoard(boards.get(player),player);
        }
        else{
            this.printNotifications(player + " placed a card");
            this.drawBoard(boards.get(player),player);
        }
    }

    private void updateStarting(){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.updateStarting();
        });
    }

    @Override
    public void ReceiveUpdateOnPoints(String player, int points) {
        this.printNotifications(player + "has reached" + points);
        this.drawPoints(player,points);

    }

    private void drawPoints(String player, int points){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawPoints(player, points);
        });
    }

    @Override
    public void NotifyChangePersonalCards(ArrayList<ResourceCard> p) {
        this.drawHand(p);
    }

    @Override
    public void notifyChoiceObjective(ObjectiveCard o) {
        this.drawFinalObjective(o);


    }

    public void drawFinalObjective(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawFinalObjective(o);
        });
    }



    @Override
    public void notifyFirstHand(ArrayList<ResourceCard> hand, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) {
        this.drawHand(hand);
        this.drawStarting(startingCard);
        this.drawObjective1(o1);
        this.drawObjective2(o2);
    }

    private void drawCommonObjective(ObjectiveCard o1, ObjectiveCard o2){
        ArrayList<ObjectiveCard> commons=new ArrayList<>();
        commons.add(o1);
        commons.add(o2);
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawCommonObjective(commons);
        });
    }

    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) {
        this.drawCommonObjective(objectiveCard1,objectiveCard2);
    }

    @Override
    public void updateCommonTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4) {
        this.drawTable(points, topResDeck,topGoldDeck,tableCard1,tableCard2,tableCard3,tableCard4);
    }

    @Override
    public void NotifyNumbersOfPlayersReached() {
    }

    @Override
    public void NotifyLastRound() {
        this.printNotifications("Last round is starting, during this round drawing won't be allowed");
    }

    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) {
        this.drawAvailableColors(colors);
    }


    @Override
    public void notifyFinalColors(Map<String, Color> colors, ArrayList<String> players) {
        this.drawFinalColors(colors, players);
    }

    @Override
    public void drawFinalColors(Map<String, Color> colors, ArrayList<String> players) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawFinalColors(colors, players);
        });
    }

    @Override
    public void UpdateCrashedPlayer(String nickname, String player, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table) {

    }

    @Override
    public void UpdateFirst(Map<String, Integer> points, ArrayList<ResourceCard> commons) {
        this.drawTable(points, commons.get(0), commons.get(1), commons.get(2), commons.get(3), commons.get(4), commons.get(5));
    }

    @Override
    public void addGroupText(ArrayList<Text> chat, String player) {
        this.drawChat(chat, player);

    }

    @Override
    public void drawChat(ArrayList<Text> chat, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawChat(chat, player);
        });
    }

    @Override
    public void printNotifications(String message) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawNotifications(message);
        });
    }

    @Override
    public void confirmJoin() {

    }

    public void confirmCreate(){

    }

    public void drawError(String message){
        showAlert("Error", message);
    }

    @Override
    public void restoreChat(ArrayList<Text> chat, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.restoreChat(chat, player);
        });
    }
}
