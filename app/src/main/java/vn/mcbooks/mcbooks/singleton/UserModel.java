package vn.mcbooks.mcbooks.singleton;

import java.io.Serializable;

/**
 * Created by hungtran on 6/22/16.
 */
public class UserModel implements Serializable{
    private String username;
    private String email;
    private String avatarURL;
    private String mobilePhone;

    public UserModel() {
    }

    public UserModel(String username, String email, String avatarURL) {
        this.username = username;
        this.email = email;
        this.avatarURL = avatarURL;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
