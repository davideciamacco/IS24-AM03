package it.polimi.ingsw.is24am03.server.model.cards;

import it.polimi.ingsw.is24am03.server.model.enums.CornerItem;
import javafx.util.Pair;
import java.util.ArrayList;

/**
 * Abstract class representing a playable card in the game.
 * Extends the base class Card and provides methods to manipulate corners and game state.
 */
public abstract class PlayableCard extends Card {

    /**
     * List of pairs representing the corners at the back of the card and their coverage state.
     */
    private final ArrayList<Pair<Corner, Boolean>> backCorners;

    /**
     * List of pairs representing the corners at the front of the card and their coverage state.
     */
    private final ArrayList<Pair<Corner, Boolean>> frontCorners;

    /**
     * Boolean flag indicating whether the card is currently facing up.
     */
    private boolean face;

    /**
     * Boolean flag indicating whether the card has already been placed on the game board.
     */
    private boolean cardAlreadyPlaced;

    /**
     * Constructor to initialize a playable card with specific corners at front and back.
     *
     * @param id Unique identifier of the card.
     * @param points Points associated with the card.
     * @param corner0 Corner 0 at the front of the card.
     * @param corner1 Corner 1 at the front of the card.
     * @param corner2 Corner 2 at the front of the card.
     * @param corner3 Corner 3 at the front of the card.
     * @param back0 Corner 0 at the back of the card.
     * @param back1 Corner 1 at the back of the card.
     * @param back2 Corner 2 at the back of the card.
     * @param back3 Corner 3 at the back of the card.
     */
    public PlayableCard(int id, int points, Corner corner0, Corner corner1, Corner corner2, Corner corner3,
                        Corner back0, Corner back1, Corner back2, Corner back3) {
        super(id, points);

        this.face = true;
        this.cardAlreadyPlaced = false;

        this.backCorners = new ArrayList<>();
        this.backCorners.add(new Pair<>(back0, false));
        this.backCorners.add(new Pair<>(back1, false));
        this.backCorners.add(new Pair<>(back2, false));
        this.backCorners.add(new Pair<>(back3, false));

        this.frontCorners = new ArrayList<>();
        this.frontCorners.add(new Pair<>(corner0, false));
        this.frontCorners.add(new Pair<>(corner1, false));
        this.frontCorners.add(new Pair<>(corner2, false));
        this.frontCorners.add(new Pair<>(corner3, false));
    }

    /**
     * Retrieves the corner at the front of the card by index.
     *
     * @param index Index of the corner (0 to 3).
     * @return Corner object at the specified index.
     */
    public Corner getFrontCorner(int index) {
        return frontCorners.get(index).getKey();
    }

    /**
     * Retrieves the corner at the back of the card by index.
     *
     * @param index Index of the corner (0 to 3).
     * @return Corner object at the specified index.
     */
    public Corner getBackCorner(int index) {
        return backCorners.get(index).getKey();
    }

    /**
     * Checks if the card is currently facing up.
     *
     * @return True if the card is facing up, false otherwise.
     */
    public boolean getFace() {
        return face;
    }

    /**
     * Flips the card, changing its face up/down status.
     */
    public void rotate() {
        face = !face;
    }

    /**
     * Sets the coverage state of a specific corner on the card.
     *
     * @param corner Index of the corner to set coverage for (0 to 3).
     */
    public void setCoverage(int corner) {
        if (face) {
            Pair<Corner, Boolean> pair = frontCorners.get(corner);
            frontCorners.set(corner, new Pair<>(pair.getKey(), true));
        } else {
            Pair<Corner, Boolean> pair = backCorners.get(corner);
            backCorners.set(corner, new Pair<>(pair.getKey(), true));
        }
    }

    /**
     * Marks the card as already placed on the game board.
     */
    public void setCardAlreadyPlaced() {
        cardAlreadyPlaced = true;
    }

    /**
     * Abstract method to get the type of the card.
     *
     * @return Integer representing the type of the card.
     */
    public abstract int getType();

    /**
     * Abstract method to get the main object associated with the card.
     *
     * @return CornerItem representing the main object.
     */
    public abstract CornerItem getObject();

    /**
     * Abstract method to get the scoring type of the card.
     *
     * @return Integer representing the scoring type.
     */
    public abstract int getScoringType();

    /**
     * Abstract method to get the kingdoms type associated with the card.
     *
     * @return CornerItem representing the kingdoms type.
     */
    public abstract CornerItem getKingdomsType();

    /**
     * Abstract method to get the list of requirements for the card.
     *
     * @return ArrayList of CornerItem representing the requirements.
     */
    public abstract ArrayList<CornerItem> getRequirements();

    /**
     * Checks if the card has already been placed on the game board.
     *
     * @return True if the card has been placed, false otherwise.
     */
    public boolean getCardAlreadyPlaced() {
        return cardAlreadyPlaced;
    }

    /**
     * For testing purposes only: Retrieves the list of back corners with their coverage states.
     *
     * @return ArrayList of Pair -Corner, Boolean- representing back corners and their coverage states.
     */
    public ArrayList<Pair<Corner, Boolean>> getBack() {
        return backCorners;
    }

    /**
     * For testing purposes only: Sets the coverage state of a specific front corner.
     *
     * @param cornerIndex Index of the front corner to set coverage for (0 to 3).
     * @param covered     Boolean indicating whether the corner is covered.
     */
    public void setCornerCoverage(int cornerIndex, boolean covered) {
        if (face) {
            Pair<Corner, Boolean> pair = frontCorners.get(cornerIndex);
            frontCorners.set(cornerIndex, new Pair<>(pair.getKey(), covered));
        } else {
            Pair<Corner, Boolean> pair = backCorners.get(cornerIndex);
            backCorners.set(cornerIndex, new Pair<>(pair.getKey(), covered));
        }
    }

    /**
     * For testing purposes only: Retrieves the list of front corners with their coverage states.
     *
     * @return ArrayList of Pair -Corner, Boolean- representing front corners and their coverage states.
     */
    public ArrayList<Pair<Corner, Boolean>> getFront() {
        return frontCorners;
    }
}
