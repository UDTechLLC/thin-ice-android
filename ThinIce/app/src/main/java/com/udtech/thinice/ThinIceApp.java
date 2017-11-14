package com.udtech.thinice;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.orm.SchemaGenerator;
import com.orm.SugarDb;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.udtech.thinice.eventbus.model.devices.DeleteDevice;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.devices.TShirt;

import io.fabric.sdk.android.Fabric;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class ThinIceApp extends com.orm.SugarApp {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "F95PO5h8ND4a3ZXESVAt1YGsw";
    private static final String TWITTER_SECRET = "VvTjBhYvy84UWyBBygRISRMlac23W7q5x8FbHnfdkxa9ccnjV0";

    @Override
    public void onCreate() {
        super.onCreate();
//        SchemaGenerator schemaGenerator = new SchemaGenerator(this);
//        schemaGenerator.createDatabase(new SugarDb(this).getDB());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Picasso initialisation
        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        //hardcoding english locale
        Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.locale = Locale.UK; // or whichever locale you desire
        else
            configuration.setLocale(Locale.UK);
        Locale.setDefault(Locale.UK);
        Resources.getSystem().updateConfiguration(configuration, null);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.udtech.thinice", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
