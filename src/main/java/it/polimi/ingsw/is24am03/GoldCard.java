package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class GoldCard extends ResourceCard{
    private ArrayList<Resources> requirementsList;
    private int scoringType;
    private CornerItem object;

    public ArrayList<Resources> getRequirementsList(){
        return requiremnetsList;
    }

    public int getScoringType(){
        return scoringType;
    }

    public CornerItem getObject(){
        return object;
    }
}
