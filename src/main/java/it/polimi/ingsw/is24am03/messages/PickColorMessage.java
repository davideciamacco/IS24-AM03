package it.polimi.ingsw.is24am03.messages;

public class PickColorMessage extends Message{
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
