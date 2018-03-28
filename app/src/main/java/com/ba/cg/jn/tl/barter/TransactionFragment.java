package com.ba.cg.jn.tl.barter;


import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements TransactionViewInterface {
    public static final String ARGS_TRANSACTION_ID = "transaction_id";

    private static String mTransactionId;
    private TransactionPresenter transactionPresenter;
    private TextView creationDateTextView;
    private TextView friendTextView;
    private TextView amountBorrowedLoanedTextView;
    private TextView notesTextView;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        Bundle args = getArguments();
        mTransactionId = args.getString(ARGS_TRANSACTION_ID, null);
        creationDateTextView = v.findViewById(R.id.creationDateTextView);
        friendTextView = v.findViewById(R.id.friendTextView);
        amountBorrowedLoanedTextView = v.findViewById(R.id.amountBorrowedLoanedTextView);
        notesTextView = v.findViewById(R.id.notesTextView);

        Log.d("TRANSACTION_FRAG", mTransactionId);

        ConstraintLayout layout = (ConstraintLayout) v.findViewById(R.id.transactionLayout);
        TextView amountBorrowedLoanedHeader = v.findViewById(R.id.amountBorrowedLoanedHeader);

        final Button interactiveButton = v.findViewById(R.id.editTransactionButton);
        interactiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transactionPresenter != null) {
                    switch (transactionPresenter.getButtonState()) {
                        case INFORMATION:
                            // Change to editing mode
                            transactionPresenter.setButtonState(TransactionPresenter.ButtonState.EDITING);
                            showInformationScreen();
                            break;
                        case EDITING:
                            transactionPresenter.sendRequestForModification();
                            break;
                        case APPROVAL:
                            transactionPresenter.sendConfirmationForTransaction();
                            break;
                        default:
                            break;
                    }
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


        View approvalView = v.findViewById(R.id.approvalGreyView);
        v.setBackgroundColor(Color.parseColor("#EEEEEE"));

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

    public void showTransactionInformation(Transaction transaction) {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(transaction.getName());
        }

        creationDateTextView.setText(transaction.getDate());
        amountBorrowedLoanedTextView.setText("$ " + String.format("%,.2f", transaction.getCashValue()));
        notesTextView.setText(transaction.getNotes());

//        for (String key : transaction.getTargetUserIds().keySet()) {
//            friendTextView.setText(key);
//        }
    }

    // TODO: Write the code to show approval screen
    public void showApprovalScreen() {
        if (getView() != null) {
            View approvalView = getView().findViewById(R.id.approvalGreyView);
            approvalView.setBackgroundColor(Color.parseColor("#000000"));
            approvalView.setAlpha(1);

            Button interactiveButton = getView().findViewById(R.id.editTransactionButton);
            interactiveButton.setBackgroundColor(Color.parseColor("#66BB6A"));
            interactiveButton.setText("Approve");
            interactiveButton.setEnabled(true);

            Button completeTransactionButton = getView().findViewById(R.id.complete_transaction_button);
            completeTransactionButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            completeTransactionButton.setEnabled(false);
        }
    }

    public void showApprovalScreenWithEditTransactionDisabled() {
        if (getView() != null) {
            View approvalView = getView().findViewById(R.id.approvalGreyView);
            approvalView.setBackgroundColor(Color.parseColor("#000000"));
            approvalView.setAlpha(1);

            Button interactiveButton = getView().findViewById(R.id.editTransactionButton);
            interactiveButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            interactiveButton.setText("Pending Approval");
            interactiveButton.setEnabled(false);

            Button completeTransactionButton = getView().findViewById(R.id.complete_transaction_button);
            completeTransactionButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            completeTransactionButton.setEnabled(false);
        }
    }

    public void showInformationScreen() {
        if (getView() != null) {

            View informationView = getView().findViewById(R.id.approvalGreyView);
            informationView.setBackgroundColor(Color.parseColor("#EEEEEE"));
            informationView.setAlpha(0);

            Button interactiveButton = getView().findViewById(R.id.editTransactionButton);
            interactiveButton.setEnabled(true);

            if (transactionPresenter.getButtonState() == TransactionPresenter.ButtonState.INFORMATION) {
                interactiveButton.setBackgroundColor(Color.parseColor("#66BB6A"));
                interactiveButton.setText("Edit Transaction");
            } else {
                interactiveButton.setBackgroundColor(Color.parseColor("#29B6F6"));
                interactiveButton.setText("Save Transaction");
            }

            Button completeTransactionButton = getView().findViewById(R.id.complete_transaction_button);
            if (transactionPresenter.canCompleteTransaction()) {
                completeTransactionButton.setBackgroundColor(Color.parseColor("#F44336"));
                completeTransactionButton.setEnabled(true);
            } else {
                completeTransactionButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
                completeTransactionButton.setEnabled(false);
            }
        }
    }
  
    public void setFriendTextView(String str){
        friendTextView.setText(str);
    }

    // TODO: write these
    public void enableTransactionCompleteButton() {

    }

    public void disableTransactionCompleteButton() {

    }
}
