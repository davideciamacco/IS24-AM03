package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

//message sent from client to server
public class PickColorMessage extends Message{

    @Serial
    private static final long serialVersionUID= 7752447421997914894L;
    private final String nickname;
    private final String color;
    public PickColorMessage(String nickname,String color) {
        super(MessageType.PICK_COLOR);
        this.nickname=nickname;
        this.color=color;
    }

    public String getNickname() {
        return nickname;
    }

    public String getColor() {
        return color;
    }
}
