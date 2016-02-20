package com.udtech.thinice;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.squareup.picasso.Picasso;

import java.util.Locale;

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
        Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.locale = Locale.UK; // or whichever locale you desire
        else
            configuration.setLocale(Locale.UK);
        Locale.setDefault(Locale.UK);
        Resources.getSystem().updateConfiguration(configuration, null);
    }
}
