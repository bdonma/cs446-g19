package com.ba.cg.jn.tl.barter;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by Caroline George
 */

public class DependencyCycleUtils {

    private static Map<String, Transaction> selfuserTransactionMap = new HashMap<String, Transaction>();
    private static HashMap<String, String> selfuserListOfFriends = new HashMap<>();
    private static Map<String, Transaction> userTransactionMap = new HashMap<String, Transaction>();


    private static String currentFBUserID;

    private static String getFBuserID() {
        return currentFBUserID;
    }

    private static void setFBuserID(String fui) {
        currentFBUserID = fui;
    }

    //TTRansactions: userTransaction
    //              stop the original loop  = true
    //STRansactions: secondTransaction
    //              continue the loop      = false

    public static void handleDependencies(Transaction t, Transaction s, String lender, String lender2) {


        //if both transactions uses money
        if ((t.getBarterValue() == 0.0f) && (s.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);

                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(s.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();

                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);

                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            }
        } //endif both transactions uses money
        //else if t transaction uses money
        else if ((t.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);


                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                //Set up transaction
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                float sUnitCost = s.getCashValue() / s.getBarterValue();
                float rBarterValue = t.getCashValue() / sUnitCost;
                r.setBarterValue(rBarterValue);
                FirebaseUtilities.addTransaction(r);


                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                float sBarterValue = s.getBarterValue() - rBarterValue;
                s.setCashValue(sNewCost);

                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            }
        } //endif t transaction uses money
        //else if s transaction uses money
        else if ((s.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tUnitValue = t.getCashValue() / t.getBarterValue();
                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                float tNewBartValue = t.getCashValue() / tUnitValue;
                t.setBarterValue(tNewBartValue);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                r.setBarterValue(s.getBarterValue());
                FirebaseUtilities.addTransaction(r);

                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);

                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);

            }
        } //endif s transaction uses money
        //else neither use money
        else {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tUnitValue = t.getCashValue() / t.getBarterValue();
                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                float tNewBartValue = t.getCashValue() / tUnitValue;
                t.setBarterValue(tNewBartValue);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());

                float sUnitCost = s.getCashValue() / s.getBarterValue();
                float rBarterValue = t.getCashValue() / sUnitCost;
                r.setBarterValue(rBarterValue);
                FirebaseUtilities.addTransaction(r);


                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                float sNewBarterValue = s.getBarterValue() - rBarterValue;
                s.setBarterValue(sNewBarterValue);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);

            }
        } //endelse neither use money
    }




    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */

    private static void selfuserGetInitialListOfTransaction() {
        Query transactionListQuery = FirebaseUtilities.getDatabaseReference().child("transactions");
        transactionListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    Map<String, Boolean> transactionIDs = new HashMap<String, Boolean>();

                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction currentTransaction = transactionSnapshot.getValue(Transaction.class);

                        if (currentTransaction.getCreatorId().equals(FirebaseUtilities.getUser().getUid())) {
                            transactionIDs.put(transactionSnapshot.getKey(), true);
                            continue;
                        } // if

                        for (Map.Entry<String, Boolean> entry : currentTransaction.getTargetUserIds().entrySet()) {
                            if (entry.getKey().equals(FirebaseUtilities.getUser().getUid())) {
                                transactionIDs.put(transactionSnapshot.getKey(), true);
                            } // if
                        } // for
                        selfuserStartUserTransactions();
                    } // for

                    FirebaseUtilities.getDatabaseReference().child("users").child(FirebaseUtilities.getUser().getUid()).child("transactions").setValue(transactionIDs);
                } // if
            } // addListenerForSingleValueEvent

            @Override
            public void onCancelled(DatabaseError databaseError) {

            } // onCancelled

        }); // addListenerForSingleValueEvent

    } // getInitialListOfTransaction




    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */
    private static void selfuserStartUserTransactions() {
        Query userQuery = FirebaseUtilities.getListOfUserTransactionsWithUID(FirebaseUtilities.getUser().getUid());
        userQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildAdded: Something was added to transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly added transaction
                    final String transactionID = dataSnapshot.getKey();

                    // Get transaction with specified transactionID
                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                                selfuserTransactionMap.put(transactionID, transaction);

                                //From dashboard presenter
                                //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(selfuserTransactionMap.values());
                                //calculateAmountsForTransaction();
                                //mView.showListOfTransactions(transactionsToAdd);
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleValueEvent
                } // if
            } // onChildAdded

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildChanged: Something was changed in transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly changed transaction
                    final String transactionID = dataSnapshot.getKey();

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);

                                // Get transaction with specified transactionID
                                if (selfuserTransactionMap.containsKey(transactionID)) {
                                    selfuserTransactionMap.put(transactionID, transaction);

                                    //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(selfuserTransactionMap.values());
                                    //calculateAmountsForTransaction();
                                    //mView.showListOfTransactions(transactionsToAdd);
                                } // if
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleValueEvent
                } // if
            } // onChildChanged

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", "startUserTransactions:onChildRemoved: Something was removed from transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly removed transaction
                    final String transactionID = dataSnapshot.getKey();

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                if (selfuserTransactionMap.containsKey(transactionID)) {
                                    selfuserTransactionMap.remove(transactionID);

                                    //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(selfuserTransactionMap.values());
                                    //calculateAmountsForTransaction();
                                    // mView.showListOfTransactions(transactionsToAdd);
                                } // if
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleEvent
                } // if
            } // onChildRemoved

            // Not used. Transactions are not set with any priority
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildMoved: Something was moved in transactions");
            } // onChildMoved

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE", "startUserTransactions:onCancelled");
            } // onCancelled

        }); // addChildEventListener

    } // startUserTransactions


    private static void selfuserGetUserFriends() {
        RealmResults<FacebookFriend> selfFriends = FacebookUtils.getRealmFacebookResults();
        List<String> adapterFriends = new ArrayList<>();

        for (int i = 0; i < selfFriends.size(); i++) {
            FacebookFriend friend = selfFriends.get(i);
            selfuserListOfFriends.put(friend.getFbId(), friend.getName());
            adapterFriends.add(selfFriends.get(i).getName());
        }

    }



    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */

    private static void userGetInitialListOfTransaction(final String taggedUserID) {
        Query transactionListQuery = FirebaseUtilities.getDatabaseReference().child("transactions");
        transactionListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    Map<String, Boolean> transactionIDs = new HashMap<String, Boolean>();

                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction currentTransaction = transactionSnapshot.getValue(Transaction.class);

                        if (currentTransaction.getCreatorId().equals(taggedUserID)) {
                            transactionIDs.put(transactionSnapshot.getKey(), true);
                            continue;
                        } // if

                        for (Map.Entry<String, Boolean> entry : currentTransaction.getTargetUserIds().entrySet()) {
                            if (entry.getKey().equals(taggedUserID)) {
                                transactionIDs.put(transactionSnapshot.getKey(), true);
                            } // if
                        } // for
                        userStartUserTransactions(taggedUserID);
                    } // for

                    FirebaseUtilities.getDatabaseReference().child("users").child(taggedUserID).child("transactions").setValue(transactionIDs);
                } // if
            } // addListenerForSingleValueEvent

            @Override
            public void onCancelled(DatabaseError databaseError) {

            } // onCancelled

        }); // addListenerForSingleValueEvent

    } // getInitialListOfTransaction




    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */
    private static void userStartUserTransactions(String taggedUserID) {
        Query userQuery = FirebaseUtilities.getListOfUserTransactionsWithUID(taggedUserID);
        userQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildAdded: Something was added to transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly added transaction
                    final String transactionID = dataSnapshot.getKey();

                    // Get transaction with specified transactionID
                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                                userTransactionMap.put(transactionID, transaction);


                                //From dashboard presenter
                                //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                //calculateAmountsForTransaction();
                                //mView.showListOfTransactions(transactionsToAdd);
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleValueEvent
                } // if
            } // onChildAdded

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildChanged: Something was changed in transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly changed transaction
                    final String transactionID = dataSnapshot.getKey();

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);

                                // Get transaction with specified transactionID
                                if (userTransactionMap.containsKey(transactionID)) {
                                    userTransactionMap.put(transactionID, transaction);

                                    //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                    //calculateAmountsForTransaction();
                                    //mView.showListOfTransactions(transactionsToAdd);
                                } // if
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleValueEvent
                } // if
            } // onChildChanged

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", "startUserTransactions:onChildRemoved: Something was removed from transactions");

                if (dataSnapshot != null) {
                    // Get the UID of the newly removed transaction
                    final String transactionID = dataSnapshot.getKey();

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                if (userTransactionMap.containsKey(transactionID)) {
                                    userTransactionMap.remove(transactionID);

                                    //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                    //calculateAmountsForTransaction();
                                    // mView.showListOfTransactions(transactionsToAdd);
                                } // if
                            } // if
                        } // onDataChange

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        } // onCancelled

                    }); // addListenerForSingleEvent
                } // if
            } // onChildRemoved

            // Not used. Transactions are not set with any priority
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildMoved: Something was moved in transactions");
            } // onChildMoved

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE", "startUserTransactions:onCancelled");
            } // onCancelled

        }); // addChildEventListener

    } // startUserTransactions
}