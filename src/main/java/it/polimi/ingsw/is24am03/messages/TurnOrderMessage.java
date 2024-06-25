package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

public class TurnOrderMessage extends Message{
    @Serial
    private static final long serialVersionUID= 3199401359828465726L;
    private ArrayList<String> turnOrder;

    public TurnOrderMessage(ArrayList<String> turnOrder){
        super(MessageType.TURN_ORDER);
        this.turnOrder=turnOrder;
    }
    public ArrayList<String> getTurnOrder() {
        return turnOrder;
    }
}
