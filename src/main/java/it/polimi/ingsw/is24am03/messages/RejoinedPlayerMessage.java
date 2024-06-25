package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class RejoinedPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= -721493688824879184L;
    private String player;

    public RejoinedPlayerMessage(String player){
        super(MessageType.NOTIFY_REJOINED_PLAYER);
        this.player=player;
    }
    public String getPlayer() {
        return player;
    }
}

