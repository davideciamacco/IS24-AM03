package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;

import java.io.Serial;

public class FirstHandMessage extends Message{
    @Serial
    private final static long serialVersionUID= -8425798425233292433L;

    public ResourceCard getPlayableCard1() {
        return playableCard1;
    }

    public ResourceCard getPlayableCard2() {
        return playableCard2;
    }

    public ResourceCard getPlayableCard3() {
        return playableCard3;
    }

    public StartingCard getStartingCard() {
        return startingCard;
    }

    public ObjectiveCard getObjectiveCard1() {
        return objectiveCard1;
    }

    public ObjectiveCard getObjectiveCard2() {
        return objectiveCard2;
    }

    private ResourceCard playableCard1;

    private ResourceCard playableCard2;

    private ResourceCard playableCard3;
    private StartingCard startingCard;
    private ObjectiveCard objectiveCard1;
    private ObjectiveCard objectiveCard2;

    public FirstHandMessage(ResourceCard playableCard1, ResourceCard playableCard2, ResourceCard playableCard3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2){
        super(MessageType.FIRST_HAND);
        this.playableCard1=playableCard1;
        this.playableCard2=playableCard2;
        this.playableCard3=playableCard3;
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
    }
}
