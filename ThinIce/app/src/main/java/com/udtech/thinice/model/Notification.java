package com.udtech.thinice.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sofi on 29.11.2015.
 */
public class Notification {
    private static final String TIMER = "time";
    private static final String NAME = "notifications";
    private static final String ENABLED = "enbld";
    private static Notification notification;
    private Context context;
    private int timer;
    private boolean enabled;

    public Notification(Context context) {
        this.context = context;
    }

    public static Notification getInstance(Context context) {
        if (notification == null) {
            notification = new Notification(context);
            notification.fetch();
        }
        return notification;
    }

    private void fetch() {
        SharedPreferences sPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        timer = sPref.getInt(TIMER, 1);
        enabled = sPref.getBoolean(ENABLED, false);
    }

    public void save() {
        SharedPreferences sPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(ENABLED, enabled);
        ed.putInt(TIMER, timer);
        ed.commit();
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
        save();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        save();
    }
}
