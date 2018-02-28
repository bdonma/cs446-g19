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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnDashboardActionSelected {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startDashboardFrag();
                // ...
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void signInUi() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
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
}
