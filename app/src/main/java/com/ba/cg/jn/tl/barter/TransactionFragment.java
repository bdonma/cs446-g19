package com.ba.cg.jn.tl.barter;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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
    private TransactionPresenter transactionPresenter;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_transaction_form, container, false);
        ConstraintLayout layout = (ConstraintLayout) v.findViewById(R.id.transactionLayout);
        TextView amountBorrowedLoanedHeader = v.findViewById(R.id.amountBorrowedLoanedHeader);
        Transaction transaction;

        final Button editTransactionsButton = v.findViewById(R.id.editTransactionButton);
        editTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionPresenter.toggleEditMode();

                if(transactionPresenter.getEditModeOn()){
                    editTransactionsButton.setText("Edit Transaction");
                } else {
                    editTransactionsButton.setText("Save");
                }
            }
        });

        View approvalView = v.findViewById(R.id.approvalGreyView);
        approvalView.setBackgroundColor(20);

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

        transactionPresenter.getTransactionInformation();
    }

    public void showTransactionInformation() {

    }

    // TODO: Write the code to show approval screen
    public void showApprovalScreen() {

    }

    public void showInformationScreen() {

    }
}
