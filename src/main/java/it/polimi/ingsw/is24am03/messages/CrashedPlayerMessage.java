package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the notification to all the players connected in game about the crashing of a player
 */
public class CrashedPlayerMessage extends Message{

    @Serial
    private static final long serialVersionUID= 2581900526775374492L;

    private String player;

    /**
     *
     * @return the nickname of the player who has crashed
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Constructor of a CrashedPlayerMessage
     * @param player nickname of the player who has crashed
     */
    public CrashedPlayerMessage(String player){
        super(MessageType.NOTIFY_CRASHED_PLAYER);
        this.player=player;
    }
}
