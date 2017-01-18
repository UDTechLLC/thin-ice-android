package com.udtech.thinice.ui.main.devices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.Switch;
import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.SearchDevices;
import com.udtech.thinice.eventbus.model.devices.ShowFrontDevice;
import com.udtech.thinice.model.Notification;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.utils.DelayedDeviceStart;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class DeviceControl extends FrameLayout {
    private Device device;
    private boolean deviceRunned;
    private int selectedPosition = 1;
    private ProgressDialog pd;
    private GestureDetector gdt = new GestureDetector(getContext(), new GestureListener());

    public DeviceControl(Context context) {
        super(context);
        addView(inflate(getContext(), R.layout.item_wear_control, null));
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
    }

    public void setDevice(Device device) {
        deviceRunned = !device.isDisabled();
        initView();
    }

    private void initView() {
        device = DeviceManager.getDevice();
        ((EditText) findViewById(R.id.name)).setText(device.getName());
        ((Switch) findViewById(R.id.timer_enabled)).setChecked(DelayedDeviceStart.getTShirtInstance(device).getTimeLeft() != 0);
        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
            }
        });
        findViewById(R.id.save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.name)).getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error")
                            .setMessage("Device name can not be empty")
                            .setCancelable(true)
                            .setNegativeButton("Close",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if(((EditText) findViewById(R.id.name)).getText().toString().equals(device.getName())){
                    if (((Switch) findViewById(R.id.timer_enabled)).isChecked()) {
                        DelayedDeviceStart.getTShirtInstance(device).start(selectedPosition == 1 ? 1800000 : 3600000, (MainActivity) getContext());
                        EventBus.getDefault().post(new SendMessage(new Protocol().getOff()));
                    } else {
                        DelayedDeviceStart.getTShirtInstance(device).cancel();
                    }
                }else{
                    EventBus.getDefault().post(new SendMessage(new Protocol().setName(((EditText) findViewById(R.id.name)).getText().toString())));
                    pd = new ProgressDialog(getContext());
                    pd.setTitle("Renaming");
                    pd.setMessage("Please wait");
                    pd.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DeviceManager.initDevice(device);
                                    DeviceManager.initDevice(null);
                                }
                            });
                           EventBus.getDefault().post(new DeleteDevice(device));
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ((Activity)getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    EventBus.getDefault().post(new SearchDevices());
                                }
                            });
                        }
                    }).start();
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                }
                switchCards();
            }
        });
        final View selection = findViewById(R.id.selection);
        final ViewGroup parent = ((ViewGroup) selection.getParent());
        ((TextView) findViewById(R.id.enale_timer_text)).setTextColor(device.getTimer().getTime() == 0 ? Color.WHITE : getResources().getColor(R.color.textViewColor));
        findViewById(R.id.one).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 1;
                parent.addView(selection, lp);
                setTextColor(1);
                Notification.getInstance(getContext()).setTimer(1);
                selectedPosition = 1;
                device.setTimer(((Switch) findViewById(R.id.timer_enabled)).isChecked() ? new Date(new Date().getTime() + 3600000/*hour*/ / (selectedPosition == 2 ? 1 : 2)) : new Date(0));
            }
        });
        findViewById(R.id.two).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                parent.removeAllViews();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.weight = 2;
                parent.addView(selection, lp);
                setTextColor(2);
                Notification.getInstance(getContext()).setTimer(1);
                selectedPosition = 2;
                device.setTimer(((Switch) findViewById(R.id.timer_enabled)).isChecked() ? new Date(new Date().getTime() + 3600000/*hour*/ / (selectedPosition == 2 ? 1 : 2)) : new Date(0));
            }
        });
        findViewById(R.id.delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
                EventBus.getDefault().post(new DeleteDevice(device));
            }
        });
    }

    public void setTextColor(int count) {
        List<View> views = Arrays.asList(new View[]{findViewById(R.id.one), findViewById(R.id.two)});
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setAlpha(1f);
        }
        for (int i = count; i < views.size(); i++) {
            views.get(i).setAlpha(0.5f);
        }
    }

    public void switchCards() {
        EventBus.getDefault().post(new ShowFrontDevice());
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowFrontDevice(true));
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
