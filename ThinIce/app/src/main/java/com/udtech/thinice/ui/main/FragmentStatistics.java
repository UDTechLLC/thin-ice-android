package com.udtech.thinice.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

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
    private Iterator<Day> days;
    private int daysCount = 0;
    private boolean wasCleared = false;

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
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                long weekCal = 0, weekTotalCal = 0, weekTime = calendar.getTime().getTime(), twoWeekCal = 0, twoWeekTotalCal = 0,
                        twoWeekTime = calendar.getTime().getTime(), monthCal = 0, monthTotalCal = 0, monthTime = calendar.getTime().getTime(), totalCal = 0,
                        totalTotalCal = 0, totalTime = calendar.getTime().getTime();
                long correction = 0;
                List<Pair<Long, Long>> avgTempListWeek = new ArrayList<Pair<Long, Long>>();
                List<Pair<Long, Long>> avgTempListTwoWeek = new ArrayList<Pair<Long, Long>>();
                List<Pair<Long, Long>> avgTempListMonth = new ArrayList<Pair<Long, Long>>();
                List<Pair<Long, Long>> avgTempListTotal = new ArrayList<Pair<Long, Long>>();
                int count = 0;
                while (days.hasNext()) {
                    Day day = days.next();
                    if (day.getUser().getId() == UserSessionManager.getSession(getContext()).getId())
                        count++;
                }
                days = Day.findAll(Day.class);
                daysCount = 0;
                while (days.hasNext()) {
                    Day day = days.next();
                    daysCount++;
                    if (day.getUser().getId() == UserSessionManager.getSession(getContext()).getId()) {
                        Pair<Long, Long> data = day.getAVGTempWithTimeCoficient();
                        long dayTime = data.first;
                        long dayCal = (long) ((float) data.second);
                        calendar = new GregorianCalendar();
                        calendar.setTime(new Date(dayTime));
                        if (DateUtils.isSameWeek(new Date(), day.getDate())) {
                            weekTime += data.second * 1000;
                            Pair<Long, Long> avgTemp = day.getAVGTempWithTimeCoficient();
                            avgTempListWeek.add(avgTemp);
                            weekCal += day.getTotalDataForStatistics().second;
                        }
                        if (DateUtils.inTwoWeeks(new Date(), day.getDate())) {
                            twoWeekTime += data.second * 1000;
                            Pair<Long, Long> avgTemp = day.getAVGTempWithTimeCoficient();
                            avgTempListTwoWeek.add(avgTemp);
                            twoWeekCal += day.getTotalDataForStatistics().second;
                            avgTempListTwoWeek.add(avgTemp);
                        }
                        if (DateUtils.isSameMonth(new Date(), day.getDate())) {
                            monthTime += data.second * 1000;
                            Pair<Long, Long> avgTemp = day.getAVGTempWithTimeCoficient();
                            avgTempListMonth.add(avgTemp);
                            monthCal += day.getTotalDataForStatistics().second;
                            monthTotalCal += (long) SessionManager.getManager(getContext()).getTargetCalories();
                        }
                        totalTime += data.second * 1000;
                        Pair<Long, Long> avgTemp = day.getAVGTempWithTimeCoficient();
                        avgTempListTotal.add(avgTemp);
                        totalCal += day.getTotalDataForStatistics().second;
                        totalTotalCal += (long) SessionManager.getManager(getContext()).getTargetCalories();
                    }
                }
                days = Day.findAll(Day.class);
                calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                final long finalWeekTotalCal = 125 * 7;
                final long finalTwoWeekTotalCal = 125 * 14;
                final long finalWeekCal = weekCal;
                final long finalWeekTime = weekTime;
                final long finalTwoWeekTime = twoWeekTime;
                final long finalTwoWeekCal = twoWeekCal;
                final long finalMonthTime = monthTime;
                final long finalMonthCal = monthCal;
                final long finalMonthTotalCal = 125 * 30;
                int avgTempWeek = 0;
                int avgTempTwoWeek = 0;
                int avgTempMonth = 0;
                int avgTempTotal = 0;

                long tempSum = 0;
                long timeSum = 0;
                for (Pair<Long, Long> pair : avgTempListWeek) {
                    tempSum += pair.first * pair.second;
                    timeSum += pair.second;
                }
                if (timeSum != 0)
                    avgTempWeek = (int) (tempSum / timeSum);

                tempSum = 0;
                timeSum = 0;
                for (Pair<Long, Long> pair : avgTempListTwoWeek) {
                    tempSum += pair.first * pair.second;
                    timeSum += pair.second;
                }
                if (timeSum != 0)
                    avgTempTwoWeek = (int) (tempSum / timeSum);
                tempSum = 0;
                timeSum = 0;
                for (Pair<Long, Long> pair : avgTempListMonth) {
                    tempSum += pair.first * pair.second;
                    timeSum += pair.second;
                }
                if (timeSum != 0)
                    avgTempMonth = (int) (tempSum / timeSum);
                tempSum = 0;
                timeSum = 0;
                for (Pair<Long, Long> pair : avgTempListTotal) {
                    tempSum += pair.first * pair.second;
                    timeSum += pair.second;
                }
                if (timeSum != 0)
                    avgTempTotal = (int) (tempSum / timeSum);
                calendar.setTime(days.next().getDate());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                float diff = new Date().getTime() - calendar.getTime().getTime();
                diff /= 1000;
                diff /= 3600;
                diff /= 24;
                final long finalTotalTotalCal = count * 125;
                final long finalTotalCal = totalCal;
                final long finalTotalTime = totalTime;
                final int finalAvgTempWeek = avgTempWeek;
                final int finalAvgTempTwoWeek = avgTempTwoWeek;
                final int finalAvgTempTotal = avgTempTotal;
                final int finalAvgTempMonth = avgTempMonth;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        week = FragmentStatisticPage.getInstance((int) finalWeekTotalCal, (int) finalWeekCal, finalWeekTime, finalAvgTempWeek);
                        twoWeeks = FragmentStatisticPage.getInstance((int) finalTwoWeekTotalCal, (int) finalTwoWeekCal, finalTwoWeekTime, finalAvgTempTwoWeek);
                        month = FragmentStatisticPage.getInstance((int) finalMonthTotalCal, (int) finalMonthCal, finalMonthTime, finalAvgTempMonth);
                        allTime = FragmentStatisticPage.getInstance((int) finalTotalTotalCal, (int) finalTotalCal, finalTotalTime, finalAvgTempTotal);
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
                                if (wasCleared)
                                    prev.onEvent(new ClearStatistics());
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
                wasCleared = true;
            }
        });
        alert = builder.create();
        alert.show();

    }

}
