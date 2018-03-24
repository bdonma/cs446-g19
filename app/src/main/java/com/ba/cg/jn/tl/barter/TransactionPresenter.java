package com.ba.cg.jn.tl.barter;

import android.view.View;

public class TransactionPresenter {
    private View v;
    private boolean editModeOn;

    public TransactionPresenter(View v){
        this.v = v;
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
