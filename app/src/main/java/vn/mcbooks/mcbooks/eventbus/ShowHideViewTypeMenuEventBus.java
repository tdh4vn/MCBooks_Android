package vn.mcbooks.mcbooks.eventbus;

/**
 * Created by hungtran on 6/23/16.
 */
public class ShowHideViewTypeMenuEventBus {
    private boolean isShow;

    public ShowHideViewTypeMenuEventBus(boolean isReady) {
        this.isShow = isReady;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean ready) {
        isShow = ready;
    }
}
