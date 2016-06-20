package vn.mcbooks.mcbooks.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.mcbooks.mcbooks.fragment.AudioFavoriteFragment;
import vn.mcbooks.mcbooks.fragment.FavoriteBooksFragment;
import vn.mcbooks.mcbooks.fragment.VideoFavoriteFragment;

/**
 * Created by hungtran on 6/17/16.
 */
public class FavoriteViewPagerAdapter extends FragmentStatePagerAdapter {
    public FavoriteViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FavoriteBooksFragment tab1 = new FavoriteBooksFragment();
                return tab1;
            case 1:
                AudioFavoriteFragment tab2 = new AudioFavoriteFragment();
                return tab2;
            case 2:
                VideoFavoriteFragment tab3 = new VideoFavoriteFragment();
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
