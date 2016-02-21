package com.udtech.thinice.bluetooth.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.udtech.thinice.bluetooth.BluetoothManager;
import com.udtech.thinice.bluetooth.bus.BluetoothCommunicator;
import com.udtech.thinice.bluetooth.bus.BondedDevice;
import com.udtech.thinice.bluetooth.bus.ClientConnectionFail;
import com.udtech.thinice.bluetooth.bus.ClientConnectionSuccess;
import com.udtech.thinice.bluetooth.bus.ServeurConnectionFail;
import com.udtech.thinice.bluetooth.bus.ServeurConnectionSuccess;

import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Rami MARTIN on 13/04/2014.
 */
public abstract class BluetoothFragmentActivity extends FragmentActivity {

    protected BluetoothManager mBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothManager = BluetoothManager.getInstance(this);
        checkBluetoothAviability();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
        mBluetoothManager.setUUID(myUUID());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothManager.REQUEST_DISCOVERABLE_CODE) {
            if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_REFUSED) {
                finish();
            } else if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_ACCEPTED) {
                onBluetoothStartDiscovery();
            } else {
                finish();
            }
        }
    }

    public void closeAllConnexion(){
        mBluetoothManager.closeAllConnexion();
    }

    public void checkBluetoothAviability(){
        if(!mBluetoothManager.checkBluetoothAviability()){
            onBluetoothNotAviable();
        }
    }

    public void setTimeDiscoverable(int timeInSec){
        mBluetoothManager.setTimeDiscoverable(timeInSec);
    }

    public void startDiscovery(){
        mBluetoothManager.startDiscovery();
    }

    public void scanAllBluetoothDevice(){
        mBluetoothManager.scanAllBluetoothDevice();
    }

    public void createServeur(){
        mBluetoothManager.createServeur();
    }

    public void createClient(String addressMac){
        mBluetoothManager.createClient(addressMac);
    }

    public void sendMessage(String message){
        mBluetoothManager.sendMessage(message);
    }

    public abstract UUID myUUID();
    public abstract void onBluetoothDeviceFound(BluetoothDevice device);
    public abstract void onClientConnectionSuccess();
    public abstract void onClientConnectionFail();
    public abstract void onServeurConnectionSuccess();
    public abstract void onServeurConnectionFail();
    public abstract void onBluetoothStartDiscovery();
    public abstract void onBluetoothCommunicator(String messageReceive);
    public abstract void onBluetoothNotAviable();

    public void onEvent(BluetoothDevice device){
        onBluetoothDeviceFound(device);
    }

    public void onEvent(ClientConnectionSuccess event){
        mBluetoothManager.isConnected = true;
        onClientConnectionSuccess();
    }

    public void onEvent(ClientConnectionFail event){
        mBluetoothManager.isConnected = false;
        onClientConnectionFail();
    }

    public void onEvent(ServeurConnectionSuccess event){
        mBluetoothManager.isConnected = true;
        onServeurConnectionSuccess();
    }

    public void onEvent(ServeurConnectionFail event){
        mBluetoothManager.isConnected = false;
        onServeurConnectionFail();
    }

    public void onEvent(BluetoothCommunicator event){
        onBluetoothCommunicator(event.mMessageReceive);
    }

    public void onEvent(BondedDevice event){
        //mBluetoothManager.sendMessage("BondedDevice");
    }
}
