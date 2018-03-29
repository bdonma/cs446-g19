package com.ba.cg.jn.tl.barter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnDashboardActionSelected {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUtilities.addUser();
                FacebookUtils.getFriendOnApp();
// This will automatically trigger the migration if needed
                for (UserInfo iuser : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                    if (iuser.getProviderId().equals("facebook.com")) {
                        System.out.println("User is signed in with Facebook");
                        //btnFacebookLogin = v.findViewById(R.id.SearchFriendseditText);
                        // startFacebookPermissionsFrag();
                    }
                }
                startDashboardFrag();
                // ...
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm.getDefaultInstance().close();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);

        final RealmConfiguration configuration = new RealmConfiguration.Builder().name("sample.realm").schemaVersion(2).migration(new RealmMigration()).build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);

        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            signInUi();
        } else {
            startDashboardFrag();
        }
    }

    private void startDashboardFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment dashBoardFrag = new DashboardFragment();

        fragmentTransaction.replace(R.id.fragment_container, dashBoardFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void startFacebookPermissionsFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment facebookPermissionsFrag = new FacebookPermissionsFragment();

        fragmentTransaction.replace(R.id.fragment_container, facebookPermissionsFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void signInUi() {
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder().build();
        AuthUI.IdpConfig emailIdp = new AuthUI.IdpConfig.EmailBuilder().build();
        AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.FacebookBuilder()
                .setPermissions(Arrays.asList("user_friends", "public_profile", "email"))
                .build();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(facebookIdp, googleIdp, emailIdp))
                        .build()
                , RC_SIGN_IN);
    }


    private void signOut() {
        AuthUI.getInstance()
                .signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        signInUi();
                    }
                });
    }

    private void deleteAccount() {
        AuthUI.getInstance().delete(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        signInUi();
                    }
                });
    }

    @Override
    public void callbackSignOut() {
        signOut();
    }

    @Override
    public void callbackDeleteAccount() {
        deleteAccount();
    }

    @Override
    public void startAddTransactionFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment addTransactionFrag = new AddTransactionFormFragment();

        fragmentTransaction.replace(R.id.fragment_container, addTransactionFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void callbackStartTransactionDetailsFragment(Transaction transaction) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment addTransactionFrag = new TransactionFragment();

        Bundle args = new Bundle();
        args.putString(TransactionFragment.ARGS_TRANSACTION_ID, transaction.getTransactionId());
        addTransactionFrag.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container, addTransactionFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
