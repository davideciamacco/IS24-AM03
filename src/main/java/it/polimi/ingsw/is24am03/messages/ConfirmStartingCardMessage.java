package it.polimi.ingsw.is24am03.messages;

public class ConfirmStartingCardMessage extends ConfirmMessage{
    private final Boolean confirmStarting;

    public ConfirmStartingCardMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_CHOOSE_SIDE, details);
        this.confirmStarting =confirmStarting;
    }
    public Boolean getConfirmStarting() {
        return confirmStarting;
    }
}
