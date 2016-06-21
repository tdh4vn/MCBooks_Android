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
    private MyRating myRating;

    public Float getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(Float avgStar) {
        this.avgStar = avgStar;
    }

    public MyRating getMyRating() {
        return myRating;
    }

    public void setMyRating(MyRating myRating) {
        this.myRating = myRating;
    }

}
