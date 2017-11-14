package com.udtech.thinice.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.ClearStatistics;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.adapters.FragmentAdapterStatistics;
import com.udtech.thinice.utils.AchievementManager;
import com.udtech.thinice.utils.DateUtils;
import com.udtech.thinice.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 16.11.2015.
 */
public class FragmentStatistics extends Fragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    SmartTabLayout tabs;
    private Fragment week, twoWeeks, month, allTime;
    private MenuHolder holder;
    private Iterator<Day> days;
    private int daysCount = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AchievementManager.getInstance(getContext()).statisticsOpened(getContext());
        ButterKnife.bind(this, view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (days == null || Day.count(Day.class, null, null) != daysCount)
                    days = Day.findAll(Day.class);
                long weekCal = 0, weekTotalCal = 0, weekTime = 0, twoWeekCal = 0, twoWeekTotalCal = 0,
                        twoWeekTime = 0, monthCal = 0, monthTotalCal = 0, monthTime = 0, totalCal = 0,
                        totalTotalCal = 0, totalTime = 0;
                long correction = 0;
                try {
                    correction = new SimpleDateFormat("HH mm ss").parse("00 00 00").getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                daysCount = 0;
                while (days.hasNext()) {
                    Day day = days.next();
                    daysCount++;
                    if (day.getUser().getId() == UserSessionManager.getSession(getContext()).getId()) {
                        Pair<Long, Long> data = day.getTotalDataForStatistics();
                        long dayTime = data.first;
                        long dayCal = data.second;
                        if (DateUtils.isSameWeek(new Date(), day.getDate())) {
                            weekCal += dayCal;
                            weekTotalCal += (long) SessionManager.getManager().getTargetCalories();
                            weekTime += dayTime - correction;
                        }
                        if (DateUtils.inTwoWeeks(new Date(), day.getDate())) {
                            twoWeekCal += dayCal;
                            twoWeekTotalCal += (long) SessionManager.getManager().getTargetCalories();
                            twoWeekTime += dayTime - correction;
                        }
                        if (DateUtils.isSameMonth(new Date(), day.getDate())) {
                            monthCal += dayCal;
                            monthTotalCal += (long) SessionManager.getManager().getTargetCalories();
                            monthTime += dayTime - correction;
                        }
                        totalCal += dayCal;
                        totalTotalCal += (long) SessionManager.getManager().getTargetCalories();
                        totalTime += dayTime - correction;
                    }
                }
                final long finalWeekTotalCal = weekTotalCal;
                final long finalTwoWeekTotalCal = twoWeekTotalCal;
                final long finalWeekCal = weekCal;
                final long finalWeekTime = weekTime;
                final long finalTwoWeekTime = twoWeekTime;
                final long finalTwoWeekCal = twoWeekCal;
                final long finalMonthTime = monthTime;
                final long finalMonthCal = monthCal;
                final long finalMonthTotalCal = monthTotalCal;
                final long finalTotalTotalCal = totalTotalCal;
                final long finalTotalCal = totalCal;
                final long finalTotalTime = totalTime;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        week = FragmentStatisticPage.getInstance((int) finalWeekTotalCal, (int) finalWeekCal, finalWeekTime);
                        twoWeeks = FragmentStatisticPage.getInstance((int) finalTwoWeekTotalCal, (int) finalTwoWeekCal, finalTwoWeekTime);
                        month = FragmentStatisticPage.getInstance((int) finalMonthTotalCal, (int) finalMonthCal, finalMonthTime);
                        allTime = FragmentStatisticPage.getInstance((int) finalTotalTotalCal, (int) finalTotalCal, finalTotalTime);
                        final FragmentAdapterStatistics adapter = new FragmentAdapterStatistics(getChildFragmentManager(), Arrays.asList(new Fragment[]{week, twoWeeks, month, allTime}));
                        viewPager.setAdapter(adapter);
                        ((FragmentStatisticPage) adapter.getItem(0)).addEvent(new DecoEvent.Builder(finalWeekCal < finalWeekTotalCal ? finalWeekCal : finalWeekTotalCal)
                                .setIndex(1)
                                .setDuration(1000)
                                .build());
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            FragmentStatisticPage prev;

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (prev != null)
                                    prev.addEvent(new DecoEvent.Builder(0f)
                                            .setIndex(1)
                                            .setDuration(1000)
                                            .build());
                                prev = (FragmentStatisticPage) adapter.getItem(position);
                                prev.addEvent(new DecoEvent.Builder(prev.getRatio())
                                        .setIndex(1)
                                        .setDuration(1000)
                                        .build());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        tabs.setViewPager(viewPager);
                    }
                });

            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        holder.openPosition(MenuHolder.STATISTICS);
    }

    @OnClick(R.id.menu)
    void showMenu() {
        holder.show();
    }

    private AlertDialog alert;

    @OnClick(R.id.action)
    public void clear() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Clear statistics data")
                .setMessage("Clear all statistics data?")
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
                Iterator<Day> days = Day.findAll(Day.class);
                while (days.hasNext()) {
                    Day day = days.next();
                    day.disableStatistics();
                }
                EventBus.getDefault().post(new ClearStatistics());
            }
        });
        alert = builder.create();
        alert.show();

    }

}
