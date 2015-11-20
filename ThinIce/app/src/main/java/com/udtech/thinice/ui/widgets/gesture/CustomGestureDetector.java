package com.udtech.thinice.ui.widgets.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by JOkolot on 20.11.2015.
 */
public class CustomGestureDetector implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String TAG = "tst";

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1.getX() < e2.getX()) {
                Log.d(TAG, "Left to Right swipe performed");
            }

            if (e1.getX() > e2.getX()) {
                Log.d(TAG, "Right to Left swipe performed");
            }

            if (e1.getY() < e2.getY()) {
                Log.d(TAG, "Up to Down swipe performed");
            }

            if (e1.getY() > e2.getY()) {
                Log.d(TAG, "Down to Up swipe performed");
            }

            return true;
    }
}