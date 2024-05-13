package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class CrashedPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= 2581900526775374492L;

    private String player;

    public String getPlayer() {
        return player;
    }

    public CrashedPlayerMessage(String player){
        super(MessageType.NOTIFY_CRASHED_PLAYER);
        this.player=player;
    }
}
