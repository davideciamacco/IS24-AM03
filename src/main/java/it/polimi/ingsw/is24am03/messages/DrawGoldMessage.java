package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class DrawGoldMessage extends Message{

    @Serial
    private static final long serialVersionUID= 2817854998584129211L;
    private final String nickname;
    public DrawGoldMessage(String nickname) {
        super(MessageType.DRAW_GOLD);
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }
}
