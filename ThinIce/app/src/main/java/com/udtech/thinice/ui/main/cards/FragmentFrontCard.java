package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentFrontCard extends ListView {
    GestureDetector gd;

    public FragmentFrontCard(Context context) {
        super(context);
        gd = new GestureDetector(getContext(),new YScrollDetector());
        setFadingEdgeLength(0);
        setDividerHeight(0);
        setDivider(null);
        setAdapter(CardsAdapter.getInstance(getContext()));
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && gd.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) /2> Math.abs(distanceX);
        }
    }
}
