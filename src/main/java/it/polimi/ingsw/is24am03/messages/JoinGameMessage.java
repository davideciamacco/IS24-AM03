package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.messages.Message;
import it.polimi.ingsw.is24am03.messages.MessageType;

import java.io.Serial;


public class JoinGameMessage extends Message {

    private final String nickname;
    private final boolean hasJoined;
    public JoinGameMessage(String nickname,boolean hasJoined) {
        super(MessageType.JOIN_GAME);
        this.nickname=nickname;
        this.hasJoined=hasJoined;
    }

    public String getNickname() {
        return nickname;
    }
    public boolean getHasJoined() {
        return hasJoined;
    }
}
