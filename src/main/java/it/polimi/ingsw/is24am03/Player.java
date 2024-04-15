package it.polimi.ingsw.is24am03;
/**
 * Represents a player in the game.
 */
public class Player {
    private String nickname;
    private Color pawncolor;
    private ArrayList<PlayableCard> hand;
    private ObjectiveCard objective;
    private StartingCard startingCard;
    private boolean winner,firstPlayer;
    private int points,numObj;
    private playerBoard PlayerBoard;
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
        this.firstPlayer =null;
        this.points = 0;
        this.numObj = 0;
        this.playerBoard = null;
    }
    /**
     * Sets the player's objective card.
     * ok
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
     *ok
     * @param card The card to remove from the hand.
     */
    public void removeCard(PlayerBoard card){
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

}
