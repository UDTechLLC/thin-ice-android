package com.udtech.thinice.eventbus.model.user;

import com.udtech.thinice.model.users.User;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class CreatedUser {
    private User user;

    public User getUser() {
        return user;
    }

    public CreatedUser(User user) {

        this.user = user;
    }
}
