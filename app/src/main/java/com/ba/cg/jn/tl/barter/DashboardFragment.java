package com.ba.cg.jn.tl.barter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ba.cg.jn.tl.barter.TransactionAdapter.Transaction;
import com.ba.cg.jn.tl.barter.TransactionAdapter.TransactionAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private OnDashboardActionSelected mCallback;

    // RecyclerView
    private List<Transaction> transactionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionAdapter mAdapter;

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

        Button onboarding = v.findViewById(R.id.onboarding);
        onboarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onBoardingIntent = new Intent(getContext(), OnboardingActivity.class);
                startActivity(onBoardingIntent);
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

        // RecyclerView
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        mAdapter = new TransactionAdapter(transactionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        if (transactionList.size() < 2) {
            prepareTransactionData();
        }

        return v;
    }

    private void prepareTransactionData() {
        Transaction transaction = new Transaction("Brandon Ma", "Feb 08, 2018", "$50.00", true);
        transactionList.add(transaction);

        Transaction transaction2 = new Transaction("Tiffany Lui", "today", "$20.00", false);
        transactionList.add(transaction2);

        mAdapter.notifyDataSetChanged();
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
