package it.polimi.ingsw.is24am03.messages;

public class DrawResourceMessage extends Message{
    private final String nickname;
    public DrawResourceMessage(String nickname) {
        super(MessageType.DRAW_RESOURCE);
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }
}
