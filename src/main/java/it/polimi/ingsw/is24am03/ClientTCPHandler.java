package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.messages.ConfirmGameMessage;
import it.polimi.ingsw.is24am03.messages.CreateGameMessage;
import it.polimi.ingsw.is24am03.messages.Message;
import it.polimi.ingsw.is24am03.server.controller.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTCPHandler implements Runnable {
    private final Socket socket;
    private GameController gameController;
    private String username;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private final Queue<Message> lastReceivedMessages;

    private final ExecutorService parseExecutors = Executors.newCachedThreadPool();


    public ClientTCPHandler(Socket socket, GameController gameController) {
        this.socket = socket;
        this.username = "";
        this.gameController = gameController;
        this.lastReceivedMessages = new ArrayDeque<>();
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection with client failed over TCP");
        }
    }


    public void run() {
        this.startParserAgent();
        this.messagesHopper();
    }

    private void startParserAgent(){
        parseExecutors.execute( () -> {
            Message response;
            parserLoop:
            while (true)
                synchronized (lastReceivedMessages) {
                    while (lastReceivedMessages.isEmpty()) {
                        try {
                            lastReceivedMessages.wait();
                        } catch (InterruptedException e) {
                            break parserLoop;
                        }
                    }
                    try {
                        response = this.messageParser(lastReceivedMessages.poll());
                        sendUpdate(response);
                    }
                    finally{

                    }
                }
        });
    }
    private void messagesHopper()  {
        parseExecutors.execute( () -> {
            while(true) {
                synchronized (lastReceivedMessages) {
                    try {
                        Message incomingMessage = (Message) inputStream.readObject();
                        lastReceivedMessages.add(incomingMessage);
                        lastReceivedMessages.notifyAll();
                        lastReceivedMessages.wait(1);
                    } catch ( ClassNotFoundException | InterruptedException e) {
                        break;
                    } catch ( IOException ignored){

                    }
                }
            }
        });
    }

    private Message messageParser(Message inputMessage){
        Message outputMessage=null;

        switch (inputMessage.getMessageType()) {
            case CREATE_GAME -> outputMessage = this.parse((CreateGameMessage) inputMessage);
        }
        return outputMessage;
    }

    private Message parse(CreateGameMessage createGameMessage){
        boolean result=false;
        String errorType = "";
        String desc = "";
        try {
            gameController.createGame(createGameMessage.getPlayerNumber(), username);
            result = true;
        }
        finally {
            return new ConfirmGameMessage(result, errorType, desc, false);
        }
    }

    private void sendUpdate(Message update){
        synchronized (outputStream) {
            try {
                outputStream.writeObject(update);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }

}
