package com.udtech.thinice.ui.authorization.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class FragmentAdapterRegistration extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public FragmentAdapterRegistration(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Information" : "Account";
    }
}
