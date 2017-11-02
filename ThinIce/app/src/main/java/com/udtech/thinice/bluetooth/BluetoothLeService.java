package com.udtech.thinice.bluetooth;

/**
 * Created by JOkolot on 04.05.2016.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.DevicePaired;
import com.udtech.thinice.eventbus.model.devices.PairDevice;
import com.udtech.thinice.model.Notification;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.pedometer.StepDetector;
import com.udtech.thinice.pedometer.StepListener;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.utils.AchievementManager;
import com.udtech.thinice.utils.SessionManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;


/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private String name;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    BluetoothGattService mThinIceService;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                EventBus.getDefault().post("State connected");
                mBluetoothGatt = gatt;
                mBluetoothGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                EventBus.getDefault().post("State disconnected");
                DeviceManager.initDevice(null);
                EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            EventBus.getDefault().post("Service discovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mThinIceService = mBluetoothGatt.getService(UUID.fromString("0000feb3-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic gattCharacteristic = mThinIceService.getCharacteristic(UUID.fromString("0000fed5-0000-1000-8000-00805f9b34fb"));
//                            BluetoothGattService servc = mBluetoothGatt.getService();
//                            services.add(servc);
                DeviceManager.initDevice(new TShirt());
                DeviceManager.getDevice().setDisabled(true);
                DeviceManager.getDevice().setName(name);
                setCharacteristicNotification(mBluetoothGatt, mThinIceService.getCharacteristic(UUID.fromString("0000fed6-0000-1000-8000-00805f9b34fb")), true);
                EventBus.getDefault().post(new DevicePaired(true));
                new Thread(new Runnable() {
                    long userId = 0;

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
                        userId = UserSessionManager.getSession(getApplicationContext()).getId();
                        while (true) {
                            if (DeviceManager.getDevice()==null||UserSessionManager.getSession(getApplicationContext()) == null || userId != UserSessionManager.getSession(getApplicationContext()).getId())
                                break;
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (Notification.getInstance(getApplicationContext()).isEnabled()) {
                                if ((System.currentTimeMillis()-Notification.getInstance(getApplicationContext()).getStartTime())/(1000*3600)>=Notification.getInstance(getApplicationContext()).getTimer()) {
                                    Notification.getInstance(getApplicationContext()).setStartTime(System.currentTimeMillis());
                                    Notification.getInstance(getApplicationContext()).save();
                                    String tittle = "Thin Ice";
                                    String subject = "Warning";
                                    String body = "Are you wearing the Thin Ice vest? If not, please don't forget to tap the Power button";
                                    NotificationManager notif = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    android.app.Notification notify = new android.app.Notification.Builder
                                            (getApplicationContext())
                                            .setContentTitle(tittle)
                                            .setDefaults(android.app.Notification.DEFAULT_ALL)
                                            .setContentText(body)
                                            .setSmallIcon(R.drawable.ic_notifications)
                                            .setContentTitle(subject)
                                            .setOnlyAlertOnce(true)
                                            .build();
                                    notify.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
                                    notif.notify(0, notify);
                                }
                            }
                        }
                    }
                }).start();
            } else {
            }
        }

        public boolean setCharacteristicNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic characteristic, boolean enable) {
            EventBus.getDefault().post("Status success");
            bluetoothGatt.setCharacteristicNotification(characteristic, enable);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
            descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : new byte[]{0x00, 0x00});
            return bluetoothGatt.writeDescriptor(descriptor); //descriptor write operation successfully started?

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            EventBus.getDefault().post("Characteristic readed");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                EventBus.getDefault().post("Status success");
                displayCharacteristic(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                          int status) {
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            displayCharacteristic(characteristic);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

        }
    };

    private void displayCharacteristic(final BluetoothGattCharacteristic characteristic) {
        final byte[] heartRate = characteristic.getValue();
        Protocol protocol = new Protocol(heartRate);
        if (!protocol.equals(DeviceManager.getDevice())) {
            if (DeviceManager.getDevice() == null) {
                disconnect();
                Notification.getInstance(getApplicationContext()).setStartTime(Long.MAX_VALUE);
            } else {
                boolean lastState = !DeviceManager.getDevice().isDisabled();
                DeviceManager.getDevice().fetch(protocol);
                boolean newState = !DeviceManager.getDevice().isDisabled();
                if(lastState!=newState){
                    if(!newState)
                        Notification.getInstance(getApplicationContext()).setStartTime(Long.MAX_VALUE);
                    else
                        Notification.getInstance(getApplicationContext()).setStartTime(System.currentTimeMillis());
                    Notification.getInstance(getApplicationContext()).save();
                }
                String resp = new String();
                for (byte byteVal : heartRate)
                    resp += (int) byteVal + ":";
                Log.d("Bluetooth test", "Response: " + resp.substring(0, resp.length() - 1));
                EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
            }
        }

    }


    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
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

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().post("Service created");
        new Thread(new Runnable() {
            long userId = 0;

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AchievementManager.getInstance(getApplicationContext()).checkTime(getApplicationContext());
                }
            }
        }).start();
        mStepDetector = new StepDetector();
        mStepDetector.addStepListener(new StepListener() {
            @Override
            public void onStep() {
                if (SessionManager.getManager(getApplicationContext()) != null)
                    SessionManager.getManager(getApplicationContext()).addStep();
            }

            @Override
            public void passValue() {

            }
        });
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        mSensorManager.unregisterListener(mStepDetector);
        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        EventBus.getDefault().unregister(this);
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();


    public boolean connect(final String address, final String name) {
        if (address == null) {
            return false;
        }
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            if (mBluetoothGatt.connect()) {
                EventBus.getDefault().post("Connection success");
                return true;
            } else {
                EventBus.getDefault().post("Connection failed");
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            return false;
        }
        device.connectGatt(this, true, mGattCallback);
        mBluetoothDeviceAddress = address;
        this.name = name;
        return true;
    }

    public void onEvent(DeleteDevice event) {
        disconnect();
        DeviceManager.initDevice(null);
        EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
    }

    public void disconnect() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Requst a write on a give {@code BluetoothGattCharacteristic}. The write result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicWrite(andorid.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    private volatile static BluetoothGattCharacteristic charac;

    public boolean writeCharacteristic(byte[] value) {
        if (mBluetoothGatt == null) {
            return false;
        }
        BluetoothGattService Service = mBluetoothGatt.getService(UUID.fromString("0000feb3-0000-1000-8000-00805f9b34fb"));
        if (Service == null) {
            return false;
        }
        charac = Service
                .getCharacteristic(UUID.fromString("0000fed5-0000-1000-8000-00805f9b34fb"));
        if (charac == null) {
            return false;
        }
        charac.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(charac);
        if (status = true)
            displayCharacteristic(charac);
        return status;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public void onEvent(PairDevice event) {
        boolean result = connect(event.getMac(), event.getName());
    }

    public void onEvent(final SendMessage event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeCharacteristic(event.getCommand());
            }
        }).start();
    }
}
