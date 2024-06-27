package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.io.Serial;

/**
 * This class represents a message that indicates a change in the game state.
 * It encapsulates the new state information.
 */
public class ChangeStateMessage extends Message{

    @Serial
    private static final long serialVersionUID= -5428639068951850947L;

    private State state;


    /**
     * Constructor of a ChangeStateMessage
     * @param state new game state to be communicated.
     */
    public ChangeStateMessage(State state){
        super(MessageType.GAME_STATE);
        this.state=state;
    }

    /**
     *
     * @return the new current game state
     */
    public State getState() {
        return state;
    }
}
