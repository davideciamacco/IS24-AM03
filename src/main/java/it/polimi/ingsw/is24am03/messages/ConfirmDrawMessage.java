package it.polimi.ingsw.is24am03.messages;

public class ConfirmDrawMessage extends ConfirmMessage{
    private final Boolean confirmdraw;
    public ConfirmDrawMessage(Boolean confirmdraw,String details){
        super(MessageType.CONFIRM_DRAW, details);
        this.confirmdraw=confirmdraw;
    }
    public Boolean getconfirmdraw(){
        return confirmdraw;
    }
}
