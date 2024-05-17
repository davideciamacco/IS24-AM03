package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.controller.GameController;
import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerMain{
    //   public final static Logger logger = Logger.getLogger(ServerMain.class.getName());

    private final int port;
    private final String hostName;
    private final int rmiPortNumber;
    private final GameController gameController;

    public ServerMain(String hostName,int port, int rmiPortNumber) throws RemoteException{
        this.port = port;
        this.hostName = hostName;
        this.rmiPortNumber = rmiPortNumber;
        this.gameController = new GameController();
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(0);
            return;
        }

        Registry registry;
        try {
            System.setProperty("java.rmi.server.hostname",this.hostName);
            registry = LocateRegistry.createRegistry(rmiPortNumber);
            registry.bind("game_controller", gameController);
        } catch (RemoteException | AlreadyBoundException e) {
            executor.shutdownNow();
            System.exit(0);
            return;
        }


        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ClientTCPHandler client = new ClientTCPHandler(socket, gameController);
                executor.execute(client);
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        String hostName;
        int portNumber = 0;
        int rmiPortNumber = 0;
        //RemoteGameController stub=null;
        //String rmiHostName;
        if (args.length == 3) {
            hostName = args[0];
            try{
                portNumber = Integer.parseInt(args[1]);
                rmiPortNumber = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                System.out.println("Invalid format for port number.");
                System.exit(-1);
            }
            try {
                ServerMain echoServer = new ServerMain(hostName, portNumber, rmiPortNumber);
                echoServer.startServer();
            }
            catch(RemoteException e){
                e.printStackTrace();
            }
        }
        else {
            System.exit(0);
        }

    }
}
//
//