package it.polimi.ingsw.is24am03;

public class Dshaped extends ObjectiveCard{
    private final  int direction;
    public Dshaped(int id, int points, CornerItem requirements, ObjectiveType type,Resources kingdomType,int direction){
        super(id,points,requirements,type,kingdomType);
        this.direction=direction;

    }
    public int getDirection() {
        return direction;
    }
}
