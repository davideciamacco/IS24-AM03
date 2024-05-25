package it.polimi.ingsw.is24am03;

public class Launch
{

    /*public void print(){
        System.out.println("\uD83C\uDF44\u200D\uD83D\uDFEB");
    }*/
    public static void main( String[] args )
    {
        if (args.length == 4) {
            String[] newArgs = new String[args.length - 1];
            for (int j = 0; j < args.length - 1; j++) {
                newArgs[j] = args[j + 1];
            }

            if (args[0].equals("--CLI")) {
                CliView.main(newArgs);
                System.out.println("  ____ ___  ____  _______  __                           \n" +
                        " / ___/ _ \\|  _ \\| ____\\ \\/ /                           \n" +
                        "| |  | | | | | | |  _|  \\  /                            \n" +
                        "| |__| |_| | |_| | |___ /  \\                            \n" +
                        " \\____\\___/|____/|_____/_/\\_\\      _    _     ___ ____  \n" +
                        "| \\ | |  / \\|_   _| | | |  _ \\    / \\  | |   |_ _/ ___| \n" +
                        "|  \\| | / _ \\ | | | | | | |_) |  / _ \\ | |    | |\\___ \\ \n" +
                        "| |\\  |/ ___ \\| | | |_| |  _ <  / ___ \\| |___ | | ___) |\n" +
                        "|_| \\_/_/   \\_\\_|  \\___/|_| \\_\\/_/   \\_\\_____|___|____/ ");
            }
        }
        else{
            ServerMain.main(args);}
    }
}