package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the notification to all the players connected in game about the new current player
 */
public class CurrentPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= -5105629868576966025L;

    private String currentPlayer;

    /**
     * Constructor of a CurrentPlayerMessage
     * @param currentPlayer nickname of the new current player
     */
    public CurrentPlayerMessage(String currentPlayer){
        super(MessageType.NOTIFY_CURRENT_PLAYER);
        this.currentPlayer=currentPlayer;
    }

    /**
     *
     * @return the nickname of the new current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
