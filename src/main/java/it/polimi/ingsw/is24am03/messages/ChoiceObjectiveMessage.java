package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;

import java.io.Serial;

/**
 * The ChoiceObjectiveMessage class represents a message that updates the objective card
 * in the client's model for the player who has selected the objective card.
 */
public class ChoiceObjectiveMessage extends Message{
    @Serial
    private final static long serialVersionUID= -4148500219850037547L;

    private String player;

    private ObjectiveCard objectiveCard;

    /**
     * Constructor of a ChoiceObjectiveMessage
     * @param player nickname of the player who has selected the objective card
     * @param objectiveCard objective card selected by the player
     */
    public ChoiceObjectiveMessage(String player, ObjectiveCard objectiveCard){
        super(MessageType.UPDATE_PERSONAL_OBJECTIVE);
        this.player=player;
        this.objectiveCard=objectiveCard;
    }

    /**
     *
     * @return the nickname of the player who has selected the objective card
     */
    public String getPlayer() {
        return player;
    }

    /**
     *
     * @return the objective card selected by the player
     */
    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }
}
