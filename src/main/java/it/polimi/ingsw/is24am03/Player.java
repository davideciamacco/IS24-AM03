package it.polimi.ingsw.is24am03;


import java.util.ArrayList;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String nickname;
    private final Color pawncolor;
    private final ArrayList<PlayableCard> hand;
    private ObjectiveCard objective;
    private StartingCard startingCard;
    private boolean winner,firstPlayer;
    private int points;
    private int numObj;
    private PlayerBoard playerBoard;
    /**
     * Constructs a new player with the given parameters.
     *
     * @param nickname     The player's nickname.
     * @param pawncolor    The color of the player's pawn.
     * @param firstPlayer  Indicates if the player is the first player.
     */
    public Player(String nickname, Color pawncolor,boolean firstPlayer) {
        this.nickname = nickname;
        this.pawncolor = pawncolor;
        this.hand = null;
        this.objective =null;
        this.startingCard = null;
        this.winner = false;
        this.firstPlayer =false;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = null;
    }
    /**
     * Sets the player's objective card.
     *
     * @param objective The objective card to set.
     */

    public void setObjective(ObjectiveCard objective) {
        this.objective = objective;
    }
    /**
     * Sets the player's starting card.
     *
     * @param startingCard The starting card to set.
     *
     */
    public void setStartingCard(StartingCard startingCard) {
        this.startingCard=startingCard;
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
     * Adds a card to the player's hand.
     *
     * @param c The card to add to the hand.
     */
    public void addCard(PlayableCard c) {
        assert hand != null;
        hand.add(c);
    }
    /**
     * Adds points to the player based on a playable card.
     *
     * @param card The card from which points are to be added.
     */
    public void addPoints(PlayableCard card) {
        int pointsToAdd = card.getPoints();
        points=points+pointsToAdd;
    }
    /**
     * Gets the player's hand.
     *
     * @return The player's hand.
     */
    public ArrayList<PlayableCard> getHand() {
        return hand;
    }
    /**
     * Gets the player's points.
     *
     * @return The player's points.
     */
    public int getPoints() {
        return points;
    }
    /**
     * Removes a card from the player's hand.
     *
     * @param card The card to remove from the hand.
     */
    public void removeCard(PlayableCard card){
        assert hand != null;
        hand.remove(card);
    }
    public boolean isFirstPlayer() {
        return firstPlayer;
    }
    public String getNickname() {
        return nickname;
    }
    public void setFirstPlayer(boolean firstPlayer){
        this.firstPlayer=firstPlayer;
    }
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }
    public int getNumObj(){
        return numObj;
    }
    public ObjectiveCard getObjectiveCard(){
        return objective;
    }
    public void setPoints(int p){
        this.points=p;
    }

    public void increaseNumObjective(int n){
        numObj=numObj+n;
    }
}