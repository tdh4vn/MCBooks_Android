package vn.mcbooks.mcbooks.intef;





import android.support.v4.app.DialogFragment;

import vn.mcbooks.mcbooks.fragment.BaseFragment;

/**
 * Created by hungtran on 6/4/16.
 */
public interface IOpenFragment {
    void openFragment(BaseFragment fragment, boolean onBackstack);
    void openDialogFragment(DialogFragment fragment);
}
