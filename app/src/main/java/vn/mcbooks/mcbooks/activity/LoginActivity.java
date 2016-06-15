package vn.mcbooks.mcbooks.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.model.LoginSocialResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.model.User;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.network_api.LoginSocialService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    //CONSTANT
    public static String LOGIN_SHARE_PREFERENCE = "login";
    public static String KEY_ID = "KEY_ID";
    public static String KEY_EMAIL = "KEY_EMAIL";
    public static String KEY_AVATAR = "KEY_AVATAR";
    public static String KEY_NAME = "KEY_NAME";
    public static String KEY_ISLOGIN = "IS_LOGIN";
    public static String KEY_TOKEN = "KEY_TOKEN";
    public static String KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE";
    public static String FACEBOOK_TYPE = "FACE_BOOK";
    public static String GOOGLE_TYPE = "GOOGLE";
    public static String DATA = "DATA";




    private Result loginResultData;
    private LoginButton loginFacebookButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private SignInButton googleLogin;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    @Override
    protected void onStart() {
        super.onStart();
        loginResultData = new Result();
        boolean isLogin = getSharedPreferences(LOGIN_SHARE_PREFERENCE, MODE_PRIVATE).getBoolean(KEY_ISLOGIN, false);
        if (isLogin){
            Intent intent = new Intent(this, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(DATA, null);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //printHashKey();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
        initView();
    }
    /*
    //Kết nối các view
     */

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView(){
        //button login with facebook
        loginFacebookButton = (LoginButton) this.findViewById(R.id.login_facebook_button);
        loginWithFacebookInit();
        loginFacebookButton.setText("Login with Facebook");

        //butotn login with google +
        googleLogin = (SignInButton) this.findViewById(R.id.login_google_button);
        loginWithGoogleInit();

    }

    private void loginWithFacebookInit(){
        if(loginFacebookButton != null){
            loginFacebookButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email", "user_birthday", "user_friends"));
        }
        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                // Application code
                                try {
                                    login(object.getString("id"),
                                            object.getString("name"),
                                            object.getString("email"),
                                            "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large",
                                            LoginSocialService.FACEBOOK);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
            }
        });
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.d("TAG","Change Token Facebook");
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void login(final String id,final String name, final String email, final String avatar, final int type){
        LoginSocialService loginSocialService = ServiceFactory.getInstance().createService(LoginSocialService.class);
        if (type == LoginSocialService.FACEBOOK){
            Call<LoginSocialResult> call = loginSocialService.loginFacebook(id, name, email, avatar);
            call.enqueue(new Callback<LoginSocialResult>() {
                @Override
                public void onResponse(Call<LoginSocialResult> call, Response<LoginSocialResult> response) {
                    if (response.body().getCode() == 1){
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_ISLOGIN, true);
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_NAME, name);
                        editor.putString(KEY_AVATAR, avatar);
                        editor.putString(KEY_ID, id);
                        editor.putString(KEY_LOGIN_TYPE, FACEBOOK_TYPE);
                        editor.putString(KEY_TOKEN, response.body().getResult().getAccessToken());
                        editor.apply();
                        loginResultData = response.body().getResult();
                        loginSuccess();
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginSocialResult> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                }
            });
        } else { //google
            Call<LoginSocialResult> call = loginSocialService.loginGoogle(id, name, email, avatar);
            call.enqueue(new Callback<LoginSocialResult>() {
                @Override
                public void onResponse(Call<LoginSocialResult> call, Response<LoginSocialResult> response) {
                    if (response.body().getCode() == 1){
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(KEY_ISLOGIN, true);
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_NAME, name);
                        editor.putString(KEY_AVATAR, avatar);
                        editor.putString(KEY_ID, id);
                        editor.putString(KEY_LOGIN_TYPE, GOOGLE_TYPE);
                        editor.putString(KEY_TOKEN, response.body().getResult().getAccessToken());
                        editor.apply();
                        loginResultData = response.body().getResult();
                        loginSuccess();
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginSocialResult> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Login Fail!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void loginSuccess(){
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA, loginResultData);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    private void loginWithGoogleInit(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleLogin.setSize(SignInButton.SIZE_WIDE);
        googleLogin.setScopes(gso.getScopeArray());
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    private void displayMessage(Profile profile) { 
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct.getPhotoUrl() != null){
                login(acct.getId(), acct.getEmail(), acct.getDisplayName(), acct.getPhotoUrl().toString(), LoginSocialService.GOOGLE);
            } else {
                login(acct.getId(), acct.getEmail(), acct.getDisplayName(), "", LoginSocialService.GOOGLE);
            }
        } else {
            Toast.makeText(this, "Login Google Failed", Toast.LENGTH_LONG).show();
        }
    }
    /*
    //get hashKey for app
     */
    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "vn.mcbooks.mcbooks",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
