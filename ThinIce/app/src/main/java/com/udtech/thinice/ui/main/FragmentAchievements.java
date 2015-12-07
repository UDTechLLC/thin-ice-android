package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.utils.AchievementManager;
import com.udtech.thinice.R;
import com.udtech.thinice.model.Achievement;
import com.udtech.thinice.ui.MainActivity;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 01.12.2015.
 */
public class FragmentAchievements extends Fragment {
    private MenuHolder holder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holder = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_achivements,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((GridView)view.findViewById(R.id.grid)).setNumColumns(3);
        ((GridView)view.findViewById(R.id.grid)).setVerticalSpacing(10);
        ((GridView)view.findViewById(R.id.grid)).setHorizontalSpacing(5);
        ((GridView)view.findViewById(R.id.grid)).setAdapter(new AchievementAdapter(getContext(), AchievementManager.getInstance(getContext()).getAchievements(getContext())));
        view.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.show();
            }
        });
        holder.openPosition(MenuHolder.ACHIEVEMENTS);
    }
    private class AchievementAdapter extends ArrayAdapter<Achievement>{

        public AchievementAdapter(Context context,  List<Achievement> objects) {
            super(context, R.layout.item_achivement, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = View.inflate(getContext(),R.layout.item_achivement,null);
            ((ImageView)convertView.findViewById(R.id.icon)).setImageDrawable(getItem(position).isOpened() ? getResources().getDrawable(getItem(position).getResourceSrc()) : getResources().getDrawable(R.mipmap.ic_achievement));
            ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position).getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(getItem(position));
                }
            });
            return  convertView;
        }
    }
}
