package com.ba.cg.jn.tl.barter;

import android.util.Log;
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
        EditText transactionNotesEditText = v.findViewById(R.id.transactionNotesEditText);
//        EditText cashValueEditText = v.findViewById(R.id.cashValueEditText);
//        EditText barterValueEditText = v.findViewById(R.id.barterValueEditText);
//        EditText barterUnitEditText = v.findViewById(R.id.barterUnitEditText);
        String transactionName = transactionNameEditText.getText().toString();
        String people = peopleEditText.getText().toString();
//        String cashValue = cashValueEditText.getText().toString();
//        String barterValue = barterValueEditText.getText().toString();
//        String barterUnit = barterUnitEditText.getText().toString();
        String notes = transactionNotesEditText.getText().toString();

        Log.d("transaction name", transactionName);
        Log.d("people", people);
//        Log.d("cash value", cashValue);
//        Log.d("barter value", barterValue);
//        Log.d("barter unit", barterUnit);
        Log.d("notes", notes);
    }
}
