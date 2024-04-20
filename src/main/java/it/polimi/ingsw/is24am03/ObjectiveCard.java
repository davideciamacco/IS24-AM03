package it.polimi.ingsw.is24am03;

public class ObjectiveCard extends Card{
    private final ObjectiveType type;
    private final CornerItem kingdomType;
    private final CornerItem requirements;
    public ObjectiveCard(int id, int points, CornerItem requirements, ObjectiveType type,CornerItem kingdomType){
        super(id,points);
        this.kingdomType=kingdomType;
        this.type=type;
        this.requirements=requirements;
    }
    public CornerItem getKingdomsType(){
        return kingdomType;
    }
    public ObjectiveType getType() {
        return type;
    }
    public CornerItem getRequirement(){
        return requirements;
    }
    public int getDirection(){
        return -1;
    }
    public int getTypeList(){
        return -1;
    }
    public int getCorner(){
        return -1;
    }
    public CornerItem getSecondColor(){
        return CornerItem.EMPTY;
    }
}