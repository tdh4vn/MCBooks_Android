package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 6/11/16.
 */
public class Ratings implements Serializable{
    @SerializedName("avg_star")
    @Expose
    private Float avgStar;
    @SerializedName("my_rating")
    @Expose
    private UserRating myRating;
    @SerializedName("rating_list")
    @Expose
    private List<UserRating> ratingList = new ArrayList<UserRating>();

    public Float getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(Float avgStar) {
        this.avgStar = avgStar;
    }

    public UserRating getMyRating() {
        return myRating;
    }

    public void setMyRating(UserRating myRating) {
        this.myRating = myRating;
    }


    public List<UserRating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<UserRating> ratingList) {
        this.ratingList = ratingList;
    }
}
