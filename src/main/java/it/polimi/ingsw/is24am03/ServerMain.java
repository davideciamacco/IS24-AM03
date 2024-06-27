package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.ClientTCPHandler;
import it.polimi.ingsw.is24am03.server.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main server class that manages client connections and RMI setup.
 * Uses TCP sockets for client communication and RMI for remote method invocation.
 */
public class ServerMain {
    private final int port;
    private final String hostName;
    private final int rmiPortNumber;
    private final GameController gameController;

    /**
     * Constructs the server with the specified hostname, ports, and initializes the game controller.
     *
     * @param hostName      The hostname or IP address where the server is running.
     * @param port          The TCP port number for client socket connections.
     * @param rmiPortNumber The RMI port number for RMI registry and remote method access.
     * @throws RemoteException If there is an issue with RMI communication.
     */
    public ServerMain(String hostName, int port, int rmiPortNumber) throws RemoteException {
        this.port = port;
        this.hostName = hostName;
        this.rmiPortNumber = rmiPortNumber;
        this.gameController = new GameController();
    }

    /**
     * Starts the server by creating a server socket for TCP connections,
     * setting up an RMI registry, and handling incoming client connections.
     */
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            System.exit(0);
            return;
        }

        Registry registry;
        try {
            System.setProperty("java.rmi.server.hostname", hostName);
            registry = LocateRegistry.createRegistry(rmiPortNumber);
            registry.bind("game_controller", gameController);
        } catch (RemoteException | AlreadyBoundException e) {
            executor.shutdownNow();
            System.err.println("Error setting up RMI: " + e.getMessage());
            System.exit(0);
            return;
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
                ClientTCPHandler client = new ClientTCPHandler(socket, gameController);
                executor.execute(client);
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
                break;
            } catch (RuntimeException e) {
                System.err.println("Runtime error: " + e.getMessage());
                System.exit(0);
            }
        }
        executor.shutdown();
    }

    /**
     * Main method to start the server.
     *
     * @param args Command-line arguments:
     *             args[0]: Hostname or IP address.
     *             args[1]: TCP port number.
     *             args[2]: RMI port number.
     */
    public static void main(String[] args) {
        String hostName;
        int portNumber = 0;
        int rmiPortNumber = 0;

        if (args.length == 3) {
            hostName = args[0];
            try {
                portNumber = Integer.parseInt(args[1]);
                rmiPortNumber = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid format for port number.");
                System.exit(-1);
            }

            try {
                ServerMain server = new ServerMain(hostName, portNumber, rmiPortNumber);
                server.startServer();
            } catch (RemoteException e) {
                System.err.println("RemoteException occurred: " + e.getMessage());
                System.exit(-1);
            }
        } else {
            System.err.println("Invalid number of arguments.");
            System.exit(-1);
        }
    }
}
