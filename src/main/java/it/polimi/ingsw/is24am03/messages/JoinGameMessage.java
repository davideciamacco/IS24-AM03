package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.messages.Message;
import it.polimi.ingsw.is24am03.messages.MessageType;

import java.io.Serial;

/**
 * This message represents the request to join a game
 */
public class JoinGameMessage extends Message {

    @Serial
    private final static long serialVersionUID= -1263752023810866829L;
    private final String nickname;
    private final boolean hasJoined;

    /**
     * Constructor of a JoinGameMessage
     * @param nickname nickname of the player who is trying to join a game
     * @param hasJoined boolean flag indicating whether the client has already joined the game
     */
    public JoinGameMessage(String nickname,boolean hasJoined) {
        super(MessageType.JOIN_GAME);
        this.nickname=nickname;
        this.hasJoined=hasJoined;
    }


    /**
     *
     * @return the nickname of the player who is trying to join a game
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return the boolean flag indicating whether the client has already joined the game
     */
    public boolean getHasJoined() {
        return hasJoined;
    }
}
