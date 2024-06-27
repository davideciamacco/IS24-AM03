package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

/**
 * This message represent the request to pick a color
 */
public class PickColorMessage extends Message{

    @Serial
    private static final long serialVersionUID= 7752447421997914894L;
    private final String nickname;
    private final String color;

    /**
     * Constructor of a PickColorMessage
     * @param nickname nickname of the player who is trying to select a color
     * @param color color selected
     */
    public PickColorMessage(String nickname,String color) {
        super(MessageType.PICK_COLOR);
        this.nickname=nickname;
        this.color=color;
    }

    /**
     *
     * @return the nickname of the player who is trying to select a color
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return the color selected
     */
    public String getColor() {
        return color;
    }
}
