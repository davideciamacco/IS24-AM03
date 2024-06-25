package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

/**
 * Represents a specific type of objective card known as "ObjectiveList".
 * This class extends {@link ObjectiveCard} to include an additional attribute typeL.
 */
public class ObjectiveList extends ObjectiveCard {
    private final int typeL;

    /**
     * Constructs an ObjectiveList objective card with the specified attributes.
     *
     * @param id the unique identifier of the objective card
     * @param points the point value of the objective card
     * @param requirements the {@link CornerItem} requirements for achieving the objective
     * @param type the {@link ObjectiveType} of the objective card
     * @param kingdomType the {@link CornerItem} representing the kingdom type for the objective
     * @param typeL an integer representing the specific type list associated with this objective
     */
    public ObjectiveList(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType, int typeL) {
        super(id, points, requirements, type, kingdomType);
        this.typeL = typeL;
    }

    /**
     * Returns the type list associated with this ObjectiveList objective card.
     *
     * @return the type list of the objective card
     */
    @Override
    public int getTypeList() {
        return typeL;
    }
}
