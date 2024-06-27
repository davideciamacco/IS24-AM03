package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.Subscribers.ChatSub;
import it.polimi.ingsw.is24am03.server.model.chat.Chat;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {
    @Test
    void testConstructor(){
        Chat chat = new Chat();
        assertEquals(0, chat.getTexts().size());
        assertEquals(0, chat.getChatSubs().size());
    }

    @Test
    void NotifyChat(){
        Chat chat = new Chat();
        ChatSub c1 = new GeneralSubscriber("Player1");
        ChatSub c2 = new GeneralSubscriber("Player2");
        chat.addSub(c1);
        chat.addSub(c2);
        Text t1 = new Text("Player1", "Player2", "Ciao");
        Text t2 = new Text("Player1", "Ciao");
        chat.NotifyChat(t1);
        assertEquals(1, chat.getTexts().size());
        assertTrue(chat.getTexts().contains(t1));

        chat.NotifyChat(t2);
        assertEquals(2, chat.getTexts().size());
        assertTrue(chat.getTexts().contains(t2));
    }

    @Test
    void getAll(){
        Chat chat = new Chat();
        Text t1 = new Text("Player2", "Player1", "Ciao");
        Text t2 = new Text("Player1", "Ciao");
        chat.NotifyChat(t1);
        chat.NotifyChat(t2);

        assertTrue(chat.getAll("Player1").contains(t1));
        assertTrue(chat.getAll("Player1").contains(t2));
        assertEquals(2, chat.getAll("Player1").size());
    }
}
