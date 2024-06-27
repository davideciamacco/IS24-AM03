package it.polimi.ingsw.is24am03.server.model.chat;

import java.io.Serializable;

public class Text implements Serializable {

    private String sender;
    private String recipient;

    private String mex;


    public Text(String sender, String mex){
        this.sender=sender;
        this.recipient=null;
        this.mex=mex;
    }

    public Text(String sender, String recipient,String mex){
        this.sender=sender;
        this.recipient=recipient;
        this.mex=mex;
    }
    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMex() {
        return mex;
    }

}
