package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * The ChooseStartingMessage class represents a message sent by the client to select the side of his starting card
 */
public class ChooseStartingMessage extends Message{
    @Serial
    private static final long serialVersionUID= -6037408784048947368L;
    private final String face;
    private final String player;

    /**
     * Constructor of a ChooseStartingMessage
     * @param player nickname of the player who sent the command to select the side of his starting card
     * @param face string specifying the selected side of the starting card (FRONT/BACK)
     */
    public ChooseStartingMessage(String player, String face) {
        super(MessageType.CHOOSE_STARTING_CARD_SIDE);
        this.face=face;
        this.player=player;
    }

    /**
     *
     * @return the string specifying the selected side of the starting card
     */
    public String getFace(){
        return face;
    }

    /**
     *
     * @return the nickname of the player who tried to select the side of his starting card
     */
    public String getPlayer(){
        return player;
    }
}
