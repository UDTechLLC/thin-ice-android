package com.udtech.thinice.ui;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.udtech.thinice.R;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_select_login);
    }
}
