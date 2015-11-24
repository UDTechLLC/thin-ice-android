package com.udtech.thinice.ui.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Sofi on 24.11.2015.
 */
public class CardsAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public CardsAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(toRealPosition(position));
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    int toRealPosition(int position) {
        int realCount = getCount();
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;

        return realPosition;
    }
}