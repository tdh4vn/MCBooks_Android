package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hungtran on 6/21/16.
 */
public class MyRating implements Serializable{
    @SerializedName("stars")
    @Expose
    private Float stars;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("create_at")
    @Expose
    private Long createAt;

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateAt() {
        return new Date(createAt);
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
