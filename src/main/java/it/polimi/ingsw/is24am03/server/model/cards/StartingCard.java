package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import java.util.ArrayList;

/**
 * Represents a starting card in the game, which is a type of playable card.
 * Extends PlayableCard and provides specific methods for starting cards.
 */
public class StartingCard extends PlayableCard {

    /**
     * List of kingdom types associated with this starting card.
     */
    private final ArrayList<CornerItem> kingdomList;

    /**
     * Constructor to initialize a starting card with specific attributes.
     *
     * @param id Unique identifier of the card.
     * @param points Points associated with the card.
     * @param corner0 Corner 0 at the front of the card.
     * @param corner1 Corner 1 at the front of the card.
     * @param corner2 Corner 2 at the front of the card.
     * @param corner3 Corner 3 at the front of the card.
     * @param back0 Corner 0 at the back of the card.
     * @param back1 Corner 1 at the back of the card.
     * @param back2 Corner 2 at the back of the card.
     * @param back3 Corner 3 at the back of the card.
     * @param kingdomList List of kingdom types associated with the card.
     */
    public StartingCard(int id, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3,
                        Corner back0, Corner back1, Corner back2, Corner back3, ArrayList<CornerItem> kingdomList) {
        super(id, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        this.kingdomList = kingdomList;
    }

    /**
     * Retrieves the list of kingdom types associated with this starting card.
     *
     * @return ArrayList of CornerItem representing the kingdom types.
     */
    public ArrayList<CornerItem> getKingdomList() {
        return this.kingdomList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getType() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<CornerItem> getRequirements() {
        return new ArrayList<>(); // Starting cards have no requirements
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CornerItem getObject() {
        return CornerItem.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScoringType() {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CornerItem getKingdomsType() {
        return CornerItem.EMPTY;
    }
}
