package com.udtech.thinice.ui.main.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udtech.thinice.R;
import java.util.List;

/**
 * Created by JOkolot on 08.02.2016.
 */
public class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    public DeviceAdapter(Context context) {
        super(context, R.layout.item_device);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(getContext(),R.layout.item_device,null);
        }
        ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position).getName());
        ((TextView)convertView.findViewById(R.id.address)).setText(getItem(position).getAddress());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ((TextView)convertView.findViewById(R.id.type)).setText(getItem(position).getType()+"");
        }

        return convertView;
    }
}
