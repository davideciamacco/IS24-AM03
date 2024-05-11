package it.polimi.ingsw.is24am03.messages;

public class ConfirmPickColorMessage extends ConfirmMessage{
    private final Boolean confirmPickColor;

    public ConfirmPickColorMessage(Boolean confirmJoinGame, String details) {
        super(MessageType.CONFIRM_PICK, details);
        this.confirmPickColor =confirmJoinGame;
    }
    public Boolean getConfirmPickColor() {
        return confirmPickColor;
    }
}

