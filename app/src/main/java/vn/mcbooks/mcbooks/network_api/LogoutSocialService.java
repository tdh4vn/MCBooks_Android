package vn.mcbooks.mcbooks.network_api;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import vn.mcbooks.mcbooks.model.LogoutSocialResult;

/**
 * Created by hungtran on 6/3/16.
 */
public interface LogoutSocialService {
    @GET("api/logout")
    Call<LogoutSocialResult> logoutSocial(@Header("Authorization") String authorization);
}
