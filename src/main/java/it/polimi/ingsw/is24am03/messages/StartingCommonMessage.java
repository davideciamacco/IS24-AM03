package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;
import java.util.ArrayList;

/**
 * This message represents the first notification to all the players about the six cards that can be drawn from the game table
 */
public class StartingCommonMessage extends Message{
    @Serial
    private final static long serialVersionUID= 1213842072151949402L;
    private ArrayList<ResourceCard> commons;

    /**
     * Constructor of a StartingCommonMessage
     * @param commons ArrayList of cards that can be drawn from the game table
     */
    public StartingCommonMessage(ArrayList<ResourceCard> commons){
        super(MessageType.FIRST_COMMON);
        this.commons=commons;
    }

    /**
     *
     * @return the ArrayList of cards that can be drawn from the game table
     */
    public ArrayList<ResourceCard> getCommons() {
        return commons;
    }


}
