package com.udtech.thinice.utils;

import android.content.Context;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.Session;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.protocol.CaloryesUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 04.12.2015.
 */
public class SessionManager {//Concept was: that when you add device/change temperature you creating/recreating session and then you can calc all data that you need.
    private static SessionManager manager;
    private Day day;
    private Context context;
    private Session deviceSession;
    private long spended, callories;
    private float calloriesTaget = 0;

    private SessionManager(Day day, Context context) {
        EventBus.getDefault().register(this);
        this.day = day;
        this.context = context;
        spended = day.getTotalTime();
        callories = (long) day.getTotalCalories();
    }

    public static SessionManager initDay(Day day, Context context) { //singletone, but if new day was started - creating new instance
        if (manager == null)
            manager = new SessionManager(day, context);
        if (!manager.day.equals(day)) {
            manager.forceClose();
            EventBus.getDefault().post(day);
            manager = new SessionManager(day, context);
        }
        return getManager(context);
    }

    public static SessionManager getManager(Context context) {
        if(manager!=null&&manager.day!=null&&!com.udtech.thinice.utils.DateUtils.isToday(manager.day.getDate())){
            Day day = new Day(UserSessionManager.getSession(context));
            day.save();
            SessionManager.initDay(day, context);
        }

        return manager;
    }

    private void recreate() {
        forceClose();
        if (DeviceManager.getDevice() != null)
            if (!DeviceManager.getDevice().isCharging() && !DeviceManager.getDevice().isDisabled()) {
                deviceSession = new Session(day);
                deviceSession.openSession((int) DeviceManager.getDevice().getTemperature());
            }
    }

    public void onEvent(DeviceChanged event) {
        recreate();
    }

    public long getSpended() { //calculate spended time for day
        if (deviceSession != null)
            if (deviceSession.getStartTime() != null)
                if (DeviceManager.getDevice() != null)
                    return spended + new Date().getTime() - deviceSession.getStartTime().getTime();
        return spended;
    }

    public float getSpendedCallories() { //calculate spended time for day
        if (deviceSession != null)
            if (DeviceManager.getDevice() != null)
                return callories + CaloryesUtils.getCallories((int) (new Date().getTime() - deviceSession.getStartTime().getTime()) / 1000, deviceSession.getTemperature());
        return day.getTotalCalories();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
        forceClose();
    }

    public float getCurrentCaloriesRatePerSecond() {
        if (deviceSession != null)
            return CaloryesUtils.getBurningSpeedPerSecond(deviceSession.getTemperature());
        else
            return 0;
    }

    public float getTargetTime() {
        Session session = deviceSession;
        if (session != null)
            return ((getTargetCallories() - day.getTotalCalories()) / CaloryesUtils.getBurningSpeedPerSecond(DeviceManager.getDevice().getTemperature())) * 1000 + spended;
        else
            return ((getTargetCallories() - day.getTotalCalories()) / CaloryesUtils.getBurningSpeedPerSecond(day.getLastTemp())) * 1000 + spended;
    }
    public float getTargetTimeForDay(Day day) {
            return ((getTargetCallories() - day.getTotalCalories()) / CaloryesUtils.getBurningSpeedPerSecond(day.getLastTemp())) * 1000+day.getTotalTime();
    }


    public float getTargetCalories() {
        if (calloriesTaget == 0)
            calloriesTaget = CaloryesUtils.getCallories(3600, TShirt.MIN_TEMPERATURE);
        return calloriesTaget;
    }

    private void forceClose() {
        if (deviceSession != null)
            deviceSession.closeSession();
        if (deviceSession != null)
            AchievementManager.getInstance(context).sessionClosed(context);
        deviceSession = null;
        spended = day.getTotalTime();


        callories = (long) day.getTotalCalories();
    }

    private static float getTargetCallories() {
        return CaloryesUtils.getCallories(3600, 15);
    }

    public void addStep() {
        if(deviceSession!=null)
            if(deviceSession.getStartTime()!=null)
        deviceSession.addStep();
    }
}
