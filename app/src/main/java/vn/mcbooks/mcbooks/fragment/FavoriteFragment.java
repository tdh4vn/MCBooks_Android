package vn.mcbooks.mcbooks.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.adapter.FavoriteViewPagerAdapter;
import vn.mcbooks.mcbooks.intef.IBottomNavigationController;
import vn.mcbooks.mcbooks.intef.ITabLayoutManager;
import vn.mcbooks.mcbooks.intef.IToolBarController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseFragment {

    //ITabLayoutManager tabLayoutManager;
    ViewPager viewPager;
    TabLayout tabLayout;

    public static FavoriteFragment create(){
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        return favoriteFragment;
    }

    public FavoriteFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(rootView);
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
//        IBottomNavigationController bottomNavigationController = (IBottomNavigationController) getActivity();
//        bottomNavigationController.setCurrentOfBottomNavigation(3);
        IToolBarController toolBarController = (IToolBarController)getActivity();
        toolBarController.setVisibilityForTitles(View.GONE);
        toolBarController.changeTitles("");
        toolBarController.setVisibilityForLogo(View.VISIBLE);
    }

    private void initView(View rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FavoriteViewPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
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
}
