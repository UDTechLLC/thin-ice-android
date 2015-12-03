package com.udtech.thinice.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by JOkolot on 03.12.2015.
 */
public class ScaledImageView extends ImageView {
    public ScaledImageView(Context context) {
        super(context);
    }

    public ScaledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaledImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        setMeasuredDimension(getMaxWidth(), (int)(height/width)*getMaxWidth());
    }
}
