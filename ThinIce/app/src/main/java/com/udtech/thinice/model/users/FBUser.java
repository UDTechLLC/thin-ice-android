package com.udtech.thinice.model.users;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class FBUser {
    private long fbId;

    public FBUser(long fbId) {
        this.fbId = fbId;
    }

    public FBUser() {
    }

    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }
}
