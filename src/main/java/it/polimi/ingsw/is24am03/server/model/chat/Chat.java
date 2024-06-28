package it.polimi.ingsw.is24am03.server.model.chat;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The chat class handles all the text messages sent by a player, either in public or in private chat
 */
public class Chat {

    /**
     * Lists of subscribers of the chat
     */
    private ArrayList<ChatSub> chatSubs;

    /**
     * List of all the message sent by all players during the game
     */
    private ArrayList<Text> texts;

    /**
     * Class' constructor
     */
    public Chat(){
        this.texts=new ArrayList<Text>();
        this.chatSubs=new ArrayList<>();
    }

    /**
     *
     * @return the subscriber of the chat
     */
    public ArrayList<ChatSub> getChatSubs() {
        return chatSubs;
    }

    /**
     * Add a sub to the sub's list
     * @param chatSub reference to the subscriber
     */
    public void addSub(ChatSub chatSub){
        this.chatSubs.add(chatSub);
    }

    /**
     * Method used to notify each susbriber of the chat when a group chat messages is sent or a specific chat sub when a private message is sent
     * @param t the text sent
     */
    public void NotifyChat(Text t){
        if(t.getRecipient()==null) {
            for (ChatSub chatSub : getChatSubs()) {
                try {
                    chatSub.ReceiveGroupText(t.getSender(), t.getMex());

                } catch (RemoteException ignored) {
                }
            }
            this.texts.add(t);
        }
        else {
            for(ChatSub chatSub: getChatSubs()){
                try{
                    if(chatSub.getSub().equals(t.getRecipient()) || chatSub.getSub().equals(t.getSender())){
                        chatSub.ReceivePrivateText(t.getSender(),t.getRecipient(),t.getMex());
                    }
                }catch (RemoteException ignored){}
            }
            this.texts.add(t);
        }
    }

    /**
     * Method used to retrieve alla text message, both from the group chat and private chats in which the player was the sender or the receiver
     * @param player nickname of the player
     * @return an arraylist of messages relative to the player
     */
    public ArrayList<Text> getAll(String player){
        ArrayList<Text> mex=new ArrayList<>();
        for(Text t: getTexts()){
          if(t.getRecipient()==null || t.getRecipient().equals(player) || (t.getSender().equals(player) && t.getRecipient()!=null)){
             mex.add(0,t);
          }
        }
        return mex;
    }

    /**
     *
     * @return an arraylist of all text messages
     */
    public ArrayList<Text> getTexts() {
        return texts;
    }
}
