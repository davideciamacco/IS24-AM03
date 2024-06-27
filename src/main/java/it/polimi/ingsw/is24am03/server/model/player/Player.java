package it.polimi.ingsw.is24am03.server.model.player;

import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.util.ArrayList;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String nickname;
    private Color pawncolor;
    private final ArrayList<ResourceCard> hand;
    private ObjectiveCard objective1;
    private ObjectiveCard objective2;
    private StartingCard startingCard;
    private boolean winner;
    private boolean firstPlayer;
    private int points;
    private int objectivechoose;
    private int numObj;
    private PlayerBoard playerBoard;
    private boolean connected;
    private ArrayList<PlayerSub> playerSubs;

    /**
     * Constructs a new player with the given nickname.
     *
     * @param nickname The player's nickname.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.objectivechoose = 0;
        this.objective1 = null;
        this.objective2 = null;
        this.startingCard = null;
        this.winner = false;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = new PlayerBoard(this);
        this.playerSubs = new ArrayList<>();
        this.connected = true;
    }

    /**
     * Sets the player's objective cards.
     *
     * @param o1 The first objective card to set.
     * @param o2 The second objective card to set.
     */
    public void setObjectiveCard(ObjectiveCard o1, ObjectiveCard o2) {
        this.objective1 = o1;
        this.objective2 = o2;
    }

    /**
     * Sets the player's choice of objective.
     *
     * @param choose The objective choice to set.
     */
    public void setObjective(int choose) {
        this.objectivechoose = choose;
    }

    /**
     * Sets the player's starting card.
     *
     * @param startingCard The starting card to set.
     */
    public void setStartingCard(StartingCard startingCard) {
        this.startingCard = startingCard;
    }

    /**
     * Sets whether the player is the winner.
     *
     * @param winner True if the player is the winner, false otherwise.
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    /**
     * Sets the player's board.
     *
     * @param playerBoard The player board to set.
     */
    public void setPlayerBoard(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Sets the player's pawn color.
     *
     * @param color The pawn color to set.
     */
    public void setPawncolor(Color color) {
        this.pawncolor = color;
    }

    /**
     * Gets the player's pawn color.
     *
     * @return The player's pawn color.
     */
    public Color getPawncolor() {
        return pawncolor;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param c The card to add to the hand.
     */
    public void addCard(ResourceCard c) {
        hand.add(c);
    }

    /**
     * Adds points to the player's score.
     *
     * @param pointsToAdd The points to add.
     */
    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }

    /**
     * Gets the player's hand.
     *
     * @return The player's hand.
     */
    public ArrayList<ResourceCard> getHand() {
        return hand;
    }

    /**
     * Gets the player's total points.
     *
     * @return The player's total points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card The card to remove.
     */
    public void removeCard(PlayableCard card) {
        hand.remove(card);
    }

    /**
     * Checks if the player is the first player.
     *
     * @return True if the player is the first player, false otherwise.
     */
    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Gets the player's nickname.
     *
     * @return The player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets whether the player is the first player.
     *
     * @param firstPlayer True if the player is the first player, false otherwise.
     */
    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * Gets the player's board.
     *
     * @return The player's board.
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Gets the number of objectives the player has achieved.
     *
     * @return The number of objectives achieved.
     */
    public int getNumObj() {
        return numObj;
    }

    /**
     * Gets the player's chosen objective card.
     *
     * @return The chosen objective card.
     */
    public ObjectiveCard getObjectiveCard() {
        if (objectivechoose == 1) {
            return objective1;
        } else {
            return objective2;
        }
    }

    /**
     * Sets the player's choice of objective card.
     *
     * @param objectivechoose The choice of objective card to set.
     */
    public void setObjectiveChoice(int objectivechoose) {
        this.objectivechoose = objectivechoose;
    }

    /**
     * Gets the first objective card of the player.
     *
     * @return The first objective card.
     */
    public ObjectiveCard getObjective1() {
        return objective1;
    }

    /**
     * Gets the second objective card of the player.
     *
     * @return The second objective card.
     */
    public ObjectiveCard getObjective2() {
        return objective2;
    }

    /**
     * Sets the player's total points.
     *
     * @param p The total points to set.
     */
    public void setPoints(int p) {
        this.points = p;
    }

    /**
     * Increases the number of objectives achieved by the player.
     *
     * @param n The number to increase by.
     */
    public void increaseNumObjective(int n) {
        numObj += n;
    }

    /**
     * Gets the starting card of the player.
     *
     * @return The starting card.
     */
    public StartingCard getStartingCard() {
        return startingCard;
    }

    /**
     * Gets the player's choice of objective.
     *
     * @return The choice of objective.
     */
    public int getObjectivechoose() {
        return objectivechoose;
    }

    /**
     * Gets the list of subscribers (observers) for the player.
     *
     * @return The list of subscribers.
     */
    public ArrayList<PlayerSub> getPlayerSubs() {
        return playerSubs;
    }

    /**
     * Sets the connection status of the player.
     *
     * @param state True if connected, false otherwise.
     */
    public void setConnected(boolean state) {
        this.connected = state;
    }

    /**
     * Checks if the player is connected.
     *
     * @return True if connected, false otherwise.
     */
    public boolean getConnected() {
        return connected;
    }

    /**
     * Checks if the player is the winner.
     *
     * @return True if the player is the winner, false otherwise.
     */
    public boolean isWinner() {
        return winner;
    }
}
