package net.umaass_providers.app.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import net.umaass_providers.app.ui.FragmentBooksReviewers;
import net.umaass_providers.app.ui.FragmentRequests;
import net.umaass_providers.app.ui.FragmentBooksSubsequent;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentRequests();
            case 1:
                return new FragmentBooksSubsequent();
            case 2:
                return new FragmentBooksReviewers();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}