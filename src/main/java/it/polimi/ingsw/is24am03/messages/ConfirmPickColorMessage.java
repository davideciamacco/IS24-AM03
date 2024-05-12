package it.polimi.ingsw.is24am03.messages;

public class ConfirmPickColorMessage extends ConfirmMessage{
    private final Boolean confirmPickColor;

    public ConfirmPickColorMessage(Boolean confirmPickColor, String details) {
        super(MessageType.CONFIRM_PICK, details);
        this.confirmPickColor =confirmPickColor;
    }
    public Boolean getConfirmPickColor() {
        return confirmPickColor;
    }
}

