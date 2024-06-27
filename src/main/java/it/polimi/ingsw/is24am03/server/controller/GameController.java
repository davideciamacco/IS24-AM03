package it.polimi.ingsw.is24am03.server.controller;

import it.polimi.ingsw.is24am03.Subscribers.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.game.Game;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import javafx.util.Pair;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * The game controller ensures the communication between client controller and server model.
 * Acts as a remote controller for the game.
 */
public class GameController extends UnicastRemoteObject implements RemoteGameController {
    /**
     * Locks used for thread synchronization.
     */
    private final Object gameLock;
    private final Object chatLock;

    private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;

    private ScheduledFuture<?> thread;

    private ArrayList<Pair<String, Long>> heartBeats;

    /**
     * The game model associated with this controller.
     * Using the GameInterface type for gameModel allows flexibility.
     */
    private Game gameModel = null;

    private Map<String, GameSub> gameSubs = new HashMap<>();
    private Map<String, ChatSub> chatSubs = new HashMap<>();
    private Map<String, PlayerBoardSub> playerboardSubs = new HashMap<>();
    private Map<String, PlayerSub> playerSubs = new HashMap<>();

    boolean timer;

    /**
     * Constructs a GameController with the specified game.
     *
     * @throws RemoteException if there is an issue with the remote communication
     */
    public GameController() throws RemoteException {
        heartBeats = new ArrayList<>();
        gameLock= new Object();
        chatLock=new Object();
        startHeartbeatChecker();
        timer=false;
    }

    /**
     * Checks if the game can start and starts it if conditions are met.
     */
    public void canStart(){
        synchronized (gameLock) {
            if (gameModel.getPlayers().size() == gameModel.getNumPlayers()) {
                gameModel.startGame();
            }
        }
    }

    /**
     * Creates a new game with the specified number of players, nickname, and connection type.
     *
     * @param numPlayers the number of players for the game
     * @param nickname the nickname of the player creating the game
     * @param connectionType the type of connection (e.g., "RMI")
     * @throws GameAlreadyCreatedException if a game has already been created
     * @throws IllegalArgumentException if the nickname is blank or the number of players is invalid
     */
    public void createGame(int numPlayers, String nickname, String connectionType) throws GameAlreadyCreatedException {
        synchronized (gameLock) {
            if(nickname.isBlank() || numPlayers<2 || numPlayers>4) throw new IllegalArgumentException();
            if(gameModel!=null) throw new GameAlreadyCreatedException();
            gameModel = new Game(numPlayers, nickname);
            if(connectionType.equals("RMI"))
                heartBeats.add(new Pair<>(nickname, System.currentTimeMillis()));
        }
    }

