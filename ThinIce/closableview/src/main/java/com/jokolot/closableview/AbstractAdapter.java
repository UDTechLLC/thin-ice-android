package com.jokolot.closableview;

import android.content.Context;

import java.util.List;

/**
 * Created by JOkolot on 01.03.2016.
 */
public abstract class AbstractAdapter<T> {
    private Context mContext;
    private List<T> mItems;
    private ViewController<T>[] mViewArray;
    private int mCurrentPosition = 0, mCurrentViewPosition = 0;

    protected AbstractAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
        mViewArray = new ViewController[3];
    }

    public ViewController<T> getView(int position) {
        if (position < mCurrentPosition) {
            mCurrentViewPosition = (mCurrentViewPosition - 1) < 0 ? mViewArray.length - 1 : (mCurrentViewPosition - 1);
        } else if (position > mCurrentPosition) {
            mCurrentViewPosition = (mCurrentViewPosition + 1) >= mViewArray.length ? 0 : (mCurrentViewPosition + 1);
        }
        mCurrentPosition = position;
        return getView(position, mViewArray[mCurrentViewPosition]);
    }
    public ViewController<T> getPrev(){
        return getView(mCurrentPosition, mViewArray[(mCurrentViewPosition - 1) < 0 ? null: (mCurrentViewPosition - 1)]);
    }
    public ViewController<T> getNext(){
        return getView(mCurrentPosition, mViewArray[(mCurrentViewPosition + 1) >= mViewArray.length ? null : (mCurrentViewPosition + 1)]);
    }

    protected abstract ViewController<T> getView(int position, ViewController<T> convertView);

    public int getItemsCount() {
        return mItems == null ? 0 : mItems.size();
    }
    public int getCurrentPosition(){
        return mCurrentPosition;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }
}
