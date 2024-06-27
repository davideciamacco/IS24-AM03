package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

/**
 * This message represents a notification to all the players connected in game that a player has earned points
 */
public class UpdatePointsMessage extends Message{

    @Serial
    private static final long serialVersionUID= -6938808360605758472L;

    private String player;

    /**
     *
     * @return the nickname of the player who has earned points
     */
    public String getPlayer() {
        return player;
    }

    /**
     *
     * @return the number of points earned by the player
     */
    public int getPoints() {
        return points;
    }

    private int points;

    /**
     * Constructor of a UpdatePointsMessage
     * @param player nickname of the player who has earned points
     * @param points number of points earned by the player
     */
    public UpdatePointsMessage(String player, int points){
        super(MessageType.UPDATE_POINTS);
        this.points=points;
        this.player=player;
    }

}
