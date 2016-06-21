package vn.mcbooks.mcbooks.model;

import java.io.Serializable;

/**
 * Created by hungtran on 6/21/16.
 */
public class RatingViewModel implements Serializable{
    private String id;
    private String name;
    private String comment;
    private int stars;
    private String avatar;

    public RatingViewModel() {
    }

    public RatingViewModel(String id, String name, String comment, int stars, String avatar) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.stars = stars;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
