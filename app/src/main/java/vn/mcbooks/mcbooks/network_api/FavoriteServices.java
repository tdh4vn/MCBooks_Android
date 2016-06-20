package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.GetMediaFavoriteResult;

/**
 * Created by hungtran on 6/17/16.api/favorite/medias
 */
public interface FavoriteServices {
    @POST("api/favorite/books/{bookid}")
    Call<BaseResult> addBookToFavorite(@Header("Authorization") String token, @Path("bookid") String bookID);
    @DELETE("api/favorite/books/{bookid}")
    Call<BaseResult> removeBookInFavorite(@Header("Authorization") String token, @Path("bookid") String bookID);
    @POST("api/favorite/medias/{mediaid}")
    Call<BaseResult> addMediaToFavorite(@Header("Authorization") String token, @Path("mediaid") String mediaID);
    @DELETE("api/favorite/medias/{mediaid}")
    Call<BaseResult> removeMediaInFavorite(@Header("Authorization") String token, @Path("mediaid") String mediaID);
    @GET("api/favorite/medias")
    Call<GetMediaFavoriteResult> getMediaFavorite(@Header("Authorization") String token, @Query("page") int page);
}
