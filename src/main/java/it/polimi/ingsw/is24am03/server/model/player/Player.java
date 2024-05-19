package it.polimi.ingsw.is24am03.server.model.player;


import it.polimi.ingsw.is24am03.Subscribers.PlayerSub;
import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String nickname;
    private Color pawncolor;
    private final ArrayList<ResourceCard> hand;
    private ObjectiveCard objective1;
    private  ObjectiveCard objective2;
    private StartingCard startingCard;
    private boolean winner,firstPlayer;
    private int points;
    private int objectivechoose;
    private int numObj;
    private  PlayerBoard playerBoard;

    private boolean connected;

    private ArrayList<PlayerSub> playerSubs;

    /**
     * Constructs a new player with the given parameters.
     *
     * @param nickname     The player's nickname.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.objectivechoose=0;
        this.objective1 =null;
        this.objective2 =null;
        this.startingCard = null;
        this.winner = false;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = new PlayerBoard(this);
        this.playerSubs=new ArrayList<>();
        this.connected = true;
    }
    public boolean isWinner(){
        return this.winner;
    }
    /**
     * Sets the player's objective card.
     *
     * @param o1 and o2 The objective card to set.
     */
    public void setObjectiveCard(ObjectiveCard o1,ObjectiveCard o2){
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

    public void setPawncolor(Color color){
        this.pawncolor=color;
    }

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
     * Adds points to the player based on a playable card.
     *
     * @param pointsToAdd represents the points that need to be added.
     */
    public void addPoints(int pointsToAdd) {
        points=points+pointsToAdd;
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
    public ObjectiveCard getObjectiveCard(){
        if(objectivechoose==1){
            return objective1;
        }else{
            return objective2;
        }
    }
    public void setObjectiveChoice(int objectivechoose){
        this.objectivechoose=objectivechoose;
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
    public StartingCard getStartingCard(){
        return startingCard;
    }
    public int getObjectivechoose() {
        return objectivechoose;
    }

    public ArrayList<PlayerSub> getPlayerSubs() {
        return playerSubs;
    }

    public void setConnected(boolean state){
        this.connected=state;
    }

    public boolean getConnected(){
        return connected;
    }

}