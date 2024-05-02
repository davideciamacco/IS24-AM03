package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.messages.Message;
import it.polimi.ingsw.is24am03.messages.MessageType;

import java.io.Serial;

/**
 * This message represents the request to create a game
 */
public class CreateGameMessage extends Message {
    @Serial
    private static final long serialVersionUID = -7527502624377210050L;

    private final int playerNumber;
    private final String nickname;

    /**
     * Constrcutor of a CreateGameMessage
     * @param playerNumber number of player of the game
     */
    public CreateGameMessage(int playerNumber,String nickname) {
        super(MessageType.CREATE_GAME);
        this.playerNumber = playerNumber;
        this.nickname=nickname;
    }

    /**
     * @return the number of player inserted
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     *
     * @return the nickname of the player who created the game
     */
    public String getNickname() {
        return nickname;
    }
}
