package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.utils.AchievementManager;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class BackCard extends FrameLayout implements CardEventListener {
    private Day day;
    private Point startPosition;

    public BackCard(Context context) {
        super(context);
        this.addView(View.inflate(context, R.layout.item_dashboard_day_back, null));
        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ((Activity) getContext()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) (getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                EventBus.getDefault().post(new ShowFrontCard());
            }
        });
        this.setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.food_edit)).setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.water_edit)).setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.protein_edit)).setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.sleep_edit)).setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.carb_edit)).setOnTouchListener(new SwipeListener());
        ((TextView) findViewById(R.id.gym_edit)).setOnTouchListener(new SwipeListener());

        ((EditText) findViewById(R.id.food_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        ((EditText) findViewById(R.id.water_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        ((EditText) findViewById(R.id.protein_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        ((EditText) findViewById(R.id.sleep_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        ((EditText) findViewById(R.id.carb_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        ((EditText) findViewById(R.id.gym_edit)).setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        findViewById(R.id.save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                EventBus.getDefault().post(new ShowFrontCard());
            }
        });


    }

    public void updateView(View view) {
        long now = System.currentTimeMillis();
        ((TextView) view.findViewById(R.id.food_edit)).setText(day.getJunkFood() + "");
        ((TextView) view.findViewById(R.id.water_edit)).setText(day.getWaterIntake() + "");
        ((TextView) view.findViewById(R.id.protein_edit)).setText(day.gethProteinMeals() + "");
        ((TextView) view.findViewById(R.id.sleep_edit)).setText(day.getHoursSlept() + "");
        ((TextView) view.findViewById(R.id.carb_edit)).setText(day.getCarbsConsumed() + "");
        ((TextView) view.findViewById(R.id.gym_edit)).setText(day.getGymHours() + "");
    }

    public void save() {
        day.setJunkFood(Integer.parseInt(((TextView) this.findViewById(R.id.food_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.food_edit)).getText().toString()));
        day.setWaterIntake(Integer.parseInt(((TextView) this.findViewById(R.id.water_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.water_edit)).getText().toString()));
        day.sethProteinMeals(Integer.parseInt(((TextView) this.findViewById(R.id.protein_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.protein_edit)).getText().toString()));
        day.setHoursSlept(Integer.parseInt(((TextView) this.findViewById(R.id.sleep_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.sleep_edit)).getText().toString()));
        day.setCarbsConsumed(Integer.parseInt(((TextView) this.findViewById(R.id.carb_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.carb_edit)).getText().toString()));
        day.setGymHours(Integer.parseInt(((TextView) this.findViewById(R.id.gym_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.gym_edit)).getText().toString()));
        View view = ((Activity) getContext()).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) (getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        day.save();
        AchievementManager.getInstance(getContext()).dayChanged(getContext());
    }

    public void setDay(Day day) {
        this.day = day;
        updateView(this);
    }

    @Override
    public void switchCards() {
        EventBus.getDefault().post(new ShowFrontCard());
    }

    @Override
    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowFrontCard(true));
    }
    private class SwipeListener implements  OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) { if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (startPosition == null)
                startPosition = new Point((int) event.getX(), (int) event.getY());
        }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (startPosition != null) {
                    Point endPoint = new Point((int) event.getX(), (int) event.getY());
                    int xDiff, yDiff;
                    xDiff = Math.abs(startPosition.x - endPoint.x);
                    yDiff = Math.abs(startPosition.y - endPoint.y);
                    if (xDiff > yDiff) {
                        if (startPosition.x - endPoint.x > 0)
                            reverseSwitchCards();
                        else
                            switchCards();
                        return true;

                    }
                    startPosition = null;
                }
            }
            return false;
        }
    }
}
