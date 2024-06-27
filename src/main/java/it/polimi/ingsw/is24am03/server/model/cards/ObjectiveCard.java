package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

/**
 * Represents an ObjectiveCard which extends the {@link Card} class.
 * This class includes additional attributes and functionalities specific to objective cards.
 */
public class ObjectiveCard extends Card {
    private final ObjectiveType type;
    private final CornerItem kingdomType;
    private final CornerItem requirements;

    /**
     * Constructs an ObjectiveCard with the specified attributes.
     *
     * @param id the unique identifier of the objective card
     * @param points the point value of the objective card
     * @param requirements the {@link CornerItem} requirements for achieving the objective
     * @param type the {@link ObjectiveType} of the objective card
     * @param kingdomType the {@link CornerItem} representing the kingdom type for the objective
     */
    public ObjectiveCard(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType) {
        super(id, points);
        this.kingdomType = kingdomType;
        this.type = type;
        this.requirements = requirements;
    }

    /**
     * Returns the kingdom type associated with this objective card.
     *
     * @return the kingdom type of the objective card
     */
    public CornerItem getKingdomsType() {
        return kingdomType;
    }

    /**
     * Returns the type of the objective card.
     *
     * @return the {@link ObjectiveType} of the objective card
     */
    public ObjectiveType getType() {
        return type;
    }

    /**
     * Returns the requirements for achieving the objective.
     *
     * @return the {@link CornerItem} requirements of the objective card
     */
    public CornerItem getRequirement() {
        return requirements;
    }

    /**
     * Returns the direction associated with this objective card.
     * This method is overridden in subclasses to provide specific direction values.
     *
     * @return -1 indicating no specific direction
     */
    public int getDirection() {
        return -1;
    }

    /**
     * Returns the type list associated with this objective card.
     * This method is overridden in subclasses to provide specific type list values.
     *
     * @return -1 indicating no specific type list
     */
    public int getTypeList() {
        return -1;
    }

    /**
     * Returns the corner associated with this objective card.
     * This method is overridden in subclasses to provide specific corner values.
     *
     * @return -1 indicating no specific corner
     */
    public int getCorner() {
        return -1;
    }

    /**
     * Returns the second color associated with this objective card.
     * This method is overridden in subclasses to provide specific second color values.
     *
     * @return {@link CornerItem#EMPTY} indicating no specific second color
     */
    public CornerItem getSecondColor() {
        return CornerItem.EMPTY;
    }
}
