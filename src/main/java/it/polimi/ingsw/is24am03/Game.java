package it.polimi.ingsw.is24am03;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to handle the main logic of a Game
 */
public class Game implements GameInterface {
    /**
     * The identification number of the game
     */

    private int gameId;
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
     * The reference to the resource deck to draw resources cards
     */
    private ResourceDeck resourceDeck;

    /**
     * The reference to the starting deck to draw starting cards
     */
    private StartingDeck startingDeck;

    /**
     * The reference to the objective deck to draw objective cards
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
     * List of the colors that can be picked by the players
     */
    private ArrayList<Color> availableColors;
    private RoundManager round;
    private State gameState;

    /**
     * Constructor of the Game objects, it initializes all the attributes and add the host to the game
     * @param gId is the identification number of the game
     * @param nPlayers is the total number of the players for the game
     * @param host is the Player that have created the game
     */
    public Game(int gId, int nPlayers, Player host) {
        this.gameId = gId;
        this.gameState = State.WAITING;
        this.numPlayers = nPlayers;
        this.tableCards = new ArrayList<ResourceCard>();
        this.resourceDeck = new ResourceDeck();
        this.goldDeck = new GoldDeck();
        this.startingDeck = new StartingDeck();
        this.objectiveDeck = new ObjectiveDeck();
        this.players = new ArrayList<Player>();
        this.availableColors = new ArrayList<Color>(List.of(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW));
        this.commonObjective = new ArrayList<ObjectiveCard>();

        addPlayer(host);
    }

    /**
     * This method is used to start the game, it chooses randomly a starting player, distributes the cards to the players,
     * initializes the common objective cards, shuffles the decks and unreveal the first four cards on the table
     */
    public void startGame() {
        this.gameState = State.PREPARATION;
        setOrder();
        startingDeck.shuffle();
        objectiveDeck.shuffle();
        goldDeck.shuffle();
        resourceDeck.shuffle();
        setCommonObjective();
        initializeTable();
        distributeCards();
        this.round = new RoundManager(players);
        this.gameState = State.PLAYING;
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
    public ArrayList<Color> getAvailableColors() {
        return availableColors;
    }

    /**
     * This method set the two common cards of the game
     */
    public void setCommonObjective() {
        commonObjective.add(objectiveDeck.draw());
        commonObjective.add(objectiveDeck.draw());
    }

    /**
     * This method give to each player the starting card, two resource card and one gold card
     */
    public void distributeCards() {
        for (Player p : players) {
            p.addCard(resourceDeck.draw());
            p.addCard(resourceDeck.draw());
            p.addCard(goldDeck.draw());
            p.setStartingCard(startingDeck.draw());
        }
    }

    /**
     * Method that set the first four cards revealed on the table
     */
    public void initializeTable() {
        tableCards.add(resourceDeck.draw());
        tableCards.add(resourceDeck.draw());
        tableCards.add(goldDeck.draw());
        tableCards.add(goldDeck.draw());
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
                w.setWinner();
        }
        return winners;
    }

    /**
     * This method allow to add a player to the game
     * @param P represent the player to be added
     */
    public void addPlayer(Player P) {
        players.add(P);
        if(players.size()==numPlayers)
            startGame();
    }

    /*public void removePlayer(Player P){
        players.remove(P);
    }*/

    /**
     * This method calculates the number of objective card realized by each player and gives to them the
     * corresponding points
     */
    public void giveObjectivePoints() {
        int flag;
        for (Player p : players) {
            flag = p.getPlayerBoard().checkObjective(p.getObjectiveCard());
            p.increaseNumObjective(flag);
            p.addPoints(p.getObjectiveCard().getPoints());
            for (ObjectiveCard oc : commonObjective) {
                flag = p.getPlayerBoard().checkObjective(oc);
                p.increaseNumObjective(flag);
                p.addPoints(oc);
            }
        }
    }

    /**
     * Add a resource card to the selected player
     * @param P represents the player who is drawing
     * @param RD consists of the resource deck from which the player is drawing
     */
    public void drawResources(Player P, ResourcesDeck RD) {
        if(RD.isEmpty())
            throw new EmptyDeckException();
        P.addCard(RD.draw());
        if(!round.nextTurn())
            endGame();
        //return???
    }

    /**
     * Add a gold card to the selected player
     * @param P represents the player who is drawing
     * @param GD consists of the gold deck from which the player is drawing
     */
    public void drawGold(Player P, GoldDeck GD) {
        P.addCard(GD.draw());
        //return???
    }

    /**
     * This method lets the player drawing from the table selecting one of the four
     * unrevealed cards
     * @param P represents the player who is drawing
     * @param choice consists of the card chosen among the four cards on the table
     */
    public void drawTable(Player P, int choice) {
        if (tableCards.get(choice) == null)
            return;
        switch (choice) {
            case 0:
                P.addCard(tableCards.get(0));
                if (!resourceDeck.isEmpty())
                    tableCards.set(0, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(0, goldDeck.draw());
                else
                    tableCards.set(0, null);
                break;

            case 1:
                P.addCard(tableCards.get(1));
                if (!resourceDeck.isEmpty())
                    tableCards.set(1, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(1, goldDeck.draw());
                else
                    tableCards.set(1, null);
                break;

            case 2:
                P.addCard(tableCards.get(2));
                if (!goldDeck.isEmpty())
                    tableCards.set(2, goldDeck.draw());
                else if (!resourceDeck.isEmpty())
                    tableCards.set(2, resourceDeck.draw());
                else
                    tableCards.set(2, null);
                break;

            case 3:
                P.addCard(tableCards.get(3));
                if (!resourceDeck.isEmpty())
                    tableCards.set(3, resourceDeck.draw());
                else if (!goldDeck.isEmpty())
                    tableCards.set(3, goldDeck.draw());
                else
                    tableCards.set(3, null);
                break;
        }
        round.nextTurn();
    }

    /**
     * This method allows to place a card
     * @param P consists of the player who is placing
     * @param c consists of the card to be placed
     * @param i consists of the vertical position in the player's board
     * @param j consists of the horizontal position in the player's board
     * @param face consists of the face of the card chosen by the player
     */

    public void placeCard(Player P, PlayableCard c, int i, int j, boolean face) {
        P.getPlayerBoard().placeCard(c, i, j, face);
        this.gameState = State.DRAWING;
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

    public ArrayList<Player> getPlayers()
    {
        return players;
    }
}
