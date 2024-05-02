package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.messages.Message;
import it.polimi.ingsw.is24am03.messages.MessageType;

import java.io.Serial;

/**
 * This is an abstract type of message sent as confirmation to some operations
 */
public abstract class ConfirmMessage extends Message {
    private final String details;

    /**
     * Constructor of a ConfirmMessage
     * @param messageType type of this message
     * @param details details of the message
     */
    ConfirmMessage(MessageType messageType, String details) {
        super(messageType);
        this.details = details;
    }


    /**
     * Getter for the details
     * @return the details of the message
     */
    public String getDetails() {
        return details;
    }
}