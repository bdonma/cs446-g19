package com.ba.cg.jn.tl.barter;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmResults;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * Created by cg on 02/04/18.
 */

public class AsyncPrepData {

    private static Transaction userTransaction;
    private static Transaction secondTransaction;
    private static Map<String, Transaction> userTransactionMap = new HashMap<String, Transaction>();
    private static HashMap<String, String> selfuserListOfFriends = new HashMap<>();

    private List<Observable<String>> iterateTransactionList;
    //private List<Observer<String>> orIterateTransactionList;

    private Observable<String> oInitUserTransactionList;
    private Observer<String> orInitUserTransactionList;

    private static Observable<String> oUserTransactionList;
    private static Observer<String> orUserTransactionList;

    private static Observable<String> oIterateTransactionList;
    private static Observer<String> orIterateUserTransactionList;


    private static String lenderOneFirebaseUserID;
    private static String secondLenderFirebaseUserID;

    private static String selfFBUserID;
    private static String lenderOneFBUserID;
    private static String secondLenderFBUserID;

    //private static Observer<String> oUserFBid;

    private Observable<String> oLenderOneFBUserID;
    private Observer<String> orLenderOneFBUserID;

    private Observable<String> oSecondLenderFBUserID;
    private Observer<String> orSecondLenderFBUserID;

    private static Boolean isDone = false;



    // private static Observable<String> oSelfFBUserID;

    private static String getSelfFBuserID() {
        return selfFBUserID;
    }
    private static void setSelfFBuserID(String fui) {
        selfFBUserID = fui;
    }



    private static String getLenderOneFirebaseuserID() {
        return lenderOneFirebaseUserID;
    }
    private static void setLenderOneFirebaseUserID(String fui) {
        lenderOneFirebaseUserID = fui;
    }

    private static String getSecondLenderFirebaseuserID() {
        return secondLenderFirebaseUserID;
    }
    private static void setSecondLenderFirebaseUserID(String fui) {
        secondLenderFirebaseUserID = fui;
    }


    private static String getLenderOneFBuserID() {
        return lenderOneFBUserID;
    }
    private static void setLenderOneFBUserID(String fui) {
        lenderOneFBUserID = fui;
    }

    private static String getSecondLenderFBuserID() {
        return secondLenderFBUserID;
    }
    private static void setSecondLenderFBUserID(String fui) {
        secondLenderFBUserID = fui;
    }

    public static Transaction getUserTransaction() {
        return userTransaction;
    }
    public static void setUserTransaction(Transaction t) {
        userTransaction = t;
    }

    public static Transaction getSecondTransaction() {
        return userTransaction;
    }
    public static void setSecondTransaction(Transaction t) {
        userTransaction = t;
    }

    private static Boolean getIsDone() {
        return isDone;
    }
    private static void setIsDone(Boolean b) {isDone = b;}



    //Function Observables
    private static Observable<String> oUserGetInitialListOfTransaction;
    private static Observer<String> orUserGetInitialListOfTransaction;


    //Initializing the observers
    private void initInitalTransactionListObserver() {
        orUserGetInitialListOfTransaction = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Toast.makeText(SimpleRxAndroidActivity.this, "onSubscribe called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String t) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onNext called: " + integer, Toast.LENGTH_SHORT).show();
                userStartUserTransactions();
            }

