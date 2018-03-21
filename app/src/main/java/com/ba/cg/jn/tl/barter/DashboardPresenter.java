package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    public void getUserTransactions() {
        // This is where I want to query for the list of transactions

        Query query = getDatabaseReference().child("transactions");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DEBUG", "getUserTransactios");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void searchForUser(String userID) {

    }
}
