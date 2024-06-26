package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.enums.State;

import java.io.Serial;

public class ChangeStateMessage extends Message{

    @Serial
    private static final long serialVersionUID= -5428639068951850947L;

    private State state;



    public ChangeStateMessage(State state){
        super(MessageType.GAME_STATE);
        this.state=state;
    }
    public State getState() {
        return state;
    }
}
