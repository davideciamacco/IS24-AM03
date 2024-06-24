package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.client.ConnectionType;

import java.io.IOException;
//import java.rmi.RemoteException;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.chat.Text;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.State;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CliView implements ViewInterface{
    private final ExecutorService inputReader;
    private final Scanner inputScan;
    private static Client client;
    private Map<String, String> commands;

    public static void main(String[] args) {
        try{
            String ANSI_RESET = "\033[0m";
            String ANSI_GREEN = "\033[0;32m";
            System.out.println(ANSI_GREEN);
            System.out.println("  ____ ___  ____  _______  __                           \n" +
                    " / ___/ _ \\|  _ \\| ____\\ \\/ /                           \n" +
                    "| |  | | | | | | |  _|  \\  /                            \n" +
                    "| |__| |_| | |_| | |___ /  \\                            \n" +
                    " \\____\\___/|____/|_____/_/\\_\\      _    _     ___ ____  \n" +
                    "| \\ | |  / \\|_   _| | | |  _ \\    / \\  | |   |_ _/ ___| \n" +
                    "|  \\| | / _ \\ | | | | | | |_) |  / _ \\ | |    | |\\___ \\ \n" +
                    "| |\\  |/ ___ \\| | | |_| |  _ <  / ___ \\| |___ | | ___) |\n" +
                    "|_| \\_/_/   \\_\\_|  \\___/|_| \\_\\/_/   \\_\\_____|___|____/ ");
            System.out.println(ANSI_RESET);
            ViewInterface view = new CliView(args[0], Integer.parseInt(args[1]), args[2]);

        }
        catch (NumberFormatException e){
            System.out.println("Expected an integer in arguments 2 and 3.");
            System.exit(-1);
        }
    }
    public static void setClient(Client client1){
        client=client1;
    }
    public CliView(String ip, int port, String connectionType) {
        inputScan = new Scanner(System.in);
        inputReader = Executors.newCachedThreadPool();
        client.setCLI(this);

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
                } catch (Exception e) {
                    System.out.println("Command for creating a game must be: 'CreateGame 2/3/4 <yournickname>'");
                }

            }
            case "Join" -> {
                try {
                    client.JoinGame(inputArray[1]);
                } catch (Exception e) {
                    System.out.println("Command for joining a game must be: 'Join <yournickname>'");
                }
            }
            case "Color" -> {
                try {
                    client.PickColor(inputArray[1]);
                } catch (Exception e) {
                    System.out.println("Missing arguments");
                }
            }
            case "Starting" -> {
                try {
                    client.ChooseStartingCardSide(inputArray[1]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }

            case "Objective" -> {
                try {
                    client.ChooseObjectiveCard(Integer.parseInt(inputArray[1]));
                } catch (Exception e) {
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
            case "DrawTable" -> {
                try {
                    client.DrawTable(Integer.parseInt(inputArray[1]));
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }

            case "RejoinGame" ->{
                try{
                    client.RejoinGame(inputArray[1]);
                } catch (Exception ignored) {
                    // ignored.printStackTrace();
                    System.out.println("Missing arguments!");
                }
            }

            case "SendGroupChat" -> {
                try {
                    String text = cliInput.substring("SendGroupChat".length()).trim();
                    client.sendGroupText(text);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            case "SendPrivateChat" -> {
                String text = cliInput.substring("SendPrivateChat".length()).trim();
                int first = text.indexOf(' ');
                try{
                    check(first);
                    try {
                        client.sendPrivateText(inputArray[1],text.substring(first+1).trim());
                    }catch(Exception ignored){
                        System.out.println("Missing arguments");
                    }

                }catch (Exception ignored){
                    System.out.println("Invalid command");
                }

            }


            default -> {
                System.out.println("Invalid Command");
            }
        }
    }



    private void check(int i) throws Exception{
        if(i==-1){
            throw new Exception();
        }
    }

    private void drawRequirements(ResourceCard card){
        if (card.getId() == 40)
            System.out.print("--FFA--");
        else if (card.getId() == 41) {
            System.out.print("--FFP--");
        } else if (card.getId() == 42) {
            System.out.print("--FFI--");
        } else if (card.getId() == 43) {
            System.out.print("-FF-FA-");
        } else if (card.getId() == 44) {
            System.out.print("-FF-FP-");
        } else if (card.getId() == 45) {
            System.out.print("-FF-FI-");
        } else if (card.getId() == 46) {
            System.out.print("--FFF--");
        } else if (card.getId() == 47) {
            System.out.print("--FFF--");
        } else if (card.getId() == 48) {
            System.out.print("--FFF--");
        } else if (card.getId() == 49) {
            System.out.print("-FFFFF-");
        } else if (card.getId() == 50) {
            System.out.print("--PPI--");
        } else if (card.getId() == 51) {
            System.out.print("--PPF--");
        } else if (card.getId() == 52) {
            System.out.print("--PPA--");
        } else if (card.getId() == 53) {
            System.out.print("-PP-PI--");
        } else if (card.getId() == 54) {
            System.out.print("-PP-PA-");
        } else if (card.getId() == 55) {
            System.out.print("-PP-PF--");
        } else if (card.getId() == 56) {
            System.out.print("--PPP--");
        } else if (card.getId() == 57) {
            System.out.print("--PPP--");
        } else if (card.getId() == 58) {
            System.out.print("--PPP--");
        } else if (card.getId() == 59) {
            System.out.print("-PPPPP-");
        } else if (card.getId() == 60) {
            System.out.print("--AAI--");
        } else if (card.getId() == 61) {
            System.out.print("--AAP--");
        } else if (card.getId() == 62) {
            System.out.print("--AAF--");
        } else if (card.getId() == 63) {
            System.out.print("-AA-AI-");
        } else if (card.getId() == 64) {
            System.out.print("-AA-AF-");
        } else if (card.getId() == 65) {
            System.out.print("-AA-AP-");
        } else if (card.getId() == 66) {
            System.out.print("--AAA--");
        } else if (card.getId() == 67) {
            System.out.print("--AAA--");
        } else if (card.getId() == 68) {
            System.out.print("--AAA--");
        } else if (card.getId() == 69) {
            System.out.print("-AAAAA-");
        } else if (card.getId() == 70) {
            System.out.print("--IIP--");
        } else if (card.getId() == 71) {
            System.out.print("--IIA--");
        } else if (card.getId() == 72) {
            System.out.print("--IIF--");
        } else if (card.getId() == 73) {
            System.out.print("-II-IA-");
        } else if (card.getId() == 74) {
            System.out.print("-II-IP-");
        } else if (card.getId() == 75) {
            System.out.print("-II-IF-");
        } else if (card.getId() == 76) {
            System.out.print("--III--");
        } else if (card.getId() == 77) {
            System.out.print("--III--");
        } else if (card.getId() == 78) {
            System.out.print("--III--");
        } else if (card.getId() == 79) {
            System.out.print("-IIIII-");
        }
    }

    private void drawCorner(PlayableCard card, int corner, boolean face)
    {
        if(face){
            if(!card.getFrontCorner(corner).isVisible()){
                if(corner==0)
                    System.out.print("┌");
                else if (corner==1) {
                    System.out.print("┐");
                }
                else if (corner==2) {
                    System.out.print("┘");
                }
                else
                    System.out.print("└");
            }
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.FUNGI))
                System.out.print("F");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.ANIMAL))
                System.out.print("A");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.INSECT))
                System.out.print("I");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.PLANT))
                System.out.print("P");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.MANUSCRIPT))
                System.out.print("M");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.INKWELL))
                System.out.print("B");
            else if(card.getFrontCorner(corner).getItem().equals(CornerItem.QUILL))
                System.out.print("Q");
            else
                System.out.print("O");
        }
        else{
            if(!card.getBackCorner(corner).isVisible()){
                if(corner==0)
                    System.out.print("┌");
                else if (corner==1) {
                    System.out.print("┐");
                }
                else if (corner==1) {
                    System.out.print("┘");
                }
                else
                    System.out.print("└");
            }
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.FUNGI))
                System.out.print("F");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.ANIMAL))
                System.out.print("A");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.INSECT))
                System.out.print("I");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.PLANT))
                System.out.print("P");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.MANUSCRIPT))
                System.out.print("M");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.INKWELL))
                System.out.print("B");
            else if(card.getBackCorner(corner).getItem().equals(CornerItem.QUILL))
                System.out.print("Q");
            else
                System.out.print("O");
        }
    }

    public void drawHand(ArrayList<ResourceCard> hand) {
        System.out.println("FRONT:         BACK:");
        System.out.println("\n");
        for (ResourceCard card : hand){
            card.drawCard();
        }
    }

    public void drawTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4) {
        System.out.print("\n");
        System.out.println("---COMMON TABLE---\n");
        if (topResDeck == null)
            System.out.print("  ┌-------┐  ");
        else
            System.out.print("  O-------O  ");
        if (tableCard1 == null)
            System.out.print("┌-------┐  ");
        else {
            drawCorner(tableCard1, 0, true);
            System.out.print("--");
            if (tableCard1.getType() == 1) {
                System.out.print("-----");
            } else {
                if (tableCard1.getScoringType() == 1) {
                    System.out.print("2-C--");
                } else if (tableCard1.getScoringType() == 2) {
                    if (tableCard1.getPoints() == 3)
                        System.out.print("-3---");
                    else
                        System.out.print("-5---");
                } else {
                    if (tableCard1.getObject().equals(CornerItem.QUILL))
                        System.out.print("1-Q--");
                    else if (tableCard1.getObject().equals(CornerItem.MANUSCRIPT)) {
                        System.out.print("1-M--");
                    } else {
                        System.out.print("1-B--");
                    }
                }
            }
            drawCorner(tableCard1, 1, true);
            System.out.print("  ");
        }
        if (tableCard2 == null)
            System.out.print("┌-------┐");
        else {
            drawCorner(tableCard2, 0, true);
            System.out.print("--");
            if (tableCard2.getType() == 1) {
                System.out.print("-----");
            } else {
                if (tableCard2.getScoringType() == 1) {
                    System.out.print("2-C--");
                } else if (tableCard2.getScoringType() == 2) {
                    if (tableCard2.getPoints() == 3)
                        System.out.print("-3---");
                    else
                        System.out.print("-5---");
                } else {
                    if (tableCard2.getObject().equals(CornerItem.QUILL))
                        System.out.print("1-Q--");
                    else if (tableCard2.getObject().equals(CornerItem.MANUSCRIPT)) {
                        System.out.print("1-M--");
                    } else {
                        System.out.print("1-B--");
                    }
                }
            }
            drawCorner(tableCard2, 1, true);

        }
        System.out.print("\n");
        if (topResDeck == null) {
            System.out.print("  | empty |  ");
        } else {
            if (topResDeck.getKingdomsType().equals(CornerItem.FUNGI)) {
                System.out.print("  |   F   |  ");
            } else if (topResDeck.getKingdomsType().equals(CornerItem.PLANT)) {
                System.out.print("  |   P   |  ");
            } else if (topResDeck.getKingdomsType().equals(CornerItem.INSECT)) {
                System.out.print("  |   I   |  ");
            } else {
                System.out.print("  |   A   |  ");
            }
        }
        if (tableCard1 == null) {
            System.out.print("| empty |  ");
        } else {
            System.out.print("|       |  ");
        }
        if (tableCard2 == null) {
            System.out.print("| empty |");
        } else {
            System.out.print("|       |");
        }
        System.out.print("\n");
        if (topResDeck == null)
            System.out.print("  └-------┘  ");
        else
            System.out.print("  O-------O  ");
        if (tableCard1 == null)
            System.out.print("└-------┘  ");
        else {
            drawCorner(tableCard1, 3, true);
            if (tableCard1.getType() == 1) {
                System.out.print("-------");
            } else {
                drawRequirements(tableCard1);
            }
            drawCorner(tableCard1, 2, true);
            System.out.print("  ");
        }
        if(tableCard2==null)
            System.out.print("└-------┘");
        else{
            drawCorner(tableCard2, 3, true);
            if (tableCard2.getType() == 1) {
                System.out.print("-------");
            } else {
                drawRequirements(tableCard2);
            }
            drawCorner(tableCard2, 2, true);
        }
        System.out.print("\n\n");

        if (topGoldDeck == null)
            System.out.print("  ┌-------┐  ");
        else
            System.out.print("  O-------O  ");
        if (tableCard3 == null)
            System.out.print("┌-------┐  ");
        else {
            drawCorner(tableCard3, 0, true);
            System.out.print("--");
            if (tableCard3.getType() == 1) {
                System.out.print("-----");
            } else {
                if (tableCard3.getScoringType() == 1) {
                    System.out.print("2-C--");
                } else if (tableCard3.getScoringType() == 2) {
                    if (tableCard3.getPoints() == 3)
                        System.out.print("-3---");
                    else
                        System.out.print("-5---");
                } else {
                    if (tableCard3.getObject().equals(CornerItem.QUILL))
                        System.out.print("1-Q--");
                    else if (tableCard1.getObject().equals(CornerItem.MANUSCRIPT)) {
                        System.out.print("1-M--");
                    } else {
                        System.out.print("1-B--");
                    }
                }
            }
            drawCorner(tableCard3, 1, true);
            System.out.print("  ");
        }
        if (tableCard4 == null)
            System.out.print("┌-------┐");
        else {
            drawCorner(tableCard4, 0, true);
            System.out.print("--");
            if (tableCard4.getType() == 1) {
                System.out.print("-----");
            } else {
                if (tableCard4.getScoringType() == 1) {
                    System.out.print("2-C--");
                } else if (tableCard4.getScoringType() == 2) {
                    if (tableCard4.getPoints() == 3)
                        System.out.print("-3---");
                    else
                        System.out.print("-5---");
                } else {
                    if (tableCard4.getObject().equals(CornerItem.QUILL))
                        System.out.print("1-Q--");
                    else if (tableCard4.getObject().equals(CornerItem.MANUSCRIPT)) {
                        System.out.print("1-M--");
                    } else {
                        System.out.print("1-B--");
                    }
                }
            }
            drawCorner(tableCard4, 1, true);

        }
        System.out.print("\n");
        if (topGoldDeck == null) {
            System.out.print("  | empty |  ");
        } else {
            if (topGoldDeck.getKingdomsType().equals(CornerItem.FUNGI)) {
                System.out.print("  |   F   |  ");
            } else if (topGoldDeck.getKingdomsType().equals(CornerItem.PLANT)) {
                System.out.print("  |   P   |  ");
            } else if (topGoldDeck.getKingdomsType().equals(CornerItem.INSECT)) {
                System.out.print("  |   I   |  ");
            } else {
                System.out.print("  |   A   |  ");
            }
        }
        if (tableCard3 == null) {
            System.out.print("| empty |  ");
        } else {
            System.out.print("|       |  ");
        }
        if (tableCard4 == null) {
            System.out.print("| empty |");
        } else {
            System.out.print("|       |");
        }
        System.out.print("\n");
        if (topGoldDeck == null)
            System.out.print("  └-------┘  ");
        else
            System.out.print("  O-------O  ");
        if (tableCard3 == null)
            System.out.print("└-------┘  ");
        else {
            drawCorner(tableCard3, 3, true);
            if (tableCard3.getType() == 1) {
                System.out.print("-------");
            } else {
                drawRequirements(tableCard3);
            }

            drawCorner(tableCard3, 2, true);
            System.out.print("  ");
        }
        if (tableCard4 == null)
            System.out.print("└-------┘");
        else {
            drawCorner(tableCard4, 3, true);
            if (tableCard4.getType() == 1) {
                System.out.print("-------");
            } else {
                drawRequirements(tableCard4);
            }
            drawCorner(tableCard4, 2, true);
        }
        System.out.println("\n");
        System.out.println("---END COMMON TABLE---\n");

    }


    public void drawAvailableColors(ArrayList<Color> colors)
    {
        String ANSI_RESET = "\033[0m";

        String ANSI_RED = "\033[0;31m";
        String ANSI_GREEN = "\033[0;32m";
        String ANSI_YELLOW = "\033[0;33m";
        String ANSI_BLUE = "\033[0;34m";

       // System.out.println("Choose a color from the available ones:");
        for(int i=0; i<colors.size(); i++){
            if(colors.get(i).equals(Color.RED))
                System.out.print(ANSI_RED + "RED  "+ ANSI_RESET );
            else if (colors.get(i).equals(Color.BLUE)) {
                System.out.print(ANSI_BLUE + "BLUE  "+ ANSI_RESET );
            }
            else if (colors.get(i).equals(Color.GREEN)) {
                System.out.print(ANSI_GREEN+"GREEN  "+ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW+"YELLOW  "+ANSI_RESET);
            }
        }
        System.out.print("\n");
    }

    public void drawStarting(StartingCard startCard){
      //  System.out.println("----STARTING CARD----\n");

        System.out.println("FRONT:         BACK:");
        System.out.println("\n");
        if (startCard.getId() == 80) {
            System.out.println("F-------A      O-------P");
            System.out.println("|       |      |   F   |");
            System.out.println("I---S---A      I---S---O");
            System.out.println("\n");
        } else if (startCard.getId() == 81) {
            System.out.println("P-------A      A-------O");
            System.out.println("|       |      |   F   |");
            System.out.println("F---S---I      O---S---F");
            System.out.println("\n");
        } else if (startCard.getId() == 82) {
            System.out.println("I-------A      O-------O");
            System.out.println("|       |      |  P F  |");
            System.out.println("F---S---P      O---S---O");
            System.out.println("\n");
        } else if (startCard.getId() == 83) {
            System.out.println("P-------I      O-------O");
            System.out.println("|       |      |  A I  |");
            System.out.println("A---S---F      O---S---O");
            System.out.println("\n");
        } else if (startCard.getId() == 84) {
            System.out.println("I-------F      O-------O");
            System.out.println("|       |      |  AIP  |");
            System.out.println("P---S---A      └---S---┘");
            System.out.println("\n");
        } else {
            System.out.println("F-------A      O-------O");
            System.out.println("|       |      |  PAF  |");
            System.out.println("P---S---I      └---S---┘ ");
            System.out.println("\n");
        }
       // System.out.println("---END STARTING CARD---\n");

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
                        // System.out.println("\n");
                        // System.out.println(board[i][j].getFront().get(3).getValue());
                        // System.out.println("\n");
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
        boolean flag2;
        int f1;
        int f2;
        if(down!=80)
        {
            for(j=firstsx; j<=lastdx; j++)
            {
                f1=0;
                f2=0;
                flag2=false;
                if((j+down+1)%2==0 && board[down+1][j]==null)
                {
                    flag2=true;
                    if(j!=0)
                    {
                        f1=1;
                        if(board[down][j-1]!=null) {
                            f1=2;
                            if (board[down][j - 1].getFace() && !board[down][j - 1].getFront().get(2).getKey().isVisible()) {
                                f1=0;
                            }
                        }
                    }
                    if(j!=80) {
                        f2=1;
                        if(board[down][j+1]!=null) {
                            f2=2;
                            if (board[down][j + 1].getFace() && !board[down][j + 1].getFront().get(3).getKey().isVisible()) {
                                f2=0;
                            }
                        }
                    }
                    if(flag2) {
                        if (f1 != 0 && f2 != 0) {
                            if ((f1 == 1 && f2 == 2) || (f1 == 2 && f2 == 1) || (f1 == 2 && f2 == 2)) {
                                if (down + 1 < 10 && j < 10)
                                    System.out.print("  (" + (down + 1) + "," + j + ")  ");
                                else if (down + 1 < 10 && j > 9) {
                                    System.out.print(" (" + (down + 1) + "," + j + ")  ");
                                } else if (down + 1 > 9 && j < 10) {
                                    System.out.print(" (" + (down + +1) + "," + j + ")  ");
                                } else {
                                    System.out.print(" (" + (down + 1) + "," + j + ") ");
                                }
                            }else {
                                System.out.print("         ");
                            }
                        }else {
                            System.out.print("         ");
                        }
                    }else {
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
        System.out.print("\n\n");
    }

    public void confirmCreate(){
        System.out.println("Game created successfully");
    }

    public void confirmJoin(){
        System.out.println("Joined successfully");
    }

    public  void  notifyJoinedPlayer(String joinedPlayer){
        System.out.println(joinedPlayer + " has joined the game\n");
    }


    public void notifyWinners(ArrayList<String> winners){
        StringBuilder message = new StringBuilder();
        for(int i=0; i< winners.size()-1;i++){
            message.append(winners.get(i)).append("-");
        }
        message.append(winners.getLast());
        message= new StringBuilder("winners are " + message);
        System.out.println(message);
    }


    public void  notifyTurnOrder(ArrayList<String> order, String player){
        StringBuilder message = new StringBuilder();
        for(int i=0; i< order.size()-1;i++){
            message.append(order.get(i)).append("-");
        }
        message.append(order.getLast());
        message= new StringBuilder("Turn order is  " + message);
        System.out.println(message);
    }


    public void notifyCurrentPlayer(String current, Map<String,PlayableCard[][]> boards, String player, ArrayList<ResourceCard> hand,State gamestate){
        System.out.println("Current player is: " + current);
        if(gamestate.equals(State.PLAYING)){
            if(current.equals(player)){
                this.drawBoard(boards.get(player));
                this.drawHand(hand);
            }
        }
        if(gamestate.equals(State.STARTING)){
            if(current.equals(player)){
                System.out.println("------CHOOSE A SIDE FOR YOUR STARTING CARD! COMMAND: 'Starting FRONT/BACK'------");
            }
        }
        if(gamestate.equals(State.OBJECTIVE)){
            if(current.equals(player)){
                System.out.println("------CHOOSE YOUR PERSONAL OBJECTIVE! COMMAND: 'Objective 1/2'------");
            }
        }
        if(gamestate.equals(State.PLAYING)){
            if(current.equals(player)) {
                System.out.println("------YOU CAN PLACE A CARD! YOU CAN SEE THE AVAILABLE INDEXES (i,j) ON YOUR SCREEN. COMMAND: 'PlaceCard 1/2/3 i j FRONT/BACK'------");
            }
        }
        if(gamestate.equals(State.DRAWING)){
            if(current.equals(player)){
                System.out.println("------DRAW A CARD!------");
                System.out.println("COMMON TABLE LAYOUT IS: ");
                System.out.println("┌--------┐ ┌-------┐ ┌-------┐");
                System.out.println("|Resource| | Table | | Table |");
                System.out.println("|  deck  | | card 1| | card 2|");
                System.out.println("└--------┘ └-------┘ └-------┘");
                System.out.println("┌--------┐ ┌-------┐ ┌-------┐");
                System.out.println("|  Gold  | | Table | | Table |");
                System.out.println("|  deck  | | card 3| | card 4|");
                System.out.println("└--------┘ └-------┘ └-------┘");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE RESOURCE DECK TYPE: 'DrawResource'");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE GOLD DECK TYPE: 'DrawGold'");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE COMMON TABLE TYPE: 'DrawTable 1/2/3/4'");
            }
        }




    }

    public void notifyCrashedPlayer(String username){
        System.out.println(username + " has crashed");
    }

    public void notifyChangeState(State gameState){
        System.out.println("Game state has changed, now is: " + gameState.toString());
    /*    if(gameState.equals(State.DRAWING)){
            if(current.equals(player)){
                System.out.println("------DRAW A CARD!------");
                System.out.println("COMMON TABLE LAYOUT IS: ");
                System.out.println("┌--------┐ ┌-------┐ ┌-------┐");
                System.out.println("|Resource| | Table | | Table |");
                System.out.println("|  deck  | | card 1| | card 2|");
                System.out.println("└--------┘ └-------┘ └-------┘");
                System.out.println("┌--------┐ ┌-------┐ ┌-------┐");
                System.out.println("|  Gold  | | Table | | Table |");
                System.out.println("|  deck  | | card 3| | card 4|");
                System.out.println("└--------┘ └-------┘ └-------┘");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE RESOURCE DECK TYPE: 'DrawResource'");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE GOLD DECK TYPE: 'DrawGold'");
                System.out.println("IN ORDER TO DRAW A CARD FROM THE COMMON TABLE TYPE: 'DrawTable 1/2/3/4'");
            }
        }*/

    }

    public void notifyRejoinedPlayer(String rejoinedPlayer){
        System.out.println(rejoinedPlayer + " has rejoined the game");
    }

    public void notifyChangePlayerBoard(String player, String nickname, Map<String,PlayableCard[][]>boards){
        if(player.equals(nickname)){
            System.out.println("You placed a card successfully\n");
            this.drawBoard(boards.get(player));
        }
        else{
            System.out.println("*************** " + player + " PLACED A CARD! HERE IS HIS/HER BOARD **************+ ");
            this.drawBoard(boards.get(player));
        }

    }

    public void ReceiveUpdateOnPoints(String player, int points){
        System.out.println("Update on "+player+" points! He/She reached "+ points +" points!");
    }

    public  void NotifyChangePersonalCards(ArrayList<ResourceCard> p){
        System.out.println("------YOUR UPDATED HAND------");
        this.drawHand(p);
        System.out.println("------END PERSONAL HAND------");


    }

    public void notifyChoiceObjective(ObjectiveCard o){
        System.out.println("------YOUR PERSONAL OBJECTIVE------");
        this.drawObjective(o);
        System.out.println("------END PERSONAL OBJECTIVE------");
    }

    public void notifyFirstHand(ArrayList<ResourceCard> hand, StartingCard startingCard, ObjectiveCard o1, ObjectiveCard o2){
        System.out.println("------YOUR PERSONAL HAND------");
        this.drawHand(hand);
        System.out.println("------END PERSONAL HAND------\n");
        System.out.println("------YOUR STARTING CARD, YOU WILL NEED TO CHOOSE THE STARTING SIDE!------");
        this.drawStarting(startingCard);
        System.out.println("------END STARTING CARD------\n");
        System.out.println("------YOUR PERSONAL OBJECTIVES, YOU WILL NEED TO CHOOSE ONE OF THEM!------");
        this.drawObjective(o1);
        this.drawObjective(o2);
        System.out.println("------END PERSONAL OBJECTIVES------");
    }


    public void notifyCommonObjective(ObjectiveCard objectiveCard1, ObjectiveCard objectiveCard2){
        System.out.println("------THESE ARE THE COMMON OBJECTIVES, EVERY TIME YOU COMPLETE ONE OF THEM YOU WILL GAIN EXTRA POINTS------");
        this.drawObjective(objectiveCard1);
        this.drawObjective(objectiveCard2);
        System.out.println("------END COMMON OBJECTIVES------");
        System.out.println("\n");
    }

    public void updateCommonTable(Map<String, Integer> points, ResourceCard topResDeck, ResourceCard topGoldDeck, ResourceCard tableCard1, ResourceCard tableCard2, ResourceCard tableCard3, ResourceCard tableCard4){
        System.out.println("------HERE IS THE UPDATED COMMON BOARD WITH EACH PLAYERS' POINTS------");
        this.drawTable(points,topResDeck,topGoldDeck,tableCard1,tableCard2,tableCard3,tableCard4);
        System.out.println("----------------------------------------------------------------------");
    }

    public void NotifyNumbersOfPlayersReached(){
        System.out.println("------THERE ARE ENOUGH PLAYERS! THE GAME WILL START IN A FEW MOMENTS------");
    }

    public void NotifyLastRound(){
        System.out.println("------LAST ROUND IS STARTING, DURING THIS ROUND DRAWING WON'T BE ALLOWED------");
    }

    public void notifyAvailableColors(ArrayList<Color> colors){
        System.out.println("------CHOOSE A COLOR FROM THE AVAILABLE ONES------");
        System.out.println("COMMAND: 'Color COLOR'");
        this.drawAvailableColors(colors);
        System.out.println("--------------------------------------------------");

    }

    public void drawFinalColors(Map<String,Color> colors, ArrayList<String> players){
        String ANSI_RESET = "\033[0m";
        String ANSI_RED = "\033[0;31m";
        String ANSI_GREEN = "\033[0;32m";
        String ANSI_YELLOW = "\033[0;33m";
        String ANSI_BLUE = "\033[0;34m";
        String square = "■";
        for(String p: players){
            if(colors.get(p).equals(Color.BLUE)){
                System.out.println( p + ":" + ANSI_BLUE  + square + ANSI_RESET);
            }
            if(colors.get(p).equals(Color.RED)){
                System.out.println( p + ":" + ANSI_RED  + square + ANSI_RESET);
            }
            if(colors.get(p).equals(Color.GREEN)){
                System.out.println( p + ":" + ANSI_GREEN  + square + ANSI_RESET);
            }
            if(colors.get(p).equals(Color.YELLOW)){
                System.out.println( p + ":" + ANSI_YELLOW  + square + ANSI_RESET);
            }
        }

    }

    public void notifyFinalColors(Map<String, Color> colors, ArrayList<String> players){
        System.out.println("------FINAL COLORS------");
        this.drawFinalColors(colors, players);
        System.out.println("------------------------");

    }

    public void UpdateCrashedPlayer(String nickname, String player, State gameState, ArrayList<ResourceCard> hand, ObjectiveCard objectiveCard, Map<String, PlayableCard[][]> boards, Map<String, Integer> points, ArrayList<String> players, ArrayList<ObjectiveCard> objectiveCards, Color color, ArrayList<ResourceCard> table){
        System.out.println("---------------------------------------------------------------------");
        System.out.println("HERE ARE SOME UPDATES YOU MIGHT'VE MISSED WHILE GONE!");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("CURRENT PLAYER: "+ nickname);
        StringBuilder message = new StringBuilder();
        for(String s: players){
            message.append(s).append("-");
        }
        message= new StringBuilder("TURN ORDER IS " + message);
        System.out.println(message);
        System.out.println("GAME NOW IS IN "+gameState + " STATE");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("OTHER PLAYERS' BOARDS, TAKE A PEEK!");
        for(String s: players){
            if(!s.equals(player)) {
                System.out.println("THIS IS " + s + " BOARD" + " WHO SCORED SO FAR " + points.get(s));
                this.drawBoard(boards.get(s));
                System.out.println("---------------------------------------------------------------------");
            }
            else{
                System.out.println("THIS IS YOUR BOARD" + " YOU SCORED SO FAR " + points.get(player));
                this.drawBoard(boards.get(player));
                System.out.println("---------------------------------------------------------------------");

            }
        }
        this.drawTable(points,table.get(0),table.get(1),table.get(2),table.get(3),table.get(4),table.get(5));
        System.out.println("--COMMON OBJECTIVES---");
        this.drawObjective(objectiveCards.get(0));
        this.drawObjective(objectiveCards.get(1));
        System.out.println("---END COMMON OBJECTIVES---");
        this.drawHand(hand);
        System.out.println("YOUR PERSONAL OBJECTIVE IS:");
        this.drawObjective(objectiveCard);
        System.out.println("---------------------------------------------------------------------");
    }


    public void UpdateFirst(Map<String,Integer> points, ArrayList<ResourceCard> commons){
        this.drawTable(points,commons.get(0),commons.get(1),commons.get(2),commons.get(3),commons.get(4),commons.get(5));
    }

    public void addGroupText(ArrayList<Text> chat, String player){
        this.drawChat(chat,player);
    }
    public void drawChat(ArrayList<Text> chat, String player){

        if(chat.get(0).getRecipient()==null){
            //stampo tutti i messaggi precedenti se ce ne sono, che abbiano receiver nullo
            if(findNumber(chat)>=2){
                System.out.println("----PREVIOUS GROUP CHAT MESSAGES----");
            }
            for(int i=chat.size()-1; i>0; i--){
                if(chat.get(i).getSender().equals(player) && chat.get(i).getRecipient()==null){
                    System.out.println(chat.get(i).getSender() + " (YOU) : " + chat.get(i).getMex());

                }
                else {
                    if(chat.get(i).getRecipient()==null){
                        System.out.println( chat.get(i).getSender() + " : " + chat.get(i).getMex());}

                }
            }
            if(findNumber(chat)>=2){
                System.out.println("************************************");
            }
            System.out.println("----NEW GROUP CHAT MESSAGE----");
            if(chat.get(0).getSender().equals(player)){
                System.out.println("YOU: " + chat.get(0).getMex());
                System.out.println("************************************");
            }
            else{
                System.out.println(chat.get(0).getSender() + " : " + chat.get(0).getMex());
                System.out.println("************************************");
            }
        }
        else{
            if(chat.get(0).getSender().equals(player)){
                //trovo tutti i messaggi scambiati con il player destinatario
                System.out.println("YOU SENT A TEXT TO "+ chat.get(0).getRecipient() + "THIS IS YOUR PRIVATE CHAT WITH: " + chat.get(0).getRecipient());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messaggi che io ho mandato a lui
                    if(chat.get(i).getRecipient()!=null) {
                        if (chat.get(i).getSender().equals(player) && chat.get(i).getRecipient().equals(chat.get(0).getRecipient())) {
                            System.out.println("YOU " + " : " + chat.get(i).getMex());
                        }
                        //messaggi che lui ha mandato a me
                        else if (chat.get(i).getSender().equals(chat.get(0).getRecipient()) && chat.get(i).getRecipient().equals(player)) {
                            System.out.println(chat.get(i).getSender() + " : " + chat.get(i).getMex());
                        }
                    }
                }
            }

            if(chat.get(0).getRecipient().equals(player)) {
                //stampo tutti i loro vecchi messaggi
                System.out.println("YOU HAVE A NEW TEXT FROM " + chat.get(0).getSender() + " THIS IS YOUR PRIVATE CHAT WITH: " + chat.get(0).getSender());
                System.out.println("************************************");
                for(int i=chat.size()-1; i>=0; i--){
                    //messa che io ho mandato a lui
                    if(chat.get(i).getRecipient()!=null) {
                        if (chat.get(i).getSender().equals(player) && chat.get(i).getRecipient().equals(chat.get(0).getSender())) {
                            System.out.println("YOU " + " : " + chat.get(i).getMex());
                        }
                        //mess che lui ha mandato a me
                        else if (chat.get(i).getSender().equals(chat.get(0).getSender()) && chat.get(i).getRecipient().equals(player)) {
                            System.out.println(chat.get(i).getSender() + " : " + chat.get(i).getMex());
                        }
                    }
                }

            }
        }
    }


    private int findNumber(ArrayList<Text> chat){
        int number=0;
        for(Text t: chat){
            if(t.getRecipient()==null){
                number++;
            }
        }
        return number;
    }


   public void printNotifications(String message){
       System.out.println(message);
   }

    @Override
    public void drawScene(SceneType sceneType) {

    }

    public void drawError(String message){
        System.out.println(message);
    }

    @Override
    public void restoreChat(ArrayList<Text> chat, String player) {

    }
}