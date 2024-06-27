package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import java.util.ArrayList;

/**
 * Represents a resource card, which is a type of playable card in the game.
 * Extends PlayableCard and implements methods specific to resource cards.
 */
public class ResourceCard extends PlayableCard {

    /**
     * The type of kingdom associated with this resource card.
     */
    private final CornerItem kingdomType;

    /**
     * Constructor to initialize a resource card with specific attributes.
     *
     * @param id Unique identifier of the card.
     * @param color Color code representing the type of resource (R, G, B, P).
     * @param points Points associated with the card.
     * @param corner0 Corner 0 at the front of the card.
     * @param corner1 Corner 1 at the front of the card.
     * @param corner2 Corner 2 at the front of the card.
     * @param corner3 Corner 3 at the front of the card.
     * @param back0 Corner 0 at the back of the card.
     * @param back1 Corner 1 at the back of the card.
     * @param back2 Corner 2 at the back of the card.
     * @param back3 Corner 3 at the back of the card.
     * @throws IllegalArgumentException If an invalid color code is provided.
     */
    public ResourceCard(int id, String color, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3,
                        Corner back0, Corner back1, Corner back2, Corner back3) throws IllegalArgumentException {
        super(id, points, corner0, corner1, corner2, corner3, back0, back1, back2, back3);
        switch (color) {
            case "R":
                this.kingdomType = CornerItem.FUNGI;
                break;
            case "G":
                this.kingdomType = CornerItem.PLANT;
                break;
            case "B":
                this.kingdomType = CornerItem.ANIMAL;
                break;
            case "P":
                this.kingdomType = CornerItem.INSECT;
                break;
            default:
                throw new IllegalArgumentException("Opzione non valida: " + color);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getType() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScoringType() {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CornerItem getKingdomsType() {
        return kingdomType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CornerItem getObject() {
        return CornerItem.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<CornerItem> getRequirements() {
        return new ArrayList<>(); // Resource cards have no requirements
    }

    /**
     * Prepares a textual representation of the card before drawing.
     *
     * @return ArrayList of strings representing the corners and additional card details.
     */
    public ArrayList<String> preDraw() {
        ArrayList<String> frontcorner = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!getFrontCorner(i).isVisible() && i == 0) {
                frontcorner.add("┌");
            } else if (!getFrontCorner(i).isVisible() && i == 1) {
                frontcorner.add("┐");
            } else if (!getFrontCorner(i).isVisible() && i == 2) {
                frontcorner.add("┘");
            } else if (!getFrontCorner(i).isVisible() && i == 3) {
                frontcorner.add("└");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.FUNGI)) {
                frontcorner.add("F");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.ANIMAL)) {
                frontcorner.add("A");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.INSECT)) {
                frontcorner.add("I");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.PLANT)) {
                frontcorner.add("P");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.MANUSCRIPT)) {
                frontcorner.add("M");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.EMPTY)) {
                frontcorner.add("O");
            } else if (getFrontCorner(i).getItem().equals(CornerItem.QUILL)) {
                frontcorner.add("Q");
            } else {
                frontcorner.add("B");
            }
        }
        switch (kingdomType) {
            case PLANT:
                frontcorner.add("P");
                break;
            case ANIMAL:
                frontcorner.add("A");
                break;
            case INSECT:
                frontcorner.add("I");
                break;
            case FUNGI:
                frontcorner.add("F");
                break;
        }
        return frontcorner;
    }

    /**
     * Draws the graphical representation of the card on the console.
     */
    public void drawCard() {
        ArrayList<String> frontcorner = preDraw();
        if (getPoints() == 0) {
            frontcorner.add("-");
        } else {
            frontcorner.add(Integer.toString(getPoints()));
        }
        System.out.println(frontcorner.get(0) + "---" + frontcorner.get(5) + "---" + frontcorner.get(1) + "      " + "O-------O");
        System.out.println("|       |      |   " + frontcorner.get(4) + "   |");
        System.out.println(frontcorner.get(3) + "-------" + frontcorner.get(2) + "      " + "O-------O");
        System.out.println("\n");
    }
}
