package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.cards.*;
import it.polimi.ingsw.is24am03.server.model.enums.Color;
import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import it.polimi.ingsw.is24am03.server.model.enums.ObjectiveType;
import it.polimi.ingsw.is24am03.server.model.exceptions.*;
import it.polimi.ingsw.is24am03.server.model.player.Player;
import it.polimi.ingsw.is24am03.server.model.player.PlayerBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class used to check effectiveness of the methods in PlayerBoard class
 */
class PlayerBoardTest {

    /**
     * Testing the placing of a gold cards and if its requirements are correctly satisfied
     */
    @Test
    void checkRequirements() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        ArrayList<CornerItem> list = new ArrayList<>();
        StartingCard card22= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card22,true);
        playerBoard.getAvailableItems().put(CornerItem.MANUSCRIPT, 1);
        ArrayList<CornerItem> requirementsList= new ArrayList<CornerItem>();
        requirementsList.add(CornerItem.MANUSCRIPT);
        PlayableCard card = new GoldCard(42, "R", 1, empty, empty, empty, empty, null, null, null, null,requirementsList, 0, CornerItem.MANUSCRIPT);

        assertTrue(playerBoard.checkRequirements(card.getRequirements()));
        ArrayList<CornerItem> requirementsList2= new ArrayList<CornerItem>();
        requirementsList2.add(CornerItem.INKWELL);
        requirementsList2.add(CornerItem.INKWELL);
        ResourceCard card2 = new GoldCard(42, "R", 1, empty, empty, empty, empty, null, null, null, null,requirementsList2, 0, CornerItem.INKWELL);
        playerBoard.getAvailableItems().put(CornerItem.INKWELL, 0);
        assertFalse(playerBoard.checkRequirements(card2.getRequirements()));

        assertThrows(RequirementsNotMetException.class, () -> {
            playerBoard.placeCard(card2,41,41,true);
        });


    }

    /**
     * Test the algorithm to retrieve how many times a certain objective is satisfied by the cards in tha player's board
     */
    @Test
    void checkOb1(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);

        ObjectiveCard card_99 = new ObjectiveList(99,2,CornerItem.MANUSCRIPT, ObjectiveType.ITEM,CornerItem.EMPTY,1);
        ArrayList<CornerItem> list = new ArrayList<>();
        StartingCard card2= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.getAvailableItems().put(CornerItem.MANUSCRIPT, 4);
        playerBoard.placeStartingCard(card2,true);
        assertEquals(4,playerBoard.checkObjective(card_99));
    }
    @Test
    void getPlayer(){
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        assertEquals(player,playerBoard.getPlayer());
    }


    @Test
    void UpdateItem2(){
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        playerBoard.getAvailableItems().put(CornerItem.MANUSCRIPT, 1);
        ArrayList<CornerItem> requirementsList = new ArrayList<>();
        requirementsList.add(CornerItem.MANUSCRIPT);
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        ArrayList<CornerItem> list = new ArrayList<>();
        StartingCard card2= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card2,true);
        assertEquals(card2,playerBoard.getBoard()[40][40]);
        ResourceCard card0 = new ResourceCard(0,"R",3,fungi, empty, notVisible, fungi,empty,empty,empty,empty);
        playerBoard.placeCard(card0, 39, 39, true);
    }

    /**
     * Testing if the points of a card are correctly assigned
     */
    @Test
    void testgiveCardPointsAndPlaceCard() {
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        playerBoard.getAvailableItems().put(CornerItem.MANUSCRIPT, 1);
        ArrayList<CornerItem> requirementsList = new ArrayList<>();
        requirementsList.add(CornerItem.MANUSCRIPT);
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        ArrayList<CornerItem> list = new ArrayList<>();
        StartingCard card2= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card2,true);
        assertEquals(card2,playerBoard.getBoard()[40][40]);


        ResourceCard card0 = new ResourceCard(0,"R",3,fungi, empty, notVisible, fungi,empty,empty,empty,empty);
        playerBoard.placeCard(card0, 41, 41, true);
        assertEquals(card0,playerBoard.getBoard()[41][41]);
        assertEquals(3, player.getPoints());


        list.add(CornerItem.MANUSCRIPT);
        ResourceCard card40 = new GoldCard(40,"R",1,notVisible,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);
        list.clear();
        playerBoard.placeCard(card40, 40, 42, true);
        assertEquals(card40,playerBoard.getBoard()[40][42]);
        assertEquals(4, player.getPoints());



        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        PlayableCard card41 = new GoldCard(41,"R",1,empty,inkwell,empty,notVisible,empty,empty,empty,empty,list,0,CornerItem.INKWELL);
        list.clear();
        playerBoard.placeCard(card40, 41, 43, true);
        assertFalse(playerBoard.getBoard()[41][43].equals(card41));
    }

    /**
     * Testing if the algorithm used to check if an objective is satified works correctly
     */
    @Test
    void testcheckObjective() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<ObjectiveCard> cards = new ArrayList<>();


        ObjectiveCard card_86 = new Dshaped(86,2,CornerItem.FUNGI,ObjectiveType.PATTERNDIAGONAL,CornerItem.FUNGI,1);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        assertEquals(card_80,playerBoard.getBoard()[40][40]);
        ResourceCard card_2 = new ResourceCard(2,"R",0,empty, notVisible, fungi, fungi,empty,empty,empty,empty);
        ResourceCard card_3 = new ResourceCard(3,"R",0,notVisible, fungi, fungi, empty,empty,empty,empty,empty);
        ResourceCard card_4 = new ResourceCard(4,"R",0,notVisible, quill, fungi, plant,empty,empty,empty,empty);
        playerBoard.placeCard(card_2,41,41,true);
        assertEquals(card_2,playerBoard.getBoard()[41][41]);
        playerBoard.placeCard(card_3,42,42,true);
        assertEquals(card_3,playerBoard.getBoard()[42][42]);
        playerBoard.placeCard(card_4,43,43,true);
        assertEquals(card_4,playerBoard.getBoard()[43][43]);
        playerBoard.checkObjective(card_86);
        assertEquals(2,playerBoard.checkObjective(card_86));


        //test card out of limit
        list.add(CornerItem.INSECT);
        StartingCard card_81= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.getBoard()[79][79]=card_81;
        playerBoard.placeStartingCard(card_81,true);
        assertEquals(card_81,playerBoard.getBoard()[79][79]);
        ResourceCard card_5 = new ResourceCard(2,"R",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);

        playerBoard.placeCard(card_5,80,80,true);
        assertEquals(card_5,playerBoard.getBoard()[80][80]);
        ResourceCard card_6 = new ResourceCard(2,"R",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);
        assertThrows(PositionOccupiedException.class, () -> {
            playerBoard.placeCard(card_6,80,80,false);
        });
        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            playerBoard.placeCard(card_6,81,81,false);
        });
        assertThrows(NoCardsAvailableException.class, () -> {
            playerBoard.placeCard(card_6,21,21,false);
        });

    }

    /**
     * Testing if the algorithm used to check if an objective type L is satified works correctly
     */

    @Test
    void testPatternL() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<ObjectiveCard> cards = new ArrayList<>();



        ObjectiveCard card_90 = new Lshaped(90,3,CornerItem.FUNGI,ObjectiveType.PATTERNL,CornerItem.FUNGI,2,CornerItem.PLANT);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ResourceCard card_2 = new ResourceCard(2,"G",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);
        ResourceCard card_3 = new ResourceCard(3,"R",0,notVisible, fungi, fungi, empty,empty,empty,empty,empty);
        ResourceCard card_4 = new ResourceCard(4,"R",0,notVisible, quill, fungi, plant,empty,empty,empty,empty);
        ResourceCard card_5 = new ResourceCard(4,"R",0,notVisible, quill, fungi, plant,empty,empty,empty,empty);
        playerBoard.placeCard(card_2,41,41,true);
        playerBoard.placeCard(card_3,42,42,true);
        playerBoard.placeCard(card_4,43,43,true);
        playerBoard.placeCard(card_4,44,42,true);
        playerBoard.checkObjective(card_90);
        assertEquals(3,playerBoard.checkObjective(card_90));


    }
    /**
     * Testing if the algorithm used to check if an objective type L is satified works correctly
     */
    @Test
    void testPatternL2() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<ObjectiveCard> cards = new ArrayList<>();



        ObjectiveCard card_90 = new Lshaped(90,3,CornerItem.FUNGI,ObjectiveType.PATTERNL,CornerItem.FUNGI,1,CornerItem.PLANT);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ResourceCard card_2 = new ResourceCard(2,"G",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);
        ResourceCard card_3 = new ResourceCard(3,"R",0,empty, fungi, fungi, empty,empty,empty,empty,empty);
        ResourceCard card_4 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        ResourceCard card_5 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        playerBoard.placeCard(card_2,41,41,true);
        playerBoard.placeCard(card_3,40,42,true);
        playerBoard.placeCard(card_4,39,43,true);
        playerBoard.placeCard(card_4,38,42,true);
        playerBoard.checkObjective(card_90);
        assertEquals(3,playerBoard.checkObjective(card_90));


    }
    /**
     * Testing if the algorithm used to check if an objective type L is satified works correctly
     */
    @Test
    void testPatternL3() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<ObjectiveCard> cards = new ArrayList<>();



        ObjectiveCard card_90 = new Lshaped(90,3,CornerItem.FUNGI,ObjectiveType.PATTERNL,CornerItem.FUNGI,0,CornerItem.PLANT);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ResourceCard card_2 = new ResourceCard(2,"G",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);
        ResourceCard card_3 = new ResourceCard(3,"R",0,empty, fungi, fungi, empty,empty,empty,empty,empty);
        ResourceCard card_4 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        ResourceCard card_5 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        playerBoard.placeCard(card_2,39,39,true);
        playerBoard.placeCard(card_3,38,38,true);
        playerBoard.placeCard(card_4,37,39,true);
        playerBoard.placeCard(card_4,36,38,true);
        playerBoard.checkObjective(card_90);
        assertEquals(3,playerBoard.checkObjective(card_90));


    }
    /**
     * Testing if the algorithm used to check if an objective type L is satified works correctly
     */
    @Test
    void testPatternL4() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<ObjectiveCard> cards = new ArrayList<>();



        ObjectiveCard card_90 = new Lshaped(90,3,CornerItem.FUNGI,ObjectiveType.PATTERNL,CornerItem.FUNGI,3,CornerItem.PLANT);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ResourceCard card_2 = new ResourceCard(2,"G",0,empty, fungi, fungi, fungi,empty,empty,empty,empty);
        ResourceCard card_3 = new ResourceCard(3,"R",0,empty, fungi, fungi, empty,empty,empty,empty,empty);
        ResourceCard card_4 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        ResourceCard card_5 = new ResourceCard(4,"R",0,empty, quill, fungi, plant,empty,empty,empty,empty);
        playerBoard.placeCard(card_2,41,39,true);
        playerBoard.placeCard(card_3,42,38,true);
        playerBoard.placeCard(card_4,43,39,true);
        playerBoard.placeCard(card_4,44,38,true);
        playerBoard.checkObjective(card_90);
        assertEquals(3,playerBoard.checkObjective(card_90));


    }
    /**
     * Testing if the algorithm used to check if an objective type ITEM is satified works correctly
     */
    @Test
    void testcheckObjectiveITEM() {
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);

        ObjectiveCard card_94 = new ObjectiveList(94,2,CornerItem.FUNGI,ObjectiveType.ITEM,CornerItem.EMPTY,3);
        playerBoard.getAvailableItems().put(CornerItem.FUNGI, 3);
        playerBoard.checkObjective(card_94);
        assertEquals(2,playerBoard.checkObjective(card_94));



        ObjectiveCard card_98 = new ObjectiveList(98,3,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,2);
        playerBoard.getAvailableItems().put(CornerItem.MANUSCRIPT, 5);
        playerBoard.getAvailableItems().put(CornerItem.INKWELL, 4);
        playerBoard.getAvailableItems().put(CornerItem.QUILL, 3);
        playerBoard.checkObjective(card_98);
        assertEquals(9,playerBoard.checkObjective(card_98));
        //     ObjectiveCard card_99 = new ObjectiveList(99,2,CornerItem.EMPTY,ObjectiveType.ITEM,CornerItem.EMPTY,1);
    }
    @Test
    void testgivepoints1(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        //   list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        playerBoard.getAvailableItems().put(CornerItem.FUNGI, 2);
        playerBoard.getAvailableItems().put(CornerItem.ANIMAL, 1);



        ResourceCard card_40 = new GoldCard(40,"R",1,empty,empty,quill,empty,empty,empty,empty,empty,list,0,CornerItem.QUILL);

        playerBoard.placeCard(card_40,41,41,false);
        assertEquals(card_40,playerBoard.getBoard()[41][41]);
        list.clear();
    }

    /**
     * Testing if the list which contains the items in the player's board is correctly updated
     */
    @Test
    void testUpdateItem(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        //     list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.FUNGI));
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.PLANT));
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.ANIMAL));
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.INSECT));
        ResourceCard card_0 = new ResourceCard(0,"R",0,fungi, fungi, notVisible, fungi,empty,empty,empty,empty);
        playerBoard.placeCard(card_0,41,41,true);
        assertEquals(4,playerBoard.getAvailableItems().get(CornerItem.FUNGI));
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.PLANT));
        assertEquals(0,playerBoard.getAvailableItems().get(CornerItem.ANIMAL));
        assertEquals(1,playerBoard.getAvailableItems().get(CornerItem.INSECT));

        PlayableCard card_1 = new ResourceCard(0,"R",0,fungi, fungi, notVisible, fungi,notVisible,notVisible,notVisible,notVisible);
        //    playerBoard.placeCard(card_1,42,42,false);

    }
    /**
     * Testing if the algorithm used to check if an objective type DIAGONAL is satified works correctly
     */
    @Test
    void testDiagonal(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ObjectiveCard card_87 = new Dshaped(87,2,CornerItem.EMPTY,ObjectiveType.PATTERNDIAGONAL,CornerItem.PLANT,0);
        assertEquals(0,card_87.getDirection());
        ResourceCard card_6 = new ResourceCard(6,"G",0,fungi, insect, empty, manuscript,empty,empty,empty,empty);
        ResourceCard card_7 = new ResourceCard(7,"G",1,empty, fungi, empty, empty,empty,empty,empty,empty);
        ResourceCard card_8 = new ResourceCard(8,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        playerBoard.placeCard(card_6,39,41,true);
        playerBoard.placeCard(card_7,38,42,true);
        playerBoard.placeCard(card_8,37,43,true);
        assertEquals(card_6,playerBoard.getBoard()[39][41]);
        assertEquals(card_7,playerBoard.getBoard()[38][42]);
        assertEquals(card_8,playerBoard.getBoard()[37][43]);
        assertEquals(2,playerBoard.checkObjective(card_87));
    }
    /**
     * Testing if the algorithm used to check if an objective type DIAGONAL is satified works correctly
     */
    @Test
    void testSingleandDoubleDiagonal(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ObjectiveCard card_87 = new Dshaped(87,2,CornerItem.EMPTY,ObjectiveType.PATTERNDIAGONAL,CornerItem.PLANT,0);
        assertEquals(0,card_87.getDirection());
        ResourceCard card_6 = new ResourceCard(6,"G",0,fungi, insect, empty, manuscript,empty,empty,empty,empty);
        ResourceCard card_7 = new ResourceCard(7,"G",1,empty, fungi, empty, empty,empty,empty,empty,empty);
        ResourceCard card_8 = new ResourceCard(8,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        ResourceCard card_9 = new ResourceCard(9,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        ResourceCard card_10 = new ResourceCard(10,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        ResourceCard card_11 = new ResourceCard(11,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        ResourceCard card_12 = new ResourceCard(11,"G",1,fungi, plant, empty, empty,empty,empty,empty,empty);
        playerBoard.placeCard(card_6,39,41,true);
        playerBoard.placeCard(card_7,38,42,true);
        playerBoard.placeCard(card_8,37,43,true);
        playerBoard.placeCard(card_9,36,44,true);
        playerBoard.placeCard(card_10,35,45,true);
        playerBoard.placeCard(card_11,34,46,true);
        playerBoard.placeCard(card_12,33,47,true);

        assertEquals(card_6,playerBoard.getBoard()[39][41]);
        assertEquals(card_7,playerBoard.getBoard()[38][42]);
        assertEquals(card_8,playerBoard.getBoard()[37][43]);
        assertEquals(card_9,playerBoard.getBoard()[36][44]);
        assertEquals(card_10,playerBoard.getBoard()[35][45]);
        assertEquals(card_11,playerBoard.getBoard()[34][46]);
        assertEquals(card_12,playerBoard.getBoard()[33][47]);
        assertEquals(4,playerBoard.checkObjective(card_87));
    }
    @Test
    void testGivePoints2(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,plant,animal,insect,empty,plant,empty,insect,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        ResourceCard card_6 = new ResourceCard(6,"G",0,fungi, insect, empty, manuscript,empty,empty,empty,empty);
        playerBoard.placeCard(card_6,39,41,true);
        ResourceCard card_7 = new ResourceCard(7,"G",0,empty, fungi, empty, empty,empty,empty,empty,empty);
        playerBoard.placeCard(card_7,40,42,true);
        assertEquals(card_6,playerBoard.getBoard()[39][41]);
        assertEquals(card_7,playerBoard.getBoard()[40][42]);

        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.ANIMAL);
        ResourceCard card_43 = new GoldCard(43,"R",2,empty,empty,empty,notVisible,empty,empty,empty,empty,list,1,CornerItem.EMPTY);
        list.clear();
        playerBoard.getAvailableItems().put(CornerItem.FUNGI, 3);
        playerBoard.getAvailableItems().put(CornerItem.ANIMAL, 1);
        playerBoard.placeCard(card_43,41,41,true);
        assertEquals(card_43,playerBoard.getBoard()[41][41]);
        assertEquals(4,player.getPoints());
    }

    /**
     * Testing the placing of a card with a not visible corner is correctly made
     */
    @Test
    void testCornerNotVisible(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,notVisible,animal,insect,notVisible,notVisible,notVisible,notVisible,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,false);
        ResourceCard card_6 = new ResourceCard(6,"G",0,fungi, insect, empty, manuscript,empty,empty,empty,empty);
        assertThrows(CornerNotVisibleException.class, () -> {
            playerBoard.placeCard(card_6,39,41,false);
        });
    }
    @Test
    void givePointType2(){
        Corner animal = new Corner("A");
        Corner fungi =new Corner("F");
        Corner insect =new Corner("I");
        Corner plant =new Corner("P");
        Corner inkwell =new Corner("K");
        Corner manuscript =new Corner("M");
        Corner quill =new Corner("Q");
        Corner empty =new Corner("E");
        Corner notVisible =new Corner("X");
        Player player = new Player("TestPlayer");
        PlayerBoard playerBoard = new PlayerBoard(player);
        ArrayList<CornerItem> list = new ArrayList<>();
        list.add(CornerItem.INSECT);
        StartingCard card_80= new StartingCard(80,0,fungi,empty,animal,insect,notVisible,notVisible,notVisible,notVisible,list);
        list.clear();
        playerBoard.placeStartingCard(card_80,true);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        list.add(CornerItem.FUNGI);
        ResourceCard card_47 = new GoldCard(47,"R",3,quill,empty,empty,empty,empty,empty,empty,empty,list,2,CornerItem.EMPTY);
        list.clear();
        playerBoard.placeCard(card_47,41,41,true);



    }

}