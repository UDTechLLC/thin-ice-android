package com.udtech.thinice.ui.main.cards;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class CardGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 30;
    private static final int SWIPE_THRESHOLD_VELOCITY = 60;
    private CardEventListener listener;
    public CardGestureListener(CardEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            return false; // Bottom to top
        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            return false; // Top to bottom
        }
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            listener.reverseSwitchCards();
            return true; // Right to left
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            listener.switchCards();
            return true; // Right to left
        }

        return false;
    }

}
