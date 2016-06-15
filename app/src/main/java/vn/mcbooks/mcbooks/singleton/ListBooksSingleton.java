package vn.mcbooks.mcbooks.singleton;

import vn.mcbooks.mcbooks.model.Result;

/**
 * Created by hungtran on 6/11/16.
 */
public class ListBooksSingleton {
    private Result result;
    private static ListBooksSingleton ourInstance = new ListBooksSingleton();

    public static ListBooksSingleton getInstance() {
        return ourInstance;
    }

    private ListBooksSingleton() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
