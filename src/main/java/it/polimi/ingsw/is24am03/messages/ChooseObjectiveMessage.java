package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * The ChooseObjectiveMessage class represents a message sent by the client to select his personal objective card
 */
public class ChooseObjectiveMessage extends Message{

    @Serial
    private static final long serialVersionUID= 3676643395062106956L;
    private String player;
    private int choose;

    /**
     * Constructor of a ChooseObjectiveMessage
     * @param player nickname of the player who typed the command to select his personal objective card
     * @param choose integer specifying which of the two objective cards to choose: 1 for the first objective card and 2 for the second objective card.
     */
    public ChooseObjectiveMessage(String player,int choose){
        super(MessageType.CHOOSE_OBJECTIVE);
        this.player=player;
        this.choose=choose;
    }

    /**
     *
     * @return the integer specifying the player's choice
     */
    public int getChoose(){
        return choose;
    }

    /**
     *
     * @return the nickname of the player who tried to select the objective card
     */
    public String getPlayer(){
        return player;
    }
}
