package vn.mcbooks.mcbooks.network_api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import vn.mcbooks.mcbooks.model.BaseResult;

/**
 * Created by hungtran on 6/22/16.
 */
public interface UserServices {
    @FormUrlEncoded
    @PUT("api/me/about")
    Call<BaseResult> updateNewProfile(@Header("Authorization") String token,
                                      @Field("name") String name,
                                      @Field("email") String email,
                                      @Field("phone") String phone );
}
