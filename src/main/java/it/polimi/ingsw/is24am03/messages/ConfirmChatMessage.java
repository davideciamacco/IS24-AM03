package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message is sent to inform the client about the outcome of his request to send a chat message
 */
public class ConfirmChatMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= -4956149756751739581L;

    /**
     *
     * @return the result about the request of sending a chat message
     */
    public Boolean isConfirmChat() {
        return confirmChat;
    }

    private Boolean confirmChat;

    /**
     * Constructor of a ConfirmChatMessage
     * @param confirmChat true if the chat message is sent successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmChatMessage(Boolean confirmChat, String details){
        super(MessageType.CONFIRM_CHAT, details);
        this.confirmChat=confirmChat;
    }


}