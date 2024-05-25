package it.polimi.ingsw.is24am03;
import it.polimi.ingsw.is24am03.client.ConnectionType;

import java.io.IOException;
//import java.rmi.RemoteException;
import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CliView implements ViewInterface{
    private final ExecutorService inputReader;
    private final Scanner inputScan;
    private final Client client;
    private Map<String, String> commands;

    public static void main(String[] args) {
        try{
            ViewInterface view = new CliView(args[0], Integer.parseInt(args[1]), args[2]);
        }
        catch (NumberFormatException e){
            System.out.println("Expected an integer in arguments 2 and 3.");
            System.exit(-1);
        }
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
                    ignored.printStackTrace();
                    System.out.println("Missing arguments");
                }
            }
            case "Join" -> {
                try {
                    client.JoinGame(inputArray[1]);
                } catch (Exception ignored) {
                    System.out.println("Missing arguments");
                }
            }
            case "Color" -> {
                try {
                    client.PickColor(inputArray[1]);
                } catch (Exception ignored) {
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

                }catch (Exception ignored){
                    System.out.println("Invalid command");
                }
                try {
                    client.sendPrivateText(inputArray[1],text.substring(first+1).trim());
                }catch(Exception ignored){
                    System.out.println("Missing arguments");
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
        System.out.println("PERSONAL HAND\n");
        System.out.println("FRONT:         BACK:");
        System.out.println("\n");
        for (ResourceCard card : hand){
            card.drawCard();
        }
        System.out.println("\n");
        System.out.println("END PERSONAL HAND\n");
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

        System.out.println("Choose a color from the available ones:");
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
        System.out.println("----STARTING CARD----\n");

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
        System.out.println("---END STARTING CARD---\n");

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

        public void notify(String message){
        System.out.println(message);
        }

        public void drawChat(ArrayList<String> chat){
            for(int i=0; i< chat.size();i++){

            }
        }

    @Override
    public void drawScene(SceneType sceneType) {

    }
}