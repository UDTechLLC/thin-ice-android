package com.udtech.thinice.ui.main.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.cards.ShowBackCard;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.widgets.animation.FlipAnimation;
import com.udtech.thinice.ui.widgets.animation.RevertAnimation;
import com.udtech.thinice.utils.DateUtils;
import com.udtech.thinice.utils.SessionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by JOkolot on 23.11.2015.
 */
public class FragmentCards extends Fragment {
    private FrameLayout animationContainer;
    private View front, back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = UserSessionManager.getSession(getActivity());
        List<Day> days = new ArrayList<>();
        Iterator<Day> daysIterator = Day.findAll(Day.class);
        while (daysIterator.hasNext()) {
            Day day = daysIterator.next();
            if (user.equals(day.getUser())) {
                days.add(day);
            }
        }
        days = days.subList(days.size() - 7 > 0 ? days.size() - 7 : 0, days.size());
        SessionManager.initDay(days.get(days.size() - 1), getActivity());
        LinearLayout container = (LinearLayout) view.findViewById(R.id.container_card);
        for (int i = days.size() - 1; i >= 0; i--) {
            CardView card = new CardView(getActivity());
            card.setDay(days.get(i));
            if (DateUtils.isToday(days.get(i).getDate())) {
                animationContainer = new FrameLayout(getActivity());
                BackCard back = new BackCard(getContext());
                back.setDay(days.get(i));
                back.setVisibility(View.GONE);
                animationContainer.addView(card);
                animationContainer.addView(back);
                container.addView(animationContainer);
                front = card;
                this.back = back;
            } else {
                container.addView(card);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(ShowBackCard event) {
        Animation animation = null;
        if (event.isReversed()) {
            animation = new RevertAnimation(front, back);
        } else {
            animation = new FlipAnimation(front, back);
        }
        animationContainer.startAnimation(animation);
    }

    public void onEvent(ShowFrontCard event) {
        Animation animation = null;
        if (event.isReversed()) {
            animation = new RevertAnimation(back, front);
        } else {
            animation = new FlipAnimation(back, front);
        }
        animationContainer.startAnimation(animation);
    }
}
