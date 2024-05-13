package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class LastRoundMessage extends Message{

    @Serial
    private final static long serialVersionUID= -1769272937780241852L;

    public String getMessage() {
        return message;
    }

    private String message;

    public LastRoundMessage(){
        super(MessageType.NOTIFY_ADDITIONAL_ROUND);
        this.message="Additional Round is starting, during this round drawing won't be permitted";
    }
}
