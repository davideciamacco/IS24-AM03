package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class DrawResourceMessage extends Message{

    @Serial
    private final static long serialVersionUID= 4076841820149814153L;
    private final String nickname;
    public DrawResourceMessage(String nickname) {
        super(MessageType.DRAW_RESOURCE);
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }
}
