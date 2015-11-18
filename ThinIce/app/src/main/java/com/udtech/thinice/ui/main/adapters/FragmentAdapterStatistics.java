package com.udtech.thinice.ui.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentAdapterStatistics extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public FragmentAdapterStatistics(FragmentManager fm, List<Fragment> fragmentList) {
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
        switch(position){
            case  0 :{return "Week";}
            case  1 :{return "Two Weeks";}
            case  2 :{return "Month";}
            case  3 :{return "All Time";}
        }
        return "";
    }
}