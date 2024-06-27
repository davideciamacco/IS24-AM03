package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

/**
 * This message represents the request to send a private message
 */
public class PrivateChatMessage extends Message {
    @Serial
    private static final long serialVersionUID= 4778436424500566450L;

    private String sender;

    private String recipient;

    private String text;

    /**
     * Constructor of a PrivateChatMessage
     * @param sender nickname of the player who is trying to send a private message
     * @param recipient nickname of the recipient of the private message
     * @param text content of the private message
     */

    public PrivateChatMessage(String sender, String recipient, String text){
        super(MessageType.PRIVATE_CHAT);
        this.recipient=recipient;
        this.sender=sender;
        this.text=text;

    }

    /**
     *
     * @return the nickname of the player who is trying to send a private message
     */
    public String getSender() {
        return sender;
    }

    /**
     *
     * @return the nickname of the recipient of the private message
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     *
     * @return the content of the private message
     */
    public String getText() {
        return text;
    }



}
