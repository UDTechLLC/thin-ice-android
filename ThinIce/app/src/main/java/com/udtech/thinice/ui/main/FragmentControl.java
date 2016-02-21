package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.ShowBackDevice;
import com.udtech.thinice.eventbus.model.devices.ShowFrontDevice;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.devices.DeviceControl;
import com.udtech.thinice.ui.main.devices.DeviceView;
import com.udtech.thinice.ui.widgets.animation.FlipAnimation;
import com.udtech.thinice.ui.widgets.animation.RevertAnimation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 30.11.2015.
 */
public class FragmentControl extends Fragment {
    private FlipAnimation swipe;
    private RevertAnimation reverse;
    private ListView front;
    DeviceControl back;
    private MenuHolder holder;

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity)getActivity();
        EventBus.getDefault().register(this);
    }

    public void switchCards(boolean reversed, Device device) {
        back.setDevice(device);
        if (reversed)
            reverseSwitchCards();
        else
            switchCards();
    }

    public void switchCards(boolean reversed) {
        if (reversed)
            reverseSwitchCards();
        else
            switchCards();
    }

    public void switchCards() {
        if (front.getVisibility() == View.GONE || swipe.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        getView().findViewById(R.id.container).startAnimation(swipe);
    }

    public void reverseSwitchCards() {
        if (front.getVisibility() == View.GONE || reverse.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        getView().findViewById(R.id.container).startAnimation(reverse);
    }

    public void onEvent(ShowBackDevice event) {
        switchCards(event.isReverse(), event.getDevice());
    }
    public void onEvent(ShowFrontDevice event) {
        switchCards(event.isReverse());
    }
    public void onEvent(DeleteDevice event){
        event.getDevice().delete();
        List<Device> devices = new ArrayList<>();
        Iterator<TShirt> tempIterator = TShirt.findAll(TShirt.class);
        while(tempIterator.hasNext())
            devices.add(tempIterator.next());
        Iterator<Insole> tempInsoleIterator = Insole.findAll(Insole.class);
        while(tempInsoleIterator.hasNext())
            devices.add(tempInsoleIterator.next());
        if(devices.size()>0)
            getView().findViewById(R.id.action).setVisibility(View.GONE);
        else
            getView().findViewById(R.id.action).setVisibility(View.VISIBLE);
        front.setAdapter(new ControlAdapter(getContext(), devices));
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder.openPosition(MenuHolder.CONTROL);
        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show();
            }
        });
        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).addToBackStack(null).add(R.id.fragment_container, new FragmentAddWear()).commit();
            }
        });
        front = (ListView) view.findViewById(R.id.devices);
        back = new DeviceControl(getContext());
        ((ViewGroup) view.findViewById(R.id.container)).addView(back);
        back.setVisibility(View.GONE);
        swipe = new FlipAnimation(front, back);
        reverse = new RevertAnimation(front, back);
        List<Device> devices = new ArrayList<>();
        Iterator<TShirt> tempIterator = TShirt.findAll(TShirt.class);
        while(tempIterator.hasNext())
            devices.add(tempIterator.next());
        Iterator<Insole> tempInsoleIterator = Insole.findAll(Insole.class);
        while(tempInsoleIterator.hasNext())
            devices.add(tempInsoleIterator.next());

        if(devices.size()>0)
            getView().findViewById(R.id.action).setVisibility(View.GONE);
        else
            getView().findViewById(R.id.action).setVisibility(View.VISIBLE);
        front.setAdapter(new ControlAdapter(getContext(), devices));


    }

    private class ControlAdapter extends ArrayAdapter<Device> {
        public ControlAdapter(Context context, List<Device> objects) {
            super(context, R.layout.item_wear, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new DeviceView(getContext());
            }
            DeviceView deviceView = (DeviceView) convertView;
            deviceView.setDevice(getItem(position));
            return deviceView;
        }
    }
}
