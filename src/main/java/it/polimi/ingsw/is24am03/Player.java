package it.polimi.ingsw.is24am03;


import java.util.ArrayList;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String nickname;
    private final  Color pawncolor;
    private final ArrayList<PlayableCard> hand;
    private  ObjectiveCard objective1;
    private  ObjectiveCard objective2;
    private  StartingCard startingCard;
    private boolean winner,firstPlayer;
    private int points;
    private int objectivechoose;
    private int numObj;
    private  PlayerBoard playerBoard;
    /**
     * Constructs a new player with the given parameters.
     *
     * @param nickname     The player's nickname.
     * @param pawncolor    The color of the player's pawn.
     */
    public Player(String nickname, Color pawncolor) {
        this.nickname = nickname;
        this.pawncolor = pawncolor;
        this.hand = new ArrayList<>();
        this.objective1 =null;
        this.objective2 =null;
        this.startingCard = null;
        this.winner = false;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = null;
    }
    public boolean isWinner(){
        return this.winner;
    }
    /**
     * Sets the player's objective card.
     *
     * @param o1 and o2 The objective card to set.
     */
    public void setObjectiveCard12(ObjectiveCard o1,ObjectiveCard o2){
        this.objective1=o1;
        this.objective2=o2;
    }
    public void setObjective(int choose) {
        this.objectivechoose = choose;
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
        hand.add(c);
    }
    /**
     * Adds points to the player based on a playable card.
     *
     * @param card The card from which points are to be added.
     */
    public void addPoints(Card card) {
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
        //    assert hand != null;
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
    public int getObjectiveCard(){
        return objectivechoose;
    }
    public ObjectiveCard getObjective1(){
        return objective1;
    }
    public ObjectiveCard getObjective2(){
        return objective2;
    }
    public void setPoints(int p){
        this.points=p;
    }
    public void increaseNumObjective(int n){
        numObj=numObj+n;
    }
    public PlayableCard getStartingCard(){
        return startingCard;
    }
}