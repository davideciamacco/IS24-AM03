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

public class ClientSocket implements Client{
    private boolean hasJoined;
    private final String ip;
    private final int port;
    private final Socket connection;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final Queue<Message> queueMessages;
    private final ExecutorService threadManager;
    private final CliView view;

    private String nickname;

    public ClientSocket(String ip, int port, CliView view) {
        this.ip = ip;
        this.port = port;
        this.queueMessages = new ArrayDeque<>();
        this.threadManager = Executors.newCachedThreadPool();
        this.view = view;
        this.hasJoined = false;

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
        this.nickname = nickname;
        this.sendMessage(requestMessage);
    }

    public void JoinGame(String nickname){
        JoinGameMessage joinMessage = new JoinGameMessage(nickname, hasJoined);
        this.nickname = nickname;
        this.sendMessage(joinMessage);
    }

    @Override
    public void PickColor(String color) {
        PickColorMessage colorMessage = new PickColorMessage(nickname, color);
        this.sendMessage(colorMessage);
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
                            queueMessages.notifyAll();
                            queueMessages.wait();
                        } catch (InterruptedException ignored) {}
                    }
                    this.parse(queueMessages.poll());
                    //System.out.println("Client riceve da server");
                }
            }
        });
    }

    private void parse(Message responseMessage){

    //    if(responseMessage == null) return;

        switch (responseMessage.getMessageType()){
            case CONFIRM_GAME -> this.parse((ConfirmGameMessage) responseMessage);
            case CONFIRM_JOIN -> this.parse((ConfirmJoinGameMessage) responseMessage);
            case CONFIRM_PICK -> this.parse((ConfirmPickColorMessage) responseMessage);
            case CONFIRM_CHOOSE_SIDE -> this.parse((ConfirmStartingCardMessage) responseMessage);
            case CONFIRM_CHOOSE_OBJECTIVE -> this.parse((ConfirmChooseObjectiveMessage) responseMessage);
            case CONFIRM_PLACE -> this.parse((ConfirmPlaceMessage) responseMessage);
            default -> {
            }

        }
    }
    private void parse(ConfirmPlaceMessage message) {
        if (message.getConfirmPlace()){
            System.out.println("Game created successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmGameMessage message) {
        if (message.getConfirmGameCreation()){
            System.out.println("Game created successfully");
            hasJoined = true;
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmChooseObjectiveMessage message) {
        if (message.getConfirmChoose()){
            System.out.println("ConfirmChooseObjective successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmJoinGameMessage message){
        if(message.getConfirmJoin()) {
            System.out.println("Joined successfully");
            hasJoined = true;
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }

    private void parse(ConfirmPickColorMessage message) {
        if (message.getConfirmPickColor()){
            System.out.println("Color picked successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void parse(ConfirmStartingCardMessage message) {
        if (message.getConfirmStarting()){
            System.out.println("Color picked successfully");
        }
        else
            System.out.println(message.getDetails());
        System.out.flush();
    }
    private void sendMessage(Message message) {
        synchronized (outputStream) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
                //System.out.println("Client invia a server");
            } catch (IOException ignored) {
            }
        }
    }
}