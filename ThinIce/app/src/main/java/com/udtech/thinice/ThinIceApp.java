package com.udtech.thinice;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.squareup.picasso.Picasso;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.TShirt;

import java.util.Iterator;
import java.util.Locale;

import de.greenrobot.event.EventBus;

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
        Iterator<TShirt> tempIterator = TShirt.findAll(TShirt.class);
        while (tempIterator.hasNext()) {
            TShirt tshirt = tempIterator.next();
            EventBus.getDefault().post(new DeleteDevice(tshirt));
            EventBus.getDefault().post(new DeviceChanged(tshirt));
            tshirt.delete();
        }
    }
}
