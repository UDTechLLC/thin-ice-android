package com.udtech.thinice.protocol;

import com.udtech.thinice.model.devices.TShirt;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by JOkolot on 20.02.2016.
 */
public class CaloryesUtils {

    public static float getCallories(int timeInSec, float avarageTemp) {
        return timeInSec * getBurningSpeedPerSecond(avarageTemp);
    }

    public static float getBurningSpeedPerSecond(float temperature) {
        return (250f - ((temperature - 10f) * 25f))/3600f;
    }
    public static float getAvarageTemp(long time, int cal) {
        return (250f-(cal/(time/1000f))*3600f)/25f+10f;
    }
    public static float getAvarageTempForStats(long time, int cal) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        time=calendar.get(Calendar.SECOND)+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.HOUR)*3600;

        return (250f-(cal/((float)(time)))*3600f)/25f+10f;
    }
}
