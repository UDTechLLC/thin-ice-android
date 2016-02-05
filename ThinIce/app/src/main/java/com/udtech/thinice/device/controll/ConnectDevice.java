package com.udtech.thinice.device.controll;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by JOkolot on 14.12.2015.
 */
public class ConnectDevice {
    private static final int REQUEST_ENABLE_BT = 123465;

    public void checkConnect(Activity activity) {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth != null) {
            if (bluetooth.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ENABLE_BT){

        }
    }
}
