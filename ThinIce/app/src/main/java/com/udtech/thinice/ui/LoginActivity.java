package com.udtech.thinice.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.user.CreatedUser;
import com.udtech.thinice.eventbus.model.user.UserInfoAdded;
import com.udtech.thinice.ui.authorization.FragmentAddWear;
import com.udtech.thinice.ui.authorization.FragmentAuth;
import com.udtech.thinice.ui.authorization.FragmentCreateProfile;
import com.udtech.thinice.ui.authorization.FragmentInnerLogin;
import com.udtech.thinice.ui.authorization.FragmentProfileInfo;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class LoginActivity extends FragmentActivity {
    private static final String TWITTER_KEY = "Q02k1l8wzOy8uKqJjiKtFMv2r";
    private static final String TWITTER_SECRET = "Aow41K2OKhX7Q9RO4QyJP7a8g9kgvp1IaIbhbXWP3WT0ZibBPM";

    private Fragment last, auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        showStartScreen();
        EventBus.getDefault().register(this);
    }

    public void showStartScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getStartFragment()).commit();
    }

    public void showLoginScreen() {
        auth = last = getLoginFragment();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, last).addToBackStack(null).commit();
    }

    public void showRegistrationScreen() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, getRegistrationFragment()).addToBackStack(null).commit();
    }

    private Fragment getStartFragment() {
        last = new FragmentAuth();
        return last;
    }

    private Fragment getLoginFragment() {
        return new FragmentInnerLogin();
    }

    private Fragment getRegistrationFragment() {
        return new FragmentCreateProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        auth.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(CreatedUser event) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        event.getUser();
        Bundle bundle = new Bundle();
        bundle.putString("email", event.getUser().getEmail());
        bundle.putString("pass", event.getUser().getPassword());
        Fragment fragment = new FragmentProfileInfo();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public void onEvent(UserInfoAdded event) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, new FragmentAddWear()).addToBackStack(null).commit();
    }
}
