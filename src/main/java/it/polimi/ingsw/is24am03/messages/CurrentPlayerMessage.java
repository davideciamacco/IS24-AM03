package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class CurrentPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= -5105629868576966025L;

    private String currentPlayer;

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public CurrentPlayerMessage(String currentPlayer){
        super(MessageType.NOTIFY_CURRENT_PLAYER);
        this.currentPlayer=currentPlayer;
    }
}
