package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class NotifyNumPlayersReachedMessage extends Message{
    @Serial
    private final static long serialVersionUID= 1551759119566639530L;

    private String message;

    public String getMessage() {
        return message;
    }

    public NotifyNumPlayersReachedMessage(){
        super(MessageType.NOTIFY_NUM_PLAYERS_REACHED);
        this.message="The numbers of players has been reached, in a few moments the game will start";
    }


}
