package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.cards.ObjectiveCard;
import it.polimi.ingsw.is24am03.server.model.cards.PlayableCard;
import it.polimi.ingsw.is24am03.server.model.cards.ResourceCard;
import it.polimi.ingsw.is24am03.server.model.cards.StartingCard;
import it.polimi.ingsw.is24am03.server.model.enums.Color;

import java.util.ArrayList;
import java.util.Map;

public interface ViewInterface {

    void drawScene(SceneType sceneType);

    void notify(String s);

    void drawBoard(PlayableCard[][] playableCards);

    void drawHand(ArrayList<ResourceCard> hand);

    void drawObjective(ObjectiveCard o);

    void drawStarting(StartingCard startingCard);

    void drawTable(Map<String, Integer> playerPoints, ResourceCard resourceDeck, ResourceCard goldDeck, ResourceCard card0, ResourceCard card1, ResourceCard card2, ResourceCard card3);

    void drawAvailableColors(ArrayList<Color> colors);
}
