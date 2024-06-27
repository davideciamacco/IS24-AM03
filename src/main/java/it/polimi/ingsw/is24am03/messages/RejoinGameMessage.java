package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the request to rejoin the game
 */
public class RejoinGameMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4866489078902127814L;
    private final String nickname;

    /**
     * Constructor of a RejoinGameMessage
     * @param nickname of the player who is trying to rejoin the game
     */
    public RejoinGameMessage(String nickname) {
        super(MessageType.REJOIN_GAME);
        this.nickname=nickname;
    }

    /**
     *
     * @return the nickname of the player who is trying to rejoin the game
     */
    public String getNickname() {
        return nickname;
    }
}
