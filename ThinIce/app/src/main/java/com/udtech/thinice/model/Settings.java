package com.udtech.thinice.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.users.User;

/**
 * Created by JOkolot on 27.11.2015.
 */
public class Settings {
    private static final String NAME = "settings";
    private static final String TEMPERATURE = "temp";
    private static final String VOLUME = "vol";
    private static final String WEIGHT = "weight";
    private static final String LENGHT = "lenght";

    //true if non metrical/celsium
    private boolean temperature;
    private boolean weight;
    private boolean lenght;
    private boolean volume;

    public static int convertTemperatureToFaringeite(float temp) {
        return Math.round((9.0f / 5.0f) * temp + 32);
    }

    public static float convertTemperatureToCelsium(float temp) {
        return (float) ((temp - 32) / 1.8);
    }

    public static float convertWeight(float weight) {
        return 2.2f * weight;
    }

    public static float convertLenght(float lenght) {
        double inches = (lenght / 2.54f);
        return (float) inches;
    }

    public static int convertVolume(float vol) {
        return Math.round(0.008345f * vol);
    }

    public static int deconvertVolume(float volume) {
        return Math.round(volume / 0.008345f);

    }

    public static int deconvertTemperature(int temp) {
        return Math.round((temp - 32f) * 5f / 9f);
    }

    public static float deconvertWeight(float weight) {
        return weight / 2.2f;
    }

    public static float deconvertLenght(float temp) {
        return temp * 2.54f;
    }

    public boolean isTemperature() {
        return temperature;
    }

    public void setTemperature(boolean temperature) {
        this.temperature = temperature;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean isLenght() {
        return lenght;
    }

    public void setLenght(boolean lenght) {
        this.lenght = lenght;
    }

    public boolean isVolume() {
        return volume;
    }

    public void setVolume(boolean volume) {
        this.volume = volume;
    }

    public Settings fetch(Context context) {
        User user = UserSessionManager.getSession(context);
        if (user != null) {
            SharedPreferences sPref = context.getSharedPreferences(NAME + user.getId(), Context.MODE_PRIVATE);
            temperature = sPref.getBoolean(TEMPERATURE, false);
            lenght = sPref.getBoolean(LENGHT, false);
            volume = sPref.getBoolean(VOLUME, false);
            weight = sPref.getBoolean(WEIGHT, false);
        } else {
            temperature = false;
            lenght = false;
            volume = false;
            weight = false;
        }
        return this;
    }

    public Settings save(Context context) {
        User user = UserSessionManager.getSession(context);
        SharedPreferences sPref = context.getSharedPreferences(NAME + user.getId(), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(TEMPERATURE, temperature);
        ed.putBoolean(LENGHT, lenght);
        ed.putBoolean(VOLUME, volume);
        ed.putBoolean(WEIGHT, weight);
        ed.commit();
        return this;
    }
}
