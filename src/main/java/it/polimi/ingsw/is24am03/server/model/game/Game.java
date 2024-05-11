package it.polimi.ingsw.is24am03.server.model.game;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.decks.GoldDeck;
import it.polimi.ingsw.is24am03.server.model.decks.ObjectiveDeck;
import it.polimi.ingsw.is24am03.server.model.decks.ResourceDeck;
import it.polimi.ingsw.is24am03.server.model.decks.StartingDeck;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.is24am03.server.model.exceptions.NotAllPlayersHaveJoinedException;
import it.polimi.ingsw.is24am03.server.model.exceptions.NullCardSelectedException;
import it.polimi.ingsw.is24am03.server.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private ArrayList<PlayableCard> tableCards;

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
     * Constructor of the Game objects, it initializes all the attributes and add the host to the game
     * @param nPlayers is the total number of the players for the game
     * @param host is the Player that have created the game
     */
    public Game(int nPlayers, String host) {
        this.lastRound = false;
        this.ending = false;
        this.roundNumber=0;
        this.currentPlayer = -1;
        this.gameState = State.WAITING;
        this.numPlayers = nPlayers;
        this.tableCards = new ArrayList<PlayableCard>();
        this.resourceDeck = new ResourceDeck();
        this.goldDeck = new GoldDeck();
        this.startingDeck = new StartingDeck();
        this.objectiveDeck = new ObjectiveDeck();
        this.players = new ArrayList<Player>();
        this.commonObjective = new ArrayList<ObjectiveCard>();
        this.availableColors = new ArrayList<Color>(List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
        addPlayer(host);
    }

    /**
     * This method is used to start the game, it chooses randomly a starting player, distributes the cards to the players,
     * initializes the common objective cards, shuffles the decks and reveals the first four cards on the table
     */
    public void startGame() throws NotAllPlayersHaveJoinedException {
        if(players.size()!=numPlayers)
            throw new NotAllPlayersHaveJoinedException();
        setOrder();
        this.gameState = State.STARTING;
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
        giveObjectivePoints();
        checkWinner();
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
        if (players.size() == numPlayers) {
            try {
                startGame();
            } catch (NotAllPlayersHaveJoinedException e) {
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
            case 0:
                p.addCard(tableCards.get(0));
                if (!resourceDeck.isEmpty())
                    tableCards.set(0, resourceDeck.drawCard());
                else if (!goldDeck.isEmpty())
                    tableCards.set(0, goldDeck.drawCard());
                else
                    tableCards.set(0, null);
                break;

            case 1:
                p.addCard(tableCards.get(1));
                if (!resourceDeck.isEmpty())
                    tableCards.set(1, resourceDeck.drawCard());
                else if (!goldDeck.isEmpty())
                    tableCards.set(1, goldDeck.drawCard());
                else
                    tableCards.set(1, null);
                break;

            case 2:
                p.addCard(tableCards.get(2));
                if (!goldDeck.isEmpty())
                    tableCards.set(2, goldDeck.drawCard());
                else if (!resourceDeck.isEmpty())
                    tableCards.set(2, resourceDeck.drawCard());
                else
                    tableCards.set(2, null);
                break;

            case 3:
                p.addCard(tableCards.get(3));
                if (!resourceDeck.isEmpty())
                    tableCards.set(3, resourceDeck.drawCard());
                else if (!goldDeck.isEmpty())
                    tableCards.set(3, goldDeck.drawCard());
                else
                    tableCards.set(3, null);
                break;
        }
        nextTurn();
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
        p.getPlayerBoard().placeCard(p.getHand().get(choice), i, j, face);
        if(p.getPoints()>=20)
            ending=true;
        if(!lastRound)
            this.gameState = State.DRAWING;
        else{
            if(currentPlayer==numPlayers-1)
                endGame();
            else
                nextTurn();
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

    public State getGameState()
    {
        return gameState;
    }


    public void selectStartingFace(String player, boolean face)
    {
        Player p = players.get(currentPlayer);
        p.getPlayerBoard().placeStartingCard(p.getStartingCard(), face);
        nextTurn();
    }

    public void setObjectiveCard(String player, int choice) {
        Player p = players.get(currentPlayer);
        p.setObjectiveChoice(choice);
        nextTurn();
    }

    public void setColor(Color color){
        Player p = players.get(currentPlayer);
        p.setPawncolor(color);
        for(int i=0; i<availableColors.size(); i++)
            if(availableColors.get(i).equals(color))
                availableColors.remove(i);
        nextTurn();
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
            else if(gameState.equals(State.STARTING))
                gameState = State.COLOR;
            else if(gameState.equals(State.COLOR))
                gameState = State.OBJECTIVE;
            else if(gameState.equals(State.OBJECTIVE))
                gameState = State.PLAYING;
            if(ending)
                lastRound = true;
        }
        if(gameState.equals(State.DRAWING))
            gameState = State.PLAYING;
        currentPlayer = (currentPlayer+1)%(numPlayers);
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

    public ArrayList<PlayableCard> getTableCards() {
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

    /*
    public ObjectiveCard drawObjectiveOptions(){
        return objectiveDeck.drawCard();
    }*/
}
