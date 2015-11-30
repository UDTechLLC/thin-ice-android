package com.udtech.thinice.ui.main;

import android.support.v4.app.Fragment;

import com.udtech.thinice.model.users.User;

/**
 * Created by JOkolot on 06.11.2015.
 */
public abstract class UserDataForm extends Fragment{
    abstract User collectData(User user);
}
