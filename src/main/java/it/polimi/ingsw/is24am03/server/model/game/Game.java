package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.Subscribers.GameSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerBoardSub;
import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.chat.Chat;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.decks.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.player.Player;
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
        //notifico a tutti che il gioco sta iniziando perchè ho raggiunto il numero di giocatori
        //l'ultimo player entrato è già iscritto al gioco

        for(GameSub gameSub: gameSubs){
            try{
                gameSub.NotifyNumbersOfPlayersReached();
            }catch (RemoteException ignored){}
        }
        //notify turn order//
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

        //NOTIFY CHANGE STATE
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.notifyChangeState(gameState);
            } catch (RemoteException ignored) {
            }
        }
        this.gameState = State.STARTING;
        startingDeck.shuffle();
        objectiveDeck.shuffle();
        goldDeck.shuffle();
        resourceDeck.shuffle();
        setCommonObjective();
        initializeTable();
        //NOFITY COMMON CARDS//

        //WE NOTIFY FIRST ON THE FIRST CARD IN RESOURCE DECK AND IN GOLD DECK

        for(GameSub gameSub: gameSubs){
            try {
                gameSub.updateCommonTable(resourceDeck.getCards().get(0),0);
                gameSub.updateCommonTable(goldDeck.getCards().get(0),1);
            }catch (RemoteException ignored){}
        }
        //notify on the four common cards
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.updateCommonTable(tableCards.get(0),2);
            } catch (RemoteException ignored) {
            }
            try {
                gameSub.updateCommonTable(tableCards.get(1),3);
            } catch (RemoteException ignored) {
            }
            try {
                gameSub.updateCommonTable(tableCards.get(2),4);
            } catch (RemoteException ignored) {
            }
            try {
                gameSub.updateCommonTable(tableCards.get(3),5);
            } catch (RemoteException ignored) {}
        }
        //DONE

        //NOTIFY ON COMMON OBJECTIVE
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.notifyCommonObjective(commonObjective.get(0), commonObjective.get(1));
            }catch(RemoteException ignored){}


        }
        //DONE

        distributeCards();
        nextTurn();
    }


    /**
     * This method is used to enter the game in the ending phase, in which points from objective cards are giver
     * to the players and the winner is calculated
     */
    public void endGame() {
        gameState = State.ENDING;
        //NOTIFY ON CHANGE STATE
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.notifyChangeState(gameState);
            } catch (RemoteException ignored) {
            }
        }
        //DONE
        giveObjectivePoints();
        //SALVO RISULTATO DI CHECK WINNER E LO NOTIFICO//
        List<String> winners= checkWinner().stream().map(Player::getNickname).toList();
        ArrayList<String> def= new ArrayList<>(winners);
        //NOTIFY ON WINNERS//
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.notifyWinners(def);
            } catch (RemoteException ignored) {
            }
        }
        //DONE
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
            p.setObjectiveCard(objectiveDeck.drawCard(),objectiveDeck.drawCard());
            //NOTIFY FIRST HAND, comprende anche la starting card e le due objetive cards//
            //trovo il sub corrispondente al player a cui devo notificare le proprie carte

            try {
                findSub(p).notifyFirstHand(p.getHand().get(0), p.getHand().get(1), p.getHand().get(2), p.getStartingCard(),p.getObjective1(),p.getObjective2());
            }catch (RemoteException ignored){}


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
        //se questo è il primo giocatore allora non devo notificare
        if(players.size()>1){
            //NOTIFY ON JOINED PLAYER, notifico a tutti tranne il player appena entrato
            for (GameSub gameSub : gameSubs) {
                try {
                    if(!gameSub.getSub().equals(player)){
                        gameSub.notifyJoinedPlayer(player);}
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
    public void drawResources(String player) throws EmptyDeckException {
        if(resourceDeck.isEmpty())
            throw new EmptyDeckException("There aren't other cards in the selected deck");
        getPlayers().get(currentPlayer).addCard(resourceDeck.drawCard());
        //DOPO PESCA HO NOTIFY_CHANGE_PERSONAL_CARDS CHE MI AGGIORNA LE CARTE DEL GIOCATORE
        try {
            findSub(player).NotifyChangePersonalCards(player, getPlayers().get(currentPlayer).getHand());
        }catch (RemoteException ignored){}

        //SE IL DECK RISORSA è EMPTY NOTIFY ON EMPTY DECK
        if(resourceDeck.isEmpty()){
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.updateCommonTable(null,0);
                } catch (RemoteException ignored) {
                }
            }
        }
        //DONE
        if(!resourceDeck.isEmpty()){
            //DOPO PESCA HO NOTIFY_RESOURCE_DECK CHE MI AGGIORNA LA CARTA PRESENTE NEL MAZZO RESOURCE
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(resourceDeck.getCards().get(0),0);
                }catch (RemoteException ignored){}
            }
        }

        //DONE

        if(resourceDeck.isEmpty() && goldDeck.isEmpty())
            ending=true;
        nextTurn();
    }

    /**
     * Add a gold card to the selected player
     * @param player represents the player who is drawing
     */
    public void drawGold(String player) throws EmptyDeckException {
        if(goldDeck.isEmpty())
            throw new EmptyDeckException("There aren't other cards in the selected deck");
        getPlayers().get(currentPlayer).addCard(goldDeck.drawCard());
        System.out.println("\nID:"+getPlayers().get(currentPlayer).getHand().getFirst().getId()+"\n");
        //DOPO PESCA HO NOTIFY_CHANGE_PERSONAL_CARDS CHE MI AGGIORNA LE CARTE DEL GIOCATORE
        try {
            findSub(getPlayers().get(currentPlayer)).NotifyChangePersonalCards(player, getPlayers().get(currentPlayer).getHand());

        }catch (RemoteException ignored){}

        //SE IL DECK GOLD è EMPTY NOTIFY ON EMPTY DECK
        if(goldDeck.isEmpty()){
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(null,1);
                }catch (RemoteException ignored){}
            }
        }
        //DONE

        //SE DOPO PESCA GOLD DECK NON è VUOTO ALLORA AGGIORNO GOLD DECK
        if(!goldDeck.isEmpty()){
            for(GameSub gameSub: gameSubs){
                try {
                    gameSub.updateCommonTable(goldDeck.getCards().get(0),1);
                }catch (RemoteException ignored){}
            }
        }
        //DONE
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
    public void drawTable(String player, int choice) throws NullCardSelectedException {
        if (tableCards.get(choice) == null)
            throw new NullCardSelectedException();
        Player p = players.get(currentPlayer);
        switch (choice) {
            case 1:
                p.addCard(tableCards.get(0));
                try {
                    findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                } catch (RemoteException ignored) {
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
                try {
                    findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                } catch (RemoteException ignored) {
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
                try {
                    findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                } catch (RemoteException ignored) {
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
                try {
                    findSub(p).NotifyChangePersonalCards(p.getNickname(), p.getHand());
                } catch (RemoteException ignored) {
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
        //notify common cards, notifico i sub di game che c'è una nuova carta in posizione index (0,1,2,3)
        for (GameSub gameSub : gameSubs) {
            try {
                gameSub.updateCommonTable(resourceCard,index);
            } catch (RemoteException ignored) {
            }
        }

        //check che il deck corrispondente sia vuoto o meno
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
                        gameSub.updateCommonTable(resourceDeck.getCards().getFirst(), deck);

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
        //notifico tutti i sub della playerboard di p che player ha piazzato una carta
        p.getPlayerBoard().notifyChangePlayerBoard(player,p.getPlayerBoard().getBoard()[i][j],i,j);
        //notifico a tutti i sub del player che ha appena giocato l'update dei punti
        for(PlayerSub playerSub: p.getPlayerSubs()){
            try{
                playerSub.ReceiveUpdateOnPoints(player,p.getPoints());
            }catch (RemoteException ignored){}
        }
        if(p.getPoints()>=20)
            ending=true;
        if(!lastRound) {
            this.gameState = State.DRAWING;
            //notifico a tutti il cambiamento dello stato
            for (GameSub gameSub : gameSubs) {
                try {
                    gameSub.notifyChangeState(gameState);
                } catch (RemoteException ignored) {
                }
            }

        }
        else{
            if(currentPlayer==numPlayers-1) {
                try {
                    findSub(player).NotifyChangePersonalCards(player, p.getHand());
                }catch (RemoteException ignored){}
                //notifico a quello che ha pescato il cambiamento delle sue carte, ora sono solo due
                endGame();
            }
            else {
                try {
                    findSub(player).NotifyChangePersonalCards(player, p.getHand());
                }catch (RemoteException ignored){}

                nextTurn();
            }
        }
    }

    /*
    public void sendMessage(Player sender, Player receiver, String message) {

    }

    public void sendMessageAll(Player sender, String message) {

    }*/


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
        //notifico tutti i sub della playerboard di p che ha piazzato una carta
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
        //NOTIFICO IL PROSSIMO PLAYER DEI COLORI RIMASTI

        nextTurn();
        //se lo stato è cambiato significa che ho finito, se è ancora color mando al current player la notifica dei colori disponibili
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
            if(ending && lastRound)
                endGame();
            else if(gameState.equals(State.DRAWING))
                roundNumber++;
            else if(gameState.equals(State.STARTING)) {
                gameState = State.COLOR;
                //NOTIFICO A TUTTI CHE SIAMO NELLO STATO DI SCELTA DEL COLORE
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.notifyChangeState(gameState);
                    } catch (RemoteException ignored) {
                    }
                }
                //notifico solo al primo client i colori disponibili, ovvero tutti i colori
                try {
                    findSub(players.getFirst()).notifyAvailableColors(availableColors);
                }catch (RemoteException ignored){}
            }
            else if(gameState.equals(State.COLOR)) {
                //qui notifico a tutti dei colori dei giocatori
                //creo mappa stringa nome, colore
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
        while(!players.get(currentPlayer).getConnected() && !timer) {
            currentPlayer = (currentPlayer + 1) % (numPlayers);
        }
        int i;
        if(timer) {
            if (numPlayersConnected == 1) {
                for (i=0; i<numPlayers; i++) {
                    if (players.get(i).getConnected())
                        players.get(i).setWinner(true);
                }
            }
            gameState = State.ENDING;
        }
        else {
                for (GameSub gameSub : gameSubs) {
                    try {
                        gameSub.notifyCurrentPlayer(players.get(currentPlayer).getNickname());
                    } catch (RemoteException ignored) {
                    }
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

    public void setGameState(State state){
        this.gameState = state;
    }

    //UPDATES FOR OBSERVERS//
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
        PlayerSub first =findSub(players.getFirst());
        for(int i=1; i<players.size(); i++) {
            players.get(i).getPlayerSubs().add(first);
        }
    }
    public void removeSub(PlayerSub playerSub){
        for(Player p: getPlayers()){
            p.getPlayerSubs().remove(playerSub);
        }
        //però tutti gli altri sub devono
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
        //ho ottenuto il sub della playerboard del primo giocatore
        PlayerBoardSub first = players.getFirst().getPlayerBoard().getPlayerBoardSubs().getFirst();
        for(int i=1; i<players.size(); i++) {
            players.get(i).getPlayerBoard().getPlayerBoardSubs().add(first);
        }

    }
    public void removeSub(PlayerBoardSub playerBoardSub){
        for(Player p: getPlayers()){
            p.getPlayerBoard().getPlayerBoardSubs().remove(playerBoardSub);
        }
    }
    public void sendPrivateMessage(String sender, String receiver, String message) throws PlayerAbsentException, BadTextException, InvalidStateException,ParametersException{
        //controllo su stato del gioco
        if(gameState==State.WAITING || gameState==State.ENDING){
            throw new InvalidStateException("You cannot send a text during this state");
        }
        List<String> player= players.stream().map(Player::getNickname).toList();
        player=new ArrayList<>(player);
        if(!player.contains(receiver)){
            throw new PlayerAbsentException("Receiver isn't part of the game");
        }
        //se il player è crashato non posso mandare messaggio
        //
        //controllo su sender==null receiver==null  oppure sono vuoti, bad text exception
        if(sender.isEmpty() || receiver.isEmpty() || message.isEmpty()){
            throw new BadTextException("receiver or text are empty,try again");
        }
        if(sender.equals(receiver)){
            throw new ParametersException("You cannot send a message to yourself");
        }

        Text t=new Text(sender, receiver,message);
        chat.NotifyChat(t);
    }
    //nel game faccio tutti i controlli sul messaggio
    public void sendGroupMessage(String sender, String message) throws BadTextException, InvalidStateException{
        if(gameState==State.WAITING || gameState==State.ENDING){
            throw new InvalidStateException("You cannot send a text during this state");
        }
        if(sender.isEmpty() || message.isEmpty()){
            throw  new BadTextException("Sender or text can't be empty");
        }

        Text t=new Text(sender, "", message);
        chat.NotifyChat(t);
    }
    /*
    public ObjectiveCard drawObjectiveOptions(){
        return objectiveDeck.drawCard();
    }*/

    //METHODS USED TO HELP FINDING THE RIGHT OBSERVER//
    //trovo sub corrispondente a un certo player
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
        //una volta trovato il giocatore ritorno il sub corrispondente a esso
        for(PlayerSub playerSub: x.getPlayerSubs()){
            try{
                if(playerSub.getSub().equals(x.getNickname())){
                    ps=playerSub;
                }
            }catch (RemoteException ignored){}
        }
        return ps;
    }





    public int getNumPlayersConnected(){
        return numPlayersConnected;
    }

    public void setNumPlayersConnected(int value){
        numPlayersConnected=value;
    }

    public void setTimer(boolean b) {
        timer=b;
    }
}
