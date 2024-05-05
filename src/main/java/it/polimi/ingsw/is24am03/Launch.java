package it.polimi.ingsw.is24am03;

public class Launch
{

    public static void main( String[] args )
    {
        if (args.length == 4) {
            String[] newArgs = new String[args.length - 1];
            for (int j = 0; j < args.length - 1; j++) {
                newArgs[j] = args[j + 1];
            }

            if (args[0].equals("--CLI"))
                CliView.main(newArgs);
        }
        else
            ServerMain.main(args);
    }
}