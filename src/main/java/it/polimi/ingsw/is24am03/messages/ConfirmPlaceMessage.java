package it.polimi.ingsw.is24am03.messages;


import java.io.Serial;


/**
 * This message is sent to inform the client about the placing of a card
 */
public class ConfirmPlaceMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= 7175787247519395747L;
    private final Boolean confirmPlace;

    /**
     * Constructor of a ConfirmPlaceMessage
     * @param confirmPlacing true if the card has been placed successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmPlaceMessage(Boolean confirmPlacing, String details) {
        super(MessageType.CONFIRM_PLACE, details);
        this.confirmPlace =confirmPlacing;
    }

    /**
     *
     * @return the result about the placing of the card
     */
    public Boolean getConfirmPlace() {
        return confirmPlace;
    }
}