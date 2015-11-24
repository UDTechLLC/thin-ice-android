package com.udtech.thinice.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.TouchedCard;
import com.udtech.thinice.eventbus.model.cards.UpdateCard;
import com.udtech.thinice.model.Day;
import com.wefika.flowlayout.FlowLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class CardView extends FrameLayout {
    private View frontSide;
    private Day day;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    EventBus.getDefault().post(new TouchedCard(day));
                return true;
            }
        });
        frontSide = View.inflate(getContext(), R.layout.item_dashboard_day, null);
        frontSide.findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void onEvent(UpdateCard event) {
        if (day.getId() == event.getDay().getId()) {
            setDay(event.getDay());
        }
    }


}
