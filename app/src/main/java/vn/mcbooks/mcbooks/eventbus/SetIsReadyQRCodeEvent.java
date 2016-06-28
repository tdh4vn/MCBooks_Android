package vn.mcbooks.mcbooks.eventbus;

/**
 * Created by hungtran on 6/22/16.
 */
public class SetIsReadyQRCodeEvent {
    private boolean isReady;

    public SetIsReadyQRCodeEvent(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
