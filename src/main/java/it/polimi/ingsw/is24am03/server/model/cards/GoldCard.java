package it.polimi.ingsw.is24am03.server.model.cards;


import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.util.ArrayList;

public class GoldCard extends ResourceCard {
    private final ArrayList<CornerItem> requirementsList;
    private final int scoringType;
    private final CornerItem object;

    public GoldCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back0, Corner back1, Corner back2, Corner back3, ArrayList<CornerItem> requirementsList, int scoringType, CornerItem object) {
        super(id, color, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        this.object=object;
        this.scoringType=scoringType;
        this.requirementsList=requirementsList;
    }

    @Override
    public ArrayList<CornerItem> getRequirements(){
        return requirementsList;
    }

    @Override
    public int getScoringType(){
        return scoringType;
    }
    @Override
    public int getType(){
        return -1;
    }

    public CornerItem getObject(){
        return object;}
}
