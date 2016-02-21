package com.udtech.thinice.utils;

import android.content.Context;

import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.Session;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.Insole;
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
public class SessionManager {
    private static SessionManager manager;
    private Day day;
    private Context context;
    private Session tShirtSession, insolesSession;

    private SessionManager(Day day, Context context) {
        EventBus.getDefault().register(this);
        this.day = day;
        this.context = context;
        recreate();
    }

    public static SessionManager initDay(Day day, Context context) {
        if (manager == null)
            manager = new SessionManager(day, context);
        if (!manager.day.equals(day)) {
            manager.forceClose();
            manager = new SessionManager(day, context);
        }
        return getManager();
    }

    public static SessionManager getManager() {
        return manager;
    }

    public static float getCaloriesRatePerSecondPerSession(Session session) {
        float rate = 0;
        rate = rate + (40f - session.getTemperature()) / 50f;
        return rate;
    }

    public boolean checkDay(Day day) {
        return this.day.equals(day);
    }

    private void recreate() {
        forceClose();
        List<Device> devices = new ArrayList<>();
        Iterator<TShirt> tempIterator = TShirt.findAll(TShirt.class);
        if (tempIterator.hasNext()) {
            TShirt tShirt = tempIterator.next();
            if (!tShirt.isDisabled()) {
                tShirtSession = new Session(day);
                tShirtSession.openSession(Math.round(tShirt.getTemperature()));
            }
        }
        Iterator<Insole> tempInsoleIterator = Insole.findAll(Insole.class);
        if (tempInsoleIterator.hasNext()) {
            Insole insole = tempInsoleIterator.next();
            if (!insole.isDisabled()) {
                insolesSession = new Session(day);
                insolesSession.openSession(Math.round(insole.getTemperature()));
            }
        }
    }

    public void onEvent(DeviceChanged event) {
        recreate();
    }

    public long getSpended() {
        long spended = 0;
        if (tShirtSession != null)
            spended += new Date().getTime() - tShirtSession.getStartTime().getTime();
        if (insolesSession != null)
            spended += new Date().getTime() - insolesSession.getStartTime().getTime();
        return spended;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
        forceClose();
    }

    public float getCurrentCaloriesRatePerSecond() {
        if (tShirtSession != null)
            return CaloryesUtils.getBurningSpeed(tShirtSession.getTemperature()) / 60000f;
        else
            return 0;
    }

    public int getSecondsRate() {
        int rate = 0;
        if (tShirtSession != null)
            rate++;
        if (insolesSession != null)
            rate++;
        return rate;
    }

    public Session[] getOpenedSessions() {
        return new Session[]{tShirtSession, insolesSession};
    }

    public void forceClose() {
        if (tShirtSession != null)
            tShirtSession.closeSession();
        if (insolesSession != null)
            insolesSession.closeSession();
        if (tShirtSession != null)
            AchievementManager.getInstance(context).sessionClosed(context, tShirtSession);
        if (insolesSession != null)
            AchievementManager.getInstance(context).sessionClosed(context, insolesSession);
        tShirtSession = null;
        insolesSession = null;
    }
}
