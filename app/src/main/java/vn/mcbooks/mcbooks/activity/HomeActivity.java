package vn.mcbooks.mcbooks.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
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
import vn.mcbooks.mcbooks.fragment.AboutMCBooksFragment;
import vn.mcbooks.mcbooks.fragment.BaseFragment;
import vn.mcbooks.mcbooks.fragment.FavoriteFragment;
import vn.mcbooks.mcbooks.fragment.HomeFragment;
import vn.mcbooks.mcbooks.fragment.MoreBooksFragment;
import vn.mcbooks.mcbooks.fragment.ProfileFragment;
import vn.mcbooks.mcbooks.fragment.QRCodeFragment;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.ILogout;
import vn.mcbooks.mcbooks.intef.IOpenFragment;
import vn.mcbooks.mcbooks.intef.IReloadData;
import vn.mcbooks.mcbooks.intef.ITabLayoutManager;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.model.Category;
import vn.mcbooks.mcbooks.model.GetBookResult;
import vn.mcbooks.mcbooks.model.GetCategoriesResult;
import vn.mcbooks.mcbooks.model.GetMediaFavoriteResult;
import vn.mcbooks.mcbooks.model.LogoutSocialResult;
import vn.mcbooks.mcbooks.model.Result;
import vn.mcbooks.mcbooks.network_api.FavoriteServices;
import vn.mcbooks.mcbooks.network_api.GetBookService;
import vn.mcbooks.mcbooks.network_api.GetCategoriesService;
import vn.mcbooks.mcbooks.network_api.LogoutSocialService;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        IOpenFragment,
        IReloadData,
        ILogout,
        IToolBarController,
        IBottomNavigationController {
    public static final int MENU_ABOUT = 0;
    public static final int MENU_LOGOUT = 1;
    public static final int MENU_CALL_MCBOOKS = 2;

    //------getbook from favorite
    int pageFavorite = 1;
    int pageMediaFavorite = 1;

    private String token;

    //-----bar layout

    private ArrayList<ILoadDataCompleteCallBack> listDataReloadCompletedCallBack;
    private ImageView imgAvatar;
    private TextView txtName;
    private TextView txtEmail;
    NavigationView navigationView;

    //-----bottom control
    AHBottomNavigation bottomNavigation;

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
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
        token = sharedPreferences.getString(LoginActivity.KEY_TOKEN, "");
        ContentManager.getInstance().setToken(token);
        getBooksInFavorite();
        getMediasInFavorite();
        setContentView(R.layout.activity_home);
        listDataReloadCompletedCallBack = new ArrayList<>();
        loginDataResult = (Result) getIntent().getExtras().getSerializable(LoginActivity.DATA);
        if (loginDataResult == null) {
            loadBooks();
        } else {
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setiReloadData(this);
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
        ContentManager.getInstance().setResult(loginDataResult);
        initNavigationView();
        if (!isReady) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang tải...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        getCategories();
        initView();
    }

    void getBooksInFavorite() {
        GetBookService getBookService = ServiceFactory.getInstance().createService(GetBookService.class);
        getBookInFavoriteByPage(getBookService);
    }


    void getBookInFavoriteByPage(final GetBookService getBookService) {
        Call<GetBookResult> getBookResultCall = getBookService.getBooksInFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), pageFavorite);
        getBookResultCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                if (response.body().getCode() != 1) {
                    Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else if (response.body().getResult().size() > 0) {
                    pageFavorite++;
                    ContentManager.getInstance().getListBookFavorite().addAll(response.body().getResult());
                    if (response.body().getResult().size() == 10) {
                        getBookInFavoriteByPage(getBookService);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getMediasInFavorite() {
        FavoriteServices favoriteServices = ServiceFactory.getInstance().createService(FavoriteServices.class);
        getMediaInFavoriteByPage(favoriteServices);
    }

    void getMediaInFavoriteByPage(final FavoriteServices favoriteServices) {
        Call<GetMediaFavoriteResult> getBookResultCall = favoriteServices.getMediaFavorite(StringUtils.tokenBuild(ContentManager.getInstance().getToken()), pageMediaFavorite);
        getBookResultCall.enqueue(new Callback<GetMediaFavoriteResult>() {
            @Override
            public void onResponse(Call<GetMediaFavoriteResult> call, Response<GetMediaFavoriteResult> response) {
                if (response.body().getCode() != 1) {
                    Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else if (response.body().getResult().size() > 0) {
                    pageMediaFavorite++;
                    ContentManager.getInstance().getListMediaFavorite().addAll(response.body().getResult());
                    if (response.body().getResult().size() == 10) {
                        getMediaInFavoriteByPage(favoriteServices);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMediaFavoriteResult> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Có lỗi xảy ra! Vui lòng khởi động lại ứng dụng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getCategories() {
        GetCategoriesService getCategoriesService = ServiceFactory.getInstance().createService(GetCategoriesService.class);
        Call<GetCategoriesResult> call = getCategoriesService.getCategories(StringUtils.tokenBuild(ContentManager.getInstance().getToken()));
        call.enqueue(new Callback<GetCategoriesResult>() {
            @Override
            public void onResponse(Call<GetCategoriesResult> call, Response<GetCategoriesResult> response) {
                if (response.body().getCode() == 1) {
                    categories = response.body().getResult();
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
        if (navigationView != null) {
            Menu menuCategories = navigationView.getMenu();
            for (int i = 0; i < categories.size(); i++) {
                menuCategories.add(R.id.categories, Menu.NONE, i, categories.get(i).getName()).setIcon(R.drawable.ic_folder_open_black_24dp);
            }
            menuCategories.add(R.id.suport, MENU_ABOUT, categories.size(),"Về MCBooks").setIcon(R.drawable.ic_share_black_24dp);
            menuCategories.add(R.id.suport, MENU_CALL_MCBOOKS, categories.size() + 1, "Gọi MCBooks").setIcon(R.drawable.ic_call_black_24dp);
            menuCategories.add(R.id.suport, MENU_LOGOUT, categories.size() + 2, "Đăng xuất").setIcon(R.drawable.ic_reply_black_24dp);
        }
    }

    private void initView() {
        //tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        View headerView = navigationView.getHeaderView(0);
        imgAvatar = (ImageView) headerView.findViewById(R.id.img_avatar);
        txtName = (TextView) headerView.findViewById(R.id.txt_name);
        txtEmail = (TextView) headerView.findViewById(R.id.txt_email);
        initMenuHeader();
        initBottomNavigation();
    }

    private void initBottomNavigation(){
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem homeButtonBottom = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home_white_24px, R.color.colorPrimary);
        AHBottomNavigationItem giftButtonBottom = new AHBottomNavigationItem(R.string.gift, R.drawable.ic_card_giftcard_white_24px, R.color.colorPrimary);
        AHBottomNavigationItem QRButtonBottom = new AHBottomNavigationItem(R.string.QR, R.mipmap.ic_qr_white, R.color.colorPrimary);
        AHBottomNavigationItem favoriteButtonBottom = new AHBottomNavigationItem(R.string.favorit, R.drawable.favorite_fill_white,R.color.colorPrimary);
        AHBottomNavigationItem aboutButtonBottom = new AHBottomNavigationItem(R.string.myaccount, R.drawable.ic_account_circle_white_24px,R.color.colorPrimary);

        bottomNavigation.addItem(homeButtonBottom);
        bottomNavigation.addItem(giftButtonBottom);
        bottomNavigation.addItem(QRButtonBottom);
        bottomNavigation.addItem(favoriteButtonBottom);
        bottomNavigation.addItem(aboutButtonBottom);

        bottomNavigation.setColored(true);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setForceTint(true);

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (true){
                    switch (position){
                        case 0:
                            getSupportActionBar().setDisplayShowTitleEnabled(true);
                            getSupportActionBar().setTitle("Danh sach sach");
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setiReloadData(HomeActivity.this);
                            addCallBack(homeFragment);
                            openFragment(homeFragment, true);

                            break;
                        case 1:
                            //tabLayout.setVisibility(View.GONE);
                            break;
                        case 2:
                            QRCodeFragment qrCodeFragment = new QRCodeFragment();
                            openFragment(qrCodeFragment, true);
                            break;
                        case 3:
                            Log.d("abc","abcdde");
                            //tabLayout.setVisibility(View.VISIBLE);
                            openFragment(FavoriteFragment.create(), true);

                            break;
                        case 4:
                            //tabLayout.setVisibility(View.GONE);
                            openFragment(new ProfileFragment(), true);

                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


    }

    private void loadBooks() {
        hotBooksReady = false;
        newBooksReady = false;
        comingBooksReady = false;
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
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null) {
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setiReloadData(HomeActivity.this);
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ContentManager.getInstance().setResult(loginDataResult);
                    Log.d("Size", listDataReloadCompletedCallBack.size() + "");
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack) {
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
        Call<GetBookResult> getNewBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), "new", 1);
        getNewBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                loginDataResult.setNewBooks(response.body().getResult());
                newBooksReady = true;
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null) {
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setiReloadData(HomeActivity.this);
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ContentManager.getInstance().setResult(loginDataResult);
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack) {
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
        Call<GetBookResult> getComingBookServiceCall = getBookService.getBooks(StringUtils.tokenBuild(token), "coming", 1);
        getComingBookServiceCall.enqueue(new Callback<GetBookResult>() {
            @Override
            public void onResponse(Call<GetBookResult> call, Response<GetBookResult> response) {
                loginDataResult.setComingBooks(response.body().getResult());
                comingBooksReady = true;
                if (hotBooksReady && newBooksReady && comingBooksReady && progressDialog != null) {
                    isReady = true;
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setiReloadData(HomeActivity.this);
                    homeFragment.setDataLoginResult(loginDataResult);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(HomeFragment.NAME)
                            .commit();
                    progressDialog.dismiss();
                    ContentManager.getInstance().setResult(loginDataResult);
                    for (ILoadDataCompleteCallBack iLoadDataCompleteCallBack : listDataReloadCompletedCallBack) {
                        iLoadDataCompleteCallBack.reloadDataComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMenuHeader() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, MODE_PRIVATE);
        String urlImg = sharedPreferences.getString(LoginActivity.KEY_AVATAR, "");
        String strName = sharedPreferences.getString(LoginActivity.KEY_NAME, "MCBooks");
        String strEmail = sharedPreferences.getString(LoginActivity.KEY_EMAIL, "email@mcbooks.vn");
        token = sharedPreferences.getString(LoginActivity.KEY_TOKEN, "");
        txtName.setText(strName);
        txtEmail.setText(strEmail);
        if (urlImg.equals("")) {
            Picasso.with(this).load("http://mcbooks.vn/images/blogo.png").transform(new CircleTransform()).into(imgAvatar);
        } else {
            Picasso.with(this).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
        }
    }

    private void initNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        String title = item.getTitle().toString();
        switch (id){
            case MENU_ABOUT:
                openFragment(new AboutMCBooksFragment(),true);
                break;
            case MENU_LOGOUT:
                logout();
                break;
            case MENU_CALL_MCBOOKS:
                callMCBooks();
                break;
            default:
                break;
        }
        for (Category category : categories) {
            if (category.getName().equals(title)) {
                MoreBooksFragment moreBooksFragment = new MoreBooksFragment();
                moreBooksFragment.titles = category.getName();
                moreBooksFragment.setIdCategory(category.getId());
                openFragment(moreBooksFragment, true);
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callMCBooks() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0437921466"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
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

    @Override
    public void changeTitles(String titles) {
        TextView textView = (TextView) findViewById(R.id.titleToolBar);
        if (textView != null){
            textView.setText(titles);
        }

    }

    @Override
    public void setVisibilityForLogo(int visibilityForLogo) {
        ImageView imageView = (ImageView) findViewById(R.id.logoToolBar);
        if (imageView != null) {
            imageView.setVisibility(visibilityForLogo);
        }
    }

    @Override
    public void setVisibilityForTitles(int visibilityForTitles) {
        TextView textView = (TextView) findViewById(R.id.titleToolBar);
        if (textView != null) {
            textView.setVisibility(visibilityForTitles);
        }
    }


    @Override
    public void setCurrentOfBottomNavigation(final int position) {
        bottomNavigation.post(new Runnable() {
            @Override
            public void run() {
                bottomNavigation.setCurrentItem(position);
            }
        });
    }
}
