package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 6/4/16.
 *
 */


public class Result implements Serializable{
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("hot_books")
    @Expose
    private List<Book> hotBooks = new ArrayList<Book>();
    @SerializedName("new_books")
    @Expose
    private List<Book> newBooks = new ArrayList<Book>();
    @SerializedName("coming_books")
    @Expose
    private List<Book> comingBooks = new ArrayList<Book>();
    @SerializedName("news")
    @Expose
    private List<New> news = new ArrayList<New>();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Book> getHotBooks() {
        return hotBooks;
    }

    public void setHotBooks(List<Book> hotBooks) {
        this.hotBooks = hotBooks;
    }


    public List<Book> getNewBooks() {
        return newBooks;
    }

    public void setNewBooks(List<Book> newBooks) {
        this.newBooks = newBooks;
    }

    public List<Book> getComingBooks() {
        return comingBooks;
    }

    public void setComingBooks(List<Book> comingBooks) {
        this.comingBooks = comingBooks;
    }

    public List<New> getNews() {
        return news;
    }

    public void setNews(List<New> news) {
        this.news = news;
    }
}
