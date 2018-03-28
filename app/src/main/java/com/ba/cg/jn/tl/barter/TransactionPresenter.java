package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class TransactionPresenter {
    private TransactionViewInterface mView;
    private boolean editModeOn;
    private Transaction transaction;

    public enum ButtonState {
        APPROVAL, EDITING, INFORMATION
    }

    private ButtonState mButtonState = ButtonState.INFORMATION;

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

    public ButtonState getButtonState() {
        return this.mButtonState;
    }

    public void setButtonState(ButtonState state) {
        this.mButtonState = state;
    }

    public void getTransactionInformation(final String transactionId) {

        Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionId);
        transactionQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    transaction = dataSnapshot.getValue(Transaction.class);
                    mView.showTransactionInformation(transaction);

                    Log.d("creator", transaction.getCreatorId());
                    Log.d("current user", FirebaseUtilities.getUser().getUid());
                    if(transaction.getCreatorId().equals(FirebaseUtilities.getUser().getUid())){
                        for (String key : transaction.getTargetUserIds().keySet()) {
                            Query transactionQuery = FirebaseUtilities.getDatabaseReference().child("users").child(key);
                            transactionQuery.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("facebookUserId").getValue() != null) {
                                        Log.d("found fbid", dataSnapshot.getValue().toString());
                                        String friendName = (String) dataSnapshot.child("name").getValue();
                                        mView.setFriendTextView(friendName);
                                    } else {
                                        Log.d("fbid", "not found");
                                        String friendEmail = (String) dataSnapshot.child("email").getValue();
                                        mView.setFriendTextView(friendEmail);
                                    }

                                } // onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                } // onCancelled

                            });
                        }
                    } else{
                        Query transactionQuery = FirebaseUtilities.getDatabaseReference().child("users").child(transaction.getCreatorId());
                        transactionQuery.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("facebookUserId").getValue() != null) {
                                    Log.d("found fbid", dataSnapshot.getValue().toString());
                                    String friendName = (String) dataSnapshot.child("name").getValue();
                                    mView.setFriendTextView(friendName);
                                }
                            } // onDataChange

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            } // onCancelled

                        });
                    }
                    mView.showTransactionInformation(transaction);

                    if (transaction.getIsActive()) {
                        mButtonState = ButtonState.INFORMATION;
                        mView.showInformationScreen();
                    } else {
                        mButtonState = ButtonState.APPROVAL;

                        if (transaction.getAcceptedIds().get(FirebaseUtilities.getUser().getUid()) == true) {
                            mView.showApprovalScreenWithEditTransactionDisabled();
                        } else {
                            mView.showApprovalScreen();
                        }
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
     * Function to be called when the approval button is showing on the view transaction fragment page
     *
     */
    public void sendConfirmationForTransaction() {

        if (transaction != null) {

            Map<String, Boolean> acceptedIds = transaction.getAcceptedIds();

            if (acceptedIds.containsKey(FirebaseUtilities.getUser().getUid())) {

                acceptedIds.put(FirebaseUtilities.getUser().getUid(), true);
                transaction.setAcceptedIds(acceptedIds);

                Boolean result = true;
                for (Map.Entry<String, Boolean> entry : transaction.getAcceptedIds().entrySet()) {
                    result = result && entry.getValue();
                    if (!result) { break; }
                }

                transaction.setIsActive(result);
                FirebaseUtilities.modifyTransaction(transaction);

            } // if

        } // if

    } // sendConfirmationForTransaction

    /**
     * TODO: Parse the transaction fields for new values
     * Function to be called when the current user modifies the transaction
     */
    public void sendRequestForModification() {

        if (transaction != null) {

            Map<String, Boolean> acceptedIds = transaction.getAcceptedIds();

            // Set the other transaction user accepted values to false
            for (String key : transaction.getAcceptedIds().keySet()) {
                acceptedIds.put(key, false);
            } // for

            acceptedIds.put(FirebaseUtilities.getUser().getUid(), true);
            transaction.setIsActive(false);

            FirebaseUtilities.modifyTransaction(transaction);
        } // if

    } // sendRequestForModification


    public boolean canCompleteTransaction() {
        return transaction.getIsCompleted();
    } // conCompleteTransaction

    /**
     * Function to be called when the current user completes the transaction
     */
    public void completeTransaction() {

        // Remove the transaction from the list of transactions
        FirebaseUtilities.removeTransactionWithUID(transaction.getTransactionId());

        // Remove transaction from creator transaction lists
        FirebaseUtilities.removeTransactionFromUserList(transaction.getCreatorId(), transaction.getTransactionId());

        // Remove transaction from targetedUserID transaction list
        for (String key : transaction.getTargetUserIds().keySet()) {
            FirebaseUtilities.removeTransactionFromUserList(key, transaction.getTransactionId());
        }

    } // completeTransaction

    public void saveTransaction() {
        // TODO: update record in Firebase
    }

} // TransactionPresenter
