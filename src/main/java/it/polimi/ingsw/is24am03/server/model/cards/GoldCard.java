package it.polimi.ingsw.is24am03.server.model.cards;


import it.polimi.ingsw.is24am03.server.model.cards.Corner;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;

import java.util.ArrayList;

public class GoldCard extends ResourceCard {
    private final ArrayList<CornerItem> requirementsList;
    private final int scoringType;
    private final CornerItem object;

    public GoldCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3, Corner back0, Corner back1, Corner back2, Corner back3, ArrayList<CornerItem> requirementsList, int scoringType, CornerItem object) {
        super(id, color, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        this.object=object;
        this.scoringType=scoringType;
        this.requirementsList=requirementsList;
    }

    @Override
    public ArrayList<CornerItem> getRequirements(){
        return requirementsList;
    }

    @Override
    public int getScoringType(){
        return scoringType;
    }
    @Override
    public int getType(){
        return -1;
    }

    public CornerItem getObject(){return object;}

    public void drawCard(){
        StringBuilder req = new StringBuilder();
        ArrayList<String> frontcorner = preDraw();
        if (scoringType==0) {
            switch (object) {
                case PLANT: {
                    frontcorner.add(getPoints()+"-P");
                    break;
                }
                case ANIMAL: {
                    frontcorner.add(getPoints()+"-A");
                    break;
                }
                case INSECT: {
                    frontcorner.add(getPoints()+"-I");
                    break;
                }
                case FUNGI: {
                    frontcorner.add(getPoints()+"-F");
                    break;
                }
                case QUILL: {
                    frontcorner.add(getPoints() + "-Q");
                    break;
                }
                case INKWELL:{
                    frontcorner.add(getPoints()+"-B");
                    break;
                }
                case MANUSCRIPT:{
                    frontcorner.add(getPoints()+"-M");
                    break;
                }
            }
        }
        else if (scoringType==1) {
            frontcorner.add(getPoints()+"-C");
        }
        else if (scoringType==2) {
            frontcorner.add(("-"+getPoints())+"-");
        }

        for (CornerItem cornerItem : requirementsList) {
            switch (cornerItem) {
                case PLANT: {
                    req.append("P");
                    break;
                }
                case ANIMAL: {
                    req.append("A");
                    break;
                }
                case INSECT: {
                    req.append("I");
                    break;
                }
                case FUNGI: {
                    req.append("F");
                    break;
                }
            }
        }

        while(req.length()<5) {
            req.append("-");
        }

        frontcorner.add(req.toString());
        System.out.println(frontcorner.get(0) + "--"+frontcorner.get(5)+"--" + frontcorner.get(1) + "      " + "O-------O");
        System.out.println("|       |      |   " + frontcorner.get(4) + "   |");
        System.out.println(frontcorner.get(3) +"-"+frontcorner.get(6)+"-" + frontcorner.get(2) + "      " + "O-------O");
        System.out.println("\n");
    }
}
