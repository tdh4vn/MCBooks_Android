package vn.mcbooks.mcbooks.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.image_helper.CircleTransform;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.IToolBarController;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {


    private ImageView imgAvatar;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        IBottomNavigationController bottomNavigationController = (IBottomNavigationController) getActivity();
//        bottomNavigationController.setCurrentOfBottomNavigation(4);
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
            Picasso.with(getActivity()).load("http://mcbooks.vn/images/blogo.png").transform(new CircleTransform()).into(imgAvatar);
        } else {
            Picasso.with(getActivity()).load(urlImg).transform(new CircleTransform()).into(imgAvatar);
        }
    }

}
