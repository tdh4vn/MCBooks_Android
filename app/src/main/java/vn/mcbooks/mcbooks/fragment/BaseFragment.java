package vn.mcbooks.mcbooks.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by hungtran on 5/29/16.
 */
public class BaseFragment extends Fragment {
    public void showToast(String content, int duration){
        Toast.makeText(getActivity(), content, duration).show();
    }
}
