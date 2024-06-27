package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the notification to all players connected in game that player has rejoined the game
 */
public class RejoinedPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= -721493688824879184L;
    private String player;

    /**
     * Constructor of a RejoinedPlayerMessage
     * @param player nickname of the player who has rejoined the game
     */
    public RejoinedPlayerMessage(String player){
        super(MessageType.NOTIFY_REJOINED_PLAYER);
        this.player=player;
    }

    /**
     *
     * @return the nickname of the player who has rejoined the game
     */
    public String getPlayer() {
        return player;
    }
}

