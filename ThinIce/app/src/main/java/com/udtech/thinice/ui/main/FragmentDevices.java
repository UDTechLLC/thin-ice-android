package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDevices extends Fragment {
    private Pair<TShirt,Insole> devices;
    private MenuHolder holder;

    public static Fragment getInstance(Pair<TShirt,Insole> devices){
        FragmentDevices fragment = new FragmentDevices();
        fragment.devices = devices;
        return fragment;
    }
    @Deprecated
    public FragmentDevices() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices,container,false);
    }

    private void setCharge(View container, int charge){
        ((ImageView)((LinearLayout)container).getChildAt(0)).setImageDrawable(getResources().getDrawable((charge/70)>=1?R.mipmap.ic_charge_fill:R.mipmap.ic_charge_empty));
        ((ImageView)((LinearLayout)container).getChildAt(1)).setImageDrawable(getResources().getDrawable((charge / 50) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
        ((ImageView)((LinearLayout)container).getChildAt(2)).setImageDrawable(getResources().getDrawable((charge / 10) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
    }
}
