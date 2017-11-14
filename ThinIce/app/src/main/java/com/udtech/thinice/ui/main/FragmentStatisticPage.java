package com.udtech.thinice.ui.main;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.ClearStatistics;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.protocol.CaloryesUtils;
import com.udtech.thinice.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatisticPage extends Fragment {
    private static final String TOTAL_VALUE = "total", DONE_VALUE = "value", TIME = "time";
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.planned)
    TextView planned;
    @BindView(R.id.calories)
    TextView calories;
    @BindView(R.id.avarage)
    TextView avarage;
    private Long total, value, time;

    @Deprecated
    public FragmentStatisticPage() {
        super();
    }

    public static Fragment getInstance(int total, int done, long time) {
        Fragment fragment = new FragmentStatisticPage();
        Bundle bundle = new Bundle();
        bundle.putInt(TOTAL_VALUE, total);
        bundle.putInt(DONE_VALUE, done);
        bundle.putLong(TIME, time);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        if (total == null)
            total = Long.valueOf(getArguments().getInt(TOTAL_VALUE));
        if (value == null)
            value = Long.valueOf(getArguments().getInt(DONE_VALUE));
        if (time == null)
            time = getArguments().getLong(TIME);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        ((DecoView) view.findViewById(R.id.graph)).addSeries(new SeriesItem.Builder(getResources().getColor(R.color.graphBase))
                .setRange(0, getArguments().getInt(TOTAL_VALUE) == 0 ? SessionManager.getManager().getTargetCalories() : getArguments().getInt(TOTAL_VALUE), getArguments().getInt(TOTAL_VALUE) == 0 ? SessionManager.getManager().getTargetCalories() : getArguments().getInt(TOTAL_VALUE))
                .setLineWidth((float) (0.06 * width))
                .build());
        final SeriesItem item = new SeriesItem.Builder(getResources().getColor(R.color.graphState))
                .setRange(0, getArguments().getInt(TOTAL_VALUE) == 0 ? SessionManager.getManager().getTargetCalories() : getArguments().getInt(TOTAL_VALUE), 0)
                .setInterpolator(new AccelerateInterpolator())
                .setSpinDuration(3000)
                .setLineWidth((float) (0.06 * width))
                .build();
        item.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - item.getMinValue()) / (item.getMaxValue() - item.getMinValue()));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
        ((DecoView) view.findViewById(R.id.graph)).addSeries(item);
        initView();
    }

    private void initView() {
        done.setText(value + " cal");
        planned.setText(Math.round(total == 0 ? SessionManager.getManager().getTargetCalories() : total) + " cal");
        calories.setText((time / 3600000) + ":" + (time / 60000<10?("0"+time / 60000):(time / 60000)) );
        Settings settings = new Settings().fetch(getActivity());
        avarage.setText((Math.round(settings.isTemperature() ?
                Settings.convertTemperatureToFaringeite(Math.round(CaloryesUtils.getAvarageTemp(time, (int) (long) value))) :
                Math.round(CaloryesUtils.getAvarageTemp(time, (int) (long) value))))
                + (settings.isTemperature() ? "°F" : "°C"));

    }

    public float getRatio() {
        return value == null || total == null ? 0 : (float) value > total ? total : value;
    }

    public void addEvent(final DecoEvent event) {
        if (getView() != null) ((DecoView) getView().findViewById(R.id.graph)).addEvent(event);
        else new Thread(new Runnable() {
            @Override
            public void run() {
                while (getView() == null)
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((DecoView) getView().findViewById(R.id.graph)).addEvent(event);
                    }
                });
            }
        }).start();
    }

    public void onEvent(ClearStatistics event) {
        addEvent(new DecoEvent.Builder(0f)
                .setIndex(1)
                .setDuration(1000)
                .build());
        total = Long.valueOf(0);
        value = Long.valueOf(0);
        time = Long.valueOf(0);
        initView();
    }
}
