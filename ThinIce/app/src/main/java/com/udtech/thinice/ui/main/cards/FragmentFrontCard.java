package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.Date;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentFrontCard extends ListView {
    //GestureDetector gd;
    private volatile static boolean isMoving = false;
    private volatile Date lastEvent;
    private volatile Point startPosition;

    public FragmentFrontCard(Context context) {
        super(context);
        setVerticalScrollBarEnabled(false);
        setFadingEdgeLength(0);
        setDividerHeight(0);
        setDivider(null);
        setAdapter(CardsAdapter.getInstance(getContext()));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (startPosition == null)
                        startPosition = new Point((int) event.getX(), (int) event.getY());
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Point endPoint = new Point((int) event.getX(), (int) event.getY());
                    int xDiff, yDiff;
                    if (startPosition == null)
                        startPosition = endPoint;
                    xDiff = Math.abs(startPosition.x - endPoint.x);
                    yDiff = Math.abs(startPosition.y - endPoint.y);
                    Log.d("Scrol defined as ", (Math.abs(startPosition.x - endPoint.x) > 100 ? Math.abs(startPosition.y - endPoint.y) > Math.abs(startPosition.x - endPoint.x) : false) ? "X" : "Y");
                    if (Math.abs(startPosition.x - endPoint.x) > 100 ? Math.abs(startPosition.y - endPoint.y) > Math.abs(startPosition.x - endPoint.x) : false) {
                        CardView card = (CardView) getChildAt(pointToPosition(endPoint.x, endPoint.y));
                        if (card != null)
                            if (startPosition.x - endPoint.x > 0)
                                card.reverseSwitchCards();
                            else
                                card.switchCards();
                        return true;

                    }
                    startPosition = null;
                }
                Log.d("Motion events", event.toString());
                isMoving = true;
                lastEvent = new Date();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1020);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (lastEvent.getTime() < new Date().getTime())
                            isMoving = false;
                    }
                }).start();
                return false;
            }
        });
    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        return super.onInterceptTouchEvent(ev) && gd.onTouchEvent(ev);
//    }
    public static boolean isMoving() {
        return isMoving;
    }
}
