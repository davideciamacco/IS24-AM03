package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

/**
 * This message represents the request to send a group message to all the players in the game
 */
public class GroupChatMessage extends Message {
    @Serial
    private static final long serialVersionUID= -7741631038694088699L;
    private String sender;
    private String text;

    /**
     * Constructor of a GroupChatMessage
     * @param sender nickname of the player who trying to send the group message
     * @param text content of the group message
     */
    public GroupChatMessage(String sender, String text) {
       super(MessageType.GROUP_CHAT);
        this.sender = sender;
        this.text = text;
    }

    /**
     *
     * @return the nickname of the player who is trying to send the group message
     */
    public String getSender() {
        return sender;
    }

    /**
     *
     * @return the content of the group message
     */
    public String getText() {
        return text;
    }
}
