package it.polimi.ingsw.is24am03.messages;


import java.io.Serial;

public class ConfirmPlaceMessage extends ConfirmMessage{
    @Serial
    private static final long serialVersionUID= 7175787247519395747L;
    private final Boolean confirmPlace;

    public ConfirmPlaceMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_PLACE, details);
        this.confirmPlace =confirmStarting;
    }
    public Boolean getConfirmPlace() {
        return confirmPlace;
    }
}