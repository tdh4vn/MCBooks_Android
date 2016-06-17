package vn.mcbooks.mcbooks.singleton;

import vn.mcbooks.mcbooks.model.Result;

/**
 * Created by hungtran on 6/11/16.
 */
public class ContentManager {
    private String token;
    private Result result;
    private static ContentManager ourInstance = new ContentManager();

    public static ContentManager getInstance() {
        return ourInstance;
    }

    private ContentManager() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
