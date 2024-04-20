package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class StartingCard extends PlayableCard{
    private final ArrayList<CornerItem> kingdomList;
    public ArrayList<CornerItem> getKingdomList() {
        return kingdomList;
    }
    public StartingCard(int id, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back0, Corner back1, Corner back2, Corner back3,ArrayList<CornerItem> kingdomList){
        super(id, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        this.kingdomList=kingdomList;
    }

    @Override
    public int getType() {
        return 0;
    }
    public ArrayList<CornerItem> getRequirements(){
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
