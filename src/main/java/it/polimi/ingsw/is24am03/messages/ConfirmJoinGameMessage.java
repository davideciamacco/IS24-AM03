package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;


public class ConfirmJoinGameMessage extends ConfirmMessage {

    private final Boolean confirmJoinGame;

    public ConfirmJoinGameMessage(Boolean confirmJoinGame, String details) {
        super(MessageType.CONFIRM_JOIN,details);
        this.confirmJoinGame =confirmJoinGame;
    }
    public Boolean getConfirmJoin() {
        return confirmJoinGame;
    }
}