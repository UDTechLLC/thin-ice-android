package com.udtech.thinice.ui.main;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.udtech.thinice.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatisticPage extends Fragment {
    private static final String TOTAL_VALUE = "total", DONE_VALUE = "value";
    @Bind(R.id.done)
    TextView done;
    @Bind(R.id.planned)
    TextView planned;

    public static Fragment getInstance(int total, int done) {
        Fragment fragment = new FragmentStatisticPage();
        Bundle bundle = new Bundle();
        bundle.putInt(TOTAL_VALUE, total);
        bundle.putInt(DONE_VALUE, done);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Deprecated
    public FragmentStatisticPage() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        ((DecoView) view.findViewById(R.id.graph)).addSeries(new SeriesItem.Builder(getResources().getColor(R.color.graphBase))
                .setRange(0, getArguments().getInt(TOTAL_VALUE), getArguments().getInt(TOTAL_VALUE))
                .setLineWidth((float) (0.06*width))
                .build());
        ((DecoView) view.findViewById(R.id.graph)).addSeries(new SeriesItem.Builder(getResources().getColor(R.color.graphState))
                .setRange(0, getArguments().getInt(TOTAL_VALUE), getArguments().getInt(DONE_VALUE))
                .setInterpolator(new DecelerateInterpolator())
                .setLineWidth((float) (0.06*width))
                .build());
        done.setText(getArguments().getInt(DONE_VALUE)+" hrs");
        planned.setText(getArguments().getInt(TOTAL_VALUE)+" hrs");
    }
}
