package it.polimi.ingsw.is24am03;

public class ObjectiveList extends ObjectiveCard{
    private final int typeL;
    public ObjectiveList(int id, int points, CornerItem requirements, ObjectiveType type,CornerItem kingdomType,int typeL){
        super(id,points,requirements,type,kingdomType);
        this.typeL=typeL;
    }
    public int getTypeList() {
        return typeL;
    }
}