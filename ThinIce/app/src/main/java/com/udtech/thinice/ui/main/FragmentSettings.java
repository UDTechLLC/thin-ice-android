package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udtech.thinice.R;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.adapters.FragmentAdapterSettings;
import com.udtech.thinice.ui.widgets.TabsView;

import java.util.Arrays;

/**
 * Created by JOkolot on 27.11.2015.
 */
public class FragmentSettings extends Fragment {
    private MenuHolder holder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder.openPosition(MenuHolder.SETTINGS);
        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new FragmentDialogHelp();
                dialog.show(getChildFragmentManager(), null);
            }
        });
        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show();
            }
        });
        ((ViewPager) view.findViewById(R.id.viewPager)).setAdapter(new FragmentAdapterSettings(getChildFragmentManager(), Arrays.asList(new Fragment[]{new FragmentSettingsMeasurements(), new FragmentSettingsNotifications()})));
        ((TabsView) view.findViewById(R.id.tabs)).setViewPager(((ViewPager) view.findViewById(R.id.viewPager)));
    }
}
