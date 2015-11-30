package com.udtech.thinice.ui.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.model.devices.Insole;
import com.udtech.thinice.model.devices.TShirt;
import com.udtech.thinice.ui.LoginActivity;
import com.udtech.thinice.ui.MainActivity;
import com.udtech.thinice.ui.SplashActivity;

/**
 * Created by Sofi on 30.11.2015.
 */
public class FragmentAddWear extends Fragment {
    private Device insoles, tshirt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_device,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
                getView().findViewById(R.id.insoles).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.tshirt).setVisibility(View.VISIBLE);
            }
        }, 2000);
        view.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();
            }
        });
        if (TShirt.findAll(TShirt.class).hasNext()) {
            tshirt = TShirt.findAll(TShirt.class).next();
            ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt_active));

        } else {
            ((ImageView) view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt));
        }
        if (Insole.findAll(Insole.class).hasNext()) {
            insoles = Insole.findAll(Insole.class).next();
            ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles_active));

        } else {
            ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles));
        }
        view.findViewById(R.id.insoles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insoles == null) {
                    insoles = new Insole();
                    insoles.setDisabled(true);
                    ((Insole) insoles).save();
                    ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles_active));

                } else {
                    ((Insole) insoles).delete();
                    ((ImageView) view.findViewById(R.id.insoles)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_insoles));
                    insoles = null;
                }

            }
        });

        view.findViewById(R.id.tshirt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tshirt == null){
                    tshirt = new TShirt();
                    tshirt.setDisabled(true);
                    ((TShirt)tshirt).save();
                    ((ImageView)view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt_active));

                }else{
                    ((TShirt)tshirt).delete();
                    ((ImageView)view.findViewById(R.id.tshirt)).setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_add_tshirt));
                    tshirt = null;
                }

            }
        });
    }
}
