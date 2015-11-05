package com.udtech.thinice.model.users;

import com.orm.SugarRecord;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class FBUser extends SugarRecord<FBUser> {
    private long fbId;
    private User user;

    public FBUser(long fbId) {
        this.fbId = fbId;
    }

    public FBUser() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }
}
