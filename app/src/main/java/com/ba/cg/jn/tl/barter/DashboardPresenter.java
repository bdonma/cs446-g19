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

    private Map<String, Integer> transactionKeys = new HashMap<String, Integer>();
    private List<Transaction> transactions = new ArrayList<Transaction>();

    public DashboardPresenter(DashboardViewInterface view) {
        this.mView = view;
    }

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
                                transactions.add(transaction);

                                int transactionIndex = transactions.indexOf(transaction);
                                transactionKeys.put(transactionID, transactionIndex);

                                mView.showListOfTransactions(transactions);
                            } else {
                                Log.d("DEBUG", "onDataChange returned a null snapshot");
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
                                if (transactionKeys.containsKey(transactionID)) {
                                    int transactionIndex = transactionKeys.get(transactionID);

                                    transactions.set(transactionIndex, transaction);

                                    mView.showListOfTransactions(transactions);
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
                                if (transactionKeys.containsKey(transactionID)) {
                                    int transactionIndex = transactionKeys.get(transactionID);

                                    // TODO: After removing trasnaction, change other keys

                                    transactions.remove(transactionIndex);
                                    transactionKeys.remove(transactionIndex);

                                    mView.showListOfTransactions(transactions);
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
