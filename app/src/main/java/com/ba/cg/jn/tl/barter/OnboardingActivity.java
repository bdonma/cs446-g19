package com.ba.cg.jn.tl.barter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OnboardingActivity extends AppCompatActivity {
    static final String TAG = "Onboarding";

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference mDatabase = database.getReference();
//
//        Transaction transaction =
//                new Transaction("My friend", FirebaseAuth.getInstance().getCurrentUser().getUid(), "10.00", "apples", "2");
//        mDatabase.child("transactions").push().setValue(transaction);
        startFirstOnboarding();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startFirstOnboarding() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment onboarding1 = new Onboarding1Fragment();

        fragmentTransaction.replace(R.id.fragment_container, onboarding1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
