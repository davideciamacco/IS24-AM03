package it.polimi.ingsw.is24am03.messages;

public class ConfirmDrawMessage extends ConfirmMessage{
    private final Boolean confirmdraw;
    public ConfirmDrawMessage(boolean confirmdraw,String details){
        super(MessageType.CONFIRM_DRAW, details);
        this.confirmdraw=confirmdraw;
    }
    public boolean getconfirmdraw(){
        return confirmdraw;
    }
}
