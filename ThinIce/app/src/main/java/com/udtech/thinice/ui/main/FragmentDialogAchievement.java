package com.udtech.thinice.ui.main;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.AchievementManager;
import com.udtech.thinice.R;
import com.udtech.thinice.model.Achievement;

/**
 * Created by JOkolot on 01.12.2015.
 */
public class FragmentDialogAchievement extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(getContext(), R.layout.fragment_dialog_achievement,null);
        dialog.setContentView(view);
        Achievement achievement = AchievementManager.getInstance(getContext()).getAchievement(getContext(),getArguments().getInt("id"));
        ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(achievement.getBigResourceSrc()));
        ((TextView)view.findViewById(R.id.name)).setText(achievement.getName());
        ((TextView)view.findViewById(R.id.description)).setText(achievement.getDescription());
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
    } @Override
      public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
