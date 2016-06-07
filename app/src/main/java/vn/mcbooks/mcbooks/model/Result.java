package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 6/4/16.
 */
public class Result {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("new_books")
    @Expose
    public List<Object> newBooks = new ArrayList<Object>();
    @SerializedName("news")
    @Expose
    public List<Object> news = new ArrayList<Object>();
}
