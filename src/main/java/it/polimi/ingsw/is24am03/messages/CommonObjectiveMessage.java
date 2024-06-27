package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;

import java.io.Serial;

/**
 * The CommonObjectiveMessage class represent a message to notify the players about the two common objective cards on the game table
 */
public class CommonObjectiveMessage extends Message{

    @Serial
    private static final long serialVersionUID= 1429328338949447966L;

    private ObjectiveCard objectiveCard1;
    private ObjectiveCard objectiveCard2;

    /**
     * Constructor of a CommonObjectiveMessage
     * @param o1 the first common objective card
     * @param o2 the second common objective card
     */
    public CommonObjectiveMessage(ObjectiveCard o1,ObjectiveCard o2){
        super(MessageType.COMMON_OBJECTIVE);
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
    }

    /**
     *
     * @return the first common objective card
     */
    public ObjectiveCard getObjectiveCard1() {
        return objectiveCard1;
    }

    /**
     *
     * @return the second common objective card
     */
    public ObjectiveCard getObjectiveCard2() {
        return objectiveCard2;
    }
}

