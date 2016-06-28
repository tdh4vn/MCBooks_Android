package vn.mcbooks.mcbooks.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.FavoriteViewPagerAdapter;
import vn.mcbooks.mcbooks.eventbus.SetBottomBarPosition;
import vn.mcbooks.mcbooks.intef.IToolBarController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseFragment {

    //ITabLayoutManager tabLayoutManager;
    ViewPager viewPager;
    TabLayout tabLayout;
    View rootView;
    private FavoriteViewPagerAdapter favoriteViewPagerAdapter;

    public static FavoriteFragment create(){
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        return favoriteFragment;
    }

    public FavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FavoriteFragment", "onStart");
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new SetBottomBarPosition(3));
        Log.d("FavoriteFragment", "onResume");
        //initView();
        favoriteViewPagerAdapter.notifyDataSetChanged();
        IToolBarController toolBarController = (IToolBarController)getActivity();
        toolBarController.setVisibilityForTitles(View.GONE);
        toolBarController.changeTitles("");
        toolBarController.setVisibilityForLogo(View.VISIBLE);
    }

    private void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        favoriteViewPagerAdapter = new FavoriteViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(favoriteViewPagerAdapter);
        controlViewPager();
    }

    void controlViewPager(){
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("FavoriteFragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FavoriteFragment", "onStop");
        //tabLayoutManager.getTabLayout().setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FavoriteFragment", "onDestroy");
    }
}
