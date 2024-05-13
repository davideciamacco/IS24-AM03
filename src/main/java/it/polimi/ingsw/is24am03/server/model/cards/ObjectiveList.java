package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

import java.io.Serializable;

public class ObjectiveList extends ObjectiveCard {
    private final int typeL;
    public ObjectiveList(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType, int typeL){
        super(id,points,requirements,type,kingdomType);
        this.typeL=typeL;
    }
    public int getTypeList() {
        return typeL;
    }
}