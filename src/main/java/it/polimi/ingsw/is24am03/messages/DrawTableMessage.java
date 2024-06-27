package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represents the request to draw a card from the game table
 */
public class DrawTableMessage extends Message{
    @Serial
    private final static long serialVersionUID= -4018213626363914695L;
    private final String nickname;
    private final int choice;

    /**
     * Constructor of a DrawTableMessage
     * @param nickname nickname of the player who is trying to draw a card from the game table
     * @param choice integer representing the choice among the six possible cards to draw
     */
    public DrawTableMessage(String nickname, int choice) {
        super(MessageType.DRAW_TABLE);
        this.choice=choice;
        this.nickname=nickname;
    }

    /**
     *
     * @return the integer representing the card selected to draw from the game table
     */
    public int getChoice(){
        return choice;
    }

    /**
     *
     * @return the nickname of the player who is trying to draw a card from the game table
     */
    public String getNickname(){
        return nickname;
    }
}
