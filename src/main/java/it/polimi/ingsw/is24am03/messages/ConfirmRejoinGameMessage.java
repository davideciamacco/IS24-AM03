package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;

public class ConfirmRejoinGameMessage extends ConfirmMessage{
    private final Boolean confirmRejoinGame;

    public ConfirmRejoinGameMessage(Boolean confirmRejoinGame, String details) {
        super(MessageType.CONFIRM_REJOIN,details);
        this.confirmRejoinGame = confirmRejoinGame;
    }
    public Boolean getConfirmRejoin() {
        return confirmRejoinGame;
    }
}