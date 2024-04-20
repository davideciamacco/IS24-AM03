package it.polimi.ingsw.is24am03;

public class Lshaped extends ObjectiveCard{
    private int corner;
    private CornerItem secondColor;

    public Lshaped(int id, int points, CornerItem requirements, ObjectiveType type,CornerItem kingdomType,int corner,CornerItem secondColor){
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