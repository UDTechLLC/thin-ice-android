package com.udtech.thinice.eventbus.model.user;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class SaveUser {
    boolean account;

    public boolean isAccount() {
        return account;
    }

    public SaveUser(boolean account) {

        this.account = account;
    }
}