    /**
     * Checks if a player can select their starting face.
     *
     * @param player the nickname of the player
     * @param face the chosen face ("FRONT" or "BACK")
     * @throws PlayerNotInTurnException if it is not the player's turn
     * @throws InvalidStateException if the game state does not allow selecting a starting face
     * @throws GameNotExistingException if the game does not exist
     * @throws ArgumentException if the face is not "FRONT" or "BACK"
     * @throws UnknownPlayerException if the player is unknown or not part of the game
     */
    public void canSelectStartingFace(String player, String face) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, ArgumentException, UnknownPlayerException{
        synchronized (gameLock) {
            if (gameModel == null)
                throw new GameNotExistingException();
            if(!gameModel.extractNicknames().contains(player)){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected()){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if (!gameModel.getGameState().equals(State.STARTING))
                throw new InvalidStateException("Action not allowed in this state");
            if(!face.equals("FRONT") && !face.equals("BACK"))
                throw new ArgumentException("Face must be 'FRONT' or 'BACK' ");
        }
    }

    /**
     * Selects the starting face for a player.
     *
     * @param player the nickname of the player
     * @param face the chosen face ("FRONT" or "BACK")
     */
    public void selectStartingFace(String player, String face) {
        synchronized (gameLock) {
            boolean faceBoolean = false;
            if (face.equals("FRONT")) {
                faceBoolean = true;
            } else if (face.equals("BACK")) {
                faceBoolean = false;
            }
            gameModel.selectStartingFace(player, faceBoolean);
        }
    }

    /**
     * Checks if a player can set their objective card choice.
     *
     * @param player the nickname of the player
     * @param choice the chosen objective card (1 or 2)
     * @throws PlayerNotInTurnException if it is not the player's turn
     * @throws GameNotExistingException if the game does not exist
     * @throws InvalidStateException if the game state does not allow setting an objective card
     * @throws UnknownPlayerException if the player is unknown or not part of the game
     */
    public void canSetObjectiveCard(String player, int choice) throws PlayerNotInTurnException, GameNotExistingException, InvalidStateException, UnknownPlayerException {
        synchronized (gameLock) {
            if (gameModel == null)
                throw new GameNotExistingException();

            if((!gameModel.extractNicknames().contains(player))|| (gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }

            if(!gameModel.getGameState().equals(State.OBJECTIVE))
                throw new InvalidStateException("Action not allowed in this state");
            if (choice < 1 || choice > 2)
                throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the objective card choice for a player.
     *
     * @param player the nickname of the player
     * @param choice the chosen objective card (1 or 2)
     */
    public void setObjectiveCard(String player, int choice) {
        synchronized (gameLock) {
            gameModel.setObjectiveCard(player, choice);
        }
    }

    /**
     * Checks if a player can draw resources.
     *
     * @param player the nickname of the player
     * @throws PlayerNotInTurnException if it is not the player's turn
     * @throws InvalidStateException if the game state does not allow drawing resources
     * @throws GameNotExistingException if the game does not exist
     * @throws EmptyDeckException if the resource deck is empty
     * @throws UnknownPlayerException if the player is unknown or not part of the game
     */
    public void canDrawResources(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, EmptyDeckException, UnknownPlayerException {
        synchronized (gameLock) {
            if (gameModel == null)
                throw new GameNotExistingException();

            if((!gameModel.extractNicknames().contains(player))||(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player))
                throw new PlayerNotInTurnException();
            if (!gameModel.getGameState().equals(State.DRAWING)){
                throw new InvalidStateException("Action not allowed in this state");}
            if (gameModel.getResourceDeck().isEmpty()){
                throw new EmptyDeckException("Deck selected is empty");
            }
        }
    }

    /**
     * Draws resources for the player from the resource deck.
     *
     * @param player represents the nickname of the player who is drawing resources
     */
    public void drawResources(String player){
        synchronized (gameLock) {
            gameModel.drawResources(player);
        }
    }

    /**
     * Checks if a player can draw gold.
     *
     * @param player the nickname of the player
     * @throws PlayerNotInTurnException


    /**
     * Draws gold for the player from the gold deck.
     *
     //* @param player   the player who is drawing gold
     //* @param goldDeck the gold deck from which gold is drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing gold
     */
    //metodo per fare check che player possa pescare da gold

    /**
     * Checks if the specified player can draw a gold card based on game rules and current state.
     *
     * @param player The nickname of the player attempting to draw a gold card.
     * @throws PlayerNotInTurnException If it's not the player's turn to perform this action.
     * @throws InvalidStateException If the game state does not allow drawing a gold card.
     * @throws GameNotExistingException If the game model is null, indicating the game does not exist.
     * @throws EmptyDeckException If the gold deck is empty and no cards can be drawn.
     * @throws UnknownPlayerException If the specified player is not part of the game or is not connected.
     */
    public void canDrawGold(String player)throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, EmptyDeckException, UnknownPlayerException{
        synchronized (gameLock) {

            if(gameModel == null)
                throw new GameNotExistingException();
            if((!gameModel.extractNicknames().contains(player))||(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }

            if (!gameModel.getGameState().equals(State.DRAWING))
                throw new InvalidStateException("Action not allowed in this state");
            if(gameModel.getGoldDeck().isEmpty()){
                throw new EmptyDeckException("Gold Deck is empty");
            }
        }
    }

    /**
     * Allows the specified player to draw a gold card from the game model.
     * This method assumes that all necessary validations (like player's turn and game state)
     * have been performed before calling this method.
     *
     * @param player The nickname of the player who is drawing the gold card.
     */
    public void drawGold(String player) {
        synchronized (gameLock) {
                gameModel.drawGold(player);
        }
    }


    /**
     * Draws a table card for the player based on the choice provided.
     *
     * @param player the player who is drawing the table card
     * @param choice the choice indicating which table card to draw
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing table cards
     */

    public void canDrawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, NullCardSelectedException, UnknownPlayerException, IllegalArgumentException{

        synchronized (gameLock)
        {
            if(gameModel == null)
                throw new GameNotExistingException();
            if((!gameModel.extractNicknames().contains(player))||(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(choice<1 || choice>4) throw new IllegalArgumentException();
            if(!gameModel.getGameState().equals(State.DRAWING)) throw new InvalidStateException("");
            if(gameModel.getTableCards().get(choice-1)==null){
               throw new NullCardSelectedException();
            }


        }
    }

    /**
     * Allows the specified player to draw a card from the table based on the given choice.
     * This method assumes that all necessary validations (like player's turn and game state)
     * have been performed before calling this method.
     *
     * @param player The nickname of the player who is drawing a card from the table.
     * @param choice The choice indicating which card from the table to draw.
     */
    public void drawTable(String player, int choice){
        synchronized (gameLock)
        {
                gameModel.drawTable(player, choice);
        }
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to be added
     * @throws FullLobbyException if the lobby is already full
     * @throws NicknameAlreadyUsedException if the provided nickname is already used by another player
     */
    public void addPlayer(String player, String ConnectionType) throws FullLobbyException, NicknameAlreadyUsedException, IllegalArgumentException, GameNotExistingException {
        synchronized (gameLock)
        {
            if(gameModel == null)
                throw new GameNotExistingException();

            if(gameModel.getPlayers().size()==gameModel.getNumPlayers()) throw new FullLobbyException();
            if(player.isBlank()) throw new IllegalArgumentException();
            for(Player p: gameModel.getPlayers())
                if(p.getNickname().equals(player)) throw new NicknameAlreadyUsedException();
            gameModel.setNumPlayersConnected(gameModel.getNumPlayersConnected()+1);
            gameModel.addPlayer(player);
            if(ConnectionType.equals("RMI"))
                heartBeats.add(new Pair<>(player, System.currentTimeMillis()));
        }
    }

    /**
     * Places a card on the table for the player.
     *
     * @param player the player placing the card
     * @param choice  the card to be placed
     * @param i      the row index for placement
     * @param j      the column index for placement
     * @param face   whether the card should be placed face up or not
     * @throws PlayerNotInTurnException if the player is not currently in turn
     * @throws InvalidStateException   if the game state is not suitable for placing cards
     */

    public void placeCard(String player, int choice, int i, int j, String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException, ArgumentException, UnknownPlayerException {
        synchronized (gameLock)
        {
            boolean faceBoolean;
            if(gameModel == null)
                throw new GameNotExistingException();
            if((!gameModel.extractNicknames().contains(player))||(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }

            if(!gameModel.getGameState().equals(State.PLAYING)) throw new InvalidStateException("Action not allowed in this state");
            if(choice<1 || choice>3) throw new ArgumentException("Choice must be 1/2/3");
            if(face.equals("FRONT"))
                faceBoolean = true;
            else if(face.equals("BACK"))
                faceBoolean = false;
            else
                throw new ArgumentException("Side choice must be FRONT or BACK");
            try {
                gameModel.placeCard(player, choice-1, i, j, faceBoolean);
            }
            catch(Exception e) {
                throw e;
            }
        }
    }

    /**
     * Checks if the specified player can pick a color for their pawn based on game rules and current state.
     *
     * @param player The nickname of the player attempting to pick a color.
     * @param color The color chosen by the player as a String representation ("RED", "BLUE", "GREEN", "YELLOW").
     * @throws PlayerNotInTurnException If it's not the player's turn to perform this action.
     * @throws InvalidStateException If the game state does not allow picking a color.
     * @throws GameNotExistingException If the game model is null, indicating the game does not exist.
     * @throws ColorAlreadyPickedException If the chosen color has already been picked by another player.
     * @throws UnknownPlayerException If the specified player is not part of the game or is not connected.
     * @throws IllegalArgumentException If the color parameter does not match any valid color.
     */
    public void canPickColor(String player, String color)throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException,ColorAlreadyPickedException, UnknownPlayerException, IllegalArgumentException{
        Color chosenColor;
        boolean flag=false;
        synchronized (gameLock)
        {
            if(gameModel == null)
                throw new GameNotExistingException();
            if((!gameModel.extractNicknames().contains(player))||(gameModel.extractNicknames().contains(player) && !gameModel.findPlayer(player).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(!gameModel.getGameState().equals(State.COLOR)) throw new InvalidStateException("Action not allowed in this state");
            switch(color)
            {
                case "RED":
                    chosenColor= Color.RED;
                    break;
                case "BLUE":
                    chosenColor= Color.BLUE;
                    break;
                case "GREEN":
                    chosenColor= Color.GREEN;
                    break;
                case "YELLOW":
                    chosenColor= Color.YELLOW;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            for(int i=0; i<gameModel.getAvailableColors().size(); i++){
                if(chosenColor.equals(gameModel.getAvailableColors().get(i)))
                    flag=true;
            }
            if(!flag)
                throw new ColorAlreadyPickedException();
        }

    }

    /**
     * Allows the specified player to pick a color for their pawn.
     * This method assumes that all necessary validations (like player's turn and game state)
     * have been performed before calling this method.
     *
     * @param player The nickname of the player who is picking a color.
     * @param color The color chosen by the player as a String representation ("RED", "BLUE", "GREEN", "YELLOW").
     */
    public void pickColor(String player, String color)  {
        Color chosenColor = null;
        synchronized (gameLock)
        {
            switch(color)
            {
                case "RED":
                    chosenColor= Color.RED;
                    break;
                case "BLUE":
                    chosenColor= Color.BLUE;
                    break;
                case "GREEN":
                    chosenColor= Color.GREEN;
                    break;
                case "YELLOW":
                    chosenColor= Color.YELLOW;
                    break;
            }
                gameModel.setColor(chosenColor);
        }
    }

    /**
     * Handles the disconnection of a player from the game.
     * Adjusts game state and notifies remaining players and subscribers accordingly.
     * If only one player remains connected, starts a timer for potential reconnection.
     *
     * @param nickname The nickname of the player who has disconnected.
     */
    public void handleCrashedPlayer(String nickname){
        synchronized (gameLock) {
            if(gameModel.getGameState().equals(State.STARTING) || gameModel.getGameState().equals(State.OBJECTIVE) || gameModel.getGameState().equals(State.COLOR)){
                System.out.println("Disconnessione durante Pre-Partita");
                System.exit(0);
            }
            gameModel.setNumPlayersConnected(gameModel.getNumPlayersConnected() - 1);
            System.out.println(gameModel.getNumPlayersConnected());
            if (gameModel.getNumPlayersConnected() <= 1) {
                startTimer();
                System.out.println("Timer avviato");
            }
            for (int i = 0; i < gameModel.getNumPlayers() && i< gameModel.getPlayers().size(); i++) {
                if (gameModel.getPlayers().get(i).getNickname().equals(nickname)) {
                    gameModel.getPlayers().get(i).setConnected(false);
                }
            }
                for (GameSub gameSub : gameModel.getGameSubs()) {
                        try {
                            if (!gameSub.getSub().equals(nickname)) {
                                gameSub.notifyCrashedPlayer(nickname);
                            }
                        } catch (RemoteException ignored) {
                        }
                    }

            if(gameModel.getNumPlayersConnected()>1)
            {
                if (gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(nickname)) {
                    if (gameModel.getGameState().equals(State.PLAYING))
                        gameModel.nextTurn();
                    else if (gameModel.getGameState().equals(State.DRAWING)) {
                        if (!gameModel.getResourceDeck().isEmpty())
                            gameModel.drawResources(nickname);
                        else if (!gameModel.getGoldDeck().isEmpty())
                            gameModel.drawGold(nickname);
                        else if (gameModel.getTableCards().get(0) == null)
                            gameModel.drawTable(nickname, 1);
                        else if (gameModel.getTableCards().get(1) == null)
                            gameModel.drawTable(nickname, 2);
                        else if (gameModel.getTableCards().get(2) == null)
                            gameModel.drawTable(nickname, 3);
                        else
                            gameModel.drawTable(nickname, 4);

                    }
                }
            } else{
                System.out.println("Partita sospesa");
            }
        }
    }

    /**
     * Reconnects a player to the game using the specified connection type.
     * Updates player connection status, increments connected player count,
     * and manages timers based on current game state and connection type.
     *
     * @param nickname      The nickname of the player reconnecting.
     * @param connectionType The type of connection used ("RMI" or other).
     * @throws InvalidStateException If the game state does not allow player reconnection.
     * @throws GameNotExistingException If the game does not exist.
     * @throws UnknownPlayerException If the player is not recognized as part of the game.
     */
    public void rejoinGame(String nickname, String connectionType) throws InvalidStateException, GameNotExistingException,UnknownPlayerException {
        int check=-1;
        int i=0;
        if(gameModel == null)
            throw new GameNotExistingException();
        while(i<gameModel.getPlayers().size() && check==-1) {
            if (gameModel.getPlayers().get(i).getNickname().equals(nickname) && !gameModel.getPlayers().get(i).getConnected())
                check = i;
            i++;
        }

        if(check==-1){
            throw new UnknownPlayerException("You aren't part of a game");
        }
        if(gameModel.getGameState().equals(State.ENDING))
            throw new InvalidStateException("Action not allowed in this state");
        gameModel.getPlayers().get(check).setConnected(true);
        //System.out.println(gameModel.getNumPlayersConnected());
        gameModel.setNumPlayersConnected(gameModel.getNumPlayersConnected()+1);
        //System.out.println(gameModel.getNumPlayersConnected());
        if(gameModel.getNumPlayersConnected()==2)
            stopTimer();
        if(connectionType.equals("RMI"))
            heartBeats.add(new Pair<>(nickname, System.currentTimeMillis()));
        }

    /**
     * Starts a timer that monitors the game state and ends the game if conditions are met.
     * The timer will only start if it hasn't been started before.
     * If fewer than 2 players are connected during the timer's execution, the game will be ended.
     */

    public void startTimer() {
        long limit = 60;

        Runnable task = () -> {
            if(gameModel.getNumPlayersConnected()<2)
            {
                //gameModel.setTimer(true);
                gameModel.endGame();
                System.out.println("Timer raggiunto.");
            }
            scheduler.shutdown();
        };
        if(!timer) {
            thread = scheduler.schedule(task, limit, TimeUnit.SECONDS);
            timer = true;
        }

    }

    /**
     * Stops the timer and cancels its associated thread. Prints a message indicating that the timer has been interrupted.
     */
    public void stopTimer(){
        timer=false;
        thread.cancel(true);
        System.out.println("Timer interrotto");
    }

    /**
     * Updates the last heartbeat timestamp for the specified player in the list of heartbeats.
     *
     * @param player The player whose heartbeat timestamp needs to be updated.
     */
    public void setLastHeartBeat(String player){
        for(int i=0; i< heartBeats.size(); i++){
            if(heartBeats.get(i).getKey().equals(player))
                heartBeats.set(i, new Pair<>(player, System.currentTimeMillis()));
        }
    }

    /**
     * Checks the heartbeats of players and handles those who have not sent a heartbeat in over 5 seconds.
     * Removes subscriptions and handles crashed players accordingly.
     */
    public void checkHeartBeats(){
        long currentTime = System.currentTimeMillis();
        for (int i=0; i<heartBeats.size(); i++) {
            if (currentTime - heartBeats.get(i).getValue() > 5000) {
                System.out.println("QUI CI ARRIVO");
                removeSub(gameSubs.get(heartBeats.get(i).getKey()));
                System.out.println("Remove 1");
                removeSub(playerSubs.get(heartBeats.get(i).getKey()));
                System.out.println("Remove2");
                removeSub(playerboardSubs.get(heartBeats.get(i).getKey()));
                System.out.println("Remove 3");
                removeSub(chatSubs.get(heartBeats.get(i).getKey()));
                System.out.println("PRIMA DI HANDLE");
                handleCrashedPlayer(heartBeats.get(i).getKey());
                System.out.println("ARRIVO ANCHE QUA");
                heartBeats.remove(i);
                i--;
            }
        }
    }

    /**
     * Starts a periodic heartbeat checker that runs every 5 seconds to monitor player heartbeats.
     * Uses a scheduler to execute the {@code checkHeartBeats} method at fixed intervals.
     */
    private void startHeartbeatChecker() {
        long heartbeatInterval = 5000; // 5 seconds
        heartbeatScheduler.scheduleAtFixedRate(this::checkHeartBeats, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieves the game model associated with this instance.
     *
     * @return The {@code Game} object representing the game model.
     */
    public Game getGameModel() {
        return gameModel;
    }

    /**
     * Adds a {@code GameSub} observer to the game model, synchronized using {@code gameLock}.
     *
     * @param gameSub The {@code GameSub} object to be added as an observer.
     * @throws RuntimeException If a {@code RemoteException} occurs while adding the observer.
     */
    public void addToObserver(GameSub gameSub) {
        synchronized (gameLock){
            gameModel.addSub(gameSub);
            try {
                gameSubs.put(gameSub.getSub(), gameSub);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a {@code PlayerSub} observer to the game model, synchronized using {@code gameLock}.
     *
     * @param playerSub The {@code PlayerSub} object to be added as an observer.
     * @throws RuntimeException If a {@code RemoteException} occurs while adding the observer.
     */
    public void addToObserver(PlayerSub playerSub){
        synchronized (gameLock){
        gameModel.addSub(playerSub);
        try {
            playerSubs.put(playerSub.getSub(), playerSub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    }

    /**
     * Adds a {@code ChatSub} observer to the game model, synchronized using {@code gameLock}.
     *
     * @param chatSub The {@code ChatSub} object to be added as an observer.
     * @throws RuntimeException If a {@code RemoteException} occurs while adding the observer.
     */
    public void addToObserver(ChatSub chatSub){
        synchronized (gameLock) {
            gameModel.addSub(chatSub);
            try {
                chatSubs.put(chatSub.getSub(), chatSub);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a {@code PlayerBoardSub} observer to the game model, synchronized using {@code gameLock}.
     *
     * @param playerBoardSub The {@code PlayerBoardSub} object to be added as an observer.
     * @throws RuntimeException If a {@code RemoteException} occurs while adding the observer.
     */
    public void addToObserver(PlayerBoardSub playerBoardSub) {
        synchronized (gameLock) {
            gameModel.addSub(playerBoardSub);
            try {
                playerboardSubs.put(playerBoardSub.getSub(), playerBoardSub);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Removes the specified {@code GameSub} observer from the game model and its associated map.
     *
     * @param gameSub The {@code GameSub} object to be removed as an observer.
     */
    public void removeSub(GameSub gameSub){
        synchronized (gameLock){
            System.out.println("Acquisisco il lock");
            gameModel.removeSub(gameSub);
            System.out.println("Rimuovo il gamesub "+ gameSubs.size());
            Iterator<Map.Entry<String, GameSub>> iterator = gameSubs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, GameSub> entry = iterator.next();
                if (entry.getValue().equals(gameSub)) {
                    iterator.remove();
                    System.out.println("Rimosso sub: " + entry.getKey());
                }
            }
        }
    }

    /**
     * Removes the specified {@code PlayerSub} observer from the game model and its associated map.
     *
     * @param gameSub The {@code PlayerSub} object to be removed as an observer.
     */
    public void removeSub(PlayerSub gameSub){
        synchronized (gameLock){
            gameModel.removeSub(gameSub);
            Iterator<Map.Entry<String, GameSub>> iterator = gameSubs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, GameSub> entry = iterator.next();
                if (entry.getValue().equals(gameSub)) {
                    iterator.remove();
                    System.out.println("Rimosso sub: " + entry.getKey());
                }
            }
        }
    }

    /**
     * Removes the specified {@code PlayerBoardSub} observer from the game model and its associated map.
     *
     * @param gameSub The {@code PlayerBoardSub} object to be removed as an observer.
     */
    public void removeSub(PlayerBoardSub gameSub){
        synchronized (gameLock){
            gameModel.removeSub(gameSub);
            Iterator<Map.Entry<String, GameSub>> iterator = gameSubs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, GameSub> entry = iterator.next();
                if (entry.getValue().equals(gameSub)) {
                    iterator.remove();
                    System.out.println("Rimosso sub: " + entry.getKey());
                }
            }
        }
    }

    /**
     * Removes the specified {@code ChatSub} observer from the game model and its associated map.
     *
     * @param gameSub The {@code ChatSub} object to be removed as an observer.
     */
    public void removeSub(ChatSub gameSub){
        synchronized (gameLock){
            gameModel.removeSub(gameSub);
            Iterator<Map.Entry<String, GameSub>> iterator = gameSubs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, GameSub> entry = iterator.next();
                if (entry.getValue().equals(gameSub)) {
                    iterator.remove();
                    System.out.println("Rimosso sub: " + entry.getKey());
                }
            }
        }
    }

    /**
     * Sends a group text message to all players in the game model.
     *
     * @param sender The sender of the message.
     * @param text The text message to be sent.
     */
    public void sendGroupText(String sender, String text) {
        synchronized (chatLock){
                gameModel.sendGroupMessage(sender, text);
        }
    }

    /**
     * Checks if the sender can send a group chat message based on game state and player status.
     *
     * @param sender The sender of the message.
     * @param text The text message to be sent.
     * @throws BadTextException If either sender or text is empty.
     * @throws InvalidStateException If the game state does not allow sending a group chat.
     * @throws GameNotExistingException If the game model is not initialized.
     * @throws UnknownPlayerException If the sender is not part of the game or not connected.
     */
    public void canSendGroupChat(String sender, String text) throws  BadTextException,InvalidStateException, GameNotExistingException,UnknownPlayerException{
        synchronized (chatLock){
            if(gameModel == null)
                throw new GameNotExistingException();
            if((!gameModel.extractNicknames().contains(sender)) || (gameModel.extractNicknames().contains(sender) && !gameModel.findPlayer(sender).getConnected())){
                throw new UnknownPlayerException("You aren't part of a game");
            }
            if(gameModel.getGameState()==State.WAITING || gameModel.getGameState()==State.ENDING){
                throw new InvalidStateException("You cannot send a text during this state");
            }
            if(sender.isEmpty() || text.isEmpty()){
                throw  new BadTextException("Sender or text can't be empty");
            }
        }
    }

    /**
     * Sends a private text message from sender to receiver using the game model.
     *
     * @param sender The sender of the message.
     * @param receiver The recipient of the message.
     * @param text The text message to be sent.
     */
    public void sendPrivateText(String sender, String receiver, String text) {
        synchronized (chatLock){
                gameModel.sendPrivateMessage(sender, receiver, text);
        }
    }

    /**
     * Checks if the sender can send a private chat message based on game state, player status, and message parameters.
     *
     * @param sender The sender of the message.
     * @param receiver The recipient of the message.
     * @param text The text message to be sent.
     * @throws PlayerAbsentException If the receiver is not part of the game or has crashed.
     * @throws BadTextException If either sender, receiver, or text is empty.
     * @throws InvalidStateException If the game state does not allow sending a private chat.
     * @throws ParametersException If sender tries to send a message to themselves.
     * @throws GameNotExistingException If the game model is not initialized.
     * @throws UnknownPlayerException If the sender or receiver is not part of the game or not connected.
     */
    public void canSendPrivateChat(String sender, String receiver, String text) throws PlayerAbsentException, BadTextException, InvalidStateException, ParametersException, GameNotExistingException, UnknownPlayerException{
        if(gameModel == null)
            throw new GameNotExistingException();
        if((!gameModel.extractNicknames().contains(sender))||(gameModel.extractNicknames().contains(sender) && !gameModel.findPlayer(sender).getConnected())){
            throw new UnknownPlayerException("You aren't part of a game");
        }
        if(gameModel.getGameState()==State.WAITING || gameModel.getGameState()==State.ENDING){
            throw new InvalidStateException("You cannot send a text during this state");
        }

        if((!gameModel.extractNicknames().contains(receiver)) || (gameModel.extractNicknames().contains(receiver) && !gameModel.findPlayer(receiver).getConnected())){
            throw new PlayerAbsentException("Receiver isn't part of the game or has crashed");
        }
        if(sender.isEmpty() || receiver.isEmpty() || text.isEmpty()){
            throw new BadTextException("receiver or text are empty,try again");
        }
        if(sender.equals(receiver)){
            throw new ParametersException("You cannot send a message to yourself");
        }
    }


    /**
     * Notifies all players except the rejoined player and performs game state management after a player rejoins.
     *
     * @param player The player who has rejoined.
     */
    public void rejoinedChief(String player){
        System.out.println("Sono in rejoinedChief\n");
        synchronized (gameLock){
           //int i=0;
            System.out.println("la dimensione è"+gameModel.getGameSubs().size());
            for(GameSub gameSub: gameModel.getGameSubs()){
                //System.out.println(gameSub.getSub());
                try {
                    if (!gameSub.getSub().equals(player)) {
                        System.out.println("ciao"+gameSub.getSub());
                        gameSub.notifyRejoinedPlayer(player);
                    }
                }catch (RemoteException e){
                    System.out.println("Ecce di rejoinedChief");
                    System.out.println(e);
                }
                finally {
                    //i++;
                }
            }
            System.out.println("Prima di manageupdate");
            gameModel.manageUpdate(player);
            System.out.println("Dopo manageupdate");
        }
        System.out.println("Fuori da rejoinedChief\n");
    }

}
