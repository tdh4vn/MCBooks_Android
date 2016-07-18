package vn.mcbooks.mcbooks.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.HomeActivity;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.IReloadUserLayout;
import vn.mcbooks.mcbooks.intef.IToolBarController;
import vn.mcbooks.mcbooks.model.BaseResult;
import vn.mcbooks.mcbooks.model.ChangeAvatarResult;
import vn.mcbooks.mcbooks.network_api.APIURL;
import vn.mcbooks.mcbooks.network_api.ServiceFactory;
import vn.mcbooks.mcbooks.network_api.UserServices;
import vn.mcbooks.mcbooks.singleton.ContentManager;
import vn.mcbooks.mcbooks.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    public static final int PICK_IMAGE = 65659;
    private ImageView imgAvatar;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnSubmit;
    private ProgressDialog progressDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        EventBus.getDefault().post(new SetBottomBarPosition(4, true));
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
        createDialog();
        return v;
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.change_avatar_string));
        progressDialog.setMessage(getString(R.string.uploading_avatar));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
                if (!TextUtils.isEmpty(edtName.getText()) || !TextUtils.isEmpty(edtPhone.getText())){
                    UserServices userServices = ServiceFactory.getInstance().createService(UserServices.class);
                    Log.d("User Update", edtPhone.getText().toString() + " - " + edtName.getText().toString());
                    Call<BaseResult> call = userServices.updateNewProfile(StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            edtName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString());
                    call.enqueue(new Callback<BaseResult>() {
                        @Override
                        public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                            if (response.body().getCode() == 1){
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, LoginActivity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(LoginActivity.KEY_ISLOGIN, true);
                                editor.putString(LoginActivity.KEY_NAME, edtName.getText().toString());
                                editor.putString(LoginActivity.KEY_PHONE, edtPhone.getText().toString());
                                ContentManager.getInstance().getUser().setUsername(edtName.getText().toString());
                                ContentManager.getInstance().getUser().setMobilePhone(edtPhone.getText().toString());
                                editor.apply();
                                showToast("Cập nhật thành công!", Toast.LENGTH_LONG);
                                ((IReloadUserLayout)getActivity()).reloadUserLayout();
                            } else {
                                showToast("Cập nhật thất bại!", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResult> call, Throwable t) {
                            showToast("Cập nhật thất bại!", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    showToast("Vui lòng điền đẩy đủ thông tin!", Toast.LENGTH_LONG);
                }
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 512);
                intent.putExtra("outputY", 512);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                getActivity().startActivityForResult(intent, 123);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == AppCompatActivity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                File file = new File(getActivity().getCacheDir(), "avatar.jpg");
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                assert inputStream != null;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                inputStream.close();
                progressDialog.show();
                //create body by image
                RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part bodyImage =
                        MultipartBody.Part.createFormData("avatar", file.getName(), image);
                UserServices userServices = ServiceFactory.getInstance().createService(UserServices.class);
                Call<ChangeAvatarResult> call
                        = userServices.changeAvatar(
                            StringUtils.tokenBuild(ContentManager.getInstance().getToken()),
                            bodyImage);

                call.enqueue(new Callback<ChangeAvatarResult>() {
                    @Override
                    public void onResponse(Call<ChangeAvatarResult> call, Response<ChangeAvatarResult> response) {
                        if (response.body().getCode() == 1){
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, LoginActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(LoginActivity.KEY_AVATAR, "http://" + response.body().getAvatarURL());
                            ContentManager.getInstance().getUser().setAvatarURL("http://" + response.body().getAvatarURL());
                            editor.apply();
                            Picasso.with(getActivity()).load("http://" + response.body().getAvatarURL()).into(imgAvatar);
                            ((IReloadUserLayout)getActivity()).reloadUserLayout();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangeAvatarResult> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi! Vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                showToast("Có lỗi xảy ra! Vui lòng thử lại vào lúc khác", Toast.LENGTH_SHORT);
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        } else {
            Log.d("Image Dir", "ff2");
        }
    }

    private void initData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGIN_SHARE_PREFERENCE, getActivity().MODE_PRIVATE);
        String urlImg = sharedPreferences.getString(LoginActivity.KEY_AVATAR, "");
        String strName = sharedPreferences.getString(LoginActivity.KEY_NAME, "MCBooks");
        String strEmail = sharedPreferences.getString(LoginActivity.KEY_EMAIL, "email@mcbooks.vn");
        String phoneNumber = sharedPreferences.getString(LoginActivity.KEY_PHONE, "");
        edtName.setText(strName);
        edtEmail.setText(strEmail);
        edtPhone.setText(phoneNumber);
        if (urlImg.equals("")){
            Picasso.with(getActivity()).load(APIURL.BaseURL + "public/img/avatar_default.png").transform(new CircleTransform()).into(imgAvatar);
        } else {
            if(urlImg.indexOf("http") != -1){
                Picasso.with(getActivity()).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
            } else {
                Picasso.with(getActivity()).load(APIURL.BaseURL + urlImg).transform(new CircleTransform()).into(imgAvatar);
            }
        }
    }

}
