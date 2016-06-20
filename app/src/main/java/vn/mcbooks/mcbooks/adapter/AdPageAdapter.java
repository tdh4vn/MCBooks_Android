package vn.mcbooks.mcbooks.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.fragment.ImageViewFragment;


/**
 * Created by hungtran on 6/18/16.
 */
public class AdPageAdapter extends FragmentStatePagerAdapter {

    public AdPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ImageViewFragment adPageAdapter1 = new ImageViewFragment();
                adPageAdapter1.imageSRC = R.drawable.ad1;
                return adPageAdapter1;

            case 1:
                ImageViewFragment adPageAdapter2 = new ImageViewFragment();
                adPageAdapter2.imageSRC = R.drawable.ad2;
                return adPageAdapter2;

            case 2:
                ImageViewFragment adPageAdapter3 = new ImageViewFragment();
                adPageAdapter3.imageSRC = R.drawable.ad3;
                return adPageAdapter3;

            default:break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
