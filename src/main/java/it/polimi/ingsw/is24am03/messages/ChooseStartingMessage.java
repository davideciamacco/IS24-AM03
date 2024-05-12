package it.polimi.ingsw.is24am03.messages;

public class ChooseStartingMessage extends Message{
    private final String face;
    private final String player;
    public ChooseStartingMessage(String player, String face) {
        super(MessageType.CHOOSE_STARTING_CARD_SIDE);
        this.face=face;
        this.player=player;
    }
    public String getFace(){
        return face;
    }
    public String getPlayer(){
        return player;
    }
}
