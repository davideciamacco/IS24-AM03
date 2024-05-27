package it.polimi.ingsw.is24am03;

public class Launch {

    public static void main(String[] args) {
        Client client = null;
        ViewInterface view= null;
        int port;

        if (args.length == 4) {
            String connectionType = args[0];
            String[] newArgs = new String[args.length - 1];
            for (int j = 0; j < args.length - 1; j++) {
                newArgs[j] = args[j + 1];
            }
            String host = args[1];
            try {
                port = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number.");
                return;
            }
            if (connectionType.equals("--CLI")) {
                    if (args[3].equals("--TCP")) {
                        client = new ClientSocket(host, port, view); // Pass null for now, we will set the view later
                    } else if (args[3].equals("--RMI")) {
                        client = new ClientRMI(host, port, view); // Pass null for now, we will set the view later
                    }
                    if (client != null) {
                        CliView.setClient(client);
                        CliView.main(newArgs);
                    } else {
                        System.err.println("Invalid connection type.");
                    }
            } else if (connectionType.equals("--GUI")) {

                if (args[3].equals("--TCP")) {
                    client = new ClientSocket(host, port, view); // Pass null for now, we will set the view later
                } else if (args[3].equals("--RMI")) {
                    client = new ClientRMI(host, port, view); // Pass null for now, we will set the view later
                }
                if (client != null) {
                    GUIView.setClient(client);  // Pass the client to GUIView
                    System.out.println("  ____ ___  ____  _______  __                           \n" +
                        " / ___/ _ \\|  _ \\| ____\\ \\/ /                           \n" +
                        "| |  | | | | | | |  _|  \\  /                            \n" +
                        "| |__| |_| | |_| | |___ /  \\                            \n" +
                        " \\____\\___/|____/|_____/_/\\_\\      _    _     ___ ____  \n" +
                        "| \\ | |  / \\|_   _| | | |  _ \\    / \\  | |   |_ _/ ___| \n" +
                        "|  \\| | / _ \\ | | | | | | |_) |  / _ \\ | |    | |\\___ \\ \n" +
                        "| |\\  |/ ___ \\| | | |_| |  _ <  / ___ \\| |___ | | ___) |\n" +
                        "|_| \\_/_/   \\_\\_|  \\___/|_| \\_\\/_/   \\_\\_____|___|____/ ");
                    GUIView.main(newArgs);
                } else {
                    System.err.println("Invalid connection type.");
                }
            } else {
                System.err.println("Invalid connection type. Use --CLI or --GUI.");
            }
        } else {
            ServerMain.main(args);
        }
    }
}
