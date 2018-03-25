package com.ba.cg.jn.tl.barter;

import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TransactionPresenter {
    private TransactionViewInterface mView;
    private boolean editModeOn;
    private Transaction transaction;

    public TransactionPresenter(TransactionViewInterface view) {
        this.mView = view;
        editModeOn = false;
        transaction = new Transaction();
    }

    public void toggleEditMode(){
        editModeOn = !editModeOn;
    }

    public boolean getEditModeOn(){
        return editModeOn;
    }


    // ------------------------------------------------------

    public void getTransactionInformation() {

        // When the view is first created query for transaction information
        // TODO: Change to the correct id
        Query transactionQuery = FirebaseUtilities.getTransactionForUID("transaction_uid");
        transactionQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    Transaction currentTransaction = dataSnapshot.getValue(Transaction.class);

                    mView.showTransactionInformation();

                    if (currentTransaction.getIsActive()) {
                        mView.showInformationScreen();
                    } else {
                        mView.showApprovalScreen();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void saveTransaction() {
        // TODO: update record in Firebase
    }


    public void checkForApproval() {

    }
}
