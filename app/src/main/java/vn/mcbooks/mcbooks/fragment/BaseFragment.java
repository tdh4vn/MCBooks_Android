package vn.mcbooks.mcbooks.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import vn.mcbooks.mcbooks.intef.ILogout;

/**
 * Created by hungtran on 5/29/16.
 */
public class BaseFragment extends Fragment {
    private ILogout iLogout;

    public void setLogoutService(ILogout iLogout) {
        this.iLogout = iLogout;
    }

    public ILogout getLogoutService() {
        return iLogout;
    }
    public void showToast(String content, int duration){
        Toast.makeText(getActivity(), content, duration).show();
    }
}
