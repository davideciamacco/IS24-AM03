package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 *  This message is sent to inform the client about the creation of a game
 */
public class ConfirmGameMessage extends ConfirmMessage {

    @Serial
    private static final long serialVersionUID= -1973642975562019779L;

    private final Boolean confirmGameCreation;

    private final String nickname;

    /**
     * Constructor of a ConfirmGameMessage
     *
     * @param confirmGameCreation true if the game has been created successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     * @param nickname nickname of the player who sent the request to create the game
     */

    public ConfirmGameMessage(Boolean confirmGameCreation, String details, String nickname) {
        super(MessageType.CONFIRM_GAME, details);
        this.confirmGameCreation = confirmGameCreation;
        this.nickname = nickname;
    }

    /**
     * @return the result of the creation of the game
     */
    public Boolean getConfirmGameCreation() {
        return confirmGameCreation;
    }

    /**
     *
     * @return the nickname of the player who tried to create the game
     */
    public String getNickname() {
        return nickname;
    }

}