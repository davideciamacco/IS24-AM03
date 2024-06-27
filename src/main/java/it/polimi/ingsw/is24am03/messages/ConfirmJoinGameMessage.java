package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 *  This message is sent to inform the client about the outcome of his request to join the game
 */
public class ConfirmJoinGameMessage extends ConfirmMessage {

    @Serial
    private static final long serialVersionUID= -5532929506798931356L;
    private final Boolean confirmJoinGame;

    private final String nickname;

    /**
     * Constructor of
     * @param confirmJoinGame true if the player has joined successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     * @param nickname nickname of the player who sent the request to join the game
     */
    public ConfirmJoinGameMessage(Boolean confirmJoinGame, String details, String nickname) {
        super(MessageType.CONFIRM_JOIN, details);
        this.confirmJoinGame = confirmJoinGame;

        this.nickname = nickname;
    }

    /**
     *
     * @return the result of the request to join the game
     */

    public Boolean getConfirmJoin() {
        return confirmJoinGame;
    }


    /**
     *
     * @return the nickname of the player who tried to join the game
     */
    public String getNickname() {
        return nickname;
    }
}