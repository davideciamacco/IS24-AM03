package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.chat.Chat;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.decks.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.player.Player;

import java.lang.reflect.Array;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to handle the main logic of a Game
 */
public class Game{

    /**
     * Index of the current player in the ArrayList
     */
    private int currentPlayer;

    /**
     * Number of rounds completed
     */
    private int roundNumber;

    /**
     * The list of all the player that have joined the game
     */
    private ArrayList<Player> players;

    /**
     * Number of players needed by the game to start
     */
    private int numPlayers;

    /**
     * The reference to the gold deck to draw gold cards
     */
    private GoldDeck goldDeck;

    /**
     * The reference to the resource deck
     */
    private ResourceDeck resourceDeck;

    /**
     * The reference to the starting deck
     */
    private StartingDeck startingDeck;

    /**
     * The reference to the objective deck
     */
    private ObjectiveDeck objectiveDeck;

    /**
     * List of the cards revealed on the table (max 4)
     */
    private ArrayList<ResourceCard> tableCards;

    /**
     * List containing the common objective card for the game
     */
    private ArrayList<ObjectiveCard> commonObjective;

    /**
     * State of the game
     */
    private State gameState;

    /**
     * Boolean which becomes true when a player reaches 20 or more points or both gold and resource decks don't contain
     * more cards
     */
    private boolean ending;

    /**
     * Boolean that becomes true when the last round begins
     */
    private boolean lastRound;

    private ArrayList<Color> availableColors;

    /**
     * Reference to Chat
     */
    private Chat chat;

    private boolean timer;

    private int numPlayersConnected;

    public ArrayList<GameSub> getGameSubs() {
        return gameSubs;
    }

    /**
     * ArrayList which contains all GameSubs. Used to implement Observer Pattern
     */
    private ArrayList<GameSub> gameSubs;



