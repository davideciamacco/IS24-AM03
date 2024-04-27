package it.polimi.ingsw.is24am03;
//import it.polimi.ingsw.client.view.CLI.CliView;
//import it.polimi.ingsw.client.view.GUI.GUILaunch;
//import it.polimi.ingsw.server.ServerMain;

/**
 * Launch file for the whole project, it instances either a client or a server depending
 * on the arguments given.
 */
public class Launch
{
    /**
     * Main method of the class
     * @param args arguments given (view readme file)
     */
    public static void main( String[] args )
    {
        if (args.length == 3) {
            ServerMain.main(args);
        }
        else
        {
            System.out.println("Parametri non sufficienti");
        }

    }
}