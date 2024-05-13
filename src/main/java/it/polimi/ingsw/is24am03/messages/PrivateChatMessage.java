package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

public class PrivateChatMessage extends Message {
    @Serial
    private static final long serialVersionUID= 4778436424500566450L;

    private String sender;

    private String recipient;

    private String text;

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getText() {
        return text;
    }
    public PrivateChatMessage(String sender, String recipient, String text){
        super(MessageType.PRIVATE_CHAT);
        this.recipient=recipient;
        this.sender=sender;
        this.text=text;

    }



}
