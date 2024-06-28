package it.polimi.ingsw.is24am03.server.model.chat;

import java.io.Serializable;

/**
 * Class text is used to implement the format of a Chat message
 */
public class Text implements Serializable {


    private String sender;
    private String recipient;

    private String mex;


    /**
     * This constructor is used for group chat message. The recipient is null, so it will be sent to all players
     * @param sender the player who sent the text
     * @param mex the text message
     */
    public Text(String sender, String mex){
        this.sender=sender;
        this.recipient=null;
        this.mex=mex;
    }

    /**
     * This constructor is used for private chat message.
     * @param sender the player who sent the text
     * @param recipient the player who has to receive the message
     * @param mex the text message
     */
    public Text(String sender, String recipient,String mex){
        this.sender=sender;
        this.recipient=recipient;
        this.mex=mex;
    }

    /**
     *
     * @return the sender of the texr
     */
    public String getSender() {
        return sender;
    }

    /**
     *
     * @return the receiver of the text
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     *
     * @return the text message
     */
    public String getMex() {
        return mex;
    }

}
