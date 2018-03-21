package com.ba.cg.jn.tl.barter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by JasonNgo on 2018-03-20.
 */

public class FirebaseUtilities {

    // Constants;
    private static final String transactions = "transactions";
    private static final String creatorID = "creatorId";

    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getDatabaseReference() {
        return getDatabase().getReference();
    }

}
