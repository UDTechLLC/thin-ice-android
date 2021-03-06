package com.udtech.thinice.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by JOkolot on 20.11.2015.
 */
public class ChildFriendlyScrollView extends ScrollView {
    private volatile static boolean isMoving = false;
    private GestureDetector mGestureDetector;

    public ChildFriendlyScrollView(Context context) {
        super(context);
    }

    public ChildFriendlyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
        setVerticalScrollBarEnabled(false);
    }

    public static boolean isMoving() {
        return isMoving;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isMoving = true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            isMoving = false;
        }
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1 == null)
                e1 = e2;
            if (e2 == null)
                e2 = e1;
            return Math.abs(e1.getRawX() - e2.getRawX()) > 100 ? Math.abs(e1.getRawY() - e2.getRawY()) > Math.abs(e1.getRawX() - e2.getRawX()) : false;
            //if x scroll in lenght more than 100px, then check if horisontall scroll is bigger than vertical than return true
        }
    }
}