package com.jokolot.closableview;

import android.view.View;

/**
 * Created by JOkolot on 01.03.2016.
 */
public abstract class ViewController<T>{
    private View closablePart;
    private float status;
    private View fullView;

    public ViewController(View view,int closablePartId) {
        fullView = view;
        closablePart = fullView.findViewById(closablePartId);
    }

    public abstract View setModelView(T model);
    public View getClosableView(){
        return  closablePart;
    }
    public View getFullView(){
        return fullView;
    }
    public float getStatus(){
        return status;
    }
    public void setStatus(float status, View parent){
        this.status = status;
        transformPage(parent,status);
    }

    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);
        if (position <= -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
            view.setAlpha(0);
        } else if (position < 1 && position > 0) { // [0,1]
            view.setAlpha(1);
            // Counteract the default slide transition
            //set Y position to swipe in from top
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);

        } else if (position < -0.5) {
            view.setAlpha(1);
            float scale = (float) (position + 1.66);
            float yPosition = ((scale - 1) * view.getHeight()) / 2;
            view.setScaleY(scale);
            view.setTranslationY(yPosition);
        } else if (position >= -0.5 && position < 0) {
            view.setAlpha(1);
        } else if (position >= 1) { // (1,+Infinity]
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}