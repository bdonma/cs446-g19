package com.ba.cg.jn.tl.barter;

import io.realm.RealmObject;

/**
 * Created by tiffanylui on 2018-03-24.
 */

public class FacebookFriend extends RealmObject {
    private String fbId;
    private String name;

    public String getFbId() {
        return fbId;
    }

    public String getName() {
        return name;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
