package com.ba.cg.jn.tl.barter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

//import io.realm.RealmResults;

/**
 * Created by brandonma on 2018-03-23.
 */

public class AddTransactionFormPresenter {
    private AddTransactionViewInterface mView;
    private HashMap<String, String> mFacebookFriendsMap = new HashMap<>();

    public AddTransactionFormPresenter(AddTransactionViewInterface v) {
        this.mView = v;
    }

    public void createTransaction(String transactionName, Map<String, Boolean> targetUserIds,
                                  float cashValue, float barterValue, String barterUnit, boolean isBorrowed,
                                  String notes, Map<String, Boolean> acceptedIds) {


        Log.d("transaction name", transactionName);
        Log.d("people", targetUserIds.toString());
        Log.d("cash value", Float.toString(cashValue));
        Log.d("barter value", Float.toString(barterValue));
        Log.d("barter unit", barterUnit);
        Log.d("is borrowed", Boolean.toString(isBorrowed));
        Log.d("notes", notes);

        Date date = new Date();
//        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = dt.format(date);

        Transaction transaction = new Transaction(transactionName, stringDate, FirebaseUtilities.getUser().getEmail(),
                targetUserIds, cashValue, barterValue, barterUnit, isBorrowed, false, false,
                notes, acceptedIds);

        //prepareAsyncData(transaction);
        FirebaseUtilities.addTransaction(transaction);
    }

    public void startGettingFacebookFriends() {
        RealmResults<FacebookFriend> results = FacebookUtils.getRealmFacebookResults();
        List<String> adapterFriends = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            FacebookFriend friend = results.get(i);
            mFacebookFriendsMap.put(friend.getName(), friend.getFbId());
            adapterFriends.add(results.get(i).getName());
        }
        mView.addPeopleAdapter(adapterFriends);
    }

    public void getUserUuid(String userName) {
        if (mFacebookFriendsMap.containsKey(userName)) {
            // user is chosen using name
            String facebookId = mFacebookFriendsMap.get(userName);
            Query facebookUser = FirebaseUtilities.getListOfUserWithFacebookId(facebookId);

            facebookUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        System.out.println(child.getKey()); // users
                        for (DataSnapshot value : child.getChildren()) {
                            if (value.getKey().equals("email")) {
                                email = value.getValue().toString();
                                mView.sendingTargetId(email);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mView.sendToast();
                }
            });
        } else {
            // Sending Email through
            mView.sendingTargetId(userName);
        }
    }
}
