package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Map;

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
    private static final String _fbAccessToken = "facebookAccessToken";
    private static final String _fbUserId = "facebookUserId";
    private static final String _emailKey = "email";


    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getDatabaseReference() {
        return getDatabase().getReference();
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getUser() {
        return getAuth().getCurrentUser();
    }

    public static void addUser() {
        DatabaseReference s = getDatabaseReference().child(_usersKey).child(getUser().getUid());
        s.child(_emailKey).setValue(getUser().getEmail());
        s.child(_nameKey).setValue(getUser().getDisplayName());
        s.child(_fbAccessToken).setValue(FacebookUtils.getAccessToken().getToken());
        s.child(_fbUserId).setValue(FacebookUtils.getUserId());
    }

    /**
     * Fetches the list of transactions for user with uid
     *
     * @param uid - UID of user whose transactions we want to pull
     * @return returns a Firebase Query object
     */
    public static Query getListOfUserTransactionsWithUID(String uid) {
        return getDatabaseReference().child(_usersKey).child(uid).child(_transactionsKey);
    }

    /**
     * Sets the list of transactions associated with a user id to the value transactions
     * @param uid - uid of the user who's list of transactions will be updated
     * @param transactions - list of transactions
     */
    public static void setListOfUserTransactionsWithUID(String uid, Map<String, Boolean> transactions) {
        getDatabaseReference().child(_usersKey).child(uid).child(_transactionsKey).setValue(transactions);
    }

    /**
     * Fetches the transaction with the specified uid
     *
     * @param uid - UID of the transaction
     * @return returns a Firebase Query object
     */
    public static Query getTransactionForUID(String uid) {
        return getDatabaseReference().child(_transactionsKey).child(uid);
    }

    /**
     * Fetches a list of all transactions currently stored
     * @return returns a Firebase Query object
     */
    public static Query getAllTransactions() {
        return getDatabaseReference().child(_transactionsKey);
    }

} // FirebaseUtilities
