package com.udtech.thinice.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.users.User;

/**
 * Created by Sofi on 29.11.2015.
 */
public class Notification {
    private static final String TIMER = "time";
    private static final String NAME = "notifications";
    private static final String ENABLED = "enbld";
    private static final String START_TIME = "start_time";
    private static Notification notification;
    private Context context;
    private int timer;
    private boolean enabled;
    private long startTime;
    private User user;

    public Notification(Context context) {
        this.context = context;
    }

    public static Notification getInstance(Context context) {
        if (notification == null) {
            notification = new Notification(context);
            notification.user = UserSessionManager.getSession(context);
            notification.fetch();
        }
        return notification;
    }

    private void fetch() {
        SharedPreferences sPref = context.getSharedPreferences(NAME+user.getId(), Context.MODE_PRIVATE);
        timer = sPref.getInt(TIMER, 1);
        enabled = sPref.getBoolean(ENABLED, false);
        startTime = sPref.getLong(START_TIME,Long.MAX_VALUE);
    }

    public void save() {
        SharedPreferences sPref = context.getSharedPreferences(NAME+user.getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(ENABLED, enabled);
        ed.putInt(TIMER, timer);
        ed.putLong(START_TIME,startTime);
        ed.commit();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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
