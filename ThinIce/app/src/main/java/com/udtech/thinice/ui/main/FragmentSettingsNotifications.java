package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rey.material.widget.Switch;
import com.udtech.thinice.R;
import com.udtech.thinice.model.Notification;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JOkolot on 27.11.2015.
 */
public class FragmentSettingsNotifications extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View selection = view.findViewById(R.id.selection);
        final ViewGroup parent = ((ViewGroup) selection.getParent());
        view.findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 1;
                parent.addView(selection, lp);
                setTextColor(1);
                Notification.getInstance(getContext()).setTimer(1);
            }
        });
        view.findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 2;
                parent.addView(selection, lp);
                setTextColor(2);
                Notification.getInstance(getContext()).setTimer(2);
            }
        });
        view.findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 3;
                parent.addView(selection, lp);
                setTextColor(3);
                Notification.getInstance(getContext()).setTimer(3);
            }
        });
        view.findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 4;
                parent.addView(selection, lp);
                setTextColor(4);
                Notification.getInstance(getContext()).setTimer(4);
            }
        });
        ((Switch) view.findViewById(R.id.enable)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                Notification.getInstance(getContext()).setEnabled(checked);
            }
        });
        switch (Notification.getInstance(getContext()).getTimer()) {
            case 1: {
                view.findViewById(R.id.one).performClick();
                break;
            }
            case 2: {
                view.findViewById(R.id.two).performClick();
                break;
            }
            case 3: {
                view.findViewById(R.id.three).performClick();
                break;
            }
            case 4: {
                view.findViewById(R.id.four).performClick();
                break;
            }
        }
        ((Switch) view.findViewById(R.id.enable)).setChecked(Notification.getInstance(getContext()).isEnabled());
    }

    public void setTextColor(int count) {
        List<View> views = Arrays.asList(new View[]{getView().findViewById(R.id.one), getView().findViewById(R.id.two), getView().findViewById(R.id.three), getView().findViewById(R.id.four)});
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setAlpha(1f);
        }
        for (int i = count; i < views.size(); i++) {
            views.get(i).setAlpha(0.5f);
        }
    }
}
