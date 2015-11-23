package com.udtech.thinice.ui.main.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.orm.SugarRecord;
import com.udtech.thinice.R;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.ui.widgets.CardView;

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
    public static CardsAdapter getInstance(Context context){
        List<Day> days = new ArrayList<>();
        Iterator<Day> daysIterator = SugarRecord.findAll(Day.class);
        while (daysIterator.hasNext())
            days.add(daysIterator.next());
        days = days.subList(days.size()-7>0?days.size()-7:0,days.size());
        return new CardsAdapter(context,days);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = new CardView(getContext());
        Day day = getItem(position);
        ((CardView)convertView).setDay(day);
        return convertView;
    }
}
