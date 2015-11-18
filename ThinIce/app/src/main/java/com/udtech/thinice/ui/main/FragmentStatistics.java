package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.udtech.thinice.R;
import com.udtech.thinice.ui.authorization.adapters.FragmentAdapterRegistration;
import com.udtech.thinice.ui.main.adapters.FragmentAdapterStatistics;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatistics extends Fragment {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    SmartTabLayout tabs;
    private Fragment week, twoWeeks, month, allTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        week = new FragmentStatisticPage();
        twoWeeks = new FragmentStatisticPage();
        month = new FragmentStatisticPage();
        allTime = new FragmentStatisticPage();
        FragmentAdapterStatistics adapter = new FragmentAdapterStatistics(getChildFragmentManager(), Arrays.asList(new Fragment[]{week, twoWeeks, month,allTime}));
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
    }
}
