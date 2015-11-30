package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowBackCard;
import com.udtech.thinice.model.Day;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentCards  extends Fragment{
    private View front, back;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        front = new ListView(getContext());
        ((ListView)front).setAdapter(CardsAdapter.getInstance(getActivity()));
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showBack(int animationId, Day day) {
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_middle,R.anim.from_middle).replace(R.id.day_container, BackCard.getInstance(day)).commit();
    }
    public void showFront(int animationId) {
        getChildFragmentManager().beginTransaction().setCustomAnimations(animationId, animationId).replace(R.id.day_container, front).commit();
    }
    public void onEvent(ShowBackCard event){
        showBack(event.isReversed() ? R.anim.to_middle : R.anim.to_middle, event.getDay());
    }
}
