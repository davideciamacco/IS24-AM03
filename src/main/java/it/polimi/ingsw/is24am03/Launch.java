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
            }
        }
        else{
            System.out.println("\uD83C\uDF44\u200D\uD83D\uDFEB");
            ServerMain.main(args);}
    }
}