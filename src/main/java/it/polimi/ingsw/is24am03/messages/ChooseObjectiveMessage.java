package it.polimi.ingsw.is24am03.messages;

import java.io.Serial;

public class ChooseObjectiveMessage extends Message{

    @Serial
    private static final long serialVersionUID= 3676643395062106956L;
    private String player;
    private int choose;
    public ChooseObjectiveMessage(String player,int choose){
        super(MessageType.CHOOSE_OBJECTIVE);
        this.player=player;
        this.choose=choose;
    }
    public int getChoose(){
        return choose;
    }
    public String getPlayer(){
        return player;
    }
}
