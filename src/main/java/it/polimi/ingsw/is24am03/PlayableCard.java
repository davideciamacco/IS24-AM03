package it.polimi.ingsw.is24am03;

import javafx.util.Pair;
import java.util.ArrayList;

public abstract class PlayableCard extends Card{
    private ArrayList<Pair<Corner, Boolean>> backCorners;
    private ArrayList<Pair<Corner, Boolean>> frontCorners;
    private boolean face;
    private boolean cardAlreadyPlaced;

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

    public void setCardAlreadyPlaced(){
        cardAlreadyPlaced=true;
    }
}
