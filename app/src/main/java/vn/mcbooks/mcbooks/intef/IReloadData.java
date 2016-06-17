package vn.mcbooks.mcbooks.intef;

/**
 * Created by hungtran on 6/15/16.
 */
public interface IReloadData {
    void reloadData();
    void addCallBack(ILoadDataCompleteCallBack iLoadDataCompleteCallBack);
    interface ILoadDataCompleteCallBack{
        void reloadDataComplete();
    }
}
