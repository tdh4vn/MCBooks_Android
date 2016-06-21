package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.SearchResult;

/**
 * Created by hungtran on 6/22/16.
 */
public interface SearchServices {
    @GET("api/search")
    Call<SearchResult> searchBook(@Header("Authorization") String token, @Query("page") int page, @Query("keyword") String keyword);
}
