package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;


public class ConfirmJoinGameMessage extends ConfirmMessage {

    private final Boolean confirmJoinGame;

    private final String nickname;

    public ConfirmJoinGameMessage(Boolean confirmJoinGame, String details, String nickname) {
        super(MessageType.CONFIRM_JOIN, details);
        this.confirmJoinGame = confirmJoinGame;
        this.nickname = nickname;
    }

    public Boolean getConfirmJoin() {
        return confirmJoinGame;
    }

    public String getNickname() {
        return nickname;
    }
}