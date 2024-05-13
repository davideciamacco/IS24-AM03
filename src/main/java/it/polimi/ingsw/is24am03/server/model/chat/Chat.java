package it.polimi.ingsw.is24am03.server.model.chat;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Chat {
    public ArrayList<ChatSub> getChatSubs() {
        return chatSubs;
    }

    private ArrayList<ChatSub> chatSubs;


    //arraylist di messaggi che indicano chat di gruppo
    private ArrayList<Text> texts;


    //attributo relativo al chat listener
    // private ChatObserver chatObserver;

    //costruttore
    public Chat(){
        this.texts=new ArrayList<Text>();
        this.chatSubs=new ArrayList<>();
    }

    public void addSub(ChatSub chatSub){
        this.chatSubs.add(chatSub);
    }

    //metodo che invoca chat listener notificandola di un nuovo messaggio

    // in questo metodo posso controllare la validità del messaggio ricevuto
    public void NotifyChat(Text t){
        if(t.getRecipient().isEmpty()) {
            //NOTIFICO TUTTI I SUB DI UN MESSAGGIO DELLA CHAT PUBBLICA
            for (ChatSub chatSub : getChatSubs()) {
                try {
                    chatSub.ReceiveGroupText(t.getSender(), t.getMex());

                } catch (RemoteException ignored) {
                }
            }
            this.texts.add(t);
        }
        else {
            //NOTIFICO SOLO IL SUB INTERESSATO DELLA CHAT PRIVATA
            for(ChatSub chatSub: getChatSubs()){
                try{
                    if(chatSub.getSub().equals(t.getRecipient())){
                        chatSub.ReceivePrivateText(t.getSender(),t.getRecipient(),t.getMex());
                        break;
                    }
                }catch (RemoteException ignored){}
            }
            this.texts.add(t);
        }
    }
    //return messages sent to a player - broadcast messages
    public ArrayList<Text> getGroupChat(String player){
        ArrayList<Text> groupChat= new ArrayList<>();
        for(Text t: getTexts()){
            if(t.getRecipient()==null){
                groupChat.add(t);
            }
        }
        return groupChat;
    }

    //return messages sent by a player from another player (only private messages)
    public ArrayList<Text> getPrivateChat(String receiver, String sender){
        ArrayList<Text> privateChat= new ArrayList<>();
        for(Text t: getTexts()){
            if((t.getRecipient().equals(receiver) && t.getSender().equals(sender))||(t.getRecipient().equals(sender) && t.getSender().equals(receiver))){
                privateChat.add(t);
            }
        }
        return privateChat;

    }

    public ArrayList<Text> getTexts() {
        return texts;
    }
}
