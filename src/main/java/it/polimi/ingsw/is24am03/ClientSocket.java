package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.messages.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ClientSocket{

    private final String ip;
    private final int port;
    private final Socket connection;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final Queue<Message> queueMessages;
    private final ExecutorService threadManager;
    private final CliView view;

    public ClientSocket(String ip, int port, CliView view) {
        this.ip = ip;
        this.port = port;
        this.queueMessages = new ArrayDeque<>();
        this.threadManager = Executors.newCachedThreadPool();
        this.view = view;


        try {
            this.connection = new Socket(ip, port);
            this.outputStream = new ObjectOutputStream(connection.getOutputStream());
            this.inputStream = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Connection Failed! Please restart the game");
        }
        this.messagesReceiver();
        this.ParserAgent();
    }

    public void CreateGame(int nPlayers, String nickname) {
        CreateGameMessage requestMessage = new CreateGameMessage(nPlayers, nickname);
        this.sendMessage(requestMessage);
    }

    private void messagesReceiver()  {
        threadManager.execute( () -> {
            boolean active = true;
            while(active) {
                synchronized (queueMessages) {
                    try {
                        Message incomingMessage = (Message) inputStream.readObject();
                        queueMessages.add(incomingMessage);
                        queueMessages.notifyAll();
                        queueMessages.wait(1);
                    } catch (IOException | ClassNotFoundException | InterruptedException e ) {
                        active = false;
                    }
                }
            }
        });
    }


    private void ParserAgent(){
        threadManager.execute( () -> {
            while(this.connection.isConnected()){
                synchronized (queueMessages){
                    while(queueMessages.isEmpty()){
                        try {
                            queueMessages.wait();
                        } catch (InterruptedException ignored) {}
                    }
                    this.parse(queueMessages.poll());
                }
            }
        });
    }

    private void parse(Message responseMessage){

    //    if(responseMessage == null) return;

        switch (responseMessage.getMessageType()){
            case CONFIRM_GAME -> this.parse((ConfirmGameMessage) responseMessage);
            default -> {
            }

        }
    }


    private void parse(ConfirmGameMessage message){
        if(message.getConfirmGameCreation())
            System.out.println("Game created successfully");
        else
            System.out.println(message.getDetails());
    }

    private void sendMessage(Message message) {
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException ignored) {
            }
        }
    }
}