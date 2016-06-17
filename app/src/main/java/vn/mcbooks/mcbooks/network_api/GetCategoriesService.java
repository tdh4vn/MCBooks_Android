package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import vn.mcbooks.mcbooks.model.GetCategoriesResult;

/**
 * Created by hungtran on 6/17/16.
 */
public interface GetCategoriesService {
    @GET("api/categories")
    Call<GetCategoriesResult> getCategories(@Header("Authorization") String token);
}
