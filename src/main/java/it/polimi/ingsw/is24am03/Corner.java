package it.polimi.ingsw.is24am03;

public class Corner {
    private boolean isVisible;
    private boolean isEmpty;
    private CornerItem item;
    private int position;

    public CornerItem getItem(){
        return item;
    }
    public boolean isVisible() {
        return isVisible;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
    public int getPosition() {
        return position;
    }
}
