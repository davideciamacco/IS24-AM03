package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;

import java.io.Serial;

/**
 * This message represents the first notification sent to a player about his personal cards, his starting card and the two objective cards from which he will have to choose
 */

public class FirstHandMessage extends Message{
    @Serial
    private final static long serialVersionUID= -8425798425233292433L;
    private ResourceCard playableCard1;

    private ResourceCard playableCard2;

    private ResourceCard playableCard3;
    private StartingCard startingCard;
    private ObjectiveCard objectiveCard1;
    private ObjectiveCard objectiveCard2;

    /**
     * Constructor of a FirstHandMessage
     * @param playableCard1 first card in the player's hand
     * @param playableCard2 second card in the player's hand
     * @param playableCard3 third card in the player's hand
     * @param startingCard starting card of the player
     * @param o1 first objective card assigned to the player
     * @param o2 second objective card assigned to the player
     */
    public FirstHandMessage(ResourceCard playableCard1, ResourceCard playableCard2, ResourceCard playableCard3, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2){
        super(MessageType.FIRST_HAND);
        this.playableCard1=playableCard1;
        this.playableCard2=playableCard2;
        this.playableCard3=playableCard3;
        this.startingCard=startingCard;
        this.objectiveCard1=o1;
        this.objectiveCard2=o2;
    }

    /**
     *
     * @return the first card in the player's hand
     */
    public ResourceCard getPlayableCard1() {
        return playableCard1;
    }

    /**
     *
     * @return the second card in the player's hand
     */
    public ResourceCard getPlayableCard2() {
        return playableCard2;
    }

    /**
     *
     * @return the third card in the player's hand
     */
    public ResourceCard getPlayableCard3() {
        return playableCard3;
    }

    /**
     *
     * @return the starting card of the player
     */
    public StartingCard getStartingCard() {
        return startingCard;
    }

    /**
     *
     * @return the first objective card assigned to the player
     */
    public ObjectiveCard getObjectiveCard1() {
        return objectiveCard1;
    }

    /**
     *
     * @return the second objective card assigned to the player
     */
    public ObjectiveCard getObjectiveCard2() {
        return objectiveCard2;
    }
}
