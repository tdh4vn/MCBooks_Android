package vn.mcbooks.mcbooks.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import vn.mcbooks.mcbooks.activity.LoginActivity;
import vn.mcbooks.mcbooks.fragment.AudioFavoriteFragment;
import vn.mcbooks.mcbooks.fragment.FavoriteBooksFragment;
import vn.mcbooks.mcbooks.fragment.VideoFavoriteFragment;

/**
 * Created by hungtran on 6/17/16.
 */
public class FavoriteViewPagerAdapter extends FragmentPagerAdapter {

    private final FavoriteBooksFragment tab1 = new FavoriteBooksFragment();
    private final AudioFavoriteFragment tab2 = new AudioFavoriteFragment();
    private final VideoFavoriteFragment tab3 = new VideoFavoriteFragment();
    public FavoriteViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
