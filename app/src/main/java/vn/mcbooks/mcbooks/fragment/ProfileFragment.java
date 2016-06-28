package vn.mcbooks.mcbooks.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.HomeActivity;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.network_api.UserServices;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {


    private ImageView imgAvatar;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnSubmit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new SetBottomBarPosition(4));
        IToolBarController toolBarController = (IToolBarController)getActivity();
        toolBarController.setVisibilityForTitles(View.GONE);
        toolBarController.changeTitles("");
        toolBarController.setVisibilityForLogo(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(v);
        return v;
    }
    private void initView(View v){
        imgAvatar = (ImageView)v.findViewById(R.id.img_avatar);
        edtEmail = (EditText) v.findViewById(R.id.txtEmail);
        edtName = (EditText) v.findViewById(R.id.txtName);
        edtPhone = (EditText) v.findViewById(R.id.txtPhone);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserServices userServices = ServiceFactory.getInstance().createService(UserServices.class);
                Call<BaseResult> call = userServices.updateNewProfile(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                        edtName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString());
                call.enqueue(new Callback<BaseResult>() {
                    @Override
                    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                        if (response.body().getCode() == 1){
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, LoginActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(LoginActivity.KEY_ISLOGIN, true);
                            editor.putString(LoginActivity.KEY_EMAIL, edtEmail.getText().toString());
                            editor.putString(LoginActivity.KEY_NAME, edtName.getText().toString());
                            editor.apply();
                            showToast("Cập nhật thành công!", Toast.LENGTH_LONG);
                        } else {
                            showToast("Cập nhật thất bại!", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResult> call, Throwable t) {
                        showToast("Cập nhật thất bại!", Toast.LENGTH_LONG);
                    }
                });
            }
        });
        initData();
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, getActivity().MODE_PRIVATE);
        String urlImg = sharedPreferences.getString(LoginActivity.KEY_AVATAR, "");
        String strName = sharedPreferences.getString(LoginActivity.KEY_NAME, "MCBooks");
        String strEmail = sharedPreferences.getString(LoginActivity.KEY_EMAIL, "email@mcbooks.vn");
        edtName.setText(strName);
        edtEmail.setText(strEmail);
        if (urlImg.equals("")){
            Picasso.with(getActivity()).load(APIURL.BaseURL + "public/img/avatar_default.png").transform(new CircleTransform()).into(imgAvatar);
        } else {
            Picasso.with(getActivity()).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
        }
    }

}
