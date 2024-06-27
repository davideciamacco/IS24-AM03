package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;


/**
 * This message is sent to inform the client about the choice of his personal objective card
 */
public class ConfirmDrawMessage extends ConfirmMessage{

    @Serial
    private static final long serialVersionUID= 1927416042622104302L;
    private final Boolean confirmdraw;

    /**
     * Constructor of a ConfirmDrawMessage
     * @param confirmdraw true if the card has been drawn successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmDrawMessage(Boolean confirmdraw,String details){
        super(MessageType.CONFIRM_DRAW, details);
        this.confirmdraw=confirmdraw;
    }

    /**
     *
     * @return the result regarding the drawing of the card
     */
    public Boolean getconfirmdraw(){
        return confirmdraw;
    }
}
