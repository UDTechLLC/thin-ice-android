package com.udtech.thinice.bluetooth.activity;

/**
 * Created by JOkolot on 21.02.2016.
 */
public interface BluetoothActivityInterface {
    public void sendMessage(String message);
    public void createClient(String addressMac);
    public void resetCurrentClient();
}
