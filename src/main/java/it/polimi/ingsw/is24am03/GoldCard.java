package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class GoldCard extends ResourceCard{
    private ArrayList<Resources> requirementsList;
    private int scoringType;
    private CornerItem object;

    public ArrayList<Resources> getRequirementsList(){
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
