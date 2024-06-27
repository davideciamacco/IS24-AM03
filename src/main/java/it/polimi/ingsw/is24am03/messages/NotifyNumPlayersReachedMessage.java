package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the notification to all players connected in game that the number of player has been reached, so the game can start
 */
public class NotifyNumPlayersReachedMessage extends Message{
    @Serial
    private final static long serialVersionUID= 1551759119566639530L;

    private String message;

    /**
     * Constructor of a NotifyPlayersReachedMessage
     */
    public NotifyNumPlayersReachedMessage(){
        super(MessageType.NOTIFY_NUM_PLAYERS_REACHED);
        this.message="The numbers of players has been reached, in a few moments the game will start";
    }

    /**
     *
     * @return the message to be displayed in clients
     */
    public String getMessage() {
        return message;
    }


}
