package it.polimi.ingsw.is24am03.messages;

public class RejoinGameMessage extends Message{
    private final String nickname;
    public RejoinGameMessage(String nickname) {
        super(MessageType.REJOIN_GAME);
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
