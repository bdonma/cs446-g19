package com.ba.cg.jn.tl.barter;

import android.view.View;

public class TransactionPresenter {
    private View view;
    private boolean editModeOn;

    public TransactionPresenter(View v){
        view = v;
        editModeOn = false;
    }

    public void toggleEditMode(){
        editModeOn = !editModeOn;
    }

    public boolean getEditModeOn(){
        return editModeOn;
    }

    public void saveTransaction(){
        // TODO: update record in Firebase
    }

}
