package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;

import java.io.Serial;


public class ChoiceObjectiveMessage extends Message{
    @Serial
    private final static long serialVersionUID= -4148500219850037547L;

    private String player;

    private ObjectiveCard objectiveCard;

    public ChoiceObjectiveMessage(String player, ObjectiveCard objectiveCard){
        super(MessageType.UPDATE_PERSONAL_OBJECTIVE);
        this.player=player;
        this.objectiveCard=objectiveCard;
    }
    public String getPlayer() {
        return player;
    }

    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }
}
