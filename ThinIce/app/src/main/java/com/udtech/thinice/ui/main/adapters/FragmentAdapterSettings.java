package com.udtech.thinice.ui.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by JOkolot on 27.11.2015.
 */
public class FragmentAdapterSettings extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public FragmentAdapterSettings(FragmentManager fm, List<Fragment> fragmentList) {
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
            case  0 :{return "Measurements";}
            case  1 :{return "Notifications";}
        }
        return "";
    }
}