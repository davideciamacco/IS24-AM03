package it.polimi.ingsw.is24am03.messages;

import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;

import java.io.Serial;
import java.util.ArrayList;

/**
 * This message notifies the player who has drawn a card about their new cards in hand
 */
public class PersonalCardsMessage extends Message{
    @Serial
    private final static long serialVersionUID= 2076020660851296694L;

    private String player;
    private ArrayList<ResourceCard> hand;

    /**
     * Constructor of a PersonalCardsMessage
     * @param player nickname of the player who has drawn a card, so the player to be notified
     * @param hand ArrayList containing the updated cards that the player currently holds in hand
     */
    public PersonalCardsMessage(String player, ArrayList<ResourceCard> hand){
        super(MessageType.UPDATE_PERSONAL_CARDS);
        this.player=player;
        this.hand=hand;
    }

    /**
     *
     * @return the nickname of the player who has drawn a card, so the player to be notified
     */
    public String getPlayer() {
        return player;
    }

    /**
     *
     * @return the ArrayList containing the updated cards that the player currently holds in hand
     */
    public ArrayList<ResourceCard> getHand() {
        return hand;
    }

}
