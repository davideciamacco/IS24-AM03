package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class StartingCard extends PlayableCard{
    private ArrayList<Resources> kingdomList;
    public ArrayList<Resources> getKingdomList() {
        return kingdomList;
    }
    public StartingCard(int id, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back,ArrayList<Resources> kingdomList){
        super(id, points, corner0, corner1, corner2, corner3, back);
        this.kingdomList=kingdomList;

    }

    @Override
    public int getType() {
        return 0;
    }
    public ArrayList<Resources> getRequirements(){
        return null;
    }
    public CornerItem getObject(){
        return CornerItem.EMPTY;
    }
    public int getScoringType(){
        return -1;
    }
    public CornerItem getKingdomsType(){
        return CornerItem.EMPTY;
    }
}