            @Override
            public void onError(Throwable e) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onError called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onComplete called", Toast.LENGTH_SHORT).show();
                //userStartUserTransactions(m);

            }
        };
    }



    private void initStartTransactionListObserver() {
        orUserTransactionList = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Toast.makeText(SimpleRxAndroidActivity.this, "onSubscribe called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String t) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onNext called: " + integer, Toast.LENGTH_SHORT).show();
                //userGetFaceBookID(t);
            }

            @Override
            public void onError(Throwable e) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onError called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onComplete called", Toast.LENGTH_SHORT).show();
                //userStartUserTransactions(m);


                Observable.fromIterable(userTransactionMap.values()).concatMap(entry2 -> {
                    if ((getUserTransaction() != null) && (entry2 != getUserTransaction()) && (entry2.getIsBorrowed() )) {
                        setSecondTransaction(entry2);

                        Map<String, Boolean> newTargetUsers = entry2.getTargetUserIds();
                        //getTargetUserIds() = {forebaseuserid,boolean}
                        //there is only one entry
                        for (Map.Entry<String, Boolean> tuser : newTargetUsers.entrySet()) {
                            final String lender2 = tuser.getKey();
                            userSecondLenderFaceBookID();
                        }

                    }
                    return Observable.just(entry2);
                });
           }
    };
    }

    //FOr the first lender
    private void initLenderOneFBidObserver() {
        orLenderOneFBUserID = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Toast.makeText(SimpleRxAndroidActivity.this, "onSubscribe called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String t) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onNext called: " + integer, Toast.LENGTH_SHORT).show();
                setLenderOneFBUserID(t);
            }

            @Override
            public void onError(Throwable e) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onError called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onComplete called", Toast.LENGTH_SHORT).show();
                //userStartUserTransactions(m);
                checkIfFirstUsersFriends();

            }
        };
    }

    //FOr the second lender
    private void initNextFBidObserver() {
        orSecondLenderFBUserID = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //Toast.makeText(SimpleRxAndroidActivity.this, "onSubscribe called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String t) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onNext called: " + integer, Toast.LENGTH_SHORT).show();
                setSecondLenderFBUserID(t);
            }

            @Override
            public void onError(Throwable e) {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onError called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                // Toast.makeText(SimpleRxAndroidActivity.this, "onComplete called", Toast.LENGTH_SHORT).show();
                //userStartUserTransactions(m);
               if (checkIfSecondUsersFriends()) {
                   setIsDone(true);
               }
               else {
                   setIsDone(false);
               }

            }
        };
    }


    //Retrieves signed in user's friend list
    private static void selfuserGetUserFriends() {
        RealmResults<FacebookFriend> selfFriends = FacebookUtils.getRealmFacebookResults();
        //List<String> adapterFriends = new ArrayList<>();

        for (int i = 0; i < selfFriends.size(); i++) {
            FacebookFriend friend = selfFriends.get(i);
            selfuserListOfFriends.put(friend.getFbId(), friend.getName());
            //adapterFriends.add(selfFriends.get(i).getName());
        }

    }

    private static void checkIfFirstUsersFriends() {
        String fbid = getLenderOneFBuserID();
        if (fbid == null) {
            //create the transaction
            FirebaseUtilities.addTransaction(getUserTransaction());
        }
        //Check if the lender is friends
        else if (selfuserListOfFriends.get(fbid) != null) {
            userGetInitialListOfTransaction();
        }
    }

    private static Boolean checkIfSecondUsersFriends() {
        String fbid = getSecondLenderFBuserID();
        if (fbid == null) {
            //create the transaction
            return false;
        }
        //Check if the lender is friends
        else if (selfuserListOfFriends.get(fbid) != null) {
            DependencyCycleUtils.handleDependencies(getUserTransaction(),getSecondTransaction(), getLenderOneFirebaseuserID(), getSecondLenderFirebaseuserID());
            return true;
        }
        //if second lender is not friends
        else {
            return false;
        }

    }



    //Initializing the observerables

    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */

    private static void userGetInitialListOfTransaction() {

        final String taggedUserID = getLenderOneFirebaseuserID();
        oUserGetInitialListOfTransaction = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {

                Query transactionListQuery = FirebaseUtilities.getDatabaseReference().child("transactions");
                transactionListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {

                            Map<String, Boolean> transactionIDs = new HashMap<String, Boolean>();

                            for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                                Transaction currentTransaction = transactionSnapshot.getValue(Transaction.class);

                                if (currentTransaction.getCreatorId().equals(taggedUserID)) {
                                    transactionIDs.put(transactionSnapshot.getKey(), true);
                                    continue;
                                } // if

                                for (Map.Entry<String, Boolean> entry : currentTransaction.getTargetUserIds().entrySet()) {
                                    if (entry.getKey().equals(taggedUserID)) {
                                        transactionIDs.put(transactionSnapshot.getKey(), true);
                                        e.onNext(taggedUserID);
                                    } // if
                                } // for

                            } // for

                            //on Complete
                            //userStartUserTransactions(taggedUserID);
                            FirebaseUtilities.getDatabaseReference().child("users").child(taggedUserID).child("transactions").setValue(transactionIDs);
                            e.onComplete();
                        } // if

                    } // addListenerForSingleValueEvent

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    } // onCancelled

                }); // addListenerForSingleValueEvent

            }
        });
        oUserGetInitialListOfTransaction.subscribe(orUserGetInitialListOfTransaction);


    } // getInitialListOfTransaction




    /** From DashboardPresenter
     * Start userQuery to add ChildEventListener to track whenever changes are made
     * to the currentUsers transactions and respond accordingly.
     */
    private  void userStartUserTransactions() {

        final String taggedUserID = getLenderOneFirebaseuserID();
        oUserTransactionList = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {

                Query userQuery = FirebaseUtilities.getListOfUserTransactionsWithUID(taggedUserID);
                userQuery.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("FIREBASE", "startUserTransactions:onChildAdded: Something was added to transactions");

                        if (dataSnapshot != null) {
                            // Get the UID of the newly added transaction
                            final String transactionID = dataSnapshot.getKey();

                            // Get transaction with specified transactionID
                            Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                            transactionQuery.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Transaction transaction = dataSnapshot.getValue(Transaction.class);
                                        userTransactionMap.put(transactionID, transaction);

                                        //CG: NEED OBSERVER FOR EVERY OBSERVABLE IN ITERATETRANSACTIONSLIST
                                        iterateTransactionList.add(Observable.just(transactionID));


                                        //From dashboard presenter
                                        //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                        //calculateAmountsForTransaction();
                                        //mView.showListOfTransactions(transactionsToAdd);
                                    } // if
                                } // onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                } // onCancelled

                            }); // addListenerForSingleValueEvent
                        } // if
                        e.onNext("Done initalizing things");
                        e.onComplete();
                    } // onChildAdded

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("FIREBASE", "startUserTransactions:onChildChanged: Something was changed in transactions");

                        if (dataSnapshot != null) {
                            // Get the UID of the newly changed transaction
                            final String transactionID = dataSnapshot.getKey();

                            Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                            transactionQuery.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Transaction transaction = dataSnapshot.getValue(Transaction.class);

                                        // Get transaction with specified transactionID
                                        if (userTransactionMap.containsKey(transactionID)) {
                                            userTransactionMap.put(transactionID, transaction);

                                            //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                            //calculateAmountsForTransaction();
                                            //mView.showListOfTransactions(transactionsToAdd);
                                        } // if
                                    } // if
                                } // onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                } // onCancelled

                            }); // addListenerForSingleValueEvent
                        } // if
                        e.onNext("Done initalizing things");
                        e.onComplete();
                    } // onChildChanged

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("FIREBASE", "startUserTransactions:onChildRemoved: Something was removed from transactions");

                        if (dataSnapshot != null) {
                            // Get the UID of the newly removed transaction
                            final String transactionID = dataSnapshot.getKey();

                            Query transactionQuery = FirebaseUtilities.getTransactionForUID(transactionID);
                            transactionQuery.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        if (userTransactionMap.containsKey(transactionID)) {
                                            userTransactionMap.remove(transactionID);

                                            //List<Transaction> transactionsToAdd = new ArrayList<Transaction>(userTransactionMap.values());
                                            //calculateAmountsForTransaction();
                                            // mView.showListOfTransactions(transactionsToAdd);
                                        } // if
                                    } // if
                                } // onDataChange

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                } // onCancelled

                            }); // addListenerForSingleEvent
                        } // if
                        e.onNext("Done initalizing things");
                        e.onComplete();
                    } // onChildRemoved

                    // Not used. Transactions are not set with any priority
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("FIREBASE", "startUserTransactions:onChildMoved: Something was moved in transactions");
                    } // onChildMoved

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("FIREBASE", "startUserTransactions:onCancelled");
                    } // onCancelled


                }); // addChildEventListener
            }
        });
        oUserTransactionList.subscribe(orUserTransactionList);

    } // startUserTransactions




    //gets lender 1 facebookid
    private void userLenderOneGetFaceBookID(final String lender) {

        oLenderOneFBUserID = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {

                Query selfuserListQuery = FirebaseUtilities.getDatabaseReference().child("users");
                selfuserListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {

                            for (DataSnapshot userFBSnapshot : dataSnapshot.getChildren()) {

                                if (userFBSnapshot.getKey() == lender) {
                                    //lenderfacebookID = userFBSnapshot.getValue(_u);
                                    if (userFBSnapshot.child("facebookUserId").getValue() != null) {
                                        Log.d("found fbid", userFBSnapshot.getValue().toString());
                                        String lenderfacebookID =  (String) userFBSnapshot.child("facebookUserId").getValue();
                                        //setFBuserID(lenderfacebookID);
                                        e.onNext(lenderfacebookID);
                                        e.onComplete();
                                    } else{
                                        Log.d("fbid", "FBID not found");
                                        //setFBuserID(null);
                                        //lenderfacebookID = (String) userFBSnapshot.child("facebookUserId").getValue();
                                        e.onNext(null);
                                        e.onComplete();
                                    }
                                }


                            } // for
                        } // if
                    } // addListenerForSingleValueEvent

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("fbid", "Response failed");
                    } // onCancelled

                }); // addListenerForSingleValueEvent
            }
        });
        oLenderOneFBUserID.subscribe(orLenderOneFBUserID);

    } //userGetFaceBookID










    private void userSecondLenderFaceBookID() {

        final String lender = getSecondLenderFirebaseuserID();

        oSecondLenderFBUserID = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {

                Query selfuserListQuery = FirebaseUtilities.getDatabaseReference().child("users");
                selfuserListQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {

                            for (DataSnapshot userFBSnapshot : dataSnapshot.getChildren()) {

                                if (userFBSnapshot.getKey() == lender) {
                                    //lenderfacebookID = userFBSnapshot.getValue(_u);
                                    if (userFBSnapshot.child("facebookUserId").getValue() != null) {
                                        Log.d("found fbid", userFBSnapshot.getValue().toString());
                                        String lenderfacebookID =  (String) userFBSnapshot.child("facebookUserId").getValue();
                                        //setFBuserID(lenderfacebookID);
                                        e.onNext(lenderfacebookID);
                                        e.onComplete();
                                    } else{
                                        Log.d("fbid", "FBID not found");
                                        //setFBuserID(null);
                                        //lenderfacebookID = (String) userFBSnapshot.child("facebookUserId").getValue();
                                        //e.onNext(lenderfacebookID);
                                      //  e.onComplete();
                                    }
                                }


                            } // for
                        } // if
                    } // addListenerForSingleValueEvent

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("fbid", "Response failed");
                    } // onCancelled

                }); // addListenerForSingleValueEvent
            }
        });

    } //userGetFaceBookID





    public void initializeObservers() {
        initInitalTransactionListObserver();
        initStartTransactionListObserver();
        initLenderOneFBidObserver();
        initNextFBidObserver();

    }




    public void prepareAsyncData(Transaction t) {

        //User doesn't have facebook
        if (FacebookUtils.getUserId() == null) {
            FirebaseUtilities.addTransaction(t);
        } else {
            setUserTransaction(t);
            initializeObservers();
            selfuserGetUserFriends();
            setSelfFBuserID(FacebookUtils.getUserId());

            Map<String, Boolean> newTargetUsers = t.getTargetUserIds();
            //getTargetUserIds() = {forebaseuserid,boolean}
            //there is only one entry
            for (Map.Entry<String, Boolean> tuser : newTargetUsers.entrySet()) {
                final String lender = tuser.getKey();
                setLenderOneFirebaseUserID(lender);

                //get the lender's id
                userLenderOneGetFaceBookID(lender);

            }
        }
    }

}
