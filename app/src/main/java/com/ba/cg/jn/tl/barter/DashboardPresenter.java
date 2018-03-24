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
        Query transactionListQuery = FirebaseUtilities.getDatabaseReference().child("transactions");
        transactionListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    List<String> transactionIDs = new ArrayList<String>();

                    for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                        Transaction currentTransaction = transactionSnapshot.getValue(Transaction.class);

                        if (currentTransaction.getCreatorId().equals(FirebaseUtilities.getUser().getUid())) {
                            transactionIDs.add(transactionSnapshot.getKey());
                            continue;
                        } // if

                        for (String targetID : currentTransaction.getTargetUserIds()) {
                            if (targetID.equals(FirebaseUtilities.getUser().getUid())) {
                                transactionIDs.add(dataSnapshot.getKey());
                                break;
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
                    final String transactionID = dataSnapshot.getValue(String.class);

                    // Get transaction with specified transactionID
                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);
                                transactionMap.put(transactionID, transaction);

                                List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
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
                    final String transactionID = dataSnapshot.getValue(String.class);

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Transaction transaction = dataSnapshot.getValue(Transaction.class);

                                // Get transaction with specified transactionID
                                if (transactionMap.containsKey(transactionID)) {
                                    transactionMap.put(transactionID, transaction);

                                    List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
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
                    final String transactionID = dataSnapshot.getValue(String.class);

                    Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                    transactionQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                if (transactionMap.containsKey(transactionID)) {
                                    transactionMap.remove(transactionID);

                                    List<Transaction> transactionsToAdd = new ArrayList<Transaction>(transactionMap.values());
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

} // DashboardPresenter
