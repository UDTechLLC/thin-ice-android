package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.udtech.thinice.R;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.utils.AchievementManager;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 27.11.2015.
 */
public class FragmentSettingsMeasurements extends Fragment {
    @Bind(R.id.volumeSpinner)
    Spinner volume;
    @Bind(R.id.temperatureSpinner)
    Spinner temperature;
    @Bind(R.id.weightSpinner)
    Spinner weight;
    @Bind(R.id.lenghtSpinner)
    Spinner lenght;
    private Settings settings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settings = new Settings().fetch(getContext());
        return inflater.inflate(R.layout.fragment_measurments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        volume.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"ml", "oz"})));
        volume.setSelection(settings.isVolume() ? 1 : 0);
        volume.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setVolume(position != 0);
                settings.save(getContext());
                EventBus.getDefault().post(settings);
                if (position != 0)
                    AchievementManager.getInstance(getContext()).settingsChanged(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        temperature.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"°C", "°F"})));
        temperature.setSelection(settings.isTemperature() ? 1 : 0);
        temperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setTemperature(position != 0);
                settings.save(getContext());
                EventBus.getDefault().post(settings);
                if (position != 0)
                    AchievementManager.getInstance(getContext()).settingsChanged(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        weight.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"Kg", "Lb"})));
        weight.setSelection(settings.isWeight() ? 1 : 0);
        weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setWeight(position != 0);
                settings.save(getContext());
                EventBus.getDefault().post(settings);
                if (position != 0)
                    AchievementManager.getInstance(getContext()).settingsChanged(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        lenght.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"Cm", "Ft'in\""})));
        lenght.setSelection(settings.isLenght() ? 1 : 0);
        lenght.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                settings.setLenght(position != 0);
                settings.save(getContext());
                EventBus.getDefault().post(settings);
                if (position != 0)
                    AchievementManager.getInstance(getContext()).settingsChanged(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
