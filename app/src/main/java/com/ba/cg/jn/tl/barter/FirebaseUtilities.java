package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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
    private static final String _transactionIdKey = "transactionId";
    private static final String _acceptedIdsKey = "acceptedIds";
    private static final String _dateKey = "date";

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

    public static void checkForExistingUserAccounts() {

        Query userQuery = getDatabaseReference().child(_usersKey).orderByChild(_emailKey).equalTo(FirebaseUtilities.getUser().getEmail());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.getChildrenCount() > 1) {

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            if (userSnapshot.child(_fbUserId).getValue() == null) {

                                final String oldUserKey = userSnapshot.getKey();

                                Map<String, Boolean> transactionsToCopy = (HashMap<String, Boolean>) userSnapshot.child(_transactionsKey).getValue();

                                for (Map.Entry<String, Boolean> entry : transactionsToCopy.entrySet()) {
                                    Query transaction = getDatabaseReference().child(_transactionsKey).child(entry.getKey());
                                    transaction.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                // It'll never be creatorID

                                                Map<String, Boolean> targetUserIds = (HashMap<String, Boolean>) dataSnapshot.child(_targetUserIds).getValue();
                                                Map<String, Boolean> newTargetUserIds = new HashMap<String, Boolean>();

                                                for (Map.Entry<String, Boolean> entry : targetUserIds.entrySet()) {
                                                    if (entry.getKey() == oldUserKey) {
                                                        newTargetUserIds.put(getUser().getUid(), entry.getValue());
                                                    }
                                                }

                                                Map<String, Boolean> acceptedIds = (HashMap<String, Boolean>) dataSnapshot.child(_acceptedIdsKey).getValue();
                                                Map<String, Boolean> newAcceptedIds = new HashMap<String, Boolean>();

                                                for (Map.Entry<String, Boolean> entry : acceptedIds.entrySet()) {
                                                    if (entry.getKey() == oldUserKey) {
                                                        newAcceptedIds.put(getUser().getUid(), entry.getValue());
                                                    }
                                                }

                                                getDatabaseReference().child(_transactionsKey).child(_targetUserIds).setValue(newTargetUserIds);
                                                getDatabaseReference().child(_transactionsKey).child(_acceptedIdsKey).setValue(newAcceptedIds);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                            }
                        }
                    }

                } // if
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    public static void addTransaction(Transaction transaction) {
        DatabaseReference transactionRefPush = getDatabaseReference().child(_transactionsKey).push();
        final DatabaseReference usersRef = getDatabaseReference().child(_usersKey);
        Query testQuery;
        final String transactionKey = transactionRefPush.getKey();
        final DatabaseReference transactionRef = getDatabaseReference().child(_transactionsKey).child(transactionKey);

        // TODO: might not need this first line
        transaction.setTransactionId(transactionKey);

        transactionRef.child(_transactionIdKey).setValue(transactionKey);
        transactionRef.child(_nameKey).setValue(transaction.getName());
        transactionRef.child(_cashValueKey).setValue(transaction.getCashValue());
        transactionRef.child(_isCompletedKey).setValue(false);
        transactionRef.child(_isActiveKey).setValue(false);
        transactionRef.child(_dateKey).setValue(transaction.getDate());
        transactionRef.child(_acceptedIdsKey).setValue(transaction.getAcceptedIds());

        if (transaction.getNotes().length() != 0) {
            transactionRef.child(_notesKey).setValue(transaction.getNotes());
        }

        // TODO: FIX THIS
//        if ((transaction.getBarterValue() > 0) && (transaction.getBarterUnit().length() != 0)) {
            transactionRef.child(_barterUnitKey).setValue(transaction.getBarterUnit());
            transactionRef.child(_barterValueKey).setValue(transaction.getBarterValue());
//        }

        if (transaction.getIsBorrowed()) {
            transactionRef.child(_isBorrowedKey).setValue(true);
        } else {
            transactionRef.child(_isBorrowedKey).setValue(false);
        }

        transactionRef.child(_creatorIdKey).setValue(FirebaseUtilities.getUser().getUid());

        Log.d("targetUserIds", transaction.getTargetUserIds().toString());
        for (String key : transaction.getTargetUserIds().keySet()) {
            final String targetUserEmail = key;
            Log.d("searching for user", targetUserEmail);
            testQuery = FirebaseUtilities.getDatabaseReference().child(_usersKey).orderByChild(_emailKey).equalTo(targetUserEmail);

            testQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userInfo : dataSnapshot.getChildren()) {
                        String targetUID = userInfo.getKey();
                        transactionRef.child(_targetUserIds).child(targetUID).setValue(true);
                        transactionRef.child(_acceptedIdsKey).child(targetUID).setValue(false);

                        usersRef.child(FirebaseUtilities.getUser().getUid()).child(_transactionsKey).child(transactionKey).setValue(true);
                        usersRef.child(targetUID).child(_transactionsKey).child(transactionKey).setValue(true);
                        Log.d("TEST", targetUID);
                    }

                    if(!dataSnapshot.exists()){
                        Log.d("user not found", "adding to Firebase");
                        DatabaseReference newUserRef = usersRef.push();
                        String uuid = newUserRef.getKey();
                        usersRef.child(uuid).child(_transactionsKey).child(transactionKey).setValue(true);
                        usersRef.child(uuid).child(_emailKey).setValue(targetUserEmail);
                        transactionRef.child(_targetUserIds).child(uuid).setValue(true);
                        transactionRef.child(_acceptedIdsKey).child(uuid).setValue(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//
        }
    }

    public static void removeTransactionWithUID(String uid) {
        getDatabaseReference().child(_transactionsKey).child(uid).removeValue();
    }

    public static void removeTransactionFromUserList(String userID, String transactionID) {
        getDatabaseReference().child(_usersKey).child(userID).child(_transactionsKey).child(transactionID).removeValue();
    }

    public static void changeTransactionApproval(String userID, String transactionID, Boolean result) {
        getDatabaseReference().child(_transactionsKey).child(transactionID).child(_acceptedIdsKey).child(userID).setValue(result);
    }

    public static Query getListOfUserWithFacebookId(String fbId) {
        return getDatabaseReference().child(_usersKey).orderByChild(_fbUserId).equalTo(fbId);
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

    public static void modifyTransaction(Transaction transaction) {
        getDatabaseReference().child(_transactionsKey).child(transaction.getTransactionId()).setValue(transaction);
    }

    /**
     * Fetches a list of all transactions currently stored
     * @return returns a Firebase Query object
     */
    public static Query getAllTransactions() {
        return getDatabaseReference().child(_transactionsKey);
    }

} // FirebaseUtilities
