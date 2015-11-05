package com.udtech.thinice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.udtech.thinice.R;
import com.udtech.thinice.ui.authorization.FragmentAuth;
import com.udtech.thinice.ui.authorization.FragmentInnerLogin;
import com.udtech.thinice.ui.authorization.registration.FragmentRegistration;

import io.fabric.sdk.android.Fabric;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class LoginActivity extends FragmentActivity {
    private static final String TWITTER_KEY = "Q02k1l8wzOy8uKqJjiKtFMv2r";
    private static final String TWITTER_SECRET = "Aow41K2OKhX7Q9RO4QyJP7a8g9kgvp1IaIbhbXWP3WT0ZibBPM";

    private Fragment auth, login, registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        showStartScreen();
    }

    public void showStartScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getStartFragment()).commit();
    }

    public void showLoginScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getLoginFragment()).commit();
    }

    public void showRegistrationScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getRegistrationFragment()).commit();
    }

    private Fragment getStartFragment() {
        return auth != null ? auth : (auth = new FragmentAuth());
    }

    private Fragment getLoginFragment() {
        return login != null ? login : (login = new FragmentInnerLogin());
    }

    private Fragment getRegistrationFragment() {
        return registration != null ? registration : (registration = new FragmentRegistration());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        auth.onActivityResult(requestCode, resultCode, data);
    }
}
