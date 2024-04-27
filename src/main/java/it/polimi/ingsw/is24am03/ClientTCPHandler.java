package it.polimi.ingsw.is24am03;

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
    private boolean closeConnectionFlag;
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
        closeConnectionFlag = false;
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
                    } //catch (RemoteException ignored) {}
                    finally{

                    }
                }
        });
    }
    private void messagesHopper()  {
        parseExecutors.execute( () -> {
//            timer.schedule(this::handleCrash, timerDelay);
            while(true) {
                synchronized (lastReceivedMessages) {
                    try {
                        Message incomingMessage = (Message) inputStream.readObject();
                        lastReceivedMessages.add(incomingMessage);
                        //timer.reschedule(timerDelay);
                        lastReceivedMessages.notifyAll();
                        lastReceivedMessages.wait(1);
                    } catch ( ClassNotFoundException | InterruptedException e) {
                        //logger.info("Hopper has been stopped on connection with" + this.username);
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
        /*
        if( checkGameIsSet() ){
            return new ConfirmGameMessage(false, Notifications.ERR_INVALID_ACTION.getTitle(), Notifications.ERR_INVALID_ACTION.getDescription(), false);
        }*/
        try {
            gameController.createGame(createGameMessage.getPlayerNumber(), username);
            result = true;
            //logger.info("Game CREATED Successfully");
            //this.subscribeToAllListeners();
        } /*catch (InvalidPlayerException e) {
            result = false;
            errorType = Notifications.ERR_PLAYER_NO_JOINED_IN_LOBBY.getTitle();
            desc = Notifications.ERR_PLAYER_NO_JOINED_IN_LOBBY.getDescription();
        } catch (PlayersNumberOutOfRange e) {
            result = false;
            errorType = Notifications.ERR_GAME_N_PLAYER_OUT_OF_RANGE.getTitle();
            desc = Notifications.ERR_GAME_N_PLAYER_OUT_OF_RANGE.getDescription();
        }*/
        finally {
            return new ConfirmGameMessage(result, errorType, desc, false);
        }
    }

    private void sendUpdate(Message update){
        synchronized (outputStream) {
            try {
          //      logger.info("SENDING..." + update.getMessageType()+ " to "+this.username );
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
