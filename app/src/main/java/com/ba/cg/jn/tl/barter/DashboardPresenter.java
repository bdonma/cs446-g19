package com.ba.cg.jn.tl.barter;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ba.cg.jn.tl.barter.FirebaseUtilities.getDatabaseReference;

/**
 * Created by JasonNgo on 2018-03-20.
 */

public class DashboardPresenter {

    private DashboardViewInterface mView;
    private Map<String, Transaction> transactionMap = new HashMap<String, Transaction>();

    public DashboardPresenter(DashboardViewInterface view) {
        this.mView = view;
    }

    /**
     * Initially called by DashboardPresenter during startup. Searches database for existing
     * transactions that the current user is a part of. Adds the transactions to a list that
     * is added as an entry to the record identified by the currentUser uid
     */
    public void getInitialListOfTransaction() {
        Query transactionListQuery = FirebaseUtilities.getAllTransactions();
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

                    FirebaseUtilities.setListOfUserTransactionsWithUID(FirebaseUtilities.getUser().getUid(), transactionIDs);
                } // if
            } // addListenerForSingleValueEvent

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG", "getInitialListOfTransaction:onCancelled Error");
            } // onCancelled

        }); // addListenerForSingleValueEvent

    } // getInitialListOfTransaction

    /**
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */
    public void startUserTransactions() {

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
                                transactionMap.put(transactionID, transaction);

                                List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
                                calculateAndShowAmountsForTransaction();
                                mView.showListOfTransactions(transactionsToAdd);
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
                                if (transactionMap.containsKey(transactionID)) {
                                    transactionMap.put(transactionID, transaction);

                                    List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
                                    calculateAndShowAmountsForTransaction();
                                    mView.showListOfTransactions(transactionsToAdd);
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
                                if (transactionMap.containsKey(transactionID)) {
                                    transactionMap.remove(transactionID);

                                    List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
                                    calculateAndShowAmountsForTransaction();
                                    mView.showListOfTransactions(transactionsToAdd);
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

    /**
     * Calculates the amount the current user is owed/due and updates those values.
     */
    public void calculateAndShowAmountsForTransaction() {

        float amountIOwe = 0, amountIAmDue = 0;

        for (Map.Entry<String, Transaction> entry : transactionMap.entrySet()) {
            Transaction currentTransaction = entry.getValue();

            // The current user is the creator of the transaction
            if (currentTransaction.getCreatorId().equals(FirebaseUtilities.getUser().getUid())) {
                if (currentTransaction.getIsBorrowed()) {
                    amountIAmDue += currentTransaction.getCashValue();
                } else {
                    amountIOwe += currentTransaction.getCashValue();
                }
            } else {
                if (currentTransaction.getIsBorrowed()) {
                    amountIOwe += currentTransaction.getCashValue();
                } else {
                    amountIAmDue += currentTransaction.getCashValue();
                }
            }
        }

        mView.showAmountsOfCurrentUser(amountIOwe, amountIAmDue);
    }

} // DashboardPresenter
