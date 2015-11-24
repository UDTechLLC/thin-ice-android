package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udtech.thinice.R;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.main.adapters.CardsAdapter;
import com.udtech.thinice.ui.main.cards.FragmentBackCard;
import com.udtech.thinice.ui.main.cards.FragmentFrontCard;
import com.udtech.thinice.ui.widgets.LoopPagerAdapterWrapper;
import com.udtech.thinice.ui.widgets.LoopViewPager;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class FragmentDashBoard extends Fragment{
    private MenuHolder holder;
    private Pair<TShirt,Insole> devices;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity)getActivity();
        devices = new Pair<>(new TShirt(), new Insole());
        devices.first.setCharge(88);
        devices.second.setCharge(28);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        holder.openPosition(MenuHolder.DASHBOARD);
        getChildFragmentManager().beginTransaction().add(R.id.device_container, FragmentDevices.getInstance(devices)).commit();
        initDays(view);
    }

    private void initDays(View view) {
        ((LoopViewPager)view.findViewById(R.id.cards_container)).setAdapter(new LoopPagerAdapterWrapper(new CardsAdapter(getChildFragmentManager(),
                Arrays.asList(new Fragment[]{new FragmentFrontCard(), new FragmentBackCard()}))));
    }

    @OnClick(R.id.menu)
    void showMenu(){
        holder.show();
    }
}
