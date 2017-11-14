package com.udtech.thinice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.udtech.thinice.eventbus.model.BluetoothCommand;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.PairDevice;
import com.udtech.thinice.model.Steps;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.pedometer.StepDetector;
import com.udtech.thinice.pedometer.StepListener;
import com.udtech.thinice.pedometer.Utils;
import com.udtech.thinice.protocol.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 08.02.2016.
 */
public class ThinIceService extends Service {
    private static final String TAG = "ThinIceService";
    /**
     * Receives messages from activity.
     */
    private final IBinder mBinder = new StepBinder();
    private SharedPreferences mSettings;
    private SharedPreferences mState;
    private SharedPreferences.Editor mStateEditor;
    private Utils mUtils;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    private Steps todaySteps;
    private List<Device> deviceList;
    private List<BluetoothDevice> bDevicesList;
    private List<BluetoothSocket> socketsList;
    private PowerManager.WakeLock wakeLock;
    private NotificationManager mNM;
    private int mDesiredPace;
    private float mDesiredSpeed;

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);

        Log.i(TAG, "[SERVICE] onCreate");
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Load settings
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mState = getSharedPreferences("state", 0);

        mUtils = Utils.getInstance();
        mUtils.setService(this);
        mUtils.initTTS();


        // Start detecting
        mStepDetector = new StepDetector();
        mStepDetector.addStepListener(new StepListener() {
            @Override
            public void onStep() {
                if (todaySteps != null) {
                    todaySteps.incrementSteps();
                    todaySteps.save();
                }
            }

            @Override
            public void passValue() {

            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Thin Ice")
                .setTicker("Thin Ice started")
                .setContentText("Thin Ice foreground work")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher), 128, 128, false)).build();
        startForeground(0,
                notification);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "[SERVICE] onStart");
        super.onStart(intent, startId);
    }

    public void onEvent(DeviceChanged event) {
        if (!event.getDevice().isDisabled()) {
            List<Steps> todayStepsList = Steps.find(Steps.class, "date = ?", Steps.createNewTodaySteps().getDate());
            if (todayStepsList == null || todayStepsList.size() == 0) {
                todaySteps = Steps.createNewTodaySteps();
                todaySteps.save();
            } else {
                todaySteps = todayStepsList.get(0);
            }
        } else {
            todaySteps = null;
        }
    }

    public void onEvent(DeleteDevice event) {
        todaySteps = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            stopForeground(true);
            Log.i(TAG, "[SERVICE] onDestroy");
            mUtils.shutdownTTS();

            // Unregister our receiver.
            unregisterDetector();

            mStateEditor = mState.edit();
            mStateEditor.commit();

            mNM.cancel(R.string.app_name);

            wakeLock.release();


            // Stop detecting
            mSensorManager.unregisterListener(mStepDetector);
        } catch (Exception e) {
        }
    }

    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER /*|
            Sensor.TYPE_MAGNETIC_FIELD |
            Sensor.TYPE_ORIENTATION*/);
        mSensorManager.registerListener(mStepDetector,
                mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[SERVICE] onBind");
        return mBinder;
    }

    public void createInfoNotification(String message) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle("Thin Ice") //заголовок уведомления
                .setDefaults(Notification.DEFAULT_ALL); // звук, вибро и диодный индикатор выставляются по умолчанию

        Notification notification = nb.getNotification(); //генерируем уведомление
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification); // отображаем его пользователю.
    }

    public void onEvent(BluetoothCommand command) {
        int position = 0;
        for (int i = deviceList.size(); i > 0; i--) {
            if (command.getDevice().getId() == deviceList.get(i - 1).getId()) {
                position = i - 1;
                break;
            }
        }
        try {

//            BluetoothDevice actual = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(bDevicesList.get(position).getAddress());
//            BluetoothSocket socket = null;
//            while (socket == null) {
//                try {
//                    BluetoothSocket tmp = actual.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
//                    socket = tmp;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            socket.connect();
            BluetoothSocket socket = null;
            try {
                while (socket == null)
                    socket = bDevicesList.get(position).createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Protocol protocol = new Protocol();
            switch (command.getCommand()) {
                case BluetoothCommand.SET_TEMP: {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    os.write(protocol.setTemp(command.getValue()));
                    os.flush();
                    os.close();
                    is.close();
                    break;
                }
                case BluetoothCommand.START: {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    os.write(protocol.getOn(10));
                    os.flush();
                    os.close();
//                    while (is.available() == 0)
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    byte[] buffer = new byte[is.available()];
//                    is.read(buffer);
//                    String result = new String(buffer);
//                    if (new String(buffer).equals(protocol.setTemperature(command.getValue()))) {
//
//                    }
                    break;
                }
                case BluetoothCommand.STOP: {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    os.write(protocol.getOff());
                    os.flush();
                    os.close();
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            createInfoNotification("Device was disabled");
            boolean deviceOpened = false;
        }
    }

    public void reloadSettings() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        if (mStepDetector != null) {
            mStepDetector.setSensitivity(
                    Float.valueOf(mSettings.getString("sensitivity", "10"))
            );
        }
    }



    //For Pairing
    private boolean pairDevice(BluetoothDevice device) {
        try {
            Log.d("pairDevice()", "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            Log.d("pairDevice()", "Pairing finished.");
            return (Boolean) m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
        }
        return false;
    }

    //For UnPairing
    private void unpairDevice(BluetoothDevice device) {
        try {
            Log.d("unpairDevice()", "Start Un-Pairing...");
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d("unpairDevice()", "Un-Pairing finished.");
        } catch (Exception e) {
            Log.e("unpairDevice()", e.getMessage());
        }
    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class StepBinder extends Binder {
        ThinIceService getService() {
            return ThinIceService.this;
        }
    }

    public class ConnectionThread extends Thread {
        private final BluetoothDevice mmDevice;
        private final Device localDevice;
        private BluetoothSocket mmSocket;

        public ConnectionThread(BluetoothDevice device, Device localDevice) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            this.localDevice = localDevice;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            Date dateStart = new Date();
            while (mmSocket == null) {
                try {
                    tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                    mmSocket = tmp;
                    if (new Date().getTime() - dateStart.getTime() > 10000) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mmSocket != null)
                    localDevice.save();
            }
        }

        public void run() {
            if (mmSocket != null) {
                EventBus.getDefault().post(new DeviceChanged(localDevice));
                // Cancel discovery because it will slow down the connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

//                try {
//                    // Connect the device through the socket. This will block
//                    // until it succeeds or throws an exception
//                    //mmSocket.connect();
//                } catch (IOException connectException) {
//                    // Unable to connect; close the socket and get out
//                    try {
//                        mmSocket.close();
//                    } catch (IOException closeException) {
//                    }
//                    return;
//                }

                // Do work to manage the connection (in a separate thread)
                if (deviceList == null)
                    deviceList = new ArrayList<>();
                if (bDevicesList == null)
                    bDevicesList = new ArrayList<>();
                if (socketsList == null)
                    socketsList = new ArrayList<>();
                socketsList.add(mmSocket);
                deviceList.add(localDevice);
                bDevicesList.add(mmDevice);
                localDevice.save();
                EventBus.getDefault().post(localDevice);
            }
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

}