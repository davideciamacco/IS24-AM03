package it.polimi.ingsw.is24am03;
import java.util.ArrayList;

/**
 * Class employed for managing turn and round changes within the game.
 */
public class RoundManager {
    /**
     * Progressive value representing the current round number.
     */
    private int roundNumber;
    /**
     * Player list sorted according to the game's turn order.
     * It remains fixed throughout the game.
     */
    private final ArrayList<Player> pianificationOrder;
    /**
     * Reference to the current player.
     */
    private Player currentTurn;
    /**
     * Index position in the players' ArrayList related to the current player.
     */
    private int index;
    /**
     * Field indicating whether the ongoing round is the final one,
     * employed to manage the end of the game triggered by a score
     * of twenty or more from the current player.
     */
    private boolean lastRound;
    /**
     * CLass' constructor.
     * It initializes a new instance of the class with the list of players pre-established,
     * setting the round to the first one.
     * @param p is the player list already sorted according to the game's turn order.
     */
    public RoundManager(ArrayList<Player> p) {
        this.roundNumber = 1;
        this.currentTurn = p.get(0);
        this.index=0;
        this.lastRound = false;
        this.pianificationOrder = p;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public ArrayList<Player> getPianificationOrder() {
        return this.pianificationOrder;
    }

    public Player getCurrentPlayer() {
        return this.currentTurn;

    }

    public int getIndex(){
        return this.index;
    }
    public boolean getisLastRound(){
        return this.lastRound;
    }

    /**
     * Checks whether the current player has reached twenty or more points.
     * If true, it sets lastRound to true so that the current round is the last one
     * before the end of the game.
     */
    public void checkTwentyPoints() {

        if(currentTurn.getPoints()>=20) {
            lastRound = true;
        }
    }
    /**
     * This method handles the turn transition within the game.
     * It verifies if the current player is the final one in the game's turn order.
     * If affirmative, it examines whether the played round isn't the last one, by checking lastRound value.
     * If so, it increases the round count setting Index to zero and updating the current player.
     * If the player isn't the last one in the turn order it updates the curren player increasing
     * Index without modifying the round count.
     * @throws EndingLastRoundException if the method is called when lastRound is true and the current player
     * is the last one in the game's turn order.
     */
    public void nextTurn() throws EndingLastRoundException {

        if (currentTurn.equals(pianificationOrder.getLast())){
            if (lastRound) throw new EndingLastRoundException("The final round has concluded. Counting points");

            else if (!lastRound) {
                currentTurn= pianificationOrder.get(0);
                index = 0;
                roundNumber = roundNumber + 1;
            }
        }
        else {
            currentTurn=(pianificationOrder.get(index + 1));
            index++;
        }
    }

}
