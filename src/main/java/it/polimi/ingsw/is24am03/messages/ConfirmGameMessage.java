package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 *  This message is sent to inform the client about the creation of a game
 */
public class ConfirmGameMessage extends ConfirmMessage {

    private final Boolean confirmGameCreation;

    private final String nickname;

    /**
     * Constructor of
     *
     * @param confirmGameCreation true if the game has been created successfully, false otherwise
     * @param details             details of the error
     */
    public ConfirmGameMessage(Boolean confirmGameCreation, String details, String nickname) {
        super(MessageType.CONFIRM_GAME, details);
        this.confirmGameCreation = confirmGameCreation;
        this.nickname = nickname;
    }

    /**
     * @return the result of the GameCreation
     */
    public Boolean getConfirmGameCreation() {
        return confirmGameCreation;
    }

    public String getNickname() {
        return nickname;
    }
}