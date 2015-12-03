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
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.MainActivity;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDevices extends Fragment {
    private Pair<TShirt, Insole> devices;
    private MenuHolder holder;

    public static Fragment getInstance(Pair<TShirt, Insole> devices) {
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
        holder = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.insoles_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuHolder) getActivity()).showMenuItem(MenuHolder.CONTROL);
            }
        });
        view.findViewById(R.id.tshirt_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuHolder) getActivity()).showMenuItem(MenuHolder.CONTROL);
            }
        });
        User user = UserSessionManager.getSession(getActivity());
        ((TextView)view.findViewById(R.id.name)).setText((user.getFirstName()!=null?user.getFirstName():"")+" "+(user.getLastName()!=null?user.getLastName():""));
        Iterator<TShirt> tsIterator = TShirt.findAll(TShirt.class);
        Iterator<Insole> inIterator = Insole.findAll(Insole.class);
        TShirt tShirt = null;
        Insole insole = null;
        if (tsIterator.hasNext())
            tShirt = tsIterator.next();
        if(inIterator.hasNext())
            insole = inIterator.next();
        devices = new Pair<>(tShirt,insole);
        view.findViewById(R.id.separator).setVisibility(View.VISIBLE);
        if(devices.first!=null){
            view.findViewById(R.id.tshirt_container).setVisibility(View.VISIBLE);
            setCharge(view.findViewById(R.id.charge_tshirt), devices.first.getCharge());
            ((TextView)view.findViewById(R.id.charge_tshirt_text)).setText(devices.first.getCharge()+"%");
        }else{
            view.findViewById(R.id.tshirt_container).setVisibility(View.GONE);
            view.findViewById(R.id.separator).setVisibility(View.GONE);
        }
        if(devices.second!=null){
            view.findViewById(R.id.insoles_container).setVisibility(View.VISIBLE);
            setCharge(view.findViewById(R.id.charge_insoles), devices.second.getCharge());
            ((TextView)view.findViewById(R.id.charge_insoles_text)).setText(devices.second.getCharge() + "%");
        }else{
            view.findViewById(R.id.insoles_container).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.separator).setVisibility(View.GONE);
        }
        if(devices.first == null&& devices.second == null){
            ((TextView)view.findViewById(R.id.devices_tittle)).setText("No connected devices");
        }
    }

    private void setCharge(View container, int charge) {
        ((ImageView) ((LinearLayout) container).getChildAt(0)).setImageDrawable(getResources().getDrawable((charge / 70) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(1)).setImageDrawable(getResources().getDrawable((charge / 50) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
        ((ImageView) ((LinearLayout) container).getChildAt(2)).setImageDrawable(getResources().getDrawable((charge / 10) >= 1 ? R.mipmap.ic_charge_fill : R.mipmap.ic_charge_empty));
    }
}
