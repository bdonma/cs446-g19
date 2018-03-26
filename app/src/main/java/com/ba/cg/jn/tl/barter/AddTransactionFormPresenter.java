package com.ba.cg.jn.tl.barter;

import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;

/**
 * Created by brandonma on 2018-03-23.
 */

public class AddTransactionFormPresenter {
    private View v;

    public AddTransactionFormPresenter(View v){
        this.v = v;
    }

    public void createTransaction(String transactionName, Map<String, Boolean> targetUserIds,
                                  float cashValue, float barterValue, String barterUnit, boolean isBorrowed,
                                  String notes, Map<String, Boolean> acceptedIds){


        Log.d("transaction name", transactionName);
        Log.d("people", targetUserIds.toString());
        Log.d("cash value", Float.toString(cashValue));
        Log.d("barter value", Float.toString(barterValue));
        Log.d("barter unit", barterUnit);
        Log.d("is borrowed", Boolean.toString(isBorrowed));
        Log.d("notes", notes);

        Date date = new Date();
        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = dt.format(newDate);

        Transaction transaction = new Transaction(transactionName, stringDate, FirebaseUtilities.getUser().getEmail(),
                targetUserIds, cashValue, barterValue, barterUnit, isBorrowed, false, false,
                notes, acceptedIds);

        FirebaseUtilities.addTransaction(transaction);
    }
}
