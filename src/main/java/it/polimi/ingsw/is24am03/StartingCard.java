package it.polimi.ingsw.is24am03;

import java.util.ArrayList;

public class StartingCard extends PlayableCard{
    private ArrayList<Resources> kingdomList;
    public ArrayList<Resources> getKingdomList() {
        return kingdomList;
    }

    @Override
    public int getType() {
        return 0;
    }
}
