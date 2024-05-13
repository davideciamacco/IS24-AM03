package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

public class JoinedPlayerMessage extends Message{

@Serial
    private static final long serialVersionUID= 1842763004088165760L;

private ArrayList<String> player;

    public ArrayList<String> getPlayer() {
        return player;
    }

    public JoinedPlayerMessage(ArrayList<String> player){
    super(MessageType.JOINED_PLAYER);
    this.player=player;
}
}
