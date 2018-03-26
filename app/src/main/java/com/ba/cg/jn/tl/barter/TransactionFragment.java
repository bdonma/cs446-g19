package com.ba.cg.jn.tl.barter;


import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements TransactionViewInterface {
    public static final String ARGS_TRANSACTION_ID = "transaction_id";

    private static String mTransactionId;
    private TransactionPresenter transactionPresenter;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        Bundle args = getArguments();
        mTransactionId = args.getString(ARGS_TRANSACTION_ID, null);

        Log.d("TRANSACTION_FRAG", mTransactionId);

        ConstraintLayout layout = (ConstraintLayout) v.findViewById(R.id.transactionLayout);
        TextView amountBorrowedLoanedHeader = v.findViewById(R.id.amountBorrowedLoanedHeader);

        final Button editTransactionsButton = v.findViewById(R.id.editTransactionButton);
        editTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionPresenter.toggleEditMode();

                if (transactionPresenter.getEditModeOn()) {
                    editTransactionsButton.setText("Edit Transaction");
                } else {
                    editTransactionsButton.setText("Save");
                }
            }
        });

        Button completeTransactionButton = v.findViewById(R.id.complete_transaction_button);
        completeTransactionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (transactionPresenter.canCompleteTransaction()) {
                    transactionPresenter.completeTransaction();
                    getActivity().onBackPressed();
                } else {
                    // Show toast unable to complete transaction until value is 0
                }
            } // onClick

        });

        Button approveButton = v.findViewById(R.id.approvalOrSendButton);
        approveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO: Logic to check save/approve state
                transactionPresenter.sendConfirmationForTransaction();

            }

        });

//        if(transaction.getIsBorrowed()){
//            amountBorrowedLoanedHeader.setText("Amount Borrowed:");
//        } else{
//            amountBorrowedLoanedHeader.setText("Amount Loaned:");
//        }
//
//        if(transaction.getBarterUnit() != null){
//
//        }
//
//        if(transaction.getNotes() != null){
//
//        }

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionPresenter = new TransactionPresenter(this);

        transactionPresenter.getTransactionInformation(mTransactionId);
    }

    public void showTransactionInformation() {

    }

    // TODO: Write the code to show approval screen
    public void showApprovalScreen() {
        if (getView() != null) {

            View v = getView().findViewById(R.id.approvalGreyView);

            v.setBackgroundColor(Color.parseColor("#000000"));


        }
    }

    public void showInformationScreen() {
        if (getView() != null) {

            View v = getView().findViewById(R.id.approvalGreyView);

            v.setBackgroundColor(Color.parseColor("#ffffff"));


        }
    }
}
