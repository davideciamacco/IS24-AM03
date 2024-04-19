package it.polimi.ingsw.is24am03;

public class Lshaped extends ObjectiveCard{
    private int corner;
    private Resources secondColor;

    public Lshaped(int id, int points, CornerItem requirements, ObjectiveType type,Resources kingdomType,int corner,Resources secondColor){
        super(id,points,requirements,type,kingdomType);
        this.corner=corner;
        this.secondColor=secondColor;
    }
    public int getCorner() {
        return corner;
    }
    public Resources getSecondColor() {
        return secondColor;
    }
}
