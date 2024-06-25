package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;

import java.io.Serial;

public class CommonObjectiveMessage extends Message{

    @Serial
    private static final long serialVersionUID= 1429328338949447966L;



    private ObjectiveCard objectiveCard1;
    private ObjectiveCard objectiveCard2;

    public CommonObjectiveMessage(ObjectiveCard o1,ObjectiveCard o2){
        super(MessageType.COMMON_OBJECTIVE);
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
    }
    public ObjectiveCard getObjectiveCard1() {
        return objectiveCard1;
    }

    public ObjectiveCard getObjectiveCard2() {
        return objectiveCard2;
    }
}

