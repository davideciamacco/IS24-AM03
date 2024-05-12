package it.polimi.ingsw.is24am03.messages;

public class ConfirmChooseObjectiveMessage extends ConfirmMessage{
    private final Boolean confirmChoose;

    public ConfirmChooseObjectiveMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_CHOOSE_OBJECTIVE, details);
        this.confirmChoose =confirmStarting;
    }
    public Boolean getConfirmChoose() {
        return confirmChoose;
    }
}
