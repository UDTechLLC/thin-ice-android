package com.udtech.thinice.model.users;

import com.orm.SugarRecord;

/**
 * Created by JOkolot on 03.11.2015.
 */
public class TUser extends SugarRecord<TUser> {
    private long twitterId;
    private User user;

    public TUser() {
    }

    public TUser(long twitterId) {
        this.twitterId = twitterId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(long twitterId) {
        this.twitterId = twitterId;
    }
}
