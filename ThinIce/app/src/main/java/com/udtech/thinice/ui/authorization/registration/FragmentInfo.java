package com.udtech.thinice.ui.authorization.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udtech.thinice.R;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class FragmentInfo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_info, container, false);
    }
}
