package it.polimi.ingsw.is24am03.messages;

public class DrawTableMessage extends Message{
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
