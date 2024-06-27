package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

/**
 * Represents a specific type of objective card with a directional attribute, known as "Dshaped".
 * This class extends {@link ObjectiveCard} to include an additional direction parameter.
 */
public class Dshaped extends ObjectiveCard {
    private final int direction;

    /**
     * Constructs a Dshaped objective card with the specified attributes.
     *
     * @param id the unique identifier of the objective card
     * @param points the point value of the objective card
     * @param requirements the {@link CornerItem} requirements for achieving the objective
     * @param type the {@link ObjectiveType} of the objective card
     * @param kingdomType the {@link CornerItem} representing the kingdom type for the objective
     * @param direction an integer representing the direction associated with this objective
     */
    public Dshaped(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType, int direction) {
        super(id, points, requirements, type, kingdomType);
        this.direction = direction;
    }

    /**
     * Returns the direction associated with this Dshaped objective card.
     *
     * @return the direction of the objective card
     */
    public int getDirection() {
        return direction;
    }
}
