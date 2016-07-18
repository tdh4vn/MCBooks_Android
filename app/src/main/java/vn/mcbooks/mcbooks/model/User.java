package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hungtran on 6/1/16.
 */
public class User {
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("documentation_url")
    @Expose
    public String documentationUrl;

}