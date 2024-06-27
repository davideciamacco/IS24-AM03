package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;

/**
 * This message represents the notification to all players connected in game about one of the six cards on the game table that has changed
 */
public class NotifyCommonTableMessage extends Message{

    @Serial
    private final static long serialVersionUID= 6915586491194477083L;
    private ResourceCard resourceCard;
    private int index;

    /**
     * Constructor of a NotifyCommonTableMessage
     * @param resourceCard card on the game table that has changed
     * @param index integer representing which one among the six cards on the game table has changed
     */
    public NotifyCommonTableMessage(ResourceCard resourceCard, int index){
        super(MessageType.UPDATE_COMMON_TABLE);
        this.index=index;
        this.resourceCard=resourceCard;
    }

    /**
     *
     * @return an integer representing which one among the six cards on the game table has changed
     */
    public int getIndex() {
        return index;
    }


    /**
     *
     * @return the card on the game table to be notified to players
     */
    public ResourceCard getResourceCard() {
        return resourceCard;
    }
}

