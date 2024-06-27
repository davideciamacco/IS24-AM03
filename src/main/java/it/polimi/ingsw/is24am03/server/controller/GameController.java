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
 * the game controller ensures the communication through client controller and server model
 */
public class GameController extends UnicastRemoteObject implements RemoteGameController {
    /**
     * locks used for thread synchronization
     */
    private final Object gameLock;
    private final Object chatLock;

    private final ScheduledExecutorService heartbeatScheduler = Executors.newScheduledThreadPool(1);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;

    private ScheduledFuture<?> thread;

    private ArrayList<Pair<String, Long>> heartBeats;

    /**
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
     /* @param game the game model to be associated with this controller
     */
    public GameController() throws RemoteException {
        heartBeats = new ArrayList<>();
        gameLock= new Object();
        chatLock=new Object();
        startHeartbeatChecker();
        timer=false;
    }

    /**
     *
     */
    public void canStart(){
        synchronized (gameLock) {
            if (gameModel.getPlayers().size() == gameModel.getNumPlayers()) {
                gameModel.startGame();
            }
        }
    }

    /**
     *
     * @param numPlayers
     * @param nickname
     * @param ConnectionType
     * @throws GameAlreadyCreatedException
     */
    public void createGame(int numPlayers, String nickname, String ConnectionType) throws GameAlreadyCreatedException {
        synchronized (gameLock) {
            if(nickname.isBlank() || numPlayers<2 || numPlayers>4) throw new IllegalArgumentException();
            if(gameModel!=null) throw new GameAlreadyCreatedException();
            gameModel = new Game(numPlayers, nickname);
            if(ConnectionType.equals("RMI"))
                heartBeats.add(new Pair<>(nickname, System.currentTimeMillis()));
        }
    }

    /**
     *
     * @param player
     * @param face
     * @throws PlayerNotInTurnException
     * @throws InvalidStateException
     * @throws GameNotExistingException
     * @throws ArgumentException
     * @throws UnknownPlayerException
     */
    public void canSelectStartingFace(String player, String face) throws PlayerNotInTurnException,InvalidStateException,GameNotExistingException, ArgumentException, UnknownPlayerException{
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
            //se il player non esiste all'interno del gioco Unknown player exception


        }
    }

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
          
    public void setObjectiveCard(String player, int choice) {
        synchronized (gameLock) {
            System.out.println("Sto chiamando model");
            gameModel.setObjectiveCard(player, choice);
        }
    }


    /**
     * Draws resources for the player from the resource deck.
     *
     * @param player represents the nickname of the player who is drawing resources
    //* @param RD  the resource deck from which resources are drawn
     * @throws PlayerNotInTurnException if the player is not currently in turn
    //* @throws FullHandException        if the player's hand is already full
     * @throws InvalidStateException   if the game state is not suitable for drawing resources
     */

    //metodo per verificare che player può pescare da risorse
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
    public void drawResources(String player){
        synchronized (gameLock) {
                gameModel.drawResources(player);
        }
    }

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

    public void rejoinGame(String nickname, String ConnectionType) throws InvalidStateException, GameNotExistingException,UnknownPlayerException {
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
        if(ConnectionType.equals("RMI"))
            heartBeats.add(new Pair<>(nickname, System.currentTimeMillis()));
        }


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

    public void stopTimer(){
        timer=false;
        thread.cancel(true);
        System.out.println("Timer interrotto");
    }

    public void setLastHeartBeat(String player){
        for(int i=0; i< heartBeats.size(); i++){
            if(heartBeats.get(i).getKey().equals(player))
                heartBeats.set(i, new Pair<>(player, System.currentTimeMillis()));
        }
    }

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

    private void startHeartbeatChecker() {
        long heartbeatInterval = 5000; // 5 seconds
        heartbeatScheduler.scheduleAtFixedRate(this::checkHeartBeats, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    public Game getGameModel() {
        return gameModel;
    }

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

    public void sendGroupText(String sender, String text) {
        synchronized (chatLock){
                gameModel.sendGroupMessage(sender, text);
        }
    }
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

    public void sendPrivateText(String sender, String receiver, String text) {
        synchronized (chatLock){
                gameModel.sendPrivateMessage(sender, receiver, text);
        }
    }
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

    public boolean isTimer() {
        return timer;
    }
}
