package com.ba.cg.jn.tl.barter;

import android.view.View;
import android.widget.EditText;

/**
 * Created by brandonma on 2018-03-23.
 */

public class AddTransactionFormPresenter {
    private View v;

    public AddTransactionFormPresenter(View v){
        this.v = v;
    }

    public void createTransaction(){
        EditText transactionNameEditText = v.findViewById(R.id.transactionNameEditText);
        EditText peopleEditText = v.findViewById(R.id.peopleEditText);
//        EditText cashValueEditText = v.findViewById(R.id.cashValueEditText);
//        EditText unitValueEditText = v.findViewById(R.id.unitValueEditText);
//        Spinner transactionTypeSpinner = (Spinner) v.findViewById(R.id.transactionTypeSpinner);
        EditText transactionNotesEditText = v.findViewById(R.id.transactionNotesEditText);
//        Spinner unitSpinner = (Spinner) v.findViewById(R.id.unitSpinner);
        String transactionName = transactionNameEditText.getText().toString();
        String people = peopleEditText.getText().toString();
//        String transactionType = transactionTypeSpinner.getSelectedItem().toString();
//        Double cashValue = Double.parseDouble(cashValueEditText.getText().toString().isEmpty() ? "0" : cashValueEditText.getText().toString());
//        Double unitValue = Double.parseDouble(unitValueEditText.getText().toString().isEmpty() ? "0" : unitValueEditText.getText().toString());
        String notes = transactionNotesEditText.getText().toString();

        Transaction transaction = new Transaction();
    }
}
