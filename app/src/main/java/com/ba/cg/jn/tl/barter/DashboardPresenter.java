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
import java.util.List;

import static com.ba.cg.jn.tl.barter.FirebaseUtilities.getDatabaseReference;

/**
 * Created by JasonNgo on 2018-03-20.
 */

public class DashboardPresenter {

    private DashboardViewInterface mView;

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
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildChanged: Something was changed in transactions");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", "startUserTransactions:onChildRemoved: Something was removed from transactions");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("FIREBASE", "startUserTransactions:onChildMoved: Something was moved in transactions");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE", "startUserTransactions:onCancelled");
            }
        });
    } // startUserTransactions

    private void getTransactions(List<String> transactionIDs) {
        ArrayList<Transaction> transactionArrayList = new ArrayList<>();

        for (String transactionID : transactionIDs) {
            Query transactionQuery = getDatabaseReference().child("transactions").equalTo(transactionID);
            transactionQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ArrayList<Transaction> transactionResults = new ArrayList<Transaction>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = snapshot.getValue(Transaction.class);
                        transactionResults.add(transaction);
                        Log.d("DEBUG", "added transaction");
                    } // for

//                    transactionArrayList = transactionResults;

                } // onDataChange

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DEBUG", "getUserTransactions:onCancelled", databaseError.toException());
                } // onCancelled

            });
        } // for
    }

    public void searchForUser(String userID) {

    }
}
