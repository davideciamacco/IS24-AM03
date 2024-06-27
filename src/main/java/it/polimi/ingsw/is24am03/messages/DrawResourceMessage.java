package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the request to draw a resource card
 */
public class DrawResourceMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4076841820149814153L;
    private final String nickname;

    /**
     * Constructor of a DrawResourceMessage
     * @param nickname nickname of the player who is trying to draw a resource card
     */
    public DrawResourceMessage(String nickname) {
        super(MessageType.DRAW_RESOURCE);
        this.nickname=nickname;
    }

    /**
     *
     * @return the nickname of the player who is trying to draw a resource card
     */
    public String getNickname(){
        return nickname;
    }
}
