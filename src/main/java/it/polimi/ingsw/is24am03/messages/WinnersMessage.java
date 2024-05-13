package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

public class WinnersMessage extends Message{

    @Serial
    private static final long serialVersionUID= 3096612581471739250L;

    private ArrayList<String> winners;

    public ArrayList<String> getWinners() {
        return winners;
    }

    public WinnersMessage(ArrayList<String> winners){
        super(MessageType.NOTIFY_WINNERS);
        this.winners=winners;
    }
}
