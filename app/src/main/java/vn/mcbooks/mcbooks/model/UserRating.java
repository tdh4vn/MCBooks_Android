package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hungtran on 6/11/16.
 */
public class UserRating implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("assessor")
    @Expose
    private Assessor assessor;
    @SerializedName("stars")
    @Expose
    private Integer stars;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("create_at")
    @Expose
    private Long createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Assessor getAssessor() {
        return assessor;
    }

    public void setAssessor(Assessor assessor) {
        this.assessor = assessor;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
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

    public class Assessor{
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("avatar")
        @Expose
        private String avatar;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

}

