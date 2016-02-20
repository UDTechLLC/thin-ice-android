package com.udtech.thinice.ui.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.eventbus.model.devices.PairDevice;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.TShirt;
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
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
        }
    };

    public void load() {
        adapter = null;
        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.listView).setVisibility(View.GONE);
        getView().findViewById(R.id.divider_bottom).setVisibility(View.GONE);
        getView().findViewById(R.id.divider_top).setVisibility(View.GONE);
        getView().findViewById(R.id.ic_refresh).setVisibility(View.GONE);
        ((ListView) getView().findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new PairDevice(new TShirt(), devices.get(position)));
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

                if (bluetooth != null) {

                    if (bluetooth.isEnabled()) {
                        bluetooth.cancelDiscovery();
                        bluetooth.startDiscovery();
                    } else {
                        getView().findViewById(R.id.ic_refresh).setVisibility(View.VISIBLE);
                        String status = "Bluetooth is not Enabled.";
                        Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                        getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }

                }
                if (getView() != null) {
//                    getView().findViewById(R.id.insoles).setVisibility(View.VISIBLE);
//                    getView().findViewById(R.id.tshirt).setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
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
//        if (TShirt.findAll(TShirt.class).hasNext()) {
//            tshirt = TShirt.findAll(TShirt.class).next();
//            ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt_active));
//
//        } else {
//            ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt));
//        }
//        if (Insole.findAll(Insole.class).hasNext()) {
//            insoles = Insole.findAll(Insole.class).next();
//            ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles_active));
//
//        } else {
//            ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles));
//        }
//        view.findViewById(R.id.insoles).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (insoles == null) {
//                    insoles = new Insole();
//                    insoles.setDisabled(true);
//                    ((Insole) insoles).save();
//                    ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles_active));
//
//                } else {
//                    ((Insole) insoles).delete();
//                    ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles));
//                    insoles = null;
//                }
//
//            }
//        });
//
//        view.findViewById(R.id.tshirt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tshirt == null) {
//                    tshirt = new TShirt();
//                    tshirt.setDisabled(true);
//                    ((TShirt) tshirt).save();
//                    ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt_active));
//
//                } else {
//                    ((TShirt) tshirt).delete();
//                    ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt));
//                    tshirt = null;
//                }
//
//            }
//        });
    }

    public void showDevicesList(final List<BluetoothDevice> devices) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select device").setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int i) {
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }


}
