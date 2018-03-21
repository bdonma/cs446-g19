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

    public void getUserCredentials() {

    }

    /**
     * Start initial currentUser query. Add valueEventListener to track whenever changes are made
     * to the account and respond accordingly.
     */
    public void startUserTransactions() {
        Query userQuery = getDatabaseReference().child("users").child("email").equalTo(FirebaseUtilities.getUser().getEmail());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> transactionIDs = new ArrayList<String>();

                // TODO: Create a list containing transactionID's currentUser is involved in
                for (DataSnapshot userSnapspot : dataSnapshot.getChildren()) {
                    transactionIDs = userSnapspot.child("transactions").getValue();
                }

               getTransactions(transactionIDs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

                    transactionArrayList = transactionResults;

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
