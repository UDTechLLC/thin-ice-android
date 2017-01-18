package com.udtech.thinice.ui.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.bluetooth.SendMessage;
import com.udtech.thinice.eventbus.model.devices.DevicePaired;
import com.udtech.thinice.eventbus.model.devices.PairDevice;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.protocol.Protocol;
import com.udtech.thinice.ui.main.adapters.DeviceAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class FragmentAddWear extends Fragment {
    private final static int REQUEST_ENABLE_BT = 1;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private ArrayAdapter adapter;
    private Activity activity;
    private Dialog alert;
    private ProgressDialog pd;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter == null) {
                        adapter = new DeviceAdapter(getActivity());
                        ((ListView) getView().findViewById(R.id.listView)).setAdapter(adapter);
                        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                        getView().findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.divider_bottom).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.divider_top).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.ic_refresh).setVisibility(View.VISIBLE);
                    }
                    if (!devices.contains(result.getDevice())) {
                        devices.add(result.getDevice());
                        adapter.add(result.getDevice());
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter == null) {
                        adapter = new DeviceAdapter(getActivity());
                        ((ListView) getView().findViewById(R.id.listView)).setAdapter(adapter);
                        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                        getView().findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.divider_bottom).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.divider_top).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.ic_refresh).setVisibility(View.VISIBLE);
                    }
                    if (!devices.contains(device)) {
                        devices.add(device);
                        adapter.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    public void load() {
        mHandler = new Handler();
        adapter = null;
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.listView).setVisibility(View.GONE);
        ((ListView) getView().findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Connecting")
                        .setMessage("Connect to " + devices.get(position).getName() + "(" + devices.get(position).getAddress() + ")?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd = new ProgressDialog(getContext());
                        pd.setMessage("Please wait");
                        pd.show();
                        dialog.cancel();
                        final Device device = new TShirt();
                        device.setTemperature(device.getMax());
                        EventBus.getDefault().post(new PairDevice(device, devices.get(position).getAddress(), devices.get(position).getName()));
                    }
                });
                alert = builder.create();
                alert.show();
            }
        });
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        EventBus.getDefault().register(this);
        activity = getActivity();
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
        view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        view.findViewById(R.id.ic_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                  scanLeDevice(mBluetoothAdapter.isEnabled());
            }
        }
    }

    public void onEvent(final DevicePaired event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null)
                    pd.cancel();
                if (event.isResult()) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                    ((MenuHolder) activity).openPosition(MenuHolder.DASHBOARD);
                } else {
                    alert.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Connecting")
                            .setMessage("Connect failed. Try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert = builder.create();
                    alert.show();
                }
            }
        });
    }
}
