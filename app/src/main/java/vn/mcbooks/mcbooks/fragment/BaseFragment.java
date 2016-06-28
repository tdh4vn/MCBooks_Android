package vn.mcbooks.mcbooks.fragment;

import android.drm.DrmErrorEvent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import vn.mcbooks.mcbooks.animation.CubeAnimation;
import vn.mcbooks.mcbooks.animation.MoveAnimation;
import vn.mcbooks.mcbooks.eventbus.ShowHideViewTypeMenuEventBus;
import vn.mcbooks.mcbooks.intef.ILogout;
import vn.mcbooks.mcbooks.intef.IToolBarController;

/**
 * Created by hungtran on 5/29/16.
 */
public class BaseFragment extends Fragment {
    private static final long DURATION = 300;
    private ILogout iLogout;
    public String titles = "";

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().post(new ShowHideViewTypeMenuEventBus(false));
    }

    public void setLogoutService(ILogout iLogout) {
        this.iLogout = iLogout;
    }

    public ILogout getLogoutService() {
        return iLogout;
    }
    public void showToast(String content, int duration){
        Toast.makeText(getActivity(), content, duration).show();
    }
    public void configToolBar() {
        if (!titles.equals("")){
            IToolBarController toolBarController = (IToolBarController)getActivity();
            toolBarController.changeTitles(titles);
            toolBarController.setVisibilityForLogo(View.GONE);
            toolBarController.setVisibilityForTitles(View.VISIBLE);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, DURATION);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, DURATION);
        }
    }
}
