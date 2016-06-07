package vn.mcbooks.mcbooks.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.fragment.BaseFragment;
import vn.mcbooks.mcbooks.fragment.HomeFragment;
import vn.mcbooks.mcbooks.fragment.ProfileFragment;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.model.LogoutSocialResult;
import vn.mcbooks.mcbooks.network_api.LogoutSocialService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    View.OnClickListener,
                    IOpenFragment{
    private String token;

    //-----bar layout
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtEmail;
    NavigationView navigationView;

    //-----bottom control
    private ImageButton btnHome;
    private ImageButton btnGift;
    private ImageButton btnQR;
    private ImageButton btnFavarite;
    private ImageButton btnProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initNavigationView();
    }

    private void initView(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .addToBackStack(HomeFragment.NAME)
                .commit();
        initBottomControl();
        View headerView = navigationView.getHeaderView(0);
        imgAvatar = (ImageView) headerView.findViewById(R.id.img_avatar);
        txtName = (TextView) headerView.findViewById(R.id.txt_name);
        txtEmail = (TextView) headerView.findViewById(R.id.txt_email);
        initMenuHeader();
    }

    private void initMenuHeader(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
        String urlImg = sharedPreferences.getString(LoginActivity.KEY_AVATAR, "");
        String strName = sharedPreferences.getString(LoginActivity.KEY_NAME, "MCBooks");
        String strEmail = sharedPreferences.getString(LoginActivity.KEY_EMAIL, "email@mcbooks.vn");
        token = sharedPreferences.getString(LoginActivity.KEY_TOKEN, "");
        txtName.setText(strName);
        txtEmail.setText(strEmail);
        if (urlImg.equals("")){
            Picasso.with(this).load("http://mcbooks.vn/images/blogo.png").transform(new CircleTransform()).into(imgAvatar);
        } else {
            Picasso.with(this).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
        }
    }

    private void initBottomControl(){
        btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(this);
        btnGift = (ImageButton) findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);
        btnQR = (ImageButton) findViewById(R.id.btn_QR);
        btnQR.setOnClickListener(this);
        btnFavarite = (ImageButton) findViewById(R.id.btn_favarite);
        btnFavarite.setOnClickListener(this);
        btnProfile = (ImageButton) findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(this);
        btnHome.setSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initNavigationView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
        String loginType = sharedPreferences.getString(LoginActivity.KEY_LOGIN_TYPE, "");
        if (loginType.equals(LoginActivity.FACEBOOK_TYPE)){
            if (AccessToken.getCurrentAccessToken() == null) {
                return;
            }
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                }
            }).executeAsync();
        } else {
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API).build();
            if (mGoogleApiClient.isConnected()){
                Log.d("OKE ROI","HEHE");
                mGoogleApiClient.disconnect();
            }
        }



        LogoutSocialService logoutSocialService
                = ServiceFactory.getInstance().createService(LogoutSocialService.class);
        Call<LogoutSocialResult> call = logoutSocialService.logoutSocial("Access_token " + token);
        call.enqueue(new Callback<LogoutSocialResult>() {
            @Override
            public void onResponse(Call<LogoutSocialResult> call, Response<LogoutSocialResult> response) {
                if (response.body().code.equals(Integer.valueOf(1))){
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(LoginActivity.KEY_ISLOGIN, false);
                    editor.apply();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LogoutSocialResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home:
                if (!btnHome.isSelected()){
                    btnHome.setSelected(true);
                    openFragment(new HomeFragment(), true);
                    btnGift.setSelected(false);
                    btnFavarite.setSelected(false);
                    btnProfile.setSelected(false);
                }

                break;
            case R.id.btn_gift:
                if (!btnGift.isSelected()){
                    //openFragment(new HomeFragment(), true);
                    btnGift.setSelected(true);
                    btnHome.setSelected(false);
                    btnFavarite.setSelected(false);
                    btnProfile.setSelected(false);
                }
                break;
            case R.id.btn_QR:
                btnHome.setSelected(false);
                btnGift.setSelected(false);
                btnFavarite.setSelected(false);
                btnProfile.setSelected(false);
                break;
            case R.id.btn_favarite:
                if (!btnFavarite.isSelected()){
                    btnFavarite.setSelected(true);
                    btnGift.setSelected(false);
                    btnHome.setSelected(false);
                    btnProfile.setSelected(false);
                }
                break;
            case R.id.btn_profile:
                if (!btnProfile.isSelected()){
                    btnProfile.setSelected(true);
                    openFragment(new ProfileFragment(), true);
                    btnGift.setSelected(false);
                    btnHome.setSelected(false);
                    btnFavarite.setSelected(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void openFragment(BaseFragment fragment, boolean onBackstack) {
        if (onBackstack){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(fragment.toString()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    //TODO
}
