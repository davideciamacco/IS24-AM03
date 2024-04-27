package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;

public class Lshaped extends ObjectiveCard {
    private int corner;
    private CornerItem secondColor;

    public Lshaped(int id, int points, CornerItem requirements, ObjectiveType type, CornerItem kingdomType, int corner, CornerItem secondColor){
        super(id,points,requirements,type,kingdomType);
        this.corner=corner;
        this.secondColor=secondColor;
    }
    public int getCorner() {
        return corner;
    }
    public CornerItem getSecondColor() {
        return secondColor;
    }
}