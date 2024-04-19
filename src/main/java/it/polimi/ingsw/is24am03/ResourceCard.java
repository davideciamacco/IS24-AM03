package it.polimi.ingsw.is24am03;
import java.util.ArrayList;

public class ResourceCard extends PlayableCard{
    private CornerItem kingdomType;

    public ResourceCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back){
        super(id, points, corner0, corner1, corner2, corner3, back);
        switch (color){
            case "R":
                this.kingdomType=CornerItem.FUNGI;
                break;

            case "G":
                this.kingdomType=CornerItem.PLANT;
                break;

            case "B":
                this.kingdomType=CornerItem.ANIMAL;
                break;

            case "P":
                this.kingdomType=CornerItem.INSECT;
                break;

       //     default:
       //         throw new IllegalArgumentException("Opzione non valida: " + color);
        }

    }
    @Override
    public int getType(){
        return 1;
    }
    public int getScoringType(){
        return -1;
    }
    public CornerItem getKingdomsType(){
        return kingdomType;
    }
    @Override
    public CornerItem getObject(){
        return CornerItem.EMPTY;
    }
    public ArrayList<Resources> getRequirements(){
        return null;
    }
}
