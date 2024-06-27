package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

/**
 * This message represents the notification to all the players in game about the players turn order
 */
public class TurnOrderMessage extends Message{
    @Serial
    private static final long serialVersionUID= 3199401359828465726L;
    private ArrayList<String> turnOrder;

    /**
     * Constructor of a TurnOrderMessage
     * @param turnOrder ArrayList containing the nicknames of the players ordered according to their turn order
     */
    public TurnOrderMessage(ArrayList<String> turnOrder){
        super(MessageType.TURN_ORDER);
        this.turnOrder=turnOrder;
    }

    /**
     *
     * @return the ArrayList containing the nicknames of the players ordered according to their turn order
     */
    public ArrayList<String> getTurnOrder() {
        return turnOrder;
    }
}
