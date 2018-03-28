package com.ba.cg.jn.tl.barter;

import io.realm.DynamicRealm;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by tiffanylui on 2018-03-25.
 */

public class RealmMigration implements io.realm.RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            final RealmObjectSchema userSchema = schema.get("FacebookFriends");
            userSchema.removeField("url");
        }
    }
}
