package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

public class GroupChatMessage extends Message {
    @Serial
    private static final long serialVersionUID= -7741631038694088699L;
    private String sender;
    private String text;

    public GroupChatMessage(String sender, String text) {
       super(MessageType.GROUP_CHAT);
        this.sender = sender;
        this.text = text;
    }
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
