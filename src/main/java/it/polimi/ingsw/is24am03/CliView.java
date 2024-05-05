package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.client.ConnectionType;

import java.io.IOException;
//import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CliView{
    private final ExecutorService inputReader;
    private final Scanner inputScan;
    private final Client client;
    private Map<String,String> commands;

    public static void main(String[] args){
        CliView view;
        view = new CliView(args[0], Integer.parseInt(args[1]), args[2]);
    }

    public CliView(String ip, int port, String connectionType){
        inputScan =  new Scanner(System.in);
        inputReader = Executors.newCachedThreadPool();
        if(connectionType.equals("--TCP"))
            client=new ClientSocket(ip, port, this);
        else{
            client=new ClientRMI(ip, port, this);
        }
        inputReader.execute( () -> {
            String cliInput;
            synchronized (inputScan){
                while(true){
                    cliInput = inputScan.nextLine();
                    String finalCliInput = cliInput;
                    inputReader.execute(() -> this.handle(finalCliInput));
                }
            }

        } );
    }

    private void handle(String cliInput) {

        String[] inputArray = cliInput.split("\\s+");
        String command;
        command = inputArray[0];

        switch (command){
            case "CreateGame" -> {
                try {
                    client.CreateGame(Integer.parseInt(inputArray[1]), inputArray[2]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            default -> {
                System.out.println("Invalid Command");
            }
        }
    }

   
}