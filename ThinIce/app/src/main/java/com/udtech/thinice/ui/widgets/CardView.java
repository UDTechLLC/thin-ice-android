package com.udtech.thinice.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class CardView extends FrameLayout {
    android.support.v7.widget.CardView frontSide;
    android.support.v7.widget.CardView backSide;
    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }
    private void initViews(){
        frontSide = new android.support.v7.widget.CardView(getContext());
        backSide = new android.support.v7.widget.CardView(getContext());
        this.addView(frontSide);
        frontSide.setRadius(5);
        backSide.setRadius(5);

    }
}
