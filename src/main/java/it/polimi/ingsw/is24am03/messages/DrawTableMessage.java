package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class DrawTableMessage extends Message{
    @Serial
    private final static long serialVersionUID= -4018213626363914695L;
    private final String nickname;
    private final int choice;
    public DrawTableMessage(String nickname, int choice) {
        super(MessageType.DRAW_TABLE);
        this.choice=choice;
        this.nickname=nickname;
    }

    public int getChoice(){
        return choice;
    }

    public String getNickname(){
        return nickname;
    }
}
