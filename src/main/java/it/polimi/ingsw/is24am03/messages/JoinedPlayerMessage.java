package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;
import java.util.ArrayList;

/**
 * This message represents the notification to all players connected in game that a new player has joined the game
 */
public class JoinedPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= 1842763004088165760L;

    private String player;

    /**
     * Constructor of a JoinedPlayerMessage
     * @param player nickname of the new player who has joined the game
     */
    public JoinedPlayerMessage(String player){
    super(MessageType.JOINED_PLAYER);
    this.player=player;
    }

    /**
     *
     * @return the nickname of the new player who has joined the game
     */
    public String getPlayer() {
        return player;
    }
}
