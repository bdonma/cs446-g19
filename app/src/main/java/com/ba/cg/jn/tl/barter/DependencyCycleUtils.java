package com.ba.cg.jn.tl.barter;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by Caroline George
 */

public class DependencyCycleUtils {


    private static String currentFBUserID;

    //TTRansactions: userTransaction
    //              stop the original loop  = true
    //STRansactions: secondTransaction
    //              continue the loop      = false


    //Apply Pivot rules
    public static void handleDependencies(Transaction t, Transaction s, String lender, String lender2) {


        //if both transactions uses money
        if ((t.getBarterValue() == 0.0f) && (s.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);

                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(s.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();

                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);

                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(t.getBarterValue());
                r.setBarterUnit(t.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            }
        } //endif both transactions uses money
        //else if t transaction uses money
        else if ((t.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);


                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                //Set up transaction
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                float sUnitCost = s.getCashValue() / s.getBarterValue();
                float rBarterValue = t.getCashValue() / sUnitCost;
                r.setBarterValue(rBarterValue);
                FirebaseUtilities.addTransaction(r);


                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                float sBarterValue = s.getBarterValue() - rBarterValue;
                s.setCashValue(sNewCost);

                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            }
        } //endif t transaction uses money
        //else if s transaction uses money
        else if ((s.getBarterValue() == 0.0f)) {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tUnitValue = t.getCashValue() / t.getBarterValue();
                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                float tNewBartValue = t.getCashValue() / tUnitValue;
                t.setBarterValue(tNewBartValue);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());

                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                r.setBarterValue(s.getBarterValue());
                FirebaseUtilities.addTransaction(r);

                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);

                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);

            }
        } //endif s transaction uses money
        //else neither use money
        else {
            if (t.getCashValue() == s.getCashValue()) {
                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);
                //FirebaseUtilities.deleteTransaction(s);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());
                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);
            } else if (t.getCashValue() > s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);

                r.setCashValue(s.getCashValue());
                r.setBarterValue(s.getBarterValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());
                FirebaseUtilities.addTransaction(r);

                float tUnitValue = t.getCashValue() / t.getBarterValue();
                float tNewCost = t.getCashValue() - s.getCashValue();
                t.setCashValue(tNewCost);
                float tNewBartValue = t.getCashValue() / tUnitValue;
                t.setBarterValue(tNewBartValue);

                //FirebaseUtilities.deleteTransaction(s);
                FirebaseUtilities.removeTransactionFromUserList(lender2, s.getTransactionId());
                //break nextSTransaction;
                AsyncPrepData.setSecondTransaction(null);
            } else if (t.getCashValue() < s.getCashValue()) {

                Transaction r = new Transaction();
                r.setName(s.getName());
                r.setCreatorId(t.getCreatorId());
                Map<String, Boolean> rtargetuserid = new HashMap<String, Boolean>();
                rtargetuserid.put(lender2, true);
                r.setTargetUserIds(rtargetuserid);
                r.setCashValue(t.getCashValue());
                r.setBarterUnit(s.getBarterUnit());
                r.setNotes(s.getNotes());

                float sUnitCost = s.getCashValue() / s.getBarterValue();
                float rBarterValue = t.getCashValue() / sUnitCost;
                r.setBarterValue(rBarterValue);
                FirebaseUtilities.addTransaction(r);


                float sNewCost = s.getCashValue() - t.getCashValue();
                s.setCashValue(sNewCost);
                float sNewBarterValue = s.getBarterValue() - rBarterValue;
                s.setBarterValue(sNewBarterValue);
                //FirebaseUtilities.deleteTransaction(t);
                FirebaseUtilities.removeTransactionFromUserList(lender, t.getTransactionId());

                //break nextTTransaction;
                AsyncPrepData.setUserTransaction(null);

            }
        } //endelse neither use money
    }
}