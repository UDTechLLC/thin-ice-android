package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.udtech.thinice.utils.AchievementManager;
import com.udtech.thinice.R;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.adapters.FragmentAdapterStatistics;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatistics extends Fragment {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    SmartTabLayout tabs;
    private Fragment week, twoWeeks, month, allTime;
    private MenuHolder holder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AchievementManager.getInstance(getContext()).statisticsOpened(getContext());
        ButterKnife.bind(this, view);
        week = FragmentStatisticPage.getInstance(100, 80);
        twoWeeks = FragmentStatisticPage.getInstance(200, 180);
        month = FragmentStatisticPage.getInstance(400, 280);
        allTime = FragmentStatisticPage.getInstance(3000, 2056);
        FragmentAdapterStatistics adapter = new FragmentAdapterStatistics(getChildFragmentManager(), Arrays.asList(new Fragment[]{week, twoWeeks, month, allTime}));
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        holder.openPosition(MenuHolder.STATISTICS);
    }

    @OnClick(R.id.menu)
    void showMenu(){
        holder.show();
    }
}
