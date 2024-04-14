import com.squareup.moshi.*;

public class ResourceCardAdapter {
    ResourceCard fromJson(String card) {
        if (card.length() != 6) throw new JsonDataException("Unknown card: " + card);

        Corner plant = new Corner("P");
        Corner animal = new Corner("A");
        Corner fungi = new Corner("F");
        Corner insect = new Corner("I");
        Corner quill = new Corner("Q");
        Corner inkwell = new Corner("K");
        Corner manuscript = new Corner("M");
        Corner empty = new Corner("E");
        Corner notVisible = new Corner("X");

        Corner c1,c2,c3,c4;
        c1=null;
        c2=null;
        c3=null;
        c4=null;


        int points = card.charAt(1);

        switch (card.charAt(2)){
            case 'P' : c1=plant;
            case 'A' : c1=animal;
            case 'F' : c1=fungi;
            case 'I' : c1=insect;
            case 'Q' : c1=quill;
            case 'K' : c1=inkwell;
            case 'M' : c1=manuscript;
            case 'X' : c1=notVisible;
            case 'E' : c1=empty;
        }

        switch (card.charAt(3)){
            case 'P' : c2=plant;
            case 'A' : c2=animal;
            case 'F' : c2=fungi;
            case 'I' : c2=insect;
            case 'Q' : c2=quill;
            case 'K' : c2=inkwell;
            case 'M' : c2=manuscript;
            case 'X' : c2=notVisible;
            case 'E' : c2=empty;
        }

        switch (card.charAt(4)){
            case 'P' : c3=plant;
            case 'A' : c3=animal;
            case 'F' : c3=fungi;
            case 'I' : c3=insect;
            case 'Q' : c3=quill;
            case 'K' : c3=inkwell;
            case 'M' : c3=manuscript;
            case 'X' : c3=notVisible;
            case 'E' : c3=empty;
        }

        switch (card.charAt(5)){
            case 'P' : c4=plant;
            case 'A' : c4=animal;
            case 'F' : c4=fungi;
            case 'I' : c4=insect;
            case 'Q' : c4=quill;
            case 'K' : c4=inkwell;
            case 'M' : c4=manuscript;
            case 'X' : c4=notVisible;
            case 'E' : c4=empty;
        }

        switch (card.charAt(0)){
            case 'R' : return new ResourceCard("R", c1,c2,c3,c4);
            case 'G' : return new ResourceCard("G", c1,c2,c3,c4);
            case 'B' : return new ResourceCard("B", c1,c2,c3,c4);
            case 'P' : return new ResourceCard("P", c1,c2,c3,c4);
        }

    }
}

