package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.utils.AchievementManager;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class BackCard extends FrameLayout implements CardEventListener {
    private Day day;
    final GestureDetector gdt;
    private Settings settings;
    private Point startPosition;
    private String[] values = new String[2];
    private TextView water, carbs;

    public BackCard(Context context) {
        super(context);
        gdt = new GestureDetector(context, new GestureListener());
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.addView(View.inflate(context, R.layout.item_dashboard_day_back, null));
        water = (TextView) findViewById(R.id.text_water);
        carbs = (TextView) findViewById(R.id.text_carbs);
        values[0] = (String) water.getText();
        values[1] = (String) carbs.getText();
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

        findViewById(R.id.save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                EventBus.getDefault().post(new ShowFrontCard());
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gdt.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    public void onEvent(Settings settings) {
        this.settings.fetch(getContext());
        updateView(this);
    }

    public void updateView(View view) {
        long now = System.currentTimeMillis();
        settings = new Settings().fetch(getContext());
        water.setText(values[0] + (settings.isVolume() ? "oz" : "ml"));
        carbs.setText(values[1] + "g");
        ((EditText) this.findViewById(R.id.water_edit)).setText(Math.round(settings.isVolume() ? day.getWaterIntake() / 28.3495 : day.getWaterIntake()) + "");
        ((EditText) this.findViewById(R.id.food_edit)).setText(day.getJunkFood() + "");
        ((EditText) this.findViewById(R.id.protein_edit)).setText(day.gethProteinMeals() + "");
        ((EditText) this.findViewById(R.id.sleep_edit)).setText(day.getHoursSlept() + "");
        ((EditText) this.findViewById(R.id.carb_edit)).setText(day.getCarbsConsumed() + "");
        ((EditText) this.findViewById(R.id.gym_edit)).setText(day.getGymHours() + "");

    }

    public void save() {
        day.setJunkFood(Integer.parseInt(((TextView) this.findViewById(R.id.food_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.food_edit)).getText().toString()));
        day.setWaterIntake(((TextView) this.findViewById(R.id.water_edit)).getText().toString().equals("") ?
                0 : (int) (Math.round(settings.isVolume() ? (Integer.parseInt(((TextView) this.findViewById(R.id.water_edit)).getText().toString()) * 30) :
                Integer.parseInt(((TextView) this.findViewById(R.id.water_edit)).getText().toString()))));
        day.sethProteinMeals(Integer.parseInt(((TextView) this.findViewById(R.id.protein_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.protein_edit)).getText().toString()));
        day.setHoursSlept(Integer.parseInt(((TextView) this.findViewById(R.id.sleep_edit)).getText().toString().equals("") ?
                "0" : ((TextView) this.findViewById(R.id.sleep_edit)).getText().toString()));
        day.setCarbsConsumed(((TextView) this.findViewById(R.id.carb_edit)).getText().toString().equals("") ?
                0 : Integer.parseInt(((TextView) this.findViewById(R.id.carb_edit)).getText().toString()));
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

    private class SwipeListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
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

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 30;
        private static final int SWIPE_THRESHOLD_VELOCITY = 60;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                reverseSwitchCards();
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                switchCards();
                return false; // Right to left
            }


            return true;
        }

    }
}
