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
    private GestureDetector mGestureDetector;
    private volatile static boolean isMoving = false;
    public ChildFriendlyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            isMoving = true;
        }
        if(ev.getAction() == MotionEvent.ACTION_UP){
            isMoving = false;
        }
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
    public  static boolean isMoving(){
        return  isMoving;
    }
    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}