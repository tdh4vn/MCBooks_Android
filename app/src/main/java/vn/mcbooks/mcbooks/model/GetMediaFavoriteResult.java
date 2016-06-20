package vn.mcbooks.mcbooks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungtran on 6/18/16.
 */
public class GetMediaFavoriteResult {
    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<MediaInBook> result = new ArrayList<MediaInBook>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<MediaInBook> getResult() {
        return result;
    }

    public void setResult(List<MediaInBook> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
