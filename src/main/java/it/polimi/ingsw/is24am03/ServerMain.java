package it.polimi.ingsw.is24am03;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This is the main class of the Server, it launches both RMI and TCP Services to accept clients' requests.
 */
public class ServerMain {
 //   public final static Logger logger = Logger.getLogger(ServerMain.class.getName());
//    private final int rmiPortNumber;

    private final int port;
    private final String hostName;

    private final GameController gameController;

    public ServerMain(String hostName,int port, int rmiPortNumber){
        this.port = port;
        this.hostName = hostName;
        //this.rmiPortNumber = rmiPortNumber;
        this.gameController = new GameController();
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Porta non disponibile
            System.exit(0);
            return;
        }

        /* SOCKET TCP */
        while (true) {
            try {
                Socket socket = serverSocket.accept();
         //       logger.info("Client connected");
                executor.execute(new ClientTCPHandler(socket, gameController));
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
        String rmiHostName;
        if (args.length == 3) {
            hostName = args[0];
            try{
                portNumber = Integer.parseInt(args[1]);
                rmiPortNumber = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                System.exit(-1);
            }
            ServerMain echoServer = new ServerMain(hostName, portNumber, rmiPortNumber);
            echoServer.startServer();
        }
        else {
            System.exit(0);
        }

    }



}
