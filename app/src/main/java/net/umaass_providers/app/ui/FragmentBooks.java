package net.umaass_providers.app.ui;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import net.umaass_providers.app.R;
import net.umaass_providers.app.ui.adapter.PagerAdapter;
import net.umaass_providers.app.ui.base.BaseFragment;

public class FragmentBooks extends BaseFragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_books;
    }

    @Override
    public void readView() {
        super.readView();
        tabLayout = baseView.findViewById(R.id.tab_layout);
        viewPager = baseView.findViewById(R.id.pager);
    }

    @Override
    public void functionView() {
        super.functionView();
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Subsequent"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviewers"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
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
}
