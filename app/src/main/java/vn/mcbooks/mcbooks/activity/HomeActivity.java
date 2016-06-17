package vn.mcbooks.mcbooks.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.fragment.BaseFragment;
import vn.mcbooks.mcbooks.fragment.HomeFragment;
import vn.mcbooks.mcbooks.fragment.ProfileFragment;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.ILogout;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IReloadData;
import vn.mcbooks.mcbooks.model.Category;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.GetCategoriesResult;
import vn.mcbooks.mcbooks.model.LogoutSocialResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.GetCategoriesService;
import vn.mcbooks.mcbooks.network_api.LogoutSocialService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ListBooksSingleton;
import vn.mcbooks.mcbooks.utils.StringUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    View.OnClickListener,
                    IOpenFragment,
                    IReloadData,
                    ILogout{
    private String token;

    //-----bar layout
    private ArrayList<IReloadData.ILoadDataCompleteCallBack> listDataReloadCompletedCallBack;
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

    //------Data-----
    private Result loginDataResult;
    private boolean isReady = false;
    private boolean newBooksReady = false;
    private boolean hotBooksReady = false;
    private boolean comingBooksReady = false;
    ProgressDialog progressDialog;
    List<Category> categories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listDataReloadCompletedCallBack = new ArrayList<>();
        loginDataResult = (Result) getIntent().getExtras().getSerializable(LoginActivity.DATA);
        if (loginDataResult == null){
            loadBooks();
        } else {
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setDataLoginResult(loginDataResult);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .addToBackStack(HomeFragment.NAME)
                    .commit();
            isReady = true;
            newBooksReady = true;
            hotBooksReady = true;
            comingBooksReady = true;
        }
        ListBooksSingleton.getInstance().setResult(loginDataResult);
        initNavigationView();
        if (!isReady){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Đang tải....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        getCategories();
    }

    void getCategories(){
        GetCategoriesService getCategoriesService = ServiceFactory.getInstance().createService(GetCategoriesService.class);
        Call<GetCategoriesResult> call = getCategoriesService.getCategories(StringUtils.tokenBuild(token));
        Log.d("HungTD", "getCategories");
        call.enqueue(new Callback<GetCategoriesResult>() {
            @Override
            public void onResponse(Call<GetCategoriesResult> call, Response<GetCategoriesResult> response) {
                if (response.body().getCode() == 1){
                    categories = response.body().getResult();
                    Log.d("HungTD", categories.size() + "");
                    addCategoriesToMenu();
                } else {
                    Toast.makeText(HomeActivity.this, "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoriesResult> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCategoriesToMenu() {
        Log.d("HungTD", "AddCategories");
        if (navigationView != null){
            Log.d("HungTD", categories.size() + "");
            Menu menuCategories = navigationView.getMenu();
            for (int i = 0; i < categories.size(); i++){
                menuCategories.add(R.id.categories, Menu.NONE, i, categories.get(i).getName()).setIcon(R.drawable.ic_folder_open_black_24dp);
            }
        }
    }

    private void initView(){

        initBottomControl();
        View headerView = navigationView.getHeaderView(0);
        imgAvatar = (ImageView) headerView.findViewById(R.id.img_avatar);
        txtName = (TextView) headerView.findViewById(R.id.txt_name);
        txtEmail = (TextView) headerView.findViewById(R.id.txt_email);
        initMenuHeader();
    }

    private void loadBooks(){
        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(LoginActivity.KEY_TOKEN, "");
        loginDataResult = new Result();
        Call<GetBookResult> getHotBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), "hot", 1);
        getHotBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                loginDataResult.setHotBooks(response.body().getResult());
                hotBooksReady = true;
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null){
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ListBooksSingleton.getInstance().setResult(loginDataResult);
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack){
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
        Call<GetBookResult> getNewBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), "new", 1);
        getNewBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                loginDataResult.setNewBooks(response.body().getResult());
                newBooksReady = true;
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null){
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ListBooksSingleton.getInstance().setResult(loginDataResult);
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack){
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
        Call<GetBookResult> getComingBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), "coming", 1);
        getComingBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                loginDataResult.setComingBooks(response.body().getResult());
                comingBooksReady = true;
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null){
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ListBooksSingleton.getInstance().setResult(loginDataResult);
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack){
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
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
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 1) {
                this.finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }

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
        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void logout(){
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
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setiReloadData(this);
                    this.addCallBack(homeFragment);
                    openFragment(homeFragment, true);
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

    @Override
    public void openDialogFragment(DialogFragment fragment) {
        FragmentManager ft = getSupportFragmentManager();
        fragment.show(ft, "dialog");
    }

    @Override
    public void reloadData() {
        loadBooks();
    }

    @Override
    public void addCallBack(ILoadDataCompleteCallBack iLoadDataCompleteCallBack) {
        listDataReloadCompletedCallBack.add(iLoadDataCompleteCallBack);
    }
}
