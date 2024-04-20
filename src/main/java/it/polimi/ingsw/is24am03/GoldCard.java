package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class GoldCard extends ResourceCard{
    private final ArrayList<CornerItem> requirementsList;
    private final int scoringType;
    private final CornerItem object;

    public GoldCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back0, Corner back1, Corner back2, Corner back3, ArrayList<CornerItem> requirementsList, int scoringType, CornerItem object) {
        super(id, color, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        this.object=object;
        this.scoringType=scoringType;
        this.requirementsList=requirementsList;
    }

    public ArrayList<CornerItem> getRequirementsList(){
        return requirementsList;
    }

    public int getScoringType(){
        return scoringType;
    }

    @Override
    public int getType(){
        return -1;
    }

    @Override
    public CornerItem getObject(){
        return object;
    }

}
