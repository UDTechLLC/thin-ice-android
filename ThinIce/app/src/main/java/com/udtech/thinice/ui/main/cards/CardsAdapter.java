package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.utils.SessionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JOkolot on 23.11.2015.
 */
public class CardsAdapter extends ArrayAdapter<Day> {
    @Deprecated
    public CardsAdapter(Context context, List<Day> objects) {
        super(context, R.layout.item_dashboard_day, objects);
    }

    public static CardsAdapter getInstance(Context context) {
        User user = UserSessionManager.getSession(context);
        List<Day> days = new ArrayList<>();
        Iterator<Day> daysIterator = Day.findAll(Day.class);
        while (daysIterator.hasNext()) {
            Day day = daysIterator.next();
            if (user.equals(day.getUser())) {
                days.add(day);
            }
        }
        days = days.subList(days.size() - 7 > 0 ? days.size() - 7 : 0, days.size());
        List<Day> reverseDayList = new ArrayList<>();
        for (int i = days.size() - 1; i >= 0; i--) {
            reverseDayList.add(days.get(i));
        }
        SessionManager.initDay(reverseDayList.get(0), context);
        return new CardsAdapter(context, reverseDayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = new CardView(getContext());
        Day day = getItem(position);
        ((CardView) convertView).setDay(day);
        return convertView;
    }
}
