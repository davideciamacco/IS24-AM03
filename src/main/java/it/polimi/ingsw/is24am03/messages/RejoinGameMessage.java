package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class RejoinGameMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4866489078902127814L;
    private final String nickname;
    public RejoinGameMessage(String nickname) {
        super(MessageType.REJOIN_GAME);
        this.nickname=nickname;
    }
    public String getNickname() {
        return nickname;
    }
}
