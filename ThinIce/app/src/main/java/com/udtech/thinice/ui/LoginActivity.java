package com.udtech.thinice.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.udtech.thinice.ui.authorization.ProgressFragment;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class LoginActivity extends FragmentActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TWITTER_KEY = "Q02k1l8wzOy8uKqJjiKtFMv2r";
    private static final String TWITTER_SECRET = "Aow41K2OKhX7Q9RO4QyJP7a8g9kgvp1IaIbhbXWP3WT0ZibBPM";

    private Fragment last, auth;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover devices when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(34, 46, 59));
        }
        //Fabric(twitter and crashlitics initialisation)
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        //open fragment with login variants selections
        showStartScreen();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect device.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
        EventBus.getDefault().register(this);
    }

    public void showStartScreen() {
        //open fragment with login variants selections
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getStartFragment()).commit();
    }

    public void showLoginScreen() {
        //open fragment with inner login feature, for users that contains in db
        auth = last = getLoginFragment();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, last).addToBackStack(null).commit();
    }

    public void showRegistrationScreen() {
        //open fragment with registration that will write new user in db
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_left, 0, 0, R.anim.anim_out_to_left).replace(R.id.container, getRegistrationFragment()).addToBackStack(null).commit();
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
        try {
            last.onActivityResult(requestCode, resultCode, data);
        } catch (NullPointerException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(CreatedUser event) {// on 1st step of registration ended
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        event.getUser();
        Bundle bundle = new Bundle();
        bundle.putString("email", event.getUser().getEmail());
        bundle.putString("pass", event.getUser().getPassword());
        bundle.putString("first_name", event.getUser().getFirstName());
        bundle.putString("last_name", event.getUser().getLastName());
        bundle.putLong("twitterid", event.getUser().getTwitterId());
        bundle.putLong("facebookid", event.getUser().getFacebookId());
        Fragment fragment = new FragmentProfileInfo();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ProgressFragment()).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, fragment).addToBackStack(null).commit();

    }

    public void onEvent(UserInfoAdded event) {// on 2nd step of registration ended
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_in_from_right, 0, 0, R.anim.anim_out_to_right).replace(R.id.container, new FragmentAddWear()).addToBackStack(null).commit();
    }
}
