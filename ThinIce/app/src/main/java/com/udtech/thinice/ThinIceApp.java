package com.udtech.thinice;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.udtech.thinice.model.Day;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class ThinIceApp extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
