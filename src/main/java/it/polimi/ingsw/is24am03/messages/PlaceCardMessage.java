package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class PlaceCardMessage extends Message{
    @Serial
    private final static long serialVersionUID= 6919820400982672104L;
    private final String face;
    private final String player;
    private final int i,j;
    private final int choice;

    public PlaceCardMessage(String player, int choice, int i, int j, String face) {
        super(MessageType.PLACE_CARD);
        this.face=face;
        this.player=player;
        this.choice=choice;
        this.i=i;
        this.j=j;
    }
    public String getFace(){
        return face;
    }
    public String getPlayer(){
        return player;
    }
    public int getI(){
        return i;
    }
    public int getJ(){
        return j;
    }
    public int getChoice(){
        return choice;
    }
}
