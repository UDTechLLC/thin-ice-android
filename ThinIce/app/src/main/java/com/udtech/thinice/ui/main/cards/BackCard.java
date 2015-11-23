package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class BackCard extends View implements CardEventListener{
    private GestureDetector gdt;
    private final static String DAY_ID = "day_id";
    private Day day;
    public static View getInstance(Day day){
        Fragment fragment = new BackCard();
        Bundle bundle = new Bundle();
        bundle.putLong(DAY_ID,day.getId());
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Deprecated
    public BackCard() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_dashboard_day_back,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        day = SugarRecord.findById(Day.class, getArguments().getLong(DAY_ID,0));
        ((TextView) view.findViewById(R.id.food_edit)).setText(day.getJunkFood() + "");
        ((TextView) view.findViewById(R.id.water_edit)).setText(day.getWaterIntake() + "");
        ((TextView) view.findViewById(R.id.protein_edit)).setText(day.gethProteinMeals() + "");
        ((TextView) view.findViewById(R.id.sleep_edit)).setText(day.getHoursSlept() + "");
        ((TextView) view.findViewById(R.id.carb_edit)).setText(day.getCarbsConsumed() + "");
        ((TextView) view.findViewById(R.id.gym_edit)).setText(day.getGymHours() + "");
        gdt = new GestureDetector(getContext(), new CardGestureListener(this));
    }
    public void save() {
        day.setJunkFood(Integer.parseInt(((TextView) getView().findViewById(R.id.food_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.food_edit)).getText().toString()));
        day.setWaterIntake(Integer.parseInt(((TextView) getView().findViewById(R.id.water_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.water_edit)).getText().toString()));
        day.sethProteinMeals(Integer.parseInt(((TextView) getView().findViewById(R.id.protein_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.protein_edit)).getText().toString()));
        day.setHoursSlept(Integer.parseInt(((TextView) getView().findViewById(R.id.sleep_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.sleep_edit)).getText().toString()));
        day.setCarbsConsumed(Integer.parseInt(((TextView) getView().findViewById(R.id.carb_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.carb_edit)).getText().toString()));
        day.setGymHours(Integer.parseInt(((TextView) getView().findViewById(R.id.gym_edit)).getText().toString().equals("") ?
                "0" : ((TextView) getView().findViewById(R.id.gym_edit)).getText().toString()));
        View view = ((Activity) getContext()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) (getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        day.save();
    }

    @Override
    public void switchCards() {
        EventBus.getDefault().post(new ShowFrontCard());
    }

    @Override
    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowFrontCard(true));
    }
}
