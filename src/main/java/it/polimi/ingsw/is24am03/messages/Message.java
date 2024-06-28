package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.io.Serializable;

/**
 * Abstract structure of a Message
 */
public abstract class Message implements Serializable {
    private final MessageType messageType;


    /**
     * Constructor of a Message
     * @param messageType type of the message
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Getter for the message type
     * @return the type of the message
     */
    public MessageType getMessageType() {
        return messageType;
    }
}
