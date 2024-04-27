package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.io.Serializable;

/**
 * Abstract structure of a NetMessage
 */
public abstract class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1592537124969459098L;
    private final MessageType messageType;

    /**
     * Constructor of a NetMessage
     * @param messageType type of the message
     */
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     *
     * @return the type of the message
     */
    public MessageType getMessageType() {
        return messageType;
    }
}
