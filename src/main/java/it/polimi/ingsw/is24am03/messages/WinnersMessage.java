package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

/**
 * This message represents the notification to all the players connected in game about the winners of the game
 */
public class WinnersMessage extends Message{

    @Serial
    private static final long serialVersionUID= 3096612581471739250L;

    private ArrayList<String> winners;

    /**
     *
     * @return the ArrayList containing the nicknames of the players who won
     */
    public ArrayList<String> getWinners() {
        return winners;
    }

    /**
     * Constructor of a WinnersMessage
     * @param winners ArrayList containing the nicknames of the players who won
     */
    public WinnersMessage(ArrayList<String> winners){
        super(MessageType.NOTIFY_WINNERS);
        this.winners=winners;
    }
}
