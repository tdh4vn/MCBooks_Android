package vn.mcbooks.mcbooks.eventbus;

/**
 * Created by hungtran on 6/23/16.
 */
public class ListOrGridEventBus {
    public static final int GRID = 1;
    public static final int LIST = 2;
    private int viewType;

    public ListOrGridEventBus(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
