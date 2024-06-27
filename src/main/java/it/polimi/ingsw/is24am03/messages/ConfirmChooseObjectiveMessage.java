package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message is sent to inform the client about the choice of his personal objective card
 */
public class ConfirmChooseObjectiveMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= 5317553334652634513L;
    private final Boolean confirmChoose;

    /**
     * Constructor of a ConfirmChooseObjectiveMessage
     * @param confirmChoice true if the objective card has been selected successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmChooseObjectiveMessage(Boolean confirmChoice, String details) {
        super(MessageType.CONFIRM_CHOOSE_OBJECTIVE, details);
        this.confirmChoose =confirmChoice;
    }

    /**
     *
     * @return the result of the choice of the personal objective card
     */
    public Boolean getConfirmChoose() {
        return confirmChoose;
    }
}
