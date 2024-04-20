package it.polimi.ingsw.is24am03;

import javafx.util.Pair;
import java.util.ArrayList;

public abstract class PlayableCard extends Card{
    private final ArrayList<Pair<Corner, Boolean>> backCorners;
    private final ArrayList<Pair<Corner, Boolean>> frontCorners;
    private boolean face;
    private boolean cardAlreadyPlaced;
    public PlayableCard(int id, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back0, Corner back1, Corner back2, Corner back3){
        super(id,points);

        this.face = true;

        this.cardAlreadyPlaced=false;

        this.backCorners = new ArrayList<>();
        this.backCorners.add(new Pair<>(back0,false));
        this.backCorners.add(new Pair<>(back1,false));
        this.backCorners.add(new Pair<>(back2,false));
        this.backCorners.add(new Pair<>(back3,false));

        this.frontCorners = new ArrayList<>();
        frontCorners.add(new Pair<>(corner0, false));
        frontCorners.add(new Pair<>(corner1, false));
        frontCorners.add(new Pair<>(corner2, false));
        frontCorners.add(new Pair<>(corner3, false));
    }

    public Corner getFrontCorner(int index){
        return frontCorners.get(index).getKey();
    }
    public Corner getBackCorner(int index){
        return backCorners.get(index).getKey();
    }

    public boolean getFace(){
        return face;
    }

    public void rotate(){
        face = !face;
    }

    public void setCoverage(int corner){
        if(face){
            Pair<Corner, Boolean> pair = frontCorners.get(corner);
            frontCorners.set(corner, new Pair<>(pair.getKey(),true));
        } else {
            Pair<Corner, Boolean> pair = backCorners.get(corner);
            backCorners.set(corner, new Pair<>(pair.getKey(), true));
        }
    }
    public void setCardAlreadyPlaced(){
        cardAlreadyPlaced=true;
    }

    public abstract int getType();

    public abstract CornerItem getObject();
    public abstract int  getScoringType();
    public abstract CornerItem getKingdomsType();
    public abstract ArrayList<CornerItem> getRequirements();
}
