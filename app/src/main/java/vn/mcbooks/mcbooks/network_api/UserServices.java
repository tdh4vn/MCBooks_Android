package vn.mcbooks.mcbooks.network_api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.ChangeAvatarResult;

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
    @Multipart
    @PUT("api/me/avatar")
    Call<ChangeAvatarResult> changeAvatar(@Header("Authorization") String token,
                                          @Part MultipartBody.Part avatar);
}
