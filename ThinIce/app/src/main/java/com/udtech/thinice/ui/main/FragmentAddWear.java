package com.udtech.thinice.ui.main;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.udtech.thinice.device.controll.DeviceController;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.adapters.DeviceAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class FragmentAddWear extends Fragment {
    private Device insoles, tshirt;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private ArrayAdapter adapter;
    private Activity activity;

    public void onEventMainThread(BluetoothDevice device) {
        if (adapter == null) {
            adapter = new DeviceAdapter(getActivity());
            ((ListView) getView().findViewById(R.id.listView)).setAdapter(adapter);
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            getView().findViewById(R.id.listView).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.divider_bottom).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.divider_top).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.ic_refresh).setVisibility(View.VISIBLE);
        }
        devices.add(device);
        adapter.add(device);
        adapter.notifyDataSetChanged();
    }

    public void load() {
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
                        dialog.cancel();
                        final Device device = new TShirt();
                        device.setTemperature(device.getMax());
                        DeviceController.getInstance((MainActivity) activity).addDevice(device, devices.get(position).getAddress());
                        DeviceController.getInstance((MainActivity) activity).setTemperatureInCelsium(device, device.getMax());
                        activity.onBackPressed();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        ((MainActivity) getActivity()).startDiscovery();
        ((MainActivity) getActivity()).scanAllBluetoothDevice();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
}
