package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;
public class UpdatePointsMessage extends Message{

    private static final long serialVersionUID= -6938808360605758472L;

    private String player;

    public String getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    private int points;

    public UpdatePointsMessage(String player, int points){
        super(MessageType.UPDATE_POINTS);
        this.points=points;
        this.player=player;
    }

}
