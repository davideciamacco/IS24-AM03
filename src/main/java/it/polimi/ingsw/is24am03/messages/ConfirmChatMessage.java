package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ConfirmChatMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= -4956149756751739581L;


    public boolean isConfirmChat() {
        return confirmChat;
    }

    private boolean confirmChat;

    public ConfirmChatMessage(Boolean confirmChat, String details){
        super(MessageType.CONFIRM_CHAT, details);
        this.confirmChat=confirmChat;
    }


}