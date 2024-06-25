package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ConfirmChooseObjectiveMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= 5317553334652634513L;
    private final Boolean confirmChoose;

    public ConfirmChooseObjectiveMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_CHOOSE_OBJECTIVE, details);
        this.confirmChoose =confirmStarting;
    }
    public Boolean getConfirmChoose() {
        return confirmChoose;
    }
}
