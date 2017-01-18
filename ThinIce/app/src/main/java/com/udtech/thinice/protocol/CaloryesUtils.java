package com.udtech.thinice.protocol;

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
}
