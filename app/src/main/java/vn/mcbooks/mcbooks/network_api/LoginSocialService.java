package vn.mcbooks.mcbooks.network_api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import vn.mcbooks.mcbooks.model.LoginSocialResult;

/**
 * Created by hungtran on 6/1/16.
 */
public interface LoginSocialService {
    String LOGIN_SERVICE = "LOGIN_SEVICE";
    int FACEBOOK = 1;
    int GOOGLE = 2;
    @FormUrlEncoded
    @POST("api/login")
    Call<LoginSocialResult> loginFacebook(@Field("facebook_id") String facebook_id, @Field("name") String name, @Field("email") String email, @Field("avatar") String avatar);
    @FormUrlEncoded
    @POST("api/login")
    Call<LoginSocialResult> loginGoogle(@Field("google_id") String facebook_id, @Field("name") String name, @Field("email") String email, @Field("avatar") String avatar);
}
