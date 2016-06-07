

package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class LoginSocialResult {

    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("result")
    @Expose
    public Result result;

}


