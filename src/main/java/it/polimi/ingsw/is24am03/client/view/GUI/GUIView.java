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

    /**
     * sets the correct client
     * @param client client connected
     */
    public static void setClient(Client client) {
        controller = client;
    }

    /**
     * Starts the gui application
     * @param stage sets the correct scene type
     */
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

    /**
     * Shows an alert when an exception is thrown
     * @param title title of the alert
     * @param content alert's details
     */
    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    /**
     * The main method calls the start method
     * @param args contains ip address port-number connectionType
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Draws a scene
     * @param sceneType the type of scene to draw.
     */
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

    /**
     * Draws a certain player's board
     * @param playableCards a 2D array representing the playable cards on the board.
     */
    @Override
    public void drawBoard(PlayableCard[][] playableCards) {
        // METHOD IMPLEMENTED IN CLI VIEW
    }

    /**
     * Draws the board of a certain player, who just placed a card
     * @param playableCards the player's board
     * @param player nickname who placed a card
     */
    public void drawBoard(PlayableCard[][] playableCards, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawBoard(playableCards,player);
        });
    }


    /**
     * Draws the personal cards of a player
     * @param hand an ArrayList of ResourceCard representing the player's hand.
     */
    @Override
    public void drawHand(ArrayList<ResourceCard> hand) {
    Platform.runLater(()->{
        GameViewController gameViewController=fxmlLoader.getController();
        gameViewController.drawHand(hand);
    });

    }

    /**
     * Draws an obejctive card
     * @param o the ObjectiveCard to draw.
     */
    @Override
    public void drawObjective(ObjectiveCard o) {
        //IMPLEMENTED IN CLI VIEW
    }

    /**
     * Draws the first objective card assigned to a player
     * @param o secret objective card
     */
    private void drawObjective1(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawObjective1(o);
        });
    }

    /**
     * Draws the second objective card assigned to a player
     * @param o secret objective card
     */
    private  void  drawObjective2(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawObjective2(o);
        });

    }

    /**
     * Draws the starting card assigned to a player
     * @param startingCard the StartingCard to draw.
     */
    @Override
    public void drawStarting(StartingCard startingCard) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawStarting(startingCard);
        });
    }

    /**
     * Draws the cards located in the common table
     * @param playerPoints a map of player names to their points.
     * @param resourceDeck the top card of the resource deck.
     * @param goldDeck the top card of the gold deck.
     * @param card0 the first card on the table.
     * @param card1 the second card on the table.
     * @param card2 the third card on the table.
     * @param card3 the fourth card on the table.
     */
    @Override
    public void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawTable(resourceDeck,goldDeck,card0,card1,card2,card3);
        });
    }

    /**
     * Draws the available colors from which a player can choose
     * @param colors an ArrayList of available colors.
     */
    @Override
    public void drawAvailableColors(ArrayList<Color> colors) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawAvailableColors(colors);
        });
    }

    /**
     * Notifies that a player joined the game
     * @param joinedPlayer the nickname of the joined player.
     */
    @Override
    public void notifyJoinedPlayer(String joinedPlayer) {
        //IMPLEMENTED IN CLI VIEW
    }

    /**
     * Draws a string which contains the winners of the game
     * @param winners an ArrayList of the winners' nicknames.
     */
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

    /**
     * Builds the string used to notify about the turn order
     * @param order an ArrayList of the players' nicknames in turn order.
     * @param player the nickname of the current player.
     */
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

    /**
     * Draws the turn order of the game
     * @param s string built with the turn order of the game
     */
    public void drawTurnOrder(String s){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawTurnOrder(s);
        });
    }

    /**
     * Draws principal buttons in the scene
     * @param order turn order
     * @param player the local player
     */
    public void drawButtons(ArrayList<String> order, String player){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawButtons(order, player);
        });
    }

    /**
     * Notifies about the current player. If the current player is the local player,based on the game state the notification will be different
     * @param current the nickname of the current player.
     * @param boards a map of player names to their board states.
     * @param player the nickname of the player.
     * @param hand an ArrayList of the player's hand cards.
     * @param gamestate the current game state.
     */
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

    /**
     * Draws the current player
     * @param current current player
     */

    public void drawCurrent(String current){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawCurrent(current);
        });
    }

    /**
     * Notifies that a player has crashed
     * @param username the nickname of the crashed player.
     */
    @Override
    public void notifyCrashedPlayer(String username) {
        this.printNotifications(username+" has crashed");
    }

    /**
     * Notifies that the game changes its state
     * @param gameState the new game state.
     */
    @Override
    public void notifyChangeState(State gameState) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawState(gameState);
        });
    }

    /**
     * Notifies that a player rejoined the game
     * @param rejoinedPlayer the nickname of the rejoined player.
     */
    @Override
    public void notifyRejoinedPlayer(String rejoinedPlayer) {
        this.printNotifications(rejoinedPlayer+" has rejoined the game");
    }

    /**
     * Notifies that a player placed a card and his board has changed
     * @param player the nickname of the player whose board changed.
     * @param nickname the nickname of the player.
     * @param boards a map of player names to their board states.
     */
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

    /**
     * Manages the updated view of the starting cards
     */
    private void updateStarting(){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.updateStarting();
        });
    }

    /**
     * Handles the update about the points of a player
     * @param player the nickname of the player.
     * @param points the updated points of the player.
     */
    @Override
    public void ReceiveUpdateOnPoints(String player, int points) {
        this.printNotifications(player + "has reached" + points);
        this.drawPoints(player,points);

    }

    /**
     * Draws points of a certain player
     * @param player nickname of the player who scored those points
     * @param points updated points
     */
    private void drawPoints(String player, int points){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawPoints(player, points);
        });
    }

    /**
     * Notifies about a change in the personal cards of a player
     * @param p an ArrayList of the player's personal cards.
     */
    @Override
    public void NotifyChangePersonalCards(ArrayList<ResourceCard> p) {
        this.drawHand(p);
    }

    /**
     * Notifies about a chosen objective card
     * @param o the chosen ObjectiveCard.
     */
    @Override
    public void notifyChoiceObjective(ObjectiveCard o) {
        this.drawFinalObjective(o);


    }

    /**
     * Draws the secret objective card
     * @param o card chosen by the player
     */
    public void drawFinalObjective(ObjectiveCard o){
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawFinalObjective(o);
        });
    }


    /**
     * Notifies about the cards assigned to a certain player
     * @param hand an ArrayList of ResourceCard representing the player's hand.
     * @param startingCard the player's StartingCard.
     * @param o1 the first ObjectiveCard.
     * @param o2 the second ObjectiveCard.
     */
    @Override
    public void notifyFirstHand(ArrayList<ResourceCard> hand, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2) {
        this.drawHand(hand);
        this.drawStarting(startingCard);
        this.drawObjective1(o1);
        this.drawObjective2(o2);
    }

    /**
     * Draws the common objective cards
     * @param o1 first objective card
     * @param o2 second objective card
     */
    private void drawCommonObjective(ObjectiveCard o1, ObjectiveCard o2){
        ArrayList<ObjectiveCard> commons=new ArrayList<>();
        commons.add(o1);
        commons.add(o2);
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawCommonObjective(commons);
        });
    }

    /**
     * Notifies about the common objective cards
     * @param objectiveCard1 the first common ObjectiveCard.
     * @param objectiveCard2 the second common ObjectiveCard.
     */
    @Override
    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2) {
        this.drawCommonObjective(objectiveCard1,objectiveCard2);
    }

    /**
     * Notifies about the common cards of the game
     * @param points a map of player names to their points.
     * @param topResDeck the top card of the resource deck.
     * @param topGoldDeck the top card of the gold deck.
     * @param tableCard1 the first card on the table.
     * @param tableCard2 the second card on the table.
     * @param tableCard3 the third card on the table.
     * @param tableCard4 the fourth card on the table.
     */
    @Override
    public void updateCommonTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4) {
        this.drawTable(points, topResDeck,topGoldDeck,tableCard1,tableCard2,tableCard3,tableCard4);
    }

    /**
     * Notifies that the game is about to start
     */
    @Override
    public void NotifyNumbersOfPlayersReached() {
        //IMPLEMENTED IN CLI VIEW
    }

    /**
     * Notifies about the starting of the last round
     */
    @Override
    public void NotifyLastRound() {
        this.printNotifications("Last round is starting, during this round drawing won't be allowed");
    }

    /**
     * Notifies about the colors from which a player can choose
     * @param colors an ArrayList of available colors.
     */
    @Override
    public void notifyAvailableColors(ArrayList<Color> colors) {
        this.drawAvailableColors(colors);
    }


    /**
     * Notifies about the final colors chosen by the players
     * @param colors a map of player names to their chosen colors.
     * @param players an ArrayList of the players' nicknames.
     */
    @Override
    public void notifyFinalColors(Map<String, Color> colors, ArrayList<String> players) {
        this.drawFinalColors(colors, players);
    }

    /**
     * Handles the drawing of final colors
     * @param colors a map of player names to their chosen colors.
     * @param players an ArrayList of the players' nicknames.
     */
    @Override
    public void drawFinalColors(Map<String, Color> colors, ArrayList<String> players) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawFinalColors(colors, players);
        });
    }

    /**
     * Updates a crashed player after rejoin
     * @param nickname the nickname of the crashed player.
     * @param player the nickname of the current player.
     * @param gameState the current game state.
     * @param hand an ArrayList of the player's hand cards.
     * @param objectiveCard the player's objective card.
     * @param boards a map of player names to their board states.
     * @param points a map of player names to their points.
     * @param players an ArrayList of the players' nicknames.
     * @param objectiveCards an ArrayList of the player's objective cards.
     * @param color the player's color.
     * @param table an ArrayList of the resource cards on the table.
     */
    @Override
    public void UpdateCrashedPlayer(String nickname, String player, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table) {
    }

    /**
     *
     * @param points a map of player names to their points.
     * @param commons an ArrayList of common resource cards.
     */
    @Override
    public void UpdateFirst(Map<String, Integer> points, ArrayList<ResourceCard> commons) {
        this.drawTable(points, commons.get(0), commons.get(1), commons.get(2), commons.get(3), commons.get(4), commons.get(5));
    }

    /**
     *
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player sending the message.
     */
    @Override
    public void addGroupText(ArrayList<Text> chat, String player) {
        this.drawChat(chat, player);

    }

    /**
     * Handles an update of the player chat, both private and group
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player viewing the chat.
     */
    @Override
    public void drawChat(ArrayList<Text> chat, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawChat(chat, player);
        });
    }

    /**
     * Handles a message that needs to be printed
     * @param message the message to print.
     */
    @Override
    public void printNotifications(String message) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.drawNotifications(message);
        });
    }

    /**
     * Shows a confirmation message when a player joins the game successfully
     */
    @Override
    public void confirmJoin() {
        //IMPLEMENTED IN CLI VIEW
    }

    /**
     * Shows a confirmation message when a player correctly creates a game
     */
    public void confirmCreate(){

    }

    /**
     * Used to draw an error after an exception is thrown
     * @param message the error message to draw.
     */
    public void drawError(String message){
        showAlert("Error", message);
    }

    /**
     * Method use to restore a chat after a player has rejoined the game
     * @param chat an ArrayList of Text messages in the chat.
     * @param player the nickname of the player viewing the chat.
     */
    @Override
    public void restoreChat(ArrayList<Text> chat, String player) {
        Platform.runLater(()->{
            GameViewController gameViewController=fxmlLoader.getController();
            gameViewController.restoreChat(chat, player);
        });
    }

    /**
     * Prints the updated points
     * @param updatedPoints a map of player names to their updated points.
     */
    public void printUpdatedPoints(Map<String, Integer>updatedPoints){
        //IMPLEMENTED IN CLI VIEW
    }
}
