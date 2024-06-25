package it.polimi.ingsw.is24am03.server.model.chat;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Chat {

    private ArrayList<ChatSub> chatSubs;
    private ArrayList<Text> texts;

    public Chat(){
        this.texts=new ArrayList<Text>();
        this.chatSubs=new ArrayList<>();
    }

    public ArrayList<ChatSub> getChatSubs() {
        return chatSubs;
    }
    public void addSub(ChatSub chatSub){
        this.chatSubs.add(chatSub);
    }
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

    public ArrayList<Text> getAll(String player){
        ArrayList<Text> mex=new ArrayList<>();
        for(Text t: getTexts()){
          if(t.getRecipient()==null || t.getRecipient().equals(player) || (t.getSender().equals(player) && t.getRecipient()!=null)){
             mex.add(0,t);
          }
        }
        return mex;
    }

    public ArrayList<Text> getTexts() {
        return texts;
    }
}
