package com.ba.cg.jn.tl.barter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by JasonNgo on 2018-03-20.
 */

public class FirebaseUtilities {

    // Constants;
    private static final String _usersKey = "users";
    private static final String _transactionsKey = "transactions";

    // Transaction fields
    private static final String _barterUnitKey = "barterUnit";
    private static final String _barterValueKey = "barterValue";
    private static final String _cashValueKey = "cashValue";
    private static final String _creatorIdKey = "creatorId";
    private static final String _isActiveKey = "isActive";
    private static final String _isBorrowedKey = "isBorrowed";
    private static final String _isCompletedKey = "isCompleted";
    private static final String _nameKey = "name";
    private static final String _notesKey = "notes";
    private static final String _targetUserIds = "targetUserIds";

    public static FirebaseDatabase getDatabase() { return FirebaseDatabase.getInstance(); }

    public static DatabaseReference getDatabaseReference() { return getDatabase().getReference(); }

    public static FirebaseAuth getAuth() { return FirebaseAuth.getInstance(); }

    public static FirebaseUser getUser() { return getAuth().getCurrentUser(); }

    /**
     * Fetches the list of transactions for user with uid
     *
     * @param uid - UID of user whose transactions we want to pull
     * @return returns a Firebase Query object
     */
    public static Query getListOfUserTransactionsWithUID(String uid) {
        return getDatabaseReference().child(_usersKey).child(uid).child(_transactionsKey);
    }

} // FirebaseUtilities
