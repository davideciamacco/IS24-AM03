package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

/**
 * Represents a specific type of objective card with additional corner and second color attributes, known as "Lshaped".
 * This class extends {@link ObjectiveCard} to include additional attributes and functionalities specific to Lshaped cards.
 */
public class Lshaped extends ObjectiveCard {
    private int corner;
    private CornerItem secondColor;

    /**
     * Constructs an Lshaped objective card with the specified attributes.
     *
     * @param id the unique identifier of the objective card
     * @param points the point value of the objective card
     * @param requirements the {@link CornerItem} requirements for achieving the objective
     * @param type the {@link ObjectiveType} of the objective card
     * @param kingdomType the {@link CornerItem} representing the kingdom type for the objective
     * @param corner an integer representing the corner associated with this objective
     * @param secondColor the {@link CornerItem} representing the second color for the objective
     */
    public Lshaped(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType, int corner, CornerItem secondColor) {
        super(id, points, requirements, type, kingdomType);
        this.corner = corner;
        this.secondColor = secondColor;
    }

    /**
     * Returns the corner associated with this Lshaped objective card.
     *
     * @return the corner of the objective card
     */
    public int getCorner() {
        return corner;
    }

    /**
     * Returns the second color associated with this Lshaped objective card.
     *
     * @return the second color of the objective card
     */
    public CornerItem getSecondColor() {
        return secondColor;
    }
}
