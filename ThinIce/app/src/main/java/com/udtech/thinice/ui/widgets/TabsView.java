package com.udtech.thinice.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Sofi on 16.11.2015.
 */
public class TabsView extends SmartTabLayout {
    public TabsView(Context context) {
        super(context);
    }

    public TabsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected TextView createDefaultTabView(CharSequence title) {
        TextView textView = super.createDefaultTabView(title);
        textView.setTypeface(Typeface.DEFAULT);
        return textView;
    }
}
