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
import java.util.ArrayList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.List;


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

    boolean timer;
    /**
     * Constructs a GameController with the specified game.
     *
     //* @param game the game model to be associated with this controller
     */
    public GameController() throws RemoteException {
        heartBeats = new ArrayList<>();
        gameLock= new Object();
        chatLock=new Object();
        startHeartbeatChecker();
        timer=false;
    }

    public void canStart(){
        synchronized (gameLock) {
            if (gameModel.getPlayers().size() == gameModel.getNumPlayers()) {
                gameModel.startGame();
            }
        }
    }

    public void createGame(int numPlayers, String nickname, String ConnectionType) throws GameAlreadyCreatedException {
        synchronized (gameLock) {
            if(nickname.isBlank() || numPlayers<2 || numPlayers>4) throw new IllegalArgumentException();
            if(gameModel!=null) throw new GameAlreadyCreatedException();
            gameModel = new Game(numPlayers, nickname);
            if(ConnectionType.equals("RMI"))
                heartBeats.add(new Pair<>(nickname, System.currentTimeMillis()));
        }
    }
    //metodo per verificare che la selezione della starting card può avere senso
    public void canSelectStartingFace(String player, String face) throws PlayerNotInTurnException,InvalidStateException,GameNotExistingException, ArgumentException{
        synchronized (gameLock) {
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if (gameModel == null)
                throw new GameNotExistingException();

            if (!gameModel.getGameState().equals(State.STARTING))
                throw new InvalidStateException("Action not allowed in this state");
            if(!face.equals("FRONT") && !face.equals("BACK"))
                throw new ArgumentException("Face must be 'FRONT' or 'BACK' ");

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


    //metodo per verificare che player può selezionare carta obiettivo


    public void canSetObjectiveCard(String player, int choice) throws PlayerNotInTurnException, GameNotExistingException, InvalidStateException {
        synchronized (gameLock) {
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if (gameModel == null)
                throw new GameNotExistingException();
            if(!gameModel.getGameState().equals(State.OBJECTIVE))
                throw new InvalidStateException("Action not allowed in this state");
            if (choice < 1 || choice > 2) throw new IllegalArgumentException();
        }
    }
          
    public void setObjectiveCard(String player, int choice) {
      /*  if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {

            throw new PlayerNotInTurnException();
        }
        if(!gameModel.getGameState().equals(State.OBJECTIVE))
            throw new InvalidStateException("Action not allowed in this state");
        if(gameModel == null)
            throw new GameNotExistingException();
        if(choice<1 || choice>2) throw new IllegalArgumentException();*/
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
    public void canDrawResources(String player) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, EmptyDeckException {
        synchronized (gameLock) {
            if (!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player))
                throw new PlayerNotInTurnException();
            if (gameModel == null)
                throw new GameNotExistingException();
            //if(P.getHand().size()==3) throw new FullHandException();
            if (!gameModel.getGameState().equals(State.DRAWING)){
                throw new InvalidStateException("Action not allowed in this state");}
            if (gameModel.getResourceDeck().isEmpty()){
                throw new EmptyDeckException("Deck selected is empty");
            }
        }
    }
    public void drawResources(String player) /* throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException*/ {
        synchronized (gameLock) {
           /* if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player))
                throw new PlayerNotInTurnException();
            if(gameModel == null)
                throw new GameNotExistingException();
            //if(P.getHand().size()==3) throw new FullHandException();
            if (!gameModel.getGameState().equals(State.DRAWING))
                throw new InvalidStateException("Action not allowed in this state");*/
                gameModel.drawResources(player);

            /*} catch (EmptyDeckException e)
            {}*/

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

    public void canDrawGold(String player)throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, EmptyDeckException{
        synchronized (gameLock) {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null)
                throw new GameNotExistingException();
            if (!gameModel.getGameState().equals(State.DRAWING))
                throw new InvalidStateException("Action not allowed in this state");
            if(gameModel.getGoldDeck().isEmpty()){
                throw new EmptyDeckException("Gold Deck is empty");
            }

            }



    }/*throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException*/
    public void drawGold(String player) {
            synchronized (gameLock) {
               /* if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                    throw new PlayerNotInTurnException();
                }
                if(gameModel == null)
                    throw new GameNotExistingException();
                if (!gameModel.getGameState().equals(State.DRAWING))
                    throw new InvalidStateException("Action not allowed in this state");
                try {*/
                    gameModel.drawGold(player);
               /* }
                catch (EmptyDeckException e)
                {

                }*/
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

    public void canDrawTable(String player, int choice) throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException, NullCardSelectedException{

        synchronized (gameLock)
        {
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null) throw new GameNotExistingException();
            if(choice<1 || choice>4) throw new IllegalArgumentException();
            //if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getGameState().equals(State.DRAWING)) throw new InvalidStateException("");
            if(gameModel.getTableCards().get(choice)==null){
               throw new NullCardSelectedException();
            }


        }
    }

    //metodo per fare check che player possa pescare da tavolo
    public void drawTable(String player, int choice) /*throws PlayerNotInTurnException, InvalidStateException, GameNotExistingException*/ {
        synchronized (gameLock)
        {
           /* if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null) throw new GameNotExistingException();
            if(choice<1 || choice>4) throw new IllegalArgumentException();
            //if(P.getHand().size()==3) throw new FullHandException();
            if(!gameModel.getGameState().equals(State.DRAWING)) throw new InvalidStateException("");
            try {*/
                gameModel.drawTable(player, choice);
            /*}
            catch(NullCardSelectedException e){}*/

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
            if(gameModel == null) throw new GameNotExistingException();
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

    //metodo per vefificare che player possa piazzare
   // public void canPlaceCard(String player, int choice, int i, int j,String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException
    public void placeCard(String player, int choice, int i, int j, String face) throws PlayerNotInTurnException, InvalidStateException, RemoteException, GameNotExistingException, CoordinatesOutOfBoundsException, NoCardsAvailableException, RequirementsNotMetException, ArgumentException {
        synchronized (gameLock)
        {
            boolean faceBoolean;
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(gameModel == null) throw new GameNotExistingException();
            //      if(P.getHand().size()==0) throw new EmptyHandException();
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
    //metodo per verificare che player possa scegliere colore
    public void canPickColor(String player, String color)throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, GameNotExistingException,ColorAlreadyPickedException{
        Color chosenColor;
        boolean flag=false;
        synchronized (gameLock)
        {
            if(gameModel == null) throw new GameNotExistingException();
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
    public void pickColor(String player, String color) /*throws PlayerNotInTurnException, InvalidStateException, ColorAlreadyPickedException, GameNotExistingException*/ {
        Color chosenColor=Color.RED;
        //boolean flag=false;
        synchronized (gameLock)
        {
            /*if(gameModel == null) throw new GameNotExistingException();
            if(!gameModel.getPlayers().get(gameModel.getCurrentPlayer()).getNickname().equals(player)) {
                throw new PlayerNotInTurnException();
            }
            if(!gameModel.getGameState().equals(State.COLOR)) throw new InvalidStateException("Action not allowed in this state");*/
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
            gameModel.setNumPlayersConnected(gameModel.getNumPlayersConnected() - 1);
            System.out.println(gameModel.getNumPlayersConnected());
            if (gameModel.getNumPlayersConnected() <= 1) {
                startTimer();
                System.out.println("Timer avviato");
            }
            for (int i = 0; i < gameModel.getNumPlayers(); i++) {
                if (gameModel.getPlayers().get(i).getNickname().equals(nickname)) {
                    gameModel.getPlayers().get(i).setConnected(false);
                }
            }
                    //metodo che notifica tutti i sub che il player si è disconnesso, tranne player in questione
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

    public void rejoinGame(String nickname) throws InvalidStateException {
        int check=-1;
        int i=0;
        while(i<gameModel.getPlayers().size() && check==-1) {
            if (gameModel.getPlayers().get(i).getNickname().equals(nickname) && !gameModel.getPlayers().get(i).getConnected())
                check = i;
            i++;
        }
        if(check==-1)
            throw new IllegalArgumentException();
        if(gameModel.getGameState().equals(State.ENDING))
            throw new InvalidStateException("Action not allowed in this state");
        gameModel.getPlayers().get(check).setConnected(true);
        System.out.println(gameModel.getNumPlayersConnected());
        gameModel.setNumPlayersConnected(gameModel.getNumPlayersConnected()+1);
        System.out.println(gameModel.getNumPlayersConnected());
        if(gameModel.getNumPlayersConnected()==2)
            stopTimer();
        }


    public void startTimer() {

        long limit = 20;

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
                handleCrashedPlayer(heartBeats.get(i).getKey());
                heartBeats.remove(i);
                i--;
            }
        }
    }

    private void startHeartbeatChecker() {
        long heartbeatInterval = 5000; // 100 seconds
        heartbeatScheduler.scheduleAtFixedRate(this::checkHeartBeats, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    public Game getGameModel() {
        return gameModel;
    }

    //UPDATE OBSERVERS LOGIC//
    public void addToObserver(GameSub gameSub){ synchronized (gameLock){gameModel.addSub(gameSub);}}
    public void addToObserver(PlayerSub playerSub){  synchronized (gameLock){gameModel.addSub(playerSub);}}
    public void addToObserver(ChatSub chatSub){ synchronized (gameLock){gameModel.addSub(chatSub);}}
    public void addToObserver(PlayerBoardSub playerBoardSub) { synchronized (gameLock){gameModel.addSub(playerBoardSub);}}

    public void removeSub(GameSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(PlayerSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(PlayerBoardSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}
    public void removeSub(ChatSub gameSub){synchronized (gameLock){gameModel.removeSub(gameSub);}}

    //metodo per verificare che player sender possa mandare messaggio di gruppo
    public void sendGroupText(String sender, String text) {
        synchronized (chatLock){

                gameModel.sendGroupMessage(sender, text);
        }
    }
    public void canSendGroupChat(String sender, String text) throws  BadTextException,InvalidStateException{
        synchronized (chatLock){
            if(gameModel.getGameState()==State.WAITING || gameModel.getGameState()==State.ENDING){
                throw new InvalidStateException("You cannot send a text during this state");
            }
            if(sender.isEmpty() || text.isEmpty()){
                throw  new BadTextException("Sender or text can't be empty");
            }
        }
    }

    //metodo per verificare che player possa mandare messaggio privato
    public void sendPrivateText(String sender, String receiver, String text) {
        synchronized (chatLock){
                gameModel.sendPrivateMessage(sender, receiver, text);
        }
    }
    public void canSendPrivateChat(String sender, String receiver, String text) throws PlayerAbsentException, BadTextException, InvalidStateException, ParametersException{
        //se receiver non è nella chat

        if(gameModel.getGameState()==State.WAITING || gameModel.getGameState()==State.ENDING){
            throw new InvalidStateException("You cannot send a text during this state");
        }
        List<String> player= gameModel.getPlayers().stream().filter(Player::getConnected).map(Player::getNickname).toList();
        player=new ArrayList<>(player);
        if(!player.contains(receiver)){
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
        //deve occuparsi di notificare a tutti i game sub che player è tornato

        //deve occuparsi di mandare update al player in questione
        synchronized (gameLock){
            //notifico a tutti tranne al player tornato che lui è tornato
            for(GameSub gameSub: gameModel.getGameSubs()){
                try {
                    if (!gameSub.getSub().equals(player)) {
                        gameSub.notifyRejoinedPlayer(player);
                    }
                }catch (RemoteException ignored){}
            }
            //creo notifica per il player in questione
            gameModel.manageUpdate(player);
        }
    }
}
