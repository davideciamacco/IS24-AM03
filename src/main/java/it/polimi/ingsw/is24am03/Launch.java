package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.client.view.ViewInterface;
import it.polimi.ingsw.is24am03.client.connection.Client;
import it.polimi.ingsw.is24am03.client.connection.ClientRMI;
import it.polimi.ingsw.is24am03.client.connection.ClientSocket;
import it.polimi.ingsw.is24am03.client.view.CLI.CliView;
import it.polimi.ingsw.is24am03.client.view.GUI.GUIView;

/**
 *
 */
public class Launch {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Client client = null;
        ViewInterface view= null;
        int port;

        if (args.length == 4) {
            String connectionType = args[0];
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            String host = args[1];
            try {
                port = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number.");
                return;
            }
            if (connectionType.equals("--CLI")) {
                    if (args[3].equals("--TCP")) {
                        client = new ClientSocket(host, port, view);
                    } else if (args[3].equals("--RMI")) {
                        client = new ClientRMI(host, port, view);
                    }
                    if (client != null) {
                        CliView.setClient(client);
                        CliView.main(newArgs);
                    } else {
                        System.err.println("Invalid connection type.");
                    }
            } else if (connectionType.equals("--GUI")) {

                if (args[3].equals("--TCP")) {
                    try{
                        client = new ClientSocket(host, port, view);
                    }
                    catch(RuntimeException e){
                        System.exit(0);
                    }
                } else if (args[3].equals("--RMI")) {
                    client = new ClientRMI(host, port, view);
                }
                if (client != null) {
                    GUIView.setClient(client);
                    GUIView.main(newArgs);
                } else {
                    System.err.println("Invalid connection type.");
                }
            } else {
                System.err.println("Invalid connection type. Use --CLI or --GUI.");
            }
        } else if (args.length == 3) {
            ServerMain.main(args);
        }
        else{
            System.out.println("Missing arguments\n" +
                    "Server usage: <IP Address> <TCP port> <RMI port>\n" +
                    "Client usage: --<CLI/GUI> <IP Address> <port> --<TCP/RMI>");
        }
    }
}
