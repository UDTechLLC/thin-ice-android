package com.jokolot.closableview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by JOkolot on 01.03.2016.
 */
public class ClosableListView extends FrameLayout {
    private int mCurrentItem = 0;
    private AbstractAdapter mAdapter;
    private ViewController mPrev, mCurrent, mNext;
    private float mLastYPosition;
    public ClosableListView(Context context) {
        super(context);
    }

    public ClosableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClosableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClosableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mAdapter!=null){
            if(ev.getAction() == MotionEvent.ACTION_MOVE){
                float yDiff = mLastYPosition - ev.getRawY();
                float currentScroll = mCurrent.getClosableView().getScrollY();

            }
            mLastYPosition = ev.getRawY();
        }
        return mAdapter!=null;
    }

    public void setAdapter(AbstractAdapter adapter) {
        mAdapter = adapter;
        selectView(0);
        initView();
    }

    private void initView(){
        removeAllViews();
        addView(mCurrent.getFullView());
        addView(mPrev.getFullView());
        addView(mNext.getFullView());
    }
    public void selectView(int position) {
        mCurrent = mAdapter.getView(position);
        mPrev = mAdapter.getPrev();
        mNext = mAdapter.getNext();
        setProgress(0);
    }

    public void setProgress(float progress) {
        mCurrent.setStatus(progress, this);
        mPrev.setStatus(progress - 1 < -1 ? -1 : progress - 1, this);
        mNext.setStatus(progress + 1 > 1 ? 1 : progress + 1, this);
    }

}
