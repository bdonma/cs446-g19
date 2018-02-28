package com.ba.cg.jn.tl.barter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardFragment extends Fragment {

    private OnDashboardActionSelected mCallback;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dashboard_fragment, container, false);

        TextView userGreeting = v.findViewById(R.id.user_greeting);
        userGreeting.setText(getString(R.string.user_greeting, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

        Button signOut = v.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.callbackSignOut();
            }
        });

        Button deleteAccount = v.findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.callbackDeleteAccount();
            }
        });

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.startAddTransactionFragment();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardActionSelected) {
            mCallback = (OnDashboardActionSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDashboardActionSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface OnDashboardActionSelected {
        void callbackSignOut();

        void callbackDeleteAccount();

        void startAddTransactionFragment();
    }
}
