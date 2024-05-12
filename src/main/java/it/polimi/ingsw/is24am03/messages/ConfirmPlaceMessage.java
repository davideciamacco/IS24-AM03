package it.polimi.ingsw.is24am03.messages;


public class ConfirmPlaceMessage extends ConfirmMessage{
    private final Boolean confirmPlace;

    public ConfirmPlaceMessage(Boolean confirmStarting, String details) {
        super(MessageType.CONFIRM_PLACE, details);
        this.confirmPlace =confirmStarting;
    }
    public Boolean getConfirmPlace() {
        return confirmPlace;
    }
}