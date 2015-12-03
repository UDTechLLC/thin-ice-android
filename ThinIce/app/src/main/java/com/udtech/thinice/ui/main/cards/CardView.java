package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowBackCard;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;
import com.wefika.flowlayout.FlowLayout;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class CardView extends FrameLayout {
    private View frontSide;
    private Day day;
    final GestureDetector gdt;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gdt = new GestureDetector(context, new GestureListener());
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
        frontSide = View.inflate(getContext(), R.layout.item_dashboard_day, null);
        frontSide.findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
            }
        });
        this.addView(frontSide);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }

    public void setDay(Day day) {
        this.day = day;
        checkTasks();
        ((TextView) findViewById(R.id.date)).setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.UK).format(day.getDate()));
        ((TextView) findViewById(R.id.day)).setText(DateUtils.getRelativeTimeSpanString(day.getDate().getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS));
    }

    private void checkTasks() {
        FlowLayout container = (FlowLayout) frontSide.findViewById(R.id.container_tasks);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int margins = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getContext().getResources().getDisplayMetrics());
        width -= margins;
        width = width / 4;
        container.removeAllViews();
        if (day.getGymHours() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((TextView) view.findViewById(R.id.description)).setText("Gym Session, hrs");
            ((TextView) view.findViewById(R.id.value)).setText(day.getGymHours() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(0, 246, 118));
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_gym));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
        if (day.getCarbsConsumed() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((TextView) view.findViewById(R.id.description)).setText("Carbs, g");
            ((TextView) view.findViewById(R.id.value)).setText(day.getCarbsConsumed() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(255, 111, 64));
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_carbohydrates));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
        if (day.getHoursSlept() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_sleep));
            ((TextView) view.findViewById(R.id.description)).setText("Hours Slept, hrs");
            ((TextView) view.findViewById(R.id.value)).setText(day.getHoursSlept() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(255, 215, 64));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
        if (day.gethProteinMeals() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_proteins));
            ((TextView) view.findViewById(R.id.description)).setText("H-protein Meals");
            ((TextView) view.findViewById(R.id.value)).setText(day.gethProteinMeals() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(178, 255, 89));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
        if (day.getJunkFood() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_food));
            ((TextView) view.findViewById(R.id.description)).setText("Junk Food, servings");
            ((TextView) view.findViewById(R.id.value)).setText(day.getJunkFood() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(100, 255, 218));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
        if (day.getWaterIntake() != 0) {
            View view = View.inflate(getContext(), R.layout.item_day_task, null);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.mipmap.ic_water));
            ((TextView) view.findViewById(R.id.description)).setText("Water Intake, ml");
            ((TextView) view.findViewById(R.id.value)).setText(day.getWaterIntake() + "");
            ((TextView) view.findViewById(R.id.value)).setTextColor(Color.rgb(0, 176, 255));
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.addView(view, params);
        }
    }

    public void switchCards() {
        EventBus.getDefault().post(new ShowBackCard(day));
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowBackCard(day, true));
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

    public void onEvent(ShowFrontCard event) {
        day = Day.findById(Day.class, day.getId());
        checkTasks();
    }
}
