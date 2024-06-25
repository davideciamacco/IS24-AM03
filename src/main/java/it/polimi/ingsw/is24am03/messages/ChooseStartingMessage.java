package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ChooseStartingMessage extends Message{
    @Serial
    private static final long serialVersionUID= -6037408784048947368L;
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
