package com.udtech.thinice.model.users;

import com.orm.SugarRecord;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class AppUser extends SugarRecord<AppUser> {
    private String email;
    private String pass;
    private User user;

    public AppUser() {
    }

    public AppUser(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
