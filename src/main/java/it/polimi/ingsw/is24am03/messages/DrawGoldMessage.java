package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the request to draw a gold card
 */
public class DrawGoldMessage extends Message{

    @Serial
    private static final long serialVersionUID= 2817854998584129211L;
    private final String nickname;

    /**
     * Constructor of a DrawGoldMessage
     * @param nickname nickname of the player who is trying to draw a gold card
     */
    public DrawGoldMessage(String nickname) {
        super(MessageType.DRAW_GOLD);
        this.nickname=nickname;
    }

    /**
     *
     * @return the nickname of the player who is trying to draw a gold card
     */
    public String getNickname(){
        return nickname;
    }
}
