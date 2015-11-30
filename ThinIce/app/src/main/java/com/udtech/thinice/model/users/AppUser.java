package com.udtech.thinice.model.users;

import com.orm.SugarRecord;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class AppUser extends SugarRecord<AppUser> {
    private User user;

    public AppUser() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
