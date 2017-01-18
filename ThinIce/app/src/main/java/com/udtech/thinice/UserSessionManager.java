package com.udtech.thinice;

import android.content.Context;
import android.content.SharedPreferences;

import com.udtech.thinice.model.users.User;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class UserSessionManager {//Class using preferences for saving current user, more user info contains in db
    private static final String NAME = "user_session", ID = "id";
    private static User user;

    public static void saveSession(User user, Context context) {
        SharedPreferences sPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(ID, user.getId());
        ed.commit();
    }

    public static void clearSession(Context context) {// removing user data from preferences

        SharedPreferences sPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(ID, Long.MAX_VALUE);
        ed.commit();
        user = null;
    }

    public static User getSession(Context context) { // loading current user
        SharedPreferences sPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        long id = sPref.getLong(ID, Long.MAX_VALUE);
        if (user == null)
            if (id != Long.MAX_VALUE)
                user = User.findById(User.class, id);
        return user;
    }

}
