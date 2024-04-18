package it.polimi.ingsw.is24am03;

public class ResCards {

    Corner animal = new Corner("A");
    Corner fungi =new Corner("F");
    Corner insect =new Corner("I");
    Corner plant =new Corner("P");
    Corner inkwell =new Corner("K");
    Corner manuscript =new Corner("M");
    Corner quill =new Corner("Q");
    Corner empty =new Corner("E");
    Corner notVisible =new Corner("X");

    PlayableCard card_0 = new ResourceCard(0,"R",0,fungi, empty, notVisible, fungi,empty);
    PlayableCard card_1 = new ResourceCard(1,"R",0,fungi, fungi, empty, notVisible,empty);
    PlayableCard card_2 = new ResourceCard(2,"R",0,empty, notVisible, fungi, fungi,empty);
    PlayableCard card_3 = new ResourceCard(3,"R",0,notVisible, fungi, fungi, empty,empty);
    PlayableCard card_4 = new ResourceCard(4,"R",0,notVisible, quill, fungi, plant,empty);
    PlayableCard card_5 = new ResourceCard(5,"R",0,inkwell, fungi, animal, notVisible,empty);
    PlayableCard card_6 = new ResourceCard(6,"R",0,fungi, insect, empty, manuscript,empty);
    PlayableCard card_7 = new ResourceCard(7,"R",1,empty, fungi, notVisible, empty,empty);
    PlayableCard card_8 = new ResourceCard(8,"R",1,fungi, notVisible, empty, empty,empty);
    PlayableCard card_9 = new ResourceCard(9,"R",1,notVisible, empty, empty, fungi,empty);
    PlayableCard card_10 = new ResourceCard(10,"G",0,plant, empty, notVisible, plant,empty);
    PlayableCard card_11 = new ResourceCard(11,"G",0,plant, plant, empty, notVisible,empty);
    PlayableCard card_12 = new ResourceCard(12,"G",0,empty, notVisible, plant, plant,empty);
    PlayableCard card_13 = new ResourceCard(13,"G",0,notVisible, plant, plant, empty,empty);
    PlayableCard card_14 = new ResourceCard(14,"G",0,notVisible, insect, plant, quill,empty);
    PlayableCard card_15 = new ResourceCard(15,"G",0,fungi, plant, inkwell, notVisible,empty);
    PlayableCard card_16 = new ResourceCard(16,"G",0,manuscript, notVisible, animal, plant,empty);
    PlayableCard card_17 = new ResourceCard(17,"G",1,empty, empty, notVisible, plant,empty);
    PlayableCard card_18 = new ResourceCard(18,"G",1,empty, empty, plant, notVisible,empty);
    PlayableCard card_19 = new ResourceCard(19,"G",1,notVisible, plant, empty, empty,empty);
    PlayableCard card_20 = new ResourceCard(20,"B",0,animal, animal, notVisible, empty,empty);
    PlayableCard card_21 = new ResourceCard(21,"B",0,notVisible, empty, animal, animal,empty);
    PlayableCard card_22 = new ResourceCard(22,"B",0,animal, notVisible, empty, animal,empty);
    PlayableCard card_23 = new ResourceCard(23,"B",0,empty, animal, animal, notVisible,empty);
    PlayableCard card_24 = new ResourceCard(24,"B",0,notVisible, insect, animal, inkwell,empty);
    PlayableCard card_25 = new ResourceCard(25,"B",0,plant, animal, manuscript, notVisible,empty);
    PlayableCard card_26 = new ResourceCard(26,"B",0,quill, notVisible,fungi, animal,empty);
    PlayableCard card_27 = new ResourceCard(27,"B",1,notVisible, empty, empty, plant,empty);
    PlayableCard card_28 = new ResourceCard(28,"B",1,empty, notVisible, animal, empty,empty);
    PlayableCard card_29 = new ResourceCard(29,"B",1,empty, animal, notVisible, empty,empty);
    PlayableCard card_30 = new ResourceCard(30,"P",0,insect, insect, notVisible, empty,empty);
    PlayableCard card_31 = new ResourceCard(31,"P",0,notVisible, empty, insect, insect,empty);
    PlayableCard card_32 = new ResourceCard(32,"P",0,insect, notVisible, empty, insect,empty);
    PlayableCard card_33 = new ResourceCard(33,"P",0,empty, insect, insect, empty,empty);
    PlayableCard card_34 = new ResourceCard(34,"P",0,notVisible, quill, insect, animal,empty);
    PlayableCard card_35 = new ResourceCard(35,"P",0,manuscript, insect, fungi, notVisible,empty);
    PlayableCard card_36 = new ResourceCard(36,"P",0,insect, plant, notVisible, inkwell,empty);
    PlayableCard card_37 = new ResourceCard(37,"P",1,insect, notVisible, empty, empty,empty);
    PlayableCard card_38 = new ResourceCard(38,"P",1,empty, empty, insect, notVisible,empty);
    PlayableCard card_39 = new ResourceCard(39,"P",1,notVisible, insect, empty, empty,empty);


}
