package com.udtech.thinice.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.udtech.thinice.R;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.widgets.CardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDashBoard extends Fragment{
    private MenuHolder holder;
    private Pair<TShirt,Insole> devices;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity)getActivity();
        devices = new Pair<>(new TShirt(), new Insole());
        devices.first.setCharge(88);
        devices.second.setCharge(28);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        holder.openPosition(MenuHolder.DASHBOARD);
        getChildFragmentManager().beginTransaction().add(R.id.device_container, FragmentDevices.getInstance(devices)).commit();
        initDays(view);
    }

    private void initDays(View view) {
        List<Day> days = new ArrayList<>();
        Day day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        day = new Day();
        day.setCarbsConsumed(10);
        day.setGymHours(8);
        day.setHoursSlept(25);
        day.sethProteinMeals(10);
        day.setWaterIntake(25);
        day.setJunkFood(555);
        days.add(day);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.day_container);
        for(Day day1: days){
            container.addView(new CardView(getContext(),day1));
        }
        container.getParent().requestDisallowInterceptTouchEvent(true);
    }

    @OnClick(R.id.menu)
    void showMenu(){
        holder.show();
    }
}
