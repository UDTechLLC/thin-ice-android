package com.udtech.thinice.protocol;

/**
 * Created by JOkolot on 20.02.2016.
 */
public class CaloryesUtils {
    public static final int RECOMENDED_KKALORIES = 1000;
    public static float getCallories(int timeInSec,float avarageTemp){
        return (timeInSec/60)*getBurningSpeed(avarageTemp);
    }
    public static float getBurningSpeed(float temperature){
        return 500f/((90f*temperature)/5f);
    }
    public static float getTimeLeft(float temperatureCurrent,float avarageTemp,int timeInSec){
        return  (RECOMENDED_KKALORIES-getCallories(timeInSec/60,avarageTemp))/getBurningSpeed(temperatureCurrent);
    }
    public static float getTimeLeft(float temperatureCurrent,int callories){
        return  (RECOMENDED_KKALORIES-callories)/getBurningSpeed(temperatureCurrent);
    }
}
