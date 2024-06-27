package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the notification to all the players connected in game that the final round has started
 */
public class LastRoundMessage extends Message{

    @Serial
    private final static long serialVersionUID= -1769272937780241852L;
    private String message;

    /**
     * Constructor of a LastRoundMessage
     */
    public LastRoundMessage(){
        super(MessageType.NOTIFY_ADDITIONAL_ROUND);
        this.message="Additional Round is starting, during this round drawing won't be permitted";
    }

    /**
     *
     * @return the message to be displayed on the clients
     */
    public String getMessage() {
        return message;
    }
}
