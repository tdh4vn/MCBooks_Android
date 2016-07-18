package vn.mcbooks.mcbooks.eventbus;

/**
 * Created by hungtran on 6/24/16.
 */
public class SetBottomBarPosition {
    private int position;
    private boolean isChangeFragment;

    public SetBottomBarPosition(int position, boolean isChangeFragment) {
        this.isChangeFragment = isChangeFragment;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isChangeFragment() {
        return isChangeFragment;
    }

    public void setChangeFragment(boolean changeFragment) {
        isChangeFragment = changeFragment;
    }
}
