package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.GetRatingResult;
import vn.mcbooks.mcbooks.model.RatingBookResult;

/**
 * Created by hungtran on 6/21/16.
 */
public interface RatingServices {
    @FormUrlEncoded
    @POST("api/books/{book_id}/ratings")
    Call<RatingBookResult> ratingBookByID(@Header("Authorization") String token, @Path("book_id") String bookID, @Field("stars") int starts, @Field("comment") String comment);

    @GET("api/books/{book_id}/ratings")
    Call<GetRatingResult> getRatingBookByID(@Header("Authorization") String token, @Path("book_id") String bookID, @Query("page") int page);
}
