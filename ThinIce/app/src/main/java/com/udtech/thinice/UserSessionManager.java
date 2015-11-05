package com.udtech.thinice;

import android.content.Context;
import android.content.SharedPreferences;

import com.udtech.thinice.model.users.User;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class UserSessionManager {
    private static final String NAME = "user_session", ID = "id";
    public static void saveSession(User user, Context context){
        SharedPreferences sPref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(ID, user.getId());
        ed.commit();
    }
    public static void clearSession(Context context){
        SharedPreferences sPref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(ID, Long.MAX_VALUE);
        ed.commit();
    }
    public static User getSession(Context context){
        SharedPreferences sPref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        long id = sPref.getLong(ID, Long.MAX_VALUE);
        User user = null;
        if(id!=Long.MAX_VALUE)
            user = User.findById(User.class,id);
        return user;
    }

}
