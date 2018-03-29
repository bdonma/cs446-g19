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
import android.widget.EditText;
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
    private TextView barterValueTextView;
    private TextView barterUnitTextView;
    private TextView amountBorrowedLoanedHeaderTextView;
    private EditText cashValueEdit;
    private EditText barterValueEdit;
    private EditText barterUnitEdit;
    private EditText notesEdit;
    private ConstraintLayout constraintLayout;
    private android.support.v7.app.ActionBar mActionBar;
    String lastCashValue;
    String lastBarterValue;
    String lastBarterUnit;
    String lastNote;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        Bundle args = getArguments();
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mTransactionId = args.getString(ARGS_TRANSACTION_ID, null);
        creationDateTextView = v.findViewById(R.id.creationDateTextView);
        friendTextView = v.findViewById(R.id.friendTextView);
        amountBorrowedLoanedTextView = v.findViewById(R.id.amountBorrowedLoanedTextView);
        notesTextView = v.findViewById(R.id.notesTextView);
        barterValueTextView = v.findViewById(R.id.barterValueTextView);
        barterUnitTextView = v.findViewById(R.id.barterUnitTextView);
        amountBorrowedLoanedHeaderTextView = v.findViewById(R.id.amountBorrowedLoanedHeader);
        constraintLayout = v.findViewById(R.id.transactionLayout);
        cashValueEdit = new EditText(getActivity());
        barterValueEdit = new EditText(getActivity());
        barterUnitEdit = new EditText(getActivity());
        notesEdit = new EditText(getActivity());

        cashValueEdit.setLayoutParams(amountBorrowedLoanedTextView.getLayoutParams());
        barterValueEdit.setLayoutParams(barterValueTextView.getLayoutParams());
        barterUnitEdit.setLayoutParams(barterUnitTextView.getLayoutParams());
        notesEdit.setLayoutParams(notesTextView.getLayoutParams());

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
                            enableEditing();
                            break;
                        case EDITING:
                            transactionPresenter.setButtonState(TransactionPresenter.ButtonState.INFORMATION);
                            saveTransaction();
                            showInformationScreen();
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
        v.setBackgroundColor(Color.parseColor("#80E0E0E0"));

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
        if (mActionBar != null) {
            mActionBar.setTitle(transaction.getName());
        }

        creationDateTextView.setText(transaction.getDate());
        amountBorrowedLoanedTextView.setText("$ " + String.format("%,.2f", transaction.getCashValue()));
        notesTextView.setText(transaction.getNotes());

        if(transaction.getBarterValue() >= 0){
            barterValueTextView.setText(String.format("%,.2f", transaction.getBarterValue()));
            barterUnitTextView.setText(transaction.getBarterUnit());
        } else{
            barterUnitTextView.setText("");
            barterValueTextView.setText("");
        }

        if(transaction.getIsBorrowed()){
            amountBorrowedLoanedHeaderTextView.setText("Amount borrowed");
        } else {
            amountBorrowedLoanedHeaderTextView.setText("Amount loaned");
        }
    }

    // TODO: Write the code to show approval screen
    public void showApprovalScreen() {
        if (getView() != null) {
            View approvalView = getView().findViewById(R.id.approvalGreyView);
            approvalView.setBackgroundColor(Color.parseColor("#80E0E0E0"));

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
            approvalView.setBackgroundColor(Color.parseColor("#80E0E0E0"));
            approvalView.setAlpha(1);

            Button interactiveButton = getView().findViewById(R.id.editTransactionButton);
            interactiveButton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            interactiveButton.setText("Pending Approval");
            interactiveButton.setEnabled(false);

            Button completeTransactionButton = getView().findViewById(R.id.complete_transaction_button);
            completeTransactionButton.setBackgroundColor(Color.parseColor("#BDBDBD"));
            completeTransactionButton.setEnabled(false);
        }
    }

    public void showInformationScreen() {
        if (getView() != null) {

            View informationView = getView().findViewById(R.id.approvalGreyView);
            informationView.setBackgroundColor(Color.parseColor("#00ffffff"));

            Button interactiveButton = getView().findViewById(R.id.editTransactionButton);
            interactiveButton.setEnabled(true);

            if (transactionPresenter.getButtonState() == TransactionPresenter.ButtonState.INFORMATION) {
                interactiveButton.setBackgroundColor(Color.parseColor("#47445e"));
                interactiveButton.setText("Edit Transaction");
            } else {
                interactiveButton.setBackgroundColor(Color.parseColor("#6bb298"));
                interactiveButton.setText("Save Transaction");
            }

            Button completeTransactionButton = getView().findViewById(R.id.complete_transaction_button);
            if (transactionPresenter.canCompleteTransaction()) {
                completeTransactionButton.setBackgroundColor(Color.parseColor("#F44336"));
                completeTransactionButton.setEnabled(true);
            } else {
                completeTransactionButton.setBackgroundColor(Color.parseColor("#BDBDBD"));
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

    public void enableEditing(){
        lastCashValue = amountBorrowedLoanedTextView.getText().toString().substring(2);
        lastBarterValue = barterValueTextView.getText().toString();
        lastBarterUnit = barterUnitTextView.getText().toString();
        lastNote = notesTextView.getText().toString();

        amountBorrowedLoanedTextView.setVisibility(View.INVISIBLE);
        barterValueTextView.setVisibility(View.INVISIBLE);
        barterUnitTextView.setVisibility(View.INVISIBLE);
        notesTextView.setVisibility(View.INVISIBLE);
        cashValueEdit.setText(lastCashValue);
        barterValueEdit.setText(lastBarterValue);
        barterUnitEdit.setText(lastBarterUnit);
        notesEdit.setText(lastNote);
        constraintLayout.addView(cashValueEdit);
        constraintLayout.addView(barterValueEdit);
        constraintLayout.addView(barterUnitEdit);
        constraintLayout.addView(notesEdit);
    }

    public void saveTransaction(){
        String cashValue = cashValueEdit.getText().toString();
        String barterValue = barterValueEdit.getText().toString();
        String barterUnit = barterUnitEdit.getText().toString();
        String note = notesEdit.getText().toString();
        float cashValueFloat = 0;
        float barterValueFloat;

        try{
            cashValueFloat = Float.parseFloat(cashValue);
            barterValueFloat = Float.parseFloat(barterValue);
        } catch(Exception e){
            barterValueFloat = -1;
            barterUnit = "";
        }

        if(!cashValue.equals(lastCashValue) || !barterValue.equals(lastBarterValue)
                || !barterUnit.equals(lastBarterUnit) || !note.equals(lastNote)) {
            transactionPresenter.saveTransaction(cashValueFloat, barterValueFloat, barterUnit, note);
        }

        constraintLayout.removeView(cashValueEdit);
        constraintLayout.removeView(barterValueEdit);
        constraintLayout.removeView(barterUnitEdit);
        constraintLayout.removeView(notesEdit);
        amountBorrowedLoanedTextView.setText("$ " + String.format("%,.2f", cashValueFloat));

        if(barterValueFloat < 0){
            barterValueTextView.setText("");
        } else {
            barterValueTextView.setText(String.format("%,.2f", barterValueFloat));
        }

        barterUnitTextView.setText(barterUnit);
        notesTextView.setText(note);
        amountBorrowedLoanedTextView.setVisibility(View.VISIBLE);
        barterValueTextView.setVisibility(View.VISIBLE);
        barterUnitTextView.setVisibility(View.VISIBLE);
        notesTextView.setVisibility(View.VISIBLE);
    }
}
