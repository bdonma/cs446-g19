package com.ba.cg.jn.tl.barter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements DashboardViewInterface {

    private OnDashboardActionSelected mCallback;

    // Presenter
    private DashboardPresenter mPresenter;

    // RecyclerView
    private RecyclerView mRecyclerView;
    private TransactionAdapter mAdapter = new TransactionAdapter(new ArrayList<Transaction>());

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dashboard_fragment, container, false);

        TextView userGreeting = v.findViewById(R.id.user_greeting);
        userGreeting.setText(getString(R.string.user_greeting, FirebaseUtilities.getUser().getDisplayName()));

        Button signOut = v.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.callbackSignOut();
                getActivity().onBackPressed();
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
                getActivity().onBackPressed();
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
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mRecyclerView.addOnItemTouchListener(new DashboardItemClickListener(getContext(), new DashboardItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Handle item clicks here.

            }
        }));
        return v;
    } // onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new DashboardPresenter(this);
        mPresenter.startUserTransactions();
    } // onViewCreated

    public void showListOfTransactions(List<Transaction> transactions) {
        resetAdapter();
        mAdapter.mTransactions.addAll(transactions);
        mRecyclerView.swapAdapter(mAdapter, false);
    } // showListOfTransactions

    public void resetAdapter() {
        mAdapter = new TransactionAdapter(new ArrayList<Transaction>());
        mRecyclerView.setAdapter(mAdapter);
    } // resetAdapter

    /**
     * TransactionAdapter used to handle interactions with DashboardFragments RecyclerView
     *
     * @author JasonNgo
     */
    private class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

        List<Transaction> mTransactions;

        public TransactionAdapter(List<Transaction> transactions) {
            mTransactions = transactions;
        } // TransactionAdapter

        @Override
        public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.transaction_list_row, parent, false);

            return new TransactionViewHolder(itemView);
        } // onCreateViewHolder

        @Override
        public void onBindViewHolder(TransactionViewHolder holder, int position) {
            Transaction transaction = mTransactions.get(position);
//            holder.title.setText(transaction.getTitle());
//            holder.dateCreated.setText(transaction.getDateCreated());
//            holder.value.setText(transaction.getValue());
        } // onBindViewHolder

        public int getItemCount() {
            return mTransactions.size();
        } // getItemCount

        public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title, dateCreated, value;
            RelativeLayout container;

            public TransactionViewHolder(View transactionView) {
                super(transactionView);
                container = transactionView.findViewById(R.id.transaction_list_row_container);

                // TODO: Set the viewholder values


            } // TransactionViewHolder constructor

            @Override
            public void onClick(View v) {

            }

        } // TransactionViewHolder class

    } // TransactionAdapter

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
