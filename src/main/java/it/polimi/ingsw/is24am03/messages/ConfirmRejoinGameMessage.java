package it.polimi.ingsw.is24am03.messages;
import java.io.Serial;


/**
 * This message is sent to inform the client about the outcome of his request to rejoin the game
 */
public class ConfirmRejoinGameMessage extends ConfirmMessage{

    @Serial
    private static final long serialVersionUID= -5708924798144563819L;
    private final Boolean confirmRejoinGame;


    /**
     * Constructor of a ConfirmRejoinGameMessage
     * @param confirmRejoinGame true if the player has been rejoined successfully, false otherwise
     * @param details a string containing the explanation for why the request failed
     */
    public ConfirmRejoinGameMessage(Boolean confirmRejoinGame, String details) {
        super(MessageType.CONFIRM_REJOIN,details);
        this.confirmRejoinGame = confirmRejoinGame;
    }

    /**
     *
     * @return the result of the request to rejoin the game
     */
    public Boolean getConfirmRejoin() {
        return confirmRejoinGame;
    }
}