package com.ba.cg.jn.tl.barter;

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

/**
 * Created by Caroline George
 */

public class DependencyCycleUtils {

    private static Map<String, Transaction> selfuserTransactionMap = new HashMap<String, Transaction>();
    private static Map<String, Transaction> userTransactionMap = new HashMap<String, Transaction>();



    public void handleDependencies() {



        selfuserStartUserTransactions();
        selfuserGetInitialListOfTransaction();

        //(every Transaction t for user)
        for (Map.Entry<String, Transaction> entry : selfuserTransactionMap.entrySet())
        {
            //user borrowed money
            Transaction t = entry.getValue();
            if (t.getIsBorrowed()) {
                //to the lender

                Map <String, Boolean> newTargetUsers = t.getTargetUserIds();
                nextTTransaction:
                for (Map.Entry<String, Boolean> tuser : newTargetUsers.entrySet()) {
                    //do graph call to get facebook setup
                    String lender = tuser.getKey();
                    //if lender has facebook and is a facebook friend
                    FacebookUtils.getUserId()
                    if  (lender is member of user friends){
                        userGetInitialListOfTransaction(lender);
                        userStartUserTransactions(lender);
                        //for all transactions where the lender borrowed money that wasnt from this transaction
                        for (Map.Entry<String, Transaction> entry2 : userTransactionMap.entrySet()) {
                            Transaction s = entry2.getValue();


                            if ((s.getTransactionID != t.getTransactionID) && (s.getIsBorrowed())) {

                                nextSTransaction:
                                for (Map.Entry<String, Boolean> lender2 : s.getTargetUserIds()) {
                                    //lender's lender has facebook and is a friend of lender and  lender's lender is friend of user
                                    if ( (lender2 is member of lender friends) &&
                                    (lender2 is member of user friends)){
                                        //if both transactions uses money
                                        if ((t.getBarterValue() == 0.0f) && (s.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(t.getBarterValue());
                                                r.setBarterUnit(t.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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

                                                break nextTTransaction;
                                            }
                                        } //endif both transactions uses money
                                        //else if t transaction uses money
                                        else if ((t.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                //Set up transaction
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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

                                                break nextTTransaction;
                                            }
                                        } //endif t transaction uses money
                                        //else if s transaction uses money
                                        else if ((s.getBarterValue() == 0.0f)) {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);
                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);

                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);

                                                r.setCashValue(s.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit() ());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);

                                                float tUnitValue = t.getCashValue() / t.getBarterValue();
                                                float tNewCost = t.getCashValue() - s.getCashValue();
                                                t.setCashValue(tNewCost);
                                                float tNewBartValue = t.getCashValue() / tUnitValue;
                                                t.setBarterValue(tNewBartValue);

                                                //FirebaseUtilities.deleteTransaction(s);

                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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
                                                break nextTTransaction;

                                            }
                                        } //endif s transaction uses money
                                        //else neither use money
                                        else {
                                            if (t.getCashValue() == s.getCashValue()) {
                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
                                                rtargetuserid.put(lender2.getKey(), lender2.getValue());
                                                r.setTargetUserIds(rtargetuserid);
                                                r.setCashValue(t.getCashValue());
                                                r.setBarterValue(s.getBarterValue());
                                                r.setBarterUnit(s.getBarterUnit());
                                                r.setNotes(s.getNotes());
                                                FirebaseUtilities.addTransaction(r);
                                                //FirebaseUtilities.deleteTransaction(s);
                                                //FirebaseUtilities.deleteTransaction(t);
                                                break nextTTransaction;
                                            } else if (t.getCashValue() > s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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
                                                break nextSTransaction;
                                            } else if (t.getCashValue() < s.getCashValue()) {

                                                Transaction r = new Transaction();
                                                r.setName(s.getName());
                                                r.setCreatorId(t.getCreatorId());
                                                Map<String, Boolean> rtargetuserid;
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

    public static void selfuserGetInitialListOfTransaction() {
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
    public static void selfuserStartUserTransactions() {
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





    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */

    public static void userGetInitialListOfTransaction(final String taggedUserID) {
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
    public static void userStartUserTransactions(String taggedUserID) {
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