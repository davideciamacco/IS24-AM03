package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;

/**
 * This message represents the notification to all players connected in game that one of the six cards on the game table has changed
 */
public class NotifyCommonTableMessage extends Message{

    @Serial
    private final static long serialVersionUID= 6915586491194477083L;
    private ResourceCard resourceCard;
    private int index;

    public NotifyCommonTableMessage(ResourceCard resourceCard, int index){
        super(MessageType.UPDATE_COMMON_TABLE);
        this.index=index;
        this.resourceCard=resourceCard;
    }
    public int getIndex() {
        return index;
    }

    public ResourceCard getResourceCard() {
        return resourceCard;
    }
}

