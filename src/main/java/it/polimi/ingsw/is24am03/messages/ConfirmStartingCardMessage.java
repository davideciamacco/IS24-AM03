package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message is sent to inform the client about the choice of the side of his starting card
 */
public class ConfirmStartingCardMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= -1700376198355378354L;
    private final Boolean confirmStarting;

    /**
     * Constructor of a ConfirmStartingCardMessage
     * @param confirmStarting true if the side of the starting card has been selected successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmStartingCardMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_CHOOSE_SIDE, details);
        this.confirmStarting =confirmStarting;
    }

    /**
     *
     * @return the result about the choice of the side of the starting card
     */
    public Boolean getConfirmStarting() {
        return confirmStarting;
    }
}
