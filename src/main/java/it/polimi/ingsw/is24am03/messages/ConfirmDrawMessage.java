package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ConfirmDrawMessage extends ConfirmMessage{

    @Serial
    private static final long serialVersionUID= 1927416042622104302L;
    private final Boolean confirmdraw;
    public ConfirmDrawMessage(Boolean confirmdraw,String details){
        super(MessageType.CONFIRM_DRAW, details);
        this.confirmdraw=confirmdraw;
    }
    public Boolean getconfirmdraw(){
        return confirmdraw;
    }
}
