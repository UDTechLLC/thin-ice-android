package com.udtech.thinice.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.udtech.thinice.R;
import com.udtech.thinice.ui.main.FragmentStatistics;

/**
 * Created by Sofi on 16.11.2015.
 */
public class MainActivity extends SlidingFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu);
        SlidingMenu sm = getSlidingMenu();
        sm.setFadeDegree(0.35f);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        sm.setBehindWidth((int) (width*0.4));
        getSupportFragmentManager().beginTransaction().add(R.id.container,new FragmentStatistics()).commit();
    }
}
