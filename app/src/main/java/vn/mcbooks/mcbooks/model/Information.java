package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hungtran on 6/10/16.
 */

public class Information implements Serializable{

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("author")
    @Expose
    private String author;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