    /**
     * Constructor of the Game objects, it initializes all the attributes and add the host to the game
     * @param nPlayers is the total number of the players for the game
     * @param host is the Player that have created the game
     */
    public Game(int nPlayers, String host) {
        this.timer=false;
        this.lastRound = false;
        this.ending = false;
        this.roundNumber=0;
        this.currentPlayer = -1;
        this.gameState = State.WAITING;
        this.numPlayers = nPlayers;
        this.tableCards = new ArrayList<ResourceCard>();
        this.resourceDeck = new ResourceDeck();
        this.goldDeck = new GoldDeck();
        this.startingDeck = new StartingDeck();
        this.objectiveDeck = new ObjectiveDeck();
        this.players = new ArrayList<Player>();
        this.commonObjective = new ArrayList<ObjectiveCard>();
        this.availableColors = new ArrayList<Color>(List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
        this.chat=new Chat();
        this.gameSubs=new ArrayList<>();
        this.numPlayersConnected = 1;
        addPlayer(host);
    }

    /**
     * This method is used to start the game, it chooses randomly a starting player, distributes the cards to the players,
     * initializes the common objective cards, shuffles the decks and reveals the first four cards on the table
     */
    public void startGame(){


        setOrder();
 
        for(GameSub gameSub: gameSubs){
            try{
                if(gameSub!=null)
                    gameSub.NotifyNumbersOfPlayersReached();
            }catch (RemoteException ignored){}
        }

        this.gameState = State.STARTING;
        for (GameSub gameSub : gameSubs) {
            try {
                if(gameSub!=null)
                    gameSub.notifyChangeState(gameState);
            } catch (RemoteException ignored) {
            }
        }
        startingDeck.shuffle();
        objectiveDeck.shuffle();
        goldDeck.shuffle();
        resourceDeck.shuffle();
        setCommonObjective();
        initializeTable();
        distributeCards();
        nextTurn();
    }


    /**
     * This method is used to enter the game in the ending phase, in which points from objective cards are giver
     * to the players and the winner is calculated
     */
    public void endGame() {
        gameState = State.ENDING;
        int i;
        List<String> winners = new ArrayList<>();
        if(numPlayersConnected==1) {
            for(i=0; i<numPlayers; i++) {
                if(players.get(i).getConnected()) {
                    players.get(i).setWinner(true);
                    winners.add(players.get(i).getNickname());
                }
            }
        }
        else{
            for (GameSub gameSub : gameSubs) {
                try {
                    if(gameSub!=null)
                        gameSub.notifyChangeState(gameState);
                } catch (RemoteException ignored) {
                }
            }

            giveObjectivePoints();
            winners= checkWinner().stream().map(Player::getNickname).toList();
        }
        ArrayList<String> def= new ArrayList<>(winners);
        for (GameSub gameSub : gameSubs) {
            try {
                if(gameSub!=null)
                    gameSub.notifyWinners(def);
            } catch (RemoteException ignored) {
            }
        }

    }

    public int getNumPlayers() {
        return numPlayers;
    }


    /**
     * Method used to return the available colors
     * @return the list of color that can be picked
     */
    /*
    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }*/

    /**
     * This method set the two common cards of the game
     */
    public void setCommonObjective() {
        commonObjective.add(objectiveDeck.drawCard());
        commonObjective.add(objectiveDeck.drawCard());
    }

    /**
     * This method give to each player the starting card, two resource card and one gold card
     */
    public void distributeCards() {
        for (Player p : players) {
            p.addCard(resourceDeck.drawCard());
            p.addCard(resourceDeck.drawCard());
            p.addCard(goldDeck.drawCard());
            p.setStartingCard(startingDeck.drawCard());
            p.setObjectiveCard(objectiveDeck.drawCard(), objectiveDeck.drawCard());
        }

        ArrayList<ResourceCard> commons=new ArrayList<>();
        commons.add(resourceDeck.getCards().get(0));
        commons.add(goldDeck.getCards().get(0));
        commons.add(tableCards.get(0));
        commons.add(tableCards.get(1));
        commons.add(tableCards.get(2));
        commons.add(tableCards.get(3));

        for(GameSub gameSub: gameSubs){
            try {
                gameSub.UpdateFirst(commons);
                gameSub.notifyCommonObjective(commonObjective.get(0),commonObjective.get(1));
            }catch (RemoteException ignored){}
        }
        for(Player p: players){
            if(p.getConnected()) {
                try {
                    findSub(p).notifyFirstHand(p.getHand().get(0), p.getHand().get(1), p.getHand().get(2), p.getStartingCard(), p.getObjective1(), p.getObjective2());
                } catch (RemoteException ignored) {
                }
            }
        }
        ArrayList<String> order = new ArrayList<>();
        for (Player p : players) {
            order.add(p.getNickname());
        }
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.notifyTurnOrder(order);
            } catch (RemoteException ignored) {
            }
        }
        }

    /**
     * Method that set the first four cards revealed on the table
     */
    public void initializeTable() {
        tableCards.add(resourceDeck.drawCard());
        tableCards.add(resourceDeck.drawCard());
        tableCards.add(goldDeck.drawCard());
        tableCards.add(goldDeck.drawCard());
    }

    /**
     * Method used to calculate the winner of the game
     * @return an arraylist containing reference to the winning players
     */
    public ArrayList<Player> checkWinner() {
        int maxPoints = 0;
        int maxObjective = 0;
        ArrayList<Player> winners = new ArrayList<Player>();
        for (Player p : players) {
            if (p.getPoints() > maxPoints)
                maxPoints = p.getPoints();
        }
        for (Player p : players) {
            if (p.getPoints() == maxPoints) {
                winners.add(p);
                if (p.getNumObj() > maxObjective)
                    maxObjective = p.getNumObj();
            }
        }
        for (Player w : winners) {
            if (w.getNumObj() < maxObjective)
                winners.remove(w);
            else
                w.setWinner(true);
        }
        return winners;
    }

    /**
     * This method allow to add a player to the game
     * @param player represent the player to be added
     */
    public void addPlayer(String player) {
        Player playerToAdd = new Player(player);
        players.add(playerToAdd);
        if(players.size()>1){
            for (GameSub gameSub : gameSubs) {
                try {
                        gameSub.notifyJoinedPlayer(player);
                } catch (RemoteException ignored) {
                }
            }
        }
    }

    /**
     * This method calculates the number of objective card realized by each player and gives to them the
     * corresponding points
     */
    public void giveObjectivePoints() {
        int flag;
        for (Player p : players) {
            flag = p.getPlayerBoard().checkObjective(p.getObjectiveCard());
            p.increaseNumObjective(flag);
            p.addPoints(flag);
            for (ObjectiveCard oc : commonObjective) {
                flag = p.getPlayerBoard().checkObjective(oc);
                p.increaseNumObjective(flag);
                p.addPoints(flag);
            }
        }
    }

    /**
     * Add a resource card to the selected player
     * @param player represents the player who is drawing
     */
    public void drawResources(String player){
        //if(resourceDeck.isEmpty())
          //  throw new EmptyDeckException("There aren't other cards in the selected deck");
        Player p=findPlayer(player);
        p.addCard(resourceDeck.drawCard());
        if(p.getConnected()) {
            try {
                findSub(player).NotifyChangePersonalCards(player, p.getHand());
            } catch (RemoteException ignored) {
                //System.out.println("Errore 1");
            }
        }
        if(resourceDeck.isEmpty()){
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.updateCommonTable(null,0);
                } catch (RemoteException ignored) {
                    //System.out.println("Errore 2");
                }
            }
        }
        if(!resourceDeck.isEmpty()){
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(resourceDeck.getCards().get(0),0);
                }catch (RemoteException ignored){
                    //System.out.println("Errore 3");
                }
            }
        }
        if(resourceDeck.isEmpty() && goldDeck.isEmpty())
            ending=true;
        nextTurn();
    }

    /**
     * Add a gold card to the selected player
     * @param player represents the player who is drawing
     */
    public void drawGold(String player){
       // if(goldDeck.isEmpty())
         //   throw new EmptyDeckException("There aren't other cards in the selected deck");
        findPlayer(player).addCard(goldDeck.drawCard());
        //System.out.println("\nID:"+getPlayers().get(currentPlayer).getHand().getFirst().getId()+"\n");
        //DOPO PESCA HO NOTIFY_CHANGE_PERSONAL_CARDS CHE MI AGGIORNA LE CARTE DEL GIOCATORE
        if(findPlayer(player).getConnected()) {
            try {
                findSub(player).NotifyChangePersonalCards(player, findPlayer(player).getHand());

            } catch (RemoteException ignored) {
            }
        }


        if(goldDeck.isEmpty()){
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(null,1);
                }catch (RemoteException ignored){}
            }
        }

        if(!goldDeck.isEmpty()){
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(goldDeck.getCards().get(0),1);
                }catch (RemoteException ignored){}
            }
        }
        if(resourceDeck.isEmpty() && goldDeck.isEmpty())
            ending=true;
        nextTurn();
    }

    /**
     * This method lets the player drawing from the table selecting one of the four
     * unrevealed cards
     * @param player represents the player who is drawing
     * @param choice consists of the card chosen among the four cards on the table
     */
    public void drawTable(String player, int choice) /*throws NullCardSelectedException*/ {
       // if (tableCards.get(choice) == null)
         //   throw new NullCardSelectedException();
        Player p = findPlayer(player);
        switch (choice) {
            case 1:
                p.addCard(tableCards.get(0));
                if(p.getConnected()) {
                    try {
                        findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                if (!resourceDeck.isEmpty()) {
                    tableCards.set(0, resourceDeck.drawCard());
                    handledrawTable(resourceDeck, tableCards.get(0), 2, 0);
                }
                else if (!goldDeck.isEmpty()) {
                    tableCards.set(0, goldDeck.drawCard());
                    handledrawTable(goldDeck, tableCards.get(0), 2, 1);
                }
                else {
                    tableCards.set(0, null);
                    for (GameSub gameSub : gameSubs) {
                        try {
                            gameSub.updateCommonTable(null, 2);
                        } catch (RemoteException ignored) {
                        }

                    }
                }
                break;

            case 2:
                p.addCard(tableCards.get(1));
                if(p.getConnected()) {
                    try {
                        findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                if (!resourceDeck.isEmpty()) {
                    tableCards.set(1, resourceDeck.drawCard());
                    handledrawTable(resourceDeck, tableCards.get(1), 3, 0);
                }
                else if (!goldDeck.isEmpty()) {
                    tableCards.set(1, goldDeck.drawCard());
                    handledrawTable(goldDeck, tableCards.get(1), 3, 1);
                }
                else {
                    tableCards.set(1, null);
                    for (GameSub gameSub : gameSubs) {
                        try {
                            gameSub.updateCommonTable(null, 3);
                        } catch (RemoteException ignored) {
                        }

                    }
                }
                break;

            case 3:
                p.addCard(tableCards.get(2));
                if(p.getConnected()) {
                    try {
                        findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                if (!goldDeck.isEmpty()) {
                    tableCards.set(2, goldDeck.drawCard());
                    handledrawTable(goldDeck, tableCards.get(2), 4, 1);
                }
                else if (!resourceDeck.isEmpty()) {
                    tableCards.set(2, resourceDeck.drawCard());
                    handledrawTable(resourceDeck, tableCards.get(2), 4, 0);
                }
                else {
                    tableCards.set(2, null);
                    for (GameSub gameSub : gameSubs) {
                        try {
                            gameSub.updateCommonTable(null, 4);
                        } catch (RemoteException ignored) {
                        }

                    }
                }
                break;

            case 4:
                p.addCard(tableCards.get(3));
                if(p.getConnected()) {
                    try {
                        findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                if (!resourceDeck.isEmpty()) {
                    tableCards.set(3, resourceDeck.drawCard());
                    handledrawTable(resourceDeck, tableCards.get(3), 5, 0);
                }
                else if (!goldDeck.isEmpty()) {
                    tableCards.set(3, goldDeck.drawCard());
                    handledrawTable(goldDeck, tableCards.get(3), 5, 1);
                }
                else {
                    tableCards.set(3, null);
                    for (GameSub gameSub : gameSubs) {
                        try {
                            gameSub.updateCommonTable(null, 5);
                        } catch (RemoteException ignored) {
                        }

                    }
                }
                break;
        }
        nextTurn();
    }

    public void handledrawTable(Deck d, ResourceCard resourceCard, int index, int deck) {
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.updateCommonTable(resourceCard,index);
            } catch (RemoteException ignored) {
            }
        }

        if (d.isEmpty()) {
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.updateCommonTable(null,deck);
                } catch (RemoteException ignored) {
                }
            }
        } else {
            for(GameSub gameSub: gameSubs){
                try {
                    if(deck==0)
                        gameSub.updateCommonTable(resourceDeck.getCards().get(0), deck);
                    else
                        gameSub.updateCommonTable(goldDeck.getCards().get(0), deck);

                }catch (RemoteException ignored){}
            }
        }

    }

    /**
     * This method allows to place a card
     * @param player consists of the player who is placing
     * @param choice consists of the card to be placed
     * @param i consists of the vertical position in the player's board
     * @param j consists of the horizontal position in the player's board
     * @param face consists of the face of the card chosen by the player
     */

    public void placeCard(String player, int choice, int i, int j, boolean face) {
        Player p = players.get(currentPlayer);
        try {
            p.getPlayerBoard().placeCard(p.getHand().get(choice), i, j, face);
        }
        catch(Exception e){
            throw e;
        }

        p.getPlayerBoard().notifyChangePlayerBoard(player, p.getPlayerBoard().getBoard()[i][j], i, j);

        for(PlayerSub playerSub: p.getPlayerSubs()){
            try{
                playerSub.ReceiveUpdateOnPoints(player,p.getPoints());
            }catch (RemoteException ignored){}
        }
        if(p.getPoints()>=2)
            ending=true;
        if(!lastRound) {
            this.gameState = State.DRAWING;
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.notifyChangeState(gameState);
                    gameSub.notifyCurrentPlayer(players.get(currentPlayer).getNickname());
                } catch (RemoteException ignored) {
                }
            }

        }
        else{
            if(currentPlayer==numPlayers-1) {
                if(findPlayer(player).getConnected()) {
                    try {
                        findSub(player).NotifyChangePersonalCards(player, p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                endGame();
            }
            else {
                if(findPlayer(player).getConnected()) {
                    try {
                        findSub(player).NotifyChangePersonalCards(player, p.getHand());
                    } catch (RemoteException ignored) {
                    }
                }
                gameState=State.PLAYING;
                for(GameSub gameSub: getGameSubs()){
                    try{
                        gameSub.notifyChangeState(gameState);
                    }catch (RemoteException e){}
                }
                nextTurn();
            }
        }
    }

    /**
     * Set the order of the players used for the game
     */
    public void setOrder() {
        Collections.shuffle(players);
    }

    public State getGameState() {return gameState;}

    public void selectStartingFace(String player, boolean face)
    {
        Player p = players.get(currentPlayer);
        p.getPlayerBoard().placeStartingCard(p.getStartingCard(), face);
        p.getPlayerBoard().notifyChangePlayerBoard(player, p.getPlayerBoard().getBoard()[40][40], 40,40);
        nextTurn();
    }

    public void setObjectiveCard(String player, int choice) {
        Player p = players.get(currentPlayer);
        p.setObjectiveChoice(choice);
        try{
            findSub(player).notifyChoiceObjective(player, p.getObjectiveCard());
        }catch (RemoteException ignored){}

        nextTurn();
    }

    public void setColor(Color color){
        Player p = players.get(currentPlayer);
        p.setPawncolor(color);
        for(int i=0; i<availableColors.size(); i++) {
            if (availableColors.get(i).equals(color))
                availableColors.remove(i);
        }
        nextTurn();
        if(gameState==State.COLOR){
            try {
                findSub(players.get(currentPlayer).getNickname()).notifyAvailableColors(availableColors);
            }catch (RemoteException ignored){}

        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Method used to change the current player to the next one
     */
    public void nextTurn(){
        if(currentPlayer==numPlayers-1){
            if(ending && lastRound) {
                endGame();
            }
            else if(gameState.equals(State.DRAWING)) {
                roundNumber++;
            }
            else if(gameState.equals(State.STARTING)) {
                gameState = State.COLOR;
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.notifyChangeState(gameState);
                    } catch (RemoteException ignored) {
                    }
                }
                try {
                    findSub(players.get(0)).notifyAvailableColors(availableColors);
                }catch (RemoteException ignored){}
            }
            else if(gameState.equals(State.COLOR)) {
                Map<String, Color> colors=new HashMap<>();
                for(Player p: players){
                    colors.put(p.getNickname(),p.getPawncolor());

                }
                for(GameSub gameSub: gameSubs){
                    try{
                        gameSub.notifyFinalColors(colors);
                    }catch (RemoteException ignored){}
                }

                gameState = State.OBJECTIVE;
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.notifyChangeState(gameState);
                    } catch (RemoteException ignored) {
                    }
                }
            }
            else if(gameState.equals(State.OBJECTIVE)) {
                gameState = State.PLAYING;
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.notifyChangeState(gameState);
                    } catch (RemoteException ignored) {
                    }
                }
            }
            if(ending) {
                lastRound = true;
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.NotifyLastRound();
                    } catch (RemoteException ignored) {
                    }
                }
            }

        }
        if(gameState.equals(State.DRAWING)) {
            gameState = State.PLAYING;
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.notifyChangeState(gameState);
                } catch (RemoteException ignored) {
                }
            }
        }
        currentPlayer = (currentPlayer+1)%(numPlayers);
        while(!players.get(currentPlayer).getConnected() && numPlayersConnected>1) {
            currentPlayer = (currentPlayer + 1) % (numPlayers);
        }
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.notifyCurrentPlayer(players.get(currentPlayer).getNickname());
                } catch (RemoteException ignored) {
                }
            }


    }

    public ArrayList<Color> getAvailableColors(){
        return availableColors;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void setEnding(){
        this.ending=true;
    }

    public GoldDeck getGoldDeck() {
        return goldDeck;
    }

    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    public StartingDeck getStartingDeck() {
        return startingDeck;
    }

    public ObjectiveDeck getObjectiveDeck() {
        return objectiveDeck;
    }

    public ArrayList<ResourceCard> getTableCards() {
        return tableCards;
    }

    public ArrayList<ObjectiveCard> getCommonObjective() {
        return commonObjective;
    }

    public boolean isEnding() {
        return ending;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public void setLastRound(){
        lastRound = true;
    }

    public void setGameState(State state){
        this.gameState = state;
    }

    public void addSub(GameSub gameSub){
        gameSubs.add(gameSub);
    }
    public void removeSub(GameSub gameSub){
        gameSubs.remove(gameSub);
    }
    public void addSub(PlayerSub playerSub){
        for(Player p: getPlayers()){
            p.getPlayerSubs().add(playerSub);

        }
        if(gameState==State.WAITING){
            for(int i=0; i<players.size()-1;i++){
                PlayerSub first = players.get(i).getPlayerSubs().get(0);
                players.get(players.size()-1).getPlayerSubs().add(first);
            }

        }
        /*else{
            for(Player p: players){
                p.getPlayerSubs().add(playerSub);
            }
        }*/
    }
    public void removeSub(PlayerSub playerSub){
        for(Player p: getPlayers()){
            p.getPlayerSubs().remove(playerSub);
        }
    }
    public void addSub(ChatSub chatSub){
        chat.getChatSubs().add(chatSub);
    }
    public void removeSub(ChatSub chatSub){
        chat.getChatSubs().remove(chatSub);
    }
    public void addSub(PlayerBoardSub playerBoardSub){
        for(Player p: getPlayers()){
            p.getPlayerBoard().getPlayerBoardSubs().add(playerBoardSub);
        }
        if(gameState==State.WAITING) {
            for(int i=0; i<players.size()-1;i++){
                PlayerBoardSub first = players.get(i).getPlayerBoard().getPlayerBoardSubs().get(0);
                players.get(players.size()-1).getPlayerBoard().getPlayerBoardSubs().add(first);
            }
        }/*
        else{
            for(Player p: players){
                p.getPlayerBoard().getPlayerBoardSubs().add(playerBoardSub);
            }
        }*/
    }
    public void removeSub(PlayerBoardSub playerBoardSub){
        for(Player p: getPlayers()){
            p.getPlayerBoard().getPlayerBoardSubs().remove(playerBoardSub);
        }
    }
    public void sendPrivateMessage(String sender, String receiver, String message) {
        Text t=new Text(sender, receiver,message);
        chat.NotifyChat(t);
    }
    public void sendGroupMessage(String sender, String message){
        Text t=new Text(sender,message);
        chat.NotifyChat(t);
    }

    public PlayerSub findSub(Player p) {
        PlayerSub player=null;
        for (PlayerSub playerSub : p.getPlayerSubs()) {
            try {
                if (playerSub.getSub().equals(p.getNickname())) {
                    player=playerSub;
                }
            } catch (RemoteException ignored) {
            }
        }
        return player;
    }

    public PlayerSub findSub(String nickname){
        //cerco nella lista dei giocatori il giocatore corrispondente
        Player x=null;
        for(Player p: players){
            if(p.getNickname().equals(nickname)){
                x=p;
                break;
            }
        }
        PlayerSub ps=null;
        for(PlayerSub playerSub: x.getPlayerSubs()){
            try{
                if(playerSub.getSub().equals(x.getNickname())){
                    ps=playerSub;
                }
            }catch (RemoteException ignored){}
        }
        return ps;
    }

    public Player findPlayer(String player){
        Player pl=null;
        for(Player p: players){
            if(p.getNickname().equals(player)){
                pl=p;
            }
        }
        return pl;
    }

    public void manageUpdate(String player){
        ArrayList<ResourceCard> hand=new ArrayList<>();
        hand=findPlayer(player).getHand();
        ArrayList<ResourceCard> table=new ArrayList<>();
        if(resourceDeck.isEmpty()){
            table.add(0,null);
        }
        else{
            table.add(0,resourceDeck.getCards().get(0));
        }
        if(goldDeck.isEmpty()){
            table.add(1,null);
        }
        else{
            table.add(1,goldDeck.getCards().get(0));
        }
        if(tableCards.get(0)==null){
            table.add(2,null);
        }
        else{
            table.add(2,tableCards.get(0));
        }
        if(tableCards.get(1)==null){
            table.add(3,null);
        }
        else{
            table.add(3,tableCards.get(1));
        }
        if(tableCards.get(2)==null){
            table.add(4,null);
        }
        else{
            table.add(4,tableCards.get(2));
        }
        if(tableCards.get(3)==null){
            table.add(5,null);
        }
        else{
            table.add(5,tableCards.get(3));
        }

        ArrayList<ObjectiveCard> objectiveCards =new ArrayList<>();
        objectiveCards=commonObjective;
        Color color;
        color=findPlayer(player).getPawncolor();
        String current;
        current=getPlayers().get(currentPlayer).getNickname();
        ObjectiveCard objectiveCard;
        objectiveCard=findPlayer(player).getObjectiveCard();
        Map<String, PlayableCard[][]> boards=new HashMap<>();
        for(Player p: getPlayers()){
            boards.put(p.getNickname(), p.getPlayerBoard().getBoard());
        }
        Map<String, Integer> points=new HashMap<>();
        for(Player p: getPlayers()){
            points.put(p.getNickname(),p.getPoints());
        }
        ArrayList<String> order=new ArrayList<>();
        for (Player p : players) {
            order.add(p.getNickname());
        }
        State gameState;
        gameState=getGameState();
        ArrayList<Text> chat=new ArrayList<>();
        chat=this.chat.getAll(player);
        ArrayList<Color> colors = new ArrayList<>();
        for(Player p: getPlayers())
            colors.add(p.getPawncolor());
        for(GameSub gameSub:getGameSubs()){
            try{
                if(gameSub.getSub().equals(player)){
                    gameSub.UpdateCrashedPlayer(current,chat,gameState,hand,objectiveCard,boards,points,order,objectiveCards,color,table, colors);
                }
            }catch (RemoteException ignored){}
        }

    }

    public int getNumPlayersConnected(){
        return numPlayersConnected;
    }

    public void setNumPlayersConnected(int value){
        numPlayersConnected=value;
    }


    public ArrayList<String> extractNicknames(){
        ArrayList<String> nicknames=new ArrayList<>();
        for(int i=0; i<getPlayers().size();i++){
            nicknames.add(getPlayers().get(i).getNickname());
        }
        return nicknames;
    }

}
