package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udtech.thinice.R;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentFrontCard extends ListFragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListAdapter(CardsAdapter.getInstance(getActivity()));
    }
}
