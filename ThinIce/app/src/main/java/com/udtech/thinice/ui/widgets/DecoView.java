package com.udtech.thinice.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by JOkolot on 02.12.2015.
 */
public class DecoView extends com.hookedonplay.decoviewlib.DecoView {
    public DecoView(Context context) {
        super(context);
    }

    public DecoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DecoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),
                getMeasuredWidth()); //measure view by width
    }
}
