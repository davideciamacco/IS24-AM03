package it.polimi.ingsw.is24am03.client.view.GUI;

import java.util.ArrayList;
/**
 * Enum class used to associate each score with its coordinates on the score board.
 */

public enum Points {

    ZERO("394,686"),
    ONE("459,686"),
    TWO("521,686"),
    THREE("552,637"),
    FOUR("481,637"),
    FIVE("429,637"),
    SIX("370,637"),
    SEVEN("367,582"),
    EIGHT("436,582"),
    NINE("497,582"),
    TEN("552,582"),
    ELEVEN("550,526"),
    TWELVE("483,526"),
    THIRTEEN("427,526"),
    FOURTEEN("368,526"),
    FIFTEEN("372,236"),
    SIXTEEN("429,236"),
    SEVENTEEN("487,236"),
    EIGHTEEN("546,236"),
    NINETEEN("550,410"),
    TWENTY("457,395"),
    TWENTY_ONE("370,417"),
    TWENTY_TWO("366,367"),
    TWENTY_THREE("373,311"),
    TWENTY_FOUR("399,264"),
    TWENTY_FIVE("456,250"),
    TWENTY_SIX("515,258"),
    TWENTY_SEVEN("545,307"),
    TWENTY_EIGHT("540,363"),
    TWENTY_NINE("449,323");


    private final String points;

    /**
     *
     * @param points
     */
    Points(String points) {
        this.points=points;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> coordPoints(){
        String[] coords=points.split(",");
        ArrayList<Integer> coordinates= new ArrayList<>();
        for(String coord: coords){
            coordinates.add(Integer.parseInt(coord));
        }
        return coordinates;
    }
}
