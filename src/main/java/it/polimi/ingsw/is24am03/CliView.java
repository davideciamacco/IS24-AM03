package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.client.ConnectionType;

import java.io.IOException;
//import java.rmi.RemoteException;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CliView {
    private final ExecutorService inputReader;
    private final Scanner inputScan;
    private final Client client;
    private Map<String, String> commands;

    public static void main(String[] args) {
        CliView view;
        view = new CliView(args[0], Integer.parseInt(args[1]), args[2]);
    }

    public CliView(String ip, int port, String connectionType) {
        inputScan = new Scanner(System.in);
        inputReader = Executors.newCachedThreadPool();
        if (connectionType.equals("--TCP"))
            client = new ClientSocket(ip, port, this);
        else {
            client = new ClientRMI(ip, port, this);
        }
        inputReader.execute(() -> {
            String cliInput;
            synchronized (inputScan) {
                while (true) {
                    cliInput = inputScan.nextLine();
                    String finalCliInput = cliInput;
                    inputReader.execute(() -> this.handle(finalCliInput));
                }
            }

        });
    }

    private void handle(String cliInput) {

        String[] inputArray = cliInput.split("\\s+");
        String command;
        command = inputArray[0];

        switch (command) {
            case "CreateGame" -> {
                try {
                    client.CreateGame(Integer.parseInt(inputArray[1]), inputArray[2]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            case "JoinGame" -> {
                try{
                    client.JoinGame(inputArray[1]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            case "PickColor" -> {
                try{
                    client.PickColor(inputArray[1]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            case "ChooseStartingCardSide" ->{
                try{
                    client.ChooseStartingCardSide(inputArray[1]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }

            case "ChooseObjectiveCard" ->{
                try{
                    client.ChooseObjectiveCard(Integer.parseInt(inputArray[1]));
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }

            case "PlaceCard" -> {
                try {
                    client.PlaceCard(Integer.parseInt(inputArray[1]), Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]), inputArray[4]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }

            case "DrawResource" -> {
                    client.DrawResource();
            }
            case "DrawGold" -> {
                    client.DrawGold();
            }
            case "DrawTable" ->{
                try{
                    client.DrawTable(Integer.parseInt(inputArray[1]));
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            default -> {
                System.out.println("Invalid Command");
            }
        }
    }

    private void drawHand(ArrayList<ResourceCard> hand) {
        System.out.println("FRONT:         BACK:");
        System.out.println("\n");
        for (int i = 0; i < 3; i++) {
            if (hand.get(i).getId() == 0) {
                System.out.println("F-------O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("F-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 1) {
                System.out.println("F-------F      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 2) {
                System.out.println("O-------┐      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("F-------F      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 3) {
                System.out.println("┌-------F      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-------F      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 4) {
                System.out.println("┌-------Q      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("P-------F      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 5) {
                System.out.println("B-------F      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 6) {
                System.out.println("F-------I      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("M-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 7) {
                System.out.println("O---1---F      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 8) {
                System.out.println("F---1---┐      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 9) {
                System.out.println("┌---1---O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("F-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 10) {
                System.out.println("P-------O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("P-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 11) {
                System.out.println("P-------P      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 12) {
                System.out.println("O-------┐      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("P-------P      O-------O");
                System.out.println("\n");

            } else if (hand.get(i).getId() == 13) {
                System.out.println("┌-------P      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O-------P      O-------O");
                System.out.println("\n");

            } else if (hand.get(i).getId() == 14) {
                System.out.println("┌-------I      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("Q-------P      O-------O");
                System.out.println("\n");

            } else if (hand.get(i).getId() == 15) {
                System.out.println("F-------P      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└-------B      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 16) {
                System.out.println("M-------┐      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("P-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 17) {
                System.out.println("O---1---O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("P-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 18) {
                System.out.println("O---1---O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└-------P      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 19) {
                System.out.println("┌---1---P      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 20) {
                System.out.println("A-------A      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 21) {
                System.out.println("┌-------O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("A-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 22) {
                System.out.println("A-------┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("A-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 23) {
                System.out.println("O-------A      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 24) {
                System.out.println("┌-------I      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("B-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 25) {
                System.out.println("P-------A      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└-------M      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 26) {
                System.out.println("Q-------┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("A-------F      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 27) {
                System.out.println("┌---1---O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("A-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 28) {
                System.out.println("O---1---┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O-------A      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 29) {
                System.out.println("O-------A      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 30) {
                System.out.println("I-------I      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 31) {
                System.out.println("┌-------O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("I-------I      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 32) {
                System.out.println("I-------┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("I-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 33) {
                System.out.println("O-------I      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└-------I      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 34) {
                System.out.println("┌-------Q      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("A-------I      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 35) {
                System.out.println("M-------I      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└-------F      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 36) {
                System.out.println("I-------P      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("B-------┘      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 37) {
                System.out.println("I---1---┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 38) {
                System.out.println("O---1---O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└-------I      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 39) {
                System.out.println("┌-------I      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O-------O      O-------O");
                System.out.println("\n");
            } else if (hand.get(i).getId() == 40) {
                System.out.println("┌--1-Q--O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O--FFA--Q      O-------O");
            } else if (hand.get(i).getId() == 41) {
                System.out.println("O--1-B--B      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└--FFP--O      O-------O");
            } else if (hand.get(i).getId() == 42) {
                System.out.println("M--1-M--O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O--FFI--┘      O-------O");
            } else if (hand.get(i).getId() == 43) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└-FF-FA-O      O-------O");
            } else if (hand.get(i).getId() == 44) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-FF-FP-┘      O-------O");
            } else if (hand.get(i).getId() == 45) {
                System.out.println("O--2-C--┐      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-FF-FI-O      O-------O");
            } else if (hand.get(i).getId() == 46) {
                System.out.println("O---3---┐      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("B--FFF--┘      O-------O");
            } else if (hand.get(i).getId() == 47) {
                System.out.println("Q---3---O      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└--FFF--┘      O-------O");
            } else if (hand.get(i).getId() == 48) {
                System.out.println("┌---3---M      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("└--FFF--O      O-------O");
            } else if (hand.get(i).getId() == 49) {
                System.out.println("O---5---┐      O-------O");
                System.out.println("|       |      |   F   |");
                System.out.println("O-FFFFF-┘      O-------O");
            } else if (hand.get(i).getId() == 50) {
                System.out.println("Q--1-Q--O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O--PPI--┘      O-------O");
            } else if (hand.get(i).getId() == 51) {
                System.out.println("O--1-M--M      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└--PPF--O      O-------O");
            } else if (hand.get(i).getId() == 52) {
                System.out.println("O--1-B--┐      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("B--PPA--O      O-------O");
            } else if (hand.get(i).getId() == 53) {
                System.out.println("┌--2-C--O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O-PP-PI-O      O-------O");
            } else if (hand.get(i).getId() == 54) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O-PP-PA-┘      O-------O");
            } else if (hand.get(i).getId() == 55) {
                System.out.println("O--2-C--┐      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("O-PP-PA-O      O-------O");
            } else if (hand.get(i).getId() == 56) {
                System.out.println("O---3---┐      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("Q--PPP--┘      O-------O");
            } else if (hand.get(i).getId() == 57) {
                System.out.println("M---3---O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└--PPP--┘      O-------O");
            } else if (hand.get(i).getId() == 58) {
                System.out.println("┌---3---B      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└--PPP--O      O-------O");
            } else if (hand.get(i).getId() == 59) {
                System.out.println("O---5---O      O-------O");
                System.out.println("|       |      |   P   |");
                System.out.println("└-PPPPP-┘      O-------O");
            } else if (hand.get(i).getId() == 60) {
                System.out.println("B--1-B--O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O--AAI--┘      O-------O");
            } else if (hand.get(i).getId() == 61) {
                System.out.println("┌--1-M--O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O--AAP--M      O-------O");
            } else if (hand.get(i).getId() == 62) {
                System.out.println("O--1-Q--┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("Q--AAF--O      O-------O");
            } else if (hand.get(i).getId() == 63) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└-AA-AI-O      O-------O");
            } else if (hand.get(i).getId() == 64) {
                System.out.println("O--2-C--┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O-AA-AF-O      O-------O");
            } else if (hand.get(i).getId() == 65) {
                System.out.println("┌--2-C--O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("O-AA-AP-O      O-------O");
            } else if (hand.get(i).getId() == 66) {
                System.out.println("O---3---┐      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("M--AAA--┘      O-------O");
            } else if (hand.get(i).getId() == 67) {
                System.out.println("O---3---B      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└--AAA--┘      O-------O");
            } else if (hand.get(i).getId() == 68) {
                System.out.println("┌---3---O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└--AAA--Q      O-------O");
            } else if (hand.get(i).getId() == 69) {
                System.out.println("┌---5---O      O-------O");
                System.out.println("|       |      |   A   |");
                System.out.println("└-AAAAA-O      O-------O");
            } else if (hand.get(i).getId() == 70) {
                System.out.println("O--1-Q--Q      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└--IIP--O      O-------O");
            } else if (hand.get(i).getId() == 71) {
                System.out.println("O--1-M--┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("M--IIA--O      O-------O");
            } else if (hand.get(i).getId() == 72) {
                System.out.println("┌--1-B--O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O--IIF--B      O-------O");
            } else if (hand.get(i).getId() == 73) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└-II-IA-O      O-------O");
            } else if (hand.get(i).getId() == 74) {
                System.out.println("O--2-C--O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O-II-IP-┘      O-------O");
            } else if (hand.get(i).getId() == 75) {
                System.out.println("O--2-C--┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O-II-IF-O      O-------O");
            } else if (hand.get(i).getId() == 76) {
                System.out.println("B---3---┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("O--III--┘      O-------O");
            } else if (hand.get(i).getId() == 77) {
                System.out.println("O---3---M      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└--III--┘      O-------O");
            } else if (hand.get(i).getId() == 78) {
                System.out.println("┌---3---┐      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("Q--III--O      O-------O");
            } else if (hand.get(i).getId() == 79) {
                System.out.println("O---5---O      O-------O");
                System.out.println("|       |      |   I   |");
                System.out.println("└-IIIII-┘      O-------O");
            }
        }
    }

    public void drawStarting(StartingCard startCard){
            System.out.println("FRONT:         BACK:");
            System.out.println("\n");
            for (int i = 0; i < 3; i++) {
                if (startCard.getId() == 80) {
                    System.out.println("O-------P      F-------A");
                    System.out.println("|   F   |      |       |");
                    System.out.println("I---S---O      I---S---A");
                    System.out.println("\n");
                } else if (startCard.getId() == 81) {
                    System.out.println("A-------O      P-------A");
                    System.out.println("|   F   |      |       |");
                    System.out.println("O---S---F      F---S---I");
                    System.out.println("\n");
                } else if (startCard.getId() == 82) {
                    System.out.println("O-------O      I-------A");
                    System.out.println("|  P F  |      |       |");
                    System.out.println("O---S---O      F---S---P");
                    System.out.println("\n");
                } else if (startCard.getId() == 83) {
                    System.out.println("O-------O      P-------I");
                    System.out.println("|  A I  |      |       |");
                    System.out.println("O---S---O      A---S---F");
                    System.out.println("\n");
                } else if (startCard.getId() == 84) {
                    System.out.println("O-------O      I-------F");
                    System.out.println("|  AIP  |      |       |");
                    System.out.println("└---S---┘      P---S---A");
                    System.out.println("\n");
                } else {
                    System.out.println("O-------O      F-------A");
                    System.out.println("|  PAF  |      |       |");
                    System.out.println("└---S---┘      P---S---I");
                    System.out.println("\n");
                }
            }
        }

    public void drawObjective(ObjectiveCard obj){
        if(obj.getId()==86){
            System.out.println("2 points for each group of 3 cards of the fungi kingdom placed diagonally as follows:");
            System.out.println("                ┌-------┐");
            System.out.println("                |   F   |");
            System.out.println("        ┌-------+-------┘");
            System.out.println("        |   F   |");
            System.out.println("┌-------+-------┘");
            System.out.println("|   F   |");
            System.out.println("└-------┘");
        } else if(obj.getId()==87) {
            System.out.println("2 points for each group of 3 cards of the plant kingdom placed diagonally as follows:");
            System.out.println("┌-------┐");
            System.out.println("|   P   |");
            System.out.println("└-------+-------┐");
            System.out.println("        |   P   |");
            System.out.println("        └-------+-------┐");
            System.out.println("                |   P   |");
            System.out.println("                └-------┘");
        } else if(obj.getId()==88) {
            System.out.println("2 points for each group of 3 cards of the animal kingdom placed diagonally as follows:");
            System.out.println("                ┌-------┐");
            System.out.println("                |   A   |");
            System.out.println("        ┌-------+-------┘");
            System.out.println("        |   A   |");
            System.out.println("┌-------+-------┘");
            System.out.println("|   A   |");
            System.out.println("└-------┘");
        } else if(obj.getId()==89) {
            System.out.println("2 points for each group of 3 cards of the insect kingdom placed diagonally as follows:");
            System.out.println("┌-------┐");
            System.out.println("|   I   |");
            System.out.println("└-------+-------┐");
            System.out.println("        |   I   |");
            System.out.println("        └-------+-------┐");
            System.out.println("                |   I   |");
            System.out.println("                └-------┘");
        } else if(obj.getId()==90) {
            System.out.println("3 points for each group of cards placed as follows:");
            System.out.println("┌-------┐");
            System.out.println("|   F   |");
            System.out.println("└-------┘");
            System.out.println("┌-------┐");
            System.out.println("|   F   |");
            System.out.println("└-------+-------┐");
            System.out.println("        |   P   |");
            System.out.println("        └-------┘");;
        } else if(obj.getId()==91) {
            System.out.println("3 points for each group of cards placed as follows:");
            System.out.println("        ┌-------┐");
            System.out.println("        |   P   |");
            System.out.println("        └-------┘");
            System.out.println("        ┌-------┐");
            System.out.println("        |   P   |");
            System.out.println("┌-------+-------┘");
            System.out.println("|   I   |");
            System.out.println("└-------┘");;
        } else if(obj.getId()==92) {
            System.out.println("3 points for each group of cards placed as follows:");
            System.out.println("        ┌-------┐");
            System.out.println("        |   F   |");
            System.out.println("┌-------+-------┘");
            System.out.println("|   P   |");
            System.out.println("└-------┘");
            System.out.println("┌-------┐");
            System.out.println("|   I   |");
            System.out.println("└-------┘");;
        } else if(obj.getId()==93) {
            System.out.println("3 points for each group of cards placed as follows:");
            System.out.println("");
            System.out.println("┌-------┐");
            System.out.println("|   A   |");
            System.out.println("└-------+-------┐");
            System.out.println("        |   I   |");
            System.out.println("        └-------┘");
            System.out.println("        ┌-------┐");
            System.out.println("        |   I   |");
            System.out.println("        └-------┘");;
        } else if(obj.getId()==94) {
            System.out.println("2 points for each set of 3 fungi-type resources.");
        } else if(obj.getId()==95) {
            System.out.println("2 points for each set of 3 plant-type resources.");
        } else if(obj.getId()==96) {
            System.out.println("2 points for each set of 3 animal-type resources.");
        } else if(obj.getId()==97) {
            System.out.println("2 points for each set of 3 insect-type resources.");
        } else if(obj.getId()==98) {
            System.out.println("3 points for each set of 3 different objects visible in the playing area.");
        } else if(obj.getId()==99) {
            System.out.println("2 points for each pair of manuscripts.");
        } else if(obj.getId()==100) {
            System.out.println("2 points for each pair of inkwells.");
        } else if(obj.getId()==101) {
            System.out.println("2 points for each pair of quills.");
        }
    }

    public void drawBoard(PlayableCard[][] board) {
        int firstsx = 81;
        int lastdx = -1;
        int top = 81;
        int down = 0;
        int i = 0, j = 0;
        boolean flag = false;
        String output1 = "";
        String output3 = "";
        while (firstsx == 81 && j < 81) {
            i = 0;
            while (firstsx == 81 && i < 81) {
                if (board[i][j] != null) {
                    firstsx = j;
                    lastdx = j;
                }
                i++;
            }
            j++;
        }

        while (j < 81) {
            i=0;
            while (i < 81) {
                if (board[i][j] != null && j > lastdx)
                    lastdx = j;
                i++;
            }
            j++;
        }

        i = 0;
        while (top == 81 && i < 81) {
            j = 0;
            while (j < 81) {
                if (board[i][j] != null) {
                    top = i;
                    down = i;
                }
                j++;
            }
            i++;
        }

        while (i < 81) {
            j = 0;
            while (j < 81) {
                if (board[i][j] != null)
                    down = i;
                j++;
            }
            i++;
        }

        if (firstsx != 0)
            firstsx = firstsx - 1;
        if (lastdx != 80)
            lastdx = lastdx + 1;
        System.out.println(firstsx);
        System.out.println(down);
        System.out.println(lastdx);
        if (top != 0) {
            top = top - 1;
            output1 = " ";
            for (j = firstsx; j < lastdx; j++)
                output1 = output1 + "        ";
            System.out.println(output1);
        } else {
            for (j = firstsx; j < lastdx; j++) {
                if ((j + top) % 2 == 0) {
                    if (board[top][j] != null) {
                        if (board[top][j].getFace()) {
                            if (!board[top][j].getFrontCorner(0).isVisible()) {
                                output1 = output1 + "┌";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.FUNGI)) {
                                output1 = output1 + "F";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.ANIMAL)) {
                                output1 = output1 + "A";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.INSECT)) {
                                output1 = output1 + "I";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.PLANT)) {
                                output1 = output1 + "P";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.MANUSCRIPT)) {
                                output1 = output1 + "M";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.EMPTY)) {
                                output1 = output1 + "O";
                            } else if (board[top][j].getFrontCorner(0).getItem().equals(CornerItem.QUILL)) {
                                output1 = output1 + "Q";
                            } else {
                                output1 = output1 + "B";
                            }
                            if (board[top][j].getType() == 1) {
                                if (board[top][j].getPoints() == 0)
                                    output1 = output1 + "-------";
                                else if (board[top][j].getPoints() == 1) {
                                    output1 = output1 + "---1---";
                                }
                            } else if(board[top][j].getType()==-1) {
                                if (board[top][j].getScoringType() == 2) {
                                    if (board[top][j].getPoints() == 3) {
                                        output1 = output1 + "---3---";
                                    } else {
                                        output1 = output1 + "---5---";
                                    }
                                } else if (board[top][j].getScoringType() == 1) {
                                    output1 = output1 + "--2-C--";
                                } else {
                                    if (board[top][j].getObject().equals(CornerItem.MANUSCRIPT))
                                        output1 = output1 + "--1-M--";
                                    else if (board[top][j].getObject().equals(CornerItem.QUILL)) {
                                        output1 = output1 + "--1-Q--";
                                    } else
                                        output1 = output1 + "--1-B--";
                                }

                            }
                            if (!board[top][j].getFrontCorner(1).isVisible()) {
                                output1 = output1 + "┐";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.FUNGI)) {
                                output1 = output1 + "F";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.ANIMAL)) {
                                output1 = output1 + "A";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.INSECT)) {
                                output1 = output1 + "I";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.PLANT)) {
                                output1 = output1 + "P";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.MANUSCRIPT)) {
                                output1 = output1 + "M";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.EMPTY)) {
                                output1 = output1 + "O";
                            } else if (board[top][j].getFrontCorner(1).getItem().equals(CornerItem.QUILL)) {
                                output1 = output1 + "Q";
                            } else {
                                output1 = output1 + "B ";
                            }
                        } else {
                            output1 = output1 + "O-------O";
                        }
                    } else {
                            output1 = output1 + "         ";
                    }
                } else {
                    output1 = output1 + "       ";
                }
            }
            System.out.println(output1);
        }
        for (i = top; i <= down; i++) {
            for (j = firstsx; j <= lastdx; j++) {
                flag=false;
                if ((i + j) % 2 == 0) {
                    if (board[i][j] == null) {
                        if (board[i + 1][j + 1] != null || board[i + 1][j - 1] != null || board[i - 1][j - 1] != null || board[i - 1][j + 1] != null) {
                            flag = true;
                            if (i != 0) {
                                if (j != 0) {
                                    if (board[i - 1][j - 1] != null) {
                                        if (board[i - 1][j - 1].getFace()) {
                                            if (!board[i - 1][j - 1].getFront().get(2).getKey().isVisible())
                                                flag = false;
                                        }
                                    }
                                }
                                if (j != 80) {
                                    if (board[i - 1][j + 1] != null) {
                                        if (board[i - 1][j + 1].getFace()) {
                                            if (!board[i - 1][j + 1].getFront().get(3).getKey().isVisible()) {
                                                flag = false;
                                            }
                                        }
                                    }
                                }
                            }
                            if (i != 80) {
                                if (j != 0) {
                                    if (board[i + 1][j - 1] != null) {
                                        if (board[i + 1][j - 1].getFace()) {
                                            if (!board[i + 1][j - 1].getFront().get(1).getKey().isVisible())
                                                flag = false;
                                        }
                                    }
                                }
                                if (j != 80) {
                                    if (board[i + 1][j + 1] != null) {
                                        if (board[i + 1][j + 1].getFace()) {
                                            if (!board[i + 1][j + 1].getFront().get(0).getKey().isVisible()) {
                                                flag = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (flag) {
                            if (i < 10 && j < 10)
                                System.out.print("  (" + i + "," + j + ")  ");
                            else if (i < 10 && j > 9) {
                                System.out.print(" (" + i + "," + j + ")  ");
                            } else if (i > 9 && j < 10) {
                                System.out.print(" (" + i + "," + j + ")  ");
                            } else {
                                System.out.print(" (" + i + "," + j + ") ");
                            }
                        } else {
                            System.out.print("         ");
                        }
                        if (board[i + 1][j - 1] != null) {
                            if (board[i + 1][j - 1].getFace()) {
                                if (!board[i + 1][j - 1].getFrontCorner(1).isVisible()) {
                                    output3 = output3 + "┐";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.FUNGI)) {
                                    output3 = output3 + "F";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.ANIMAL)) {
                                    output3 = output3 + "A";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.INSECT)) {
                                    output3 = output3 + "I";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.PLANT)) {
                                    output3 = output3 + "P";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.MANUSCRIPT)) {
                                    output3 = output3 + "M";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.EMPTY)) {
                                    output3 = output3 + "O";
                                } else if (board[i+1][j-1].getFrontCorner(1).getItem().equals(CornerItem.QUILL)) {
                                    output3 = output3 + "Q";
                                } else {
                                    output3 = output3 + "B";
                                }
                            } else
                                output3 = output3 + "O";
                        } else
                            output3 = output3 + " ";
                        output3 = output3 + "       ";
                        if (board[i + 1][j + 1] != null) {
                            if (board[i + 1][j + 1].getFace()) {
                                if (!board[i + 1][j + 1].getFrontCorner(0).isVisible()) {
                                    output3 = output3 + "┌";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.FUNGI)) {
                                    output3 = output3 + "F";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.ANIMAL)) {
                                    output3 = output3 + "A";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.INSECT)) {
                                    output3 = output3 + "I";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.PLANT)) {
                                    output3 = output3 + "P";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.MANUSCRIPT)) {
                                    output3 = output3 + "M";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.EMPTY)) {
                                    output3 = output3 + "O";
                                } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.QUILL)) {
                                    output3 = output3 + "Q";
                                } else {
                                    output3 = output3 + "B";
                                }
                            } else
                                output3 = output3 + "O";
                        } else
                            output3 = output3 + " ";
                    } else {
                        if (board[i][j].getType() == 1 || board[i][j].getType() == -1) {
                            if (board[i][j].getFace())
                                System.out.print("|       |");
                            else if (board[i][j].getKingdomsType().equals(CornerItem.FUNGI))
                                System.out.print("|   F   |");
                            else if (board[i][j].getKingdomsType().equals(CornerItem.PLANT))
                                System.out.print("|   P   |");
                            else if (board[i][j].getKingdomsType().equals(CornerItem.INSECT))
                                System.out.print("|   I   |");
                            else
                                System.out.print("|   A   |");
                        } else {
                            if (board[i][j].getFace())
                                System.out.print("|       |");
                            else if (board[i][j].getId() == 80)
                                System.out.print("|   I   |");
                            else if (board[i][j].getId() == 81)
                                System.out.print("|   F   |");
                            else if (board[i][j].getId() == 82)
                                System.out.print("|  P F  |");
                            else if (board[i][j].getId() == 83)
                                System.out.print("|  A I  |");
                            else if (board[i][j].getId() == 84)
                                System.out.print("| A I P |");
                            else
                                System.out.print("| P A F |");
                        }
                        if (!board[i][j].getFront().get(3).getValue()) {
                            if (board[i][j].getFace()) {
                                if (!board[i][j].getFrontCorner(3).isVisible()) {
                                    output3 = output3 + "└";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.FUNGI)) {
                                    output3 = output3 + "F";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.ANIMAL)) {
                                    output3 = output3 + "A";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.INSECT)) {
                                    output3 = output3 + "I";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.PLANT)) {
                                    output3 = output3 + "P";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.MANUSCRIPT)) {
                                    output3 = output3 + "M";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.EMPTY)) {
                                    output3 = output3 + "O";
                                } else if (board[i][j].getFrontCorner(3).getItem().equals(CornerItem.QUILL)) {
                                    output3 = output3 + "Q";
                                } else {
                                    output3 = output3 + "B";
                                }
                            } else
                                output3 = output3 + "O";

                        } else {
                            if (board[i + 1][j - 1] != null) {
                                if (board[i + 1][j - 1].getFace()) {
                                    if (!board[i + 1][j - 1].getFrontCorner(1).isVisible()) {
                                        output3 = output3 + "┐";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.FUNGI)) {
                                        output3 = output3 + "F";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.ANIMAL)) {
                                        output3 = output3 + "A";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.INSECT)) {
                                        output3 = output3 + "I";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.PLANT)) {
                                        output3 = output3 + "P";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.MANUSCRIPT)) {
                                        output3 = output3 + "M";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.EMPTY)) {
                                        output3 = output3 + "O";
                                    } else if (board[i + 1][j - 1].getFrontCorner(1).getItem().equals(CornerItem.QUILL)) {
                                        output3 = output3 + "Q";
                                    } else {
                                        output3 = output3 + "B";
                                    }
                                } else
                                    output3 = output3 + "O";
                            }
                        }
                        if (board[i][j].getFace() && board[i][j].getType() == -1) {
                            if (board[i][j].getId() == 40)
                                output3 = output3 + "--FFA--";
                            else if (board[i][j].getId() == 41) {
                                output3 = output3 + "--FFP--";
                            } else if (board[i][j].getId() == 42) {
                                output3 = output3 + "--FFI--";
                            } else if (board[i][j].getId() == 43) {
                                output3 = output3 + "-FF-FA-";
                            } else if (board[i][j].getId() == 44) {
                                output3 = output3 + "-FF-FP-";
                            } else if (board[i][j].getId() == 45) {
                                output3 = output3 + "-FF-FI-";
                            } else if (board[i][j].getId() == 46) {
                                output3 = output3 + "--FFF--";
                            } else if (board[i][j].getId() == 47) {
                                output3 = output3 + "--FFF--";
                            } else if (board[i][j].getId() == 48) {
                                output3 = output3 + "--FFF--";
                            } else if (board[i][j].getId() == 49) {
                                output3 = output3 + "-FFFFF-";
                            } else if (board[i][j].getId() == 50) {
                                output3 = output3 + "--PPI--";
                            } else if (board[i][j].getId() == 51) {
                                output3 = output3 + "--PPF--";
                            } else if (board[i][j].getId() == 52) {
                                output3 = output3 + "--PPA--";
                            } else if (board[i][j].getId() == 53) {
                                output3 = output3 + "-PP-PI--";
                            } else if (board[i][j].getId() == 54) {
                                output3 = output3 + "-PP-PA-";
                            } else if (board[i][j].getId() == 55) {
                                output3 = output3 + "-PP-PF--";
                            } else if (board[i][j].getId() == 56) {
                                output3 = output3 + "--PPP--";
                            } else if (board[i][j].getId() == 57) {
                                output3 = output3 + "--PPP--";
                            } else if (board[i][j].getId() == 58) {
                                output3 = output3 + "--PPP--";
                            } else if (board[i][j].getId() == 59) {
                                output3 = output3 + "-PPPPP-";
                            } else if (board[i][j].getId() == 60) {
                                output3 = output3 + "--AAI--";
                            } else if (board[i][j].getId() == 61) {
                                output3 = output3 + "--AAP--";
                            } else if (board[i][j].getId() == 62) {
                                output3 = output3 + "--AAF--";
                            } else if (board[i][j].getId() == 63) {
                                output3 = output3 + "-AA-AI-";
                            } else if (board[i][j].getId() == 64) {
                                output3 = output3 + "-AA-AF-";
                            } else if (board[i][j].getId() == 65) {
                                output3 = output3 + "-AA-AP-";
                            } else if (board[i][j].getId() == 66) {
                                output3 = output3 + "--AAA--";
                            } else if (board[i][j].getId() == 67) {
                                output3 = output3 + "--AAA--";
                            } else if (board[i][j].getId() == 68) {
                                output3 = output3 + "--AAA--";
                            } else if (board[i][j].getId() == 69) {
                                output3 = output3 + "-AAAAA-";
                            } else if (board[i][j].getId() == 70) {
                                output3 = output3 + "--IIP--";
                            } else if (board[i][j].getId() == 71) {
                                output3 = output3 + "--IIA--";
                            } else if (board[i][j].getId() == 72) {
                                output3 = output3 + "--IIF--";
                            } else if (board[i][j].getId() == 73) {
                                output3 = output3 + "-II-IA-";
                            } else if (board[i][j].getId() == 74) {
                                output3 = output3 + "-II-IP-";
                            } else if (board[i][j].getId() == 75) {
                                output3 = output3 + "-II-IF-";
                            } else if (board[i][j].getId() == 76) {
                                output3 = output3 + "--III--";
                            } else if (board[i][j].getId() == 77) {
                                output3 = output3 + "--III--";
                            } else if (board[i][j].getId() == 78) {
                                output3 = output3 + "--III--";
                            } else if (board[i][j].getId() == 79) {
                                output3 = output3 + "-IIIII-";
                            }
                        } else if(board[i][j].getType()==1)
                            output3 = output3 + "-------";
                        else
                            output3 = output3 + "---S---";

                    if (!board[i][j].getFront().get(2).getValue()) {
                        if (board[i][j].getFace()) {
                            if (!board[i][j].getFrontCorner(2).isVisible()) {
                                output3 = output3 + "┘";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.FUNGI)) {
                                output3 = output3 + "F";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.ANIMAL)) {
                                output3 = output3 + "A";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.INSECT)) {
                                output3 = output3 + "I";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.PLANT)) {
                                output3 = output3 + "P";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.MANUSCRIPT)) {
                                output3 = output3 + "M";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.EMPTY)) {
                                output3 = output3 + "O";
                            } else if (board[i][j].getFrontCorner(2).getItem().equals(CornerItem.QUILL)) {
                                output3 = output3 + "Q";
                            } else {
                                output3 = output3 + "B";
                            }
                        } else
                            output3 = output3 + "O";
                    } else {
                        if (board[i+1][j+1]!=null && board[i + 1][j + 1].getFace()) {
                            if (!board[i + 1][j + 1].getFrontCorner(1).isVisible()) {
                                output3 = output3 + "┌";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.FUNGI)) {
                                output3 = output3 + "F";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.ANIMAL)) {
                                output3 = output3 + "A";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.INSECT)) {
                                output3 = output3 + "I";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.PLANT)) {
                                output3 = output3 + "P";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.MANUSCRIPT)) {
                                output3 = output3 + "M";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.EMPTY)) {
                                output3 = output3 + "O";
                            } else if (board[i + 1][j + 1].getFrontCorner(0).getItem().equals(CornerItem.QUILL)) {
                                output3 = output3 + "Q";
                            } else {
                                output3 = output3 + "B";
                            }
                        } else
                            output3 = output3 + "O";
                    }

                }
            }
                    else {
                    if (j == firstsx)
                        System.out.print(" ");
                    System.out.print("       ");
                    if(i!=80 && board[i+1][j]!=null){
                        if(board[i+1][j].getFace())
                        {
                            if (board[i+1][j].getType() == 1) {
                                if (board[i+1][j].getPoints() == 0)
                                    output3 = output3 + "-------";
                                else if (board[i+1][j].getPoints() == 1) {
                                    output3 = output3 + "---1---";
                                }
                            } else if(board[i+1][j].getType()==-1){
                                if (board[i+1][j].getScoringType() == 2) {
                                    if (board[i+1][j].getPoints() == 3) {
                                        output3 = output3 + "---3---";
                                    } else {
                                        output3 = output3 + "---5---";
                                    }
                                } else if (board[i+1][j].getScoringType() == 1) {
                                    output3 = output3 + "--2-C--";
                                } else {
                                    if (board[i+1][j].getObject().equals(CornerItem.MANUSCRIPT))
                                        output3 = output3 + "--1-M--";
                                    else if (board[i+1][j].getObject().equals(CornerItem.QUILL)) {
                                        output3 = output3 + "--1-Q--";
                                    } else
                                        output3 = output3 + "--1-B--";
                                }

                            }
                            else{
                                output3=output3+"-------";
                            }
                        }
                        else
                            output3=output3+"-------";
                    }
                    else {
                        if (j == firstsx)
                            output3 = output3 + "        ";
                        else
                            output3 = output3 + "       ";
                    }
                }

                }
            System.out.print("\n"+output3+"\n");
            System.out.flush();
            output3="";
            }
        boolean flag1;
        boolean flag2;
        if(down!=80)
        {
            for(j=firstsx; j<=lastdx; j++)
            {
                flag1=false;
                flag2=false;
                if((j+down+1)%2==0 && board[down+1][j]==null)
                {
                    if(j!=0)
                    {
                        flag1=true;
                        if(board[down][j-1]!=null) {
                            if (board[down][j - 1].getFace() && !board[down][j - 1].getFront().get(2).getKey().isVisible())
                                flag1 = false;
                        }
                    }
                    if(j!=80) {
                        flag2 = true;
                        if(board[down][j+1]!=null) {
                            if (board[down][j + 1].getFace() && !board[down][j + 1].getFront().get(3).getKey().isVisible())
                                flag2 = false;
                        }
                    }
                    if(flag1 && flag2)
                    {
                        if (down < 10 && j < 10)
                            System.out.print("  (" + down + "," + j + ")  ");
                        else if (down < 10 && j > 9) {
                            System.out.print(" (" + down + "," + j + ")  ");
                        } else if (down > 9 && j < 10) {
                            System.out.print(" (" + down + "," + j + ")  ");
                        } else {
                            System.out.print(" (" + down + "," + j + ") ");
                        }
                    } else {
                        System.out.print("         ");
                    }
                }
                else{
                    if(j==firstsx)
                        System.out.print(" ");
                    System.out.print("       ");
                }

            }
            }
        }


    }