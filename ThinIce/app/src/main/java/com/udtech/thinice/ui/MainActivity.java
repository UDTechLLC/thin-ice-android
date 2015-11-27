package com.udtech.thinice.ui;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.udtech.thinice.R;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.ui.main.FragmentDashBoard;
import com.udtech.thinice.ui.main.FragmentSettings;
import com.udtech.thinice.ui.main.FragmentStatistics;
import com.udtech.thinice.ui.main.MenuHolder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Sofi on 16.11.2015.
 */
public class MainActivity extends SlidingFragmentActivity implements MenuHolder {
    private LinearLayout menu;
    private int openedMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu = (LinearLayout) getLayoutInflater().inflate(R.layout.menu, null, false);
        setBehindContentView(menu);
        SlidingMenu sm = getSlidingMenu();
        sm.setFadeDegree(0.35f);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        sm.setBehindWidth((int) (width * 0.4));
        initMenu(menu);
        try {
            checkDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openPosition(int position) {
        resetMenuSelection(menu);
        selectMenuItem(menu, position);
    }

    @Override
    public void show() {
        showMenu();
    }

    private void initMenu(final LinearLayout menu) {
        int ii = 0;
        for (int i = 0; i < menu.getChildCount(); i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                final int finalIi = ii;
                menu.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenuItem(finalIi);
                    }
                });
                ii++;
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).commit();
    }

    private void openMenuItem(int position) {
        if (position != openedMenuItem)
            switch (position) {
                case MenuHolder.SETTINGS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentSettings()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.STATISTICS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentStatistics()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.DASHBOARD: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.CONTROL: {
                    break;
                }
                case MenuHolder.ACHIEVEMENTS: {
                    break;
                }
                case MenuHolder.ACCOUNT: {
                    break;
                }
            }
        toggle();
    }

    private void resetMenuSelection(LinearLayout menu) {
        for (int i = 0; i < menu.getChildCount(); i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                deselectMenuItem((FrameLayout) menu.getChildAt(i));
            }
        }
    }

    private void deselectMenuItem(FrameLayout item) {
        TextView itemText = (TextView) (((LinearLayout) item.getChildAt(0)).getChildAt(1));
        itemText.setTextColor(getResources().getColor(R.color.textViewColor));
        item.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }

    private void selectMenuItem(LinearLayout menu, int position) {
        openedMenuItem = position;
        FrameLayout item = null;
        for (int i = 0, ii = 0; i < menu.getChildCount() && ii <= position; i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                if (ii++ == position) {
                    item = (FrameLayout) menu.getChildAt(i);
                    break;
                }
            }
        }
        if (item != null) {
            TextView itemText = (TextView) (((LinearLayout) item.getChildAt(0)).getChildAt(1));
            itemText.setTextColor(getResources().getColor(R.color.colorAccent));
            item.setBackgroundColor(Color.argb(64, 0, 0, 0));
        }
    }
    private void checkDay() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Iterator<Day> dayList =  Day.findAll(Day.class);
        boolean created = false;
        while (dayList.hasNext()) {
            Day day = dayList.next();
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTime(day.getDate());
            if((dayCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)&&(dayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)))){
                created = true;
                break;
            }
        }
        if(!created)
            new Day().save();
    }

}
