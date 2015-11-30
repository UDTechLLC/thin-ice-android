package com.udtech.thinice.ui.main.devices;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.devices.ShowBackDevice;
import com.udtech.thinice.eventbus.model.devices.ShowFrontDevice;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.devices.Device;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class DeviceControl extends FrameLayout {
    private Device device;
    public DeviceControl(Context context) {
        super(context);
        addView(inflate(getContext(), R.layout.item_wear_control, null));
    }
    public void setDevice(Device device){
        this.device = device;
        initView();
    }
    private void initView(){
        findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
            }
        });
    }
    public void switchCards() {
        EventBus.getDefault().post(new ShowFrontDevice());
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowFrontDevice( true));
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 30;
        private static final int SWIPE_THRESHOLD_VELOCITY = 60;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                reverseSwitchCards();
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                switchCards();
                return false; // Right to left
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return true;
        }

    }
}
