package it.polimi.ingsw.is24am03;

public class ResourceCard extends PlayableCard{
    private Resources kingdomType;

    public ResourceCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back){
        super(id, points, corner0, corner1, corner2, corner3, back);
        switch (color){
            case "R":
                this.kingdomType=Resources.FUNGI;
                break;

            case "G":
                this.kingdomType=Resources.PLANT;
                break;

            case "B":
                this.kingdomType=Resources.ANIMAL;
                break;

            case "P":
                this.kingdomType=Resources.INSECT;
                break;

            default:
                throw new IllegalArgumentException("Opzione non valida: " + color);
        }

    }

    public Resources getKingdomType(){
        return kingdomType;
    }

    @Override
    public int getType(){
        return 1;
    }

    @Override
    public CornerItem getObject(){
        return CornerItem.EMPTY;
    }


}
