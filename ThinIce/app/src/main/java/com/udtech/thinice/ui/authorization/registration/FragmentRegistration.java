package com.udtech.thinice.ui.authorization.registration;

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

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class FragmentRegistration extends Fragment {
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    SmartTabLayout tabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        FragmentAdapterRegistration adapter = new FragmentAdapterRegistration(getChildFragmentManager(), Arrays.asList(new Fragment[]{new FragmentInfo(), new FragmentAccount()}));
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
    }
}
