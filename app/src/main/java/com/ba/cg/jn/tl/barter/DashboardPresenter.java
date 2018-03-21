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

    /**
     * Called by DashboardFragment to fetch the list of transactions user is involved in
     */
    public void getUserTransactions() {

        // Order transactions by timestamps
        Query query = getDatabaseReference().child("transactions").orderByKey();
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Transaction> transactions = new ArrayList<Transaction>();

                // Parse through transactions to match user
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // TODO: test when model is complete
//                    Transaction transaction = snapshot.getValue(Transaction.class);

//                    if (transaction.getCreatorId() == FirebaseUtilities.getUser().getEmail()) {
//                        transactions.add(transaction);
//                        continue;
//                    } // if

//                    for (String targetId : transaction.getTargetUserIds()) {
//                        if (targetId == FirebaseUtilities.getUser().getEmail()) {
//                            transactions.add(transaction);
//                            break;
//                        } // if
//                    } // for

                    Log.d("DEBUG", "added transaction");
                } // for

                mView.showListOfTransactions(transactions);

            } // onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "getUserTransactions:onCancelled", databaseError.toException());
            } // onCancelled

        });

    } // getUserTransactions

    public void searchForUser(String userID) {

    }
}
