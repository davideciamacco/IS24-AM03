package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ConfirmPickColorMessage extends ConfirmMessage{

    @Serial
    private static final long serialVersionUID= -3909456359298065594L;
    private final Boolean confirmPickColor;

    public ConfirmPickColorMessage(Boolean confirmPickColor, String details) {
        super(MessageType.CONFIRM_PICK, details);
        this.confirmPickColor =confirmPickColor;
    }
    public Boolean getConfirmPickColor() {
        return confirmPickColor;
    }
}

