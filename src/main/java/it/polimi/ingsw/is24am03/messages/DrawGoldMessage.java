package it.polimi.ingsw.is24am03.messages;

public class DrawGoldMessage extends Message{
    private final String nickname;
    public DrawGoldMessage(String nickname) {
        super(MessageType.DRAW_GOLD);
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }
}
