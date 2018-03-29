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

    public static void handleDependencies() {

        selfuserStartUserTransactions();
        selfuserGetInitialListOfTransaction();
        selfuserGetUserFriends();

        //(every Transaction t for user)
        for (Map.Entry<String, Transaction> entry : selfuserTransactionMap.entrySet())
        {
            setFBuserID(null);
            Transaction t = entry.getValue();

            //user borrowed money
            if (t.getIsBorrowed()) {
                //to the lender

                Map <String, Boolean> newTargetUsers = t.getTargetUserIds();

                //label to break out of loop when done with the current transaction
                nextTTransaction:
                for (Map.Entry<String, Boolean> tuser : newTargetUsers.entrySet()) {
                    final String lender = tuser.getKey();

                    Query selfuserListQuery = FirebaseUtilities.getDatabaseReference().child("users");
                    selfuserListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot != null) {

                                for (DataSnapshot userFBSnapshot : dataSnapshot.getChildren()) {

                                    if (userFBSnapshot.getKey() == lender) {
                                        //lenderfacebookID = userFBSnapshot.getValue(_u);
                                        if (userFBSnapshot.child("facebookUserId").getValue() != null) {
                                            Log.d("found fbid", userFBSnapshot.getValue().toString());
                                            String lenderfacebookID =  (String) userFBSnapshot.child("facebookUserId").getValue();
                                            setFBuserID(lenderfacebookID);
                                        } else{
                                            Log.d("fbid", "FBID not found");
                                            setFBuserID(null);
                                            //lenderfacebookID = (String) userFBSnapshot.child("facebookUserId").getValue();
                                        }
                                    }


                                } // for
                            } // if
                        } // addListenerForSingleValueEvent

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("fbid", "Response failed");
                        } // onCancelled

                    }); // addListenerForSingleValueEvent


                    String firstlenderFBid = getFBuserID();
                    if (firstlenderFBid == null) {
                        break nextTTransaction;
                    }
                    //Check if the lender is friends
                    else if (selfuserListOfFriends.get(firstlenderFBid) != null) {
                        userGetInitialListOfTransaction(lender);
                        userStartUserTransactions(lender);
                        //for all transactions where the lender borrowed money that wasnt from this transaction
                        for (Map.Entry<String, Transaction> entry2 : userTransactionMap.entrySet()) {
                            Transaction s = entry2.getValue();

                            if ((s.getTransactionId() != t.getTransactionId()) && (s.getIsBorrowed())) {

                                nextSTransaction:
                                for (Map.Entry<String, Boolean> lender2 : s.getTargetUserIds().entrySet()) {
                                    setFBuserID(null);

                                    Query userListQuery = FirebaseUtilities.getDatabaseReference().child("users");
                                    userListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot != null) {

                                                for (DataSnapshot userFBSnapshot : dataSnapshot.getChildren()) {

                                                    if (userFBSnapshot.getKey() == lender) {
                                                        //lenderfacebookID = userFBSnapshot.getValue(_u);
                                                        if (userFBSnapshot.child("facebookUserId").getValue() != null) {
                                                            Log.d("found fbid", userFBSnapshot.getValue().toString());
                                                            String lender2facebookID =  (String) userFBSnapshot.child("facebookUserId").getValue();
                                                            setFBuserID(lender2facebookID);
                                                        } else{
                                                            Log.d("fbid", "FBID not found 2");
                                                            setFBuserID(null);
                                                            //lenderfacebookID = (String) userFBSnapshot.child("facebookUserId").getValue();
                                                        }
                                                    }


                                                } // for
                                            } // if
                                        } // addListenerForSingleValueEvent

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("fbid", "Second fid failed");
                                        } // onCancelled

                                    }); // addListenerForSingleValueEvent



                                    String secondlenderFBid = getFBuserID();
                                    if (secondlenderFBid == null) {
                                        break nextSTransaction;
                                    }
                                    //lender's lender has facebook and is a friend of lender and  lender's lender is friend of user
                                    else if ((selfuserListOfFriends.get(secondlenderFBid) != null)) {
                                        //if both transactions uses money
                                        if ((t.getBarterValue() == 0.0f) && (s.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(t.getBarterValue());
                                                r.setBarterUnit(t.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());
                                                //FirebaseUtilities.deleteTransaction(t);
                                                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());

                                                r.setTargetUserIds(rtargetuserid);
                                                r.setCashValue(s.getCashValue());
                                                r.setBarterValue(t.getBarterValue());
                                                r.setBarterUnit(t.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);

                                                float tNewCost = t.getCashValue() - s.getCashValue();
                                                t.setCashValue(tNewCost);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());

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

                                                break nextTTransaction;
                                            }
                                        } //endif both transactions uses money
                                        //else if t transaction uses money
                                        else if ((t.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());
                                                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(s.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);


                                                float tNewCost = t.getCashValue() - s.getCashValue();
                                                t.setCashValue(tNewCost);

                                                //FirebaseUtilities.deleteTransaction(s);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                //Set up transaction
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
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

                                                break nextTTransaction;
                                            }
                                        } //endif t transaction uses money
                                        //else if s transaction uses money
                                        else if ((s.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);
                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());
                                                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
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
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
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
                                                break nextTTransaction;

                                            }
                                        } //endif s transaction uses money
                                        //else neither use money
                                        else {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);
                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());
                                                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
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
                                                FirebaseUtilities.removeTransactionFromUserList(lender2.getKey(), s.getTransactionId());
                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid =  new HashMap<String, Boolean>();
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
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
                                                break nextTTransaction;

                                            }
                                        } //endelse neither use money

                                    } //endif: lender's lender has facebook and is a friend of lender and  lender's lender is friend of user
                                } //endif: getting lender's lender
                            } //extra if
                        } // endfor all transactions where the lender borrowed money that wasnt from this transaction
                    }  // endif of  lender has facebook and is a facebook friend
                }//end of lender for loop
            }
        }
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