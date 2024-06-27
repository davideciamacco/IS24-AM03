package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message is sent to inform the client about the choice of his color
 */
public class ConfirmPickColorMessage extends ConfirmMessage{

    @Serial
    private static final long serialVersionUID= -3909456359298065594L;
    private final Boolean confirmPickColor;

    /**
     * Constructor of a ConfirmPickColorMessage
     * @param confirmPickColor true if the color has been selected successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmPickColorMessage(Boolean confirmPickColor, String details) {
        super(MessageType.CONFIRM_PICK, details);
        this.confirmPickColor =confirmPickColor;
    }

    /**
     *
     * @return the result about the choice of the player's color
     */
    public Boolean getConfirmPickColor() {
        return confirmPickColor;
    }
}

