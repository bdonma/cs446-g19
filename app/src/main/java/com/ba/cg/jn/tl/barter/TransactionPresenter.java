package com.ba.cg.jn.tl.barter;

import android.util.Log;
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

    public void toggleEditMode() {
        editModeOn = !editModeOn;
    }

    public boolean getEditModeOn() {
        return editModeOn;
    }


    // ------------------------------------------------------

    public void getTransactionInformation(String transactionId) {

        // When the view is first created query for transaction information
        // TODO: Change to the correct id
        Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionId);
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
                    } // if

                } // if

            } // onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE", "getTransactionInformation():onCancelled");
            } // onCancelled

        }); // addValueEventListener

    } // getTransactionInformation


    /**
     * Function to be called when the approval button is showing on the view transaction fragment
     * page
     *
     * @param currentTransaction
     */
    public void sendConfirmationForTransaction(Transaction currentTransaction) {

        if (currentTransaction != null) {

//            Map<String, Boolean> acceptedIds = currentTransaction.getAcceptedIds();
//
//            if (acceptedIds.containsKey(FirebaseUtilities.getUser().getUid())) {
//
//                acceptedIds.put(FirebaseUtilities.getUser().getUid(), true);
//                currentTransaction.setAcceptedIds(acceptedIds);
//
//                Boolean result = true;
//                for (Map.Entry<String, Boolean> entry : currentTransaction.getAcceptedIds().entrySet()) {
//                    result = result && entry.getValue();
//                    if (!result) { break; }
//                }
//
//                currentTransaction.setIsActive(result);
//
//                // TODO: Change this to the proper key
//                FirebaseUtilities.getDatabaseReference().child("transactions").child("transaction_key").setValue(currentTransaction);
//
//            }
        }
    }

    public void saveTransaction() {
        // TODO: update record in Firebase
    }


    public void checkForApproval() {

    }
}
