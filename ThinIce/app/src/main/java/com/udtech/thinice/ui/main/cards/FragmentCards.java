package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowBackCard;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.ui.widgets.animation.FlipAnimation;
import com.udtech.thinice.ui.widgets.animation.RevertAnimation;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentCards extends Fragment {
    private View front, container;
    private BackCard back;
    private FlipAnimation swipe;
    private RevertAnimation reverse;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        front = new FragmentFrontCard(getContext());
        back = new BackCard(getContext());
        swipe = new FlipAnimation(front, back);
        reverse = new RevertAnimation(front, back);

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.container = inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((FrameLayout) view.findViewById(R.id.day_container)).addView(front);
        ((FrameLayout) view.findViewById(R.id.day_container)).addView(back);
        back.setVisibility(View.GONE);
    }

    public void switchCards(boolean reversed, Day day) {
        back.setDay(day);
        if(reversed)
            reverseSwitchCards();
        else
            switchCards();
    }
    public void switchCards(boolean reversed) {
        if(reversed)
            reverseSwitchCards();
        else
            switchCards();
    }


    public void onEvent(ShowBackCard event) {
        switchCards(event.isReversed(), event.getDay());
    }
    public void onEvent(ShowFrontCard event){
        switchCards(event.isReversed());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void switchCards() {
        if (front.getVisibility() == View.GONE || swipe.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        container.startAnimation(swipe);
    }

    public void reverseSwitchCards() {
        if (front.getVisibility() == View.GONE || reverse.isReversed(front)) {
            reverse.reverse();
            swipe.reverse();
        }
        container.startAnimation(reverse);
    }

}
