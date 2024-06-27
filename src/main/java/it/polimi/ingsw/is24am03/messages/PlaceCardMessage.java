package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the request to place a card
 */
public class PlaceCardMessage extends Message{
    @Serial
    private final static long serialVersionUID= 6919820400982672104L;
    private final String face;
    private final String player;
    private final int i,j;
    private final int choice;


    /**
     * Constructor of a PlaceCardMessage
     * @param player nickname of the player who is trying to place a card
     * @param choice integer representing the card to place among the three ones in the player's hand
     * @param i the row index of the position in the matrix where the card will be placed
     * @param j the column index of the position in the matrix where the card will be placed
     * @param face chosen side of the card to place
     */
    public PlaceCardMessage(String player, int choice, int i, int j, String face) {
        super(MessageType.PLACE_CARD);
        this.face=face;
        this.player=player;
        this.choice=choice;
        this.i=i;
        this.j=j;
    }

    /**
     *
     * @return the chosen side of the card to place
     */
    public String getFace(){
        return face;
    }

    /**
     *
     * @return the nickname of the player who is trying to place a card
     */
    public String getPlayer(){
        return player;
    }

    /**
     *
     * @return the row index of the position in the matrix where the card will be placed
     */
    public int getI(){
        return i;
    }

    /**
     *
     * @return the column index of the position in the matrix where the card will be placed
     */
    public int getJ(){
        return j;
    }

    /**
     *
     * @return the integer representing the card to place among the three ones in the player's hand
     */
    public int getChoice(){
        return choice;
    }
}
