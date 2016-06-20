package vn.mcbooks.mcbooks.intef;

import android.support.design.widget.TabLayout;

/**
 * Created by hungtran on 6/17/16.
 */
public interface ITabLayoutManager {
    TabLayout getTabLayout();
    void setCurrentTab(int position);
}
