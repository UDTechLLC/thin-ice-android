package com.udtech.thinice.ui.main;

/**
 * Created by JOkolot on 18.11.2015.
 */
public interface MenuHolder {
    public static final int DASHBOARD = 0, STATISTICS = 1, CONTROL = 2, ACCOUNT = 3, ACHIEVEMENTS = 4, SETTINGS = 5;
    public void openPosition(int position);
    public void show();
}
