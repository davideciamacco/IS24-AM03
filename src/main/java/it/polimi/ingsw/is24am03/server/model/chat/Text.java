package it.polimi.ingsw.is24am03.server.model.chat;

public class Text {

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMex() {
        return mex;
    }

    //stringa mittente
    private String sender;


    //stringa destinatario
    private String recipient;

    //stringa messaggio
    private String mex;

    //metodo per costruire messaggio privato
    public Text(String sender, String mex){
        this.sender=sender;
        this.recipient=null;
        this.mex=mex;
    }
    //metodo per costruire messaggio di gruppo
    public Text(String sender, String recipient,String mex){
        this.sender=sender;
        this.recipient=recipient;
        this.mex=mex;
    }

}
