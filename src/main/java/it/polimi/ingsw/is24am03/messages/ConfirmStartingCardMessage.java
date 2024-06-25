package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ConfirmStartingCardMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= -1700376198355378354L;
    private final Boolean confirmStarting;

    public ConfirmStartingCardMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_CHOOSE_SIDE, details);
        this.confirmStarting =confirmStarting;
    }
    public Boolean getConfirmStarting() {
        return confirmStarting;
    }
}
