package vn.mcbooks.mcbooks.eventbus;

/**
 * Created by hungtran on 6/24/16.
 */
public class SetBottomBarPosition {
    private int position;

    public SetBottomBarPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
