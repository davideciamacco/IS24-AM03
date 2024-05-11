
package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.messages.*;
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
    private  Socket socket;
    private GameController gameController;
    private  ObjectInputStream inputStream;
    private  ObjectOutputStream outputStream;

    private  Queue<Message> queueMessages;

    private  ExecutorService parseExecutors = Executors.newCachedThreadPool();


    public ClientTCPHandler(Socket socket, GameController gameController) {
        this.socket = socket;
        this.gameController = gameController;
        this.queueMessages = new ArrayDeque<>();
        try {

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection with client failed over TCP");
        }


    }


    public void run() {
        this.ParserAgent();
        this.messagesReceiver();
    }

    private void ParserAgent(){
        parseExecutors.execute( () -> {
            Message response;
            parserLoop:
            while (true)
                synchronized (queueMessages) {
                    while (queueMessages.isEmpty()) {
                        try {
                            queueMessages.wait();
                        } catch (InterruptedException e) {
                            break parserLoop;
                        }
                    }
                    response = this.messageParser(queueMessages.poll());
                    //System.out.println("Server riceve da client");
                    sendMessage(response);


                }
        });
    }
    private void messagesReceiver()  {
        parseExecutors.execute( () -> {
            while(true) {
                synchronized (queueMessages) {
                    try {
                        Message incomingMessage = (Message) inputStream.readObject();
                        queueMessages.add(incomingMessage);
                        queueMessages.notifyAll();
                        queueMessages.wait(1);
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
            case JOIN_GAME -> outputMessage = this.parse((JoinGameMessage) inputMessage);
            case PICK_COLOR -> outputMessage = this.parse((PickColorMessage) inputMessage);
        }
        return outputMessage;
    }


    private Message parse(CreateGameMessage createGameMessage){
        boolean result;
        String description = "";
        try {
            gameController.createGame(createGameMessage.getPlayerNumber(), createGameMessage.getNickname());
            result = true;
        }
        catch(IllegalArgumentException e)
        {
            result=false;
            description = "Invalid arguments";
        }
        catch(GameAlreadyCreatedException e)
        {
            result=false;
            description = "Game already created";
        }
        return new ConfirmGameMessage(result, description);
    }

    private Message parse(JoinGameMessage joinGameMessage){
        boolean result;
        String description = "";
        try {
            if(!joinGameMessage.getHasJoined()) {
                gameController.addPlayer(joinGameMessage.getNickname());
                result = true;
            }
            else
            {
                result = false;
                description = "Already joined";
            }
        }
        catch (NicknameAlreadyUsedException e)
        {
            result = false;
            description = "Nickname already used";
        }
        catch (FullLobbyException e)
        {
            result = false;
            description = "Lobby is full";

        }
        catch (IllegalArgumentException e)
        {
            result = false;
            description = "Nickname not allowed";
        }
        catch(InvalidStateException e )
        {
            result = false;
            description = "Game not existing";
        }
        return new ConfirmJoinGameMessage(result, description);
    }

    private Message parse(PickColorMessage pickColorMessage) {
        boolean result;
        String description = "";
        try {
            gameController.pickColor(pickColorMessage.getNickname(), pickColorMessage.getColor());
            result = true;

        } catch (ColorAlreadyPickedException e)
        {
            result=false;
            description="Color not available";
        }
        catch (PlayerNotInTurnException e)
        {
            result=false;
            description="Not your turn";
        }
        catch (InvalidStateException e)
        {
            result=false;
            description="Action not allowed in this state";
        }
        return new ConfirmPickColorMessage(result, description);
    }

    private void sendMessage(Message message){
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
                System.out.println("Server invia a client");
            } catch (IOException e) {
                try {
                    System.out.println("IOException");
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }

}
