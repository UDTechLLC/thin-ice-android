package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.udtech.thinice.R;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatisticPage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DecoView) view.findViewById(R.id.graph)).addSeries(new SeriesItem.Builder(getResources().getColor(R.color.graphBase))
                .setRange(0, 150, 150)
                .build());
        ((DecoView) view.findViewById(R.id.graph)).addSeries(new SeriesItem.Builder(getResources().getColor(R.color.graphState))
                .setRange(0, 150, 70)
                .setInterpolator(new DecelerateInterpolator())
                .build());
    }
}
