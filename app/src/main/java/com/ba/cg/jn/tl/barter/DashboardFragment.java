package com.ba.cg.jn.tl.barter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;

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

        return v;
    } // onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new DashboardPresenter(this);

        Transaction transaction1 = new Transaction();
        transaction1.setName("First transaction");
        transaction1.setCashValue((float) 4.20);

        Transaction transaction2 = new Transaction();
        transaction2.setName("Second transaction");
        transaction2.setCashValue((float) 1.99);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        showListOfTransactions(transactions);

        // TODO: Uncomment these
//        mPresenter.getInitialListOfTransaction();
//        mPresenter.startUserTransactions();
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
            holder.name.setText(transaction.getName());
//            holder.dateCreated.setText(transaction.get);

            holder.cashValue.setText("$ " + Float.toString(transaction.getCashValue()));
//            holder.barterValue.setText(Float.toString(transaction.getBarterValue()));

            if (transaction.getIsActive()) {
                if (transaction.getCreatorId() == FirebaseUtilities.getUser().getUid()) {
                    // Current user is the one who created this transaction

                } else {
                    // Current user is one of the users who owes the creator of this transaction

                }
            } else {
                // the transaction is not active yet (acknowledged by all parties)

            }

            if (position % 2 == 0) {
                holder.container.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
            } else {
                holder.container.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
            }

        } // onBindViewHolder

        public int getItemCount() {
            return mTransactions.size();
        } // getItemCount

        public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView name, dateCreated;
            public TextView cashValue, barterValue;
            ConstraintLayout container;

            public TransactionViewHolder(View transactionView) {
                super(transactionView);
                transactionView.setOnClickListener(this);
                container = transactionView.findViewById(R.id.transaction_list_row_container);

                // TODO: Set the viewholder values
                name = (TextView) transactionView.findViewById(R.id.name);
//                dateCreated = (TextView) transactionView.findViewById(R.id.dateCreated);
                cashValue = (TextView) transactionView.findViewById(R.id.cashValue);
//                barterValue = (TextView) transactionView.findViewById(R.id.barterValue);

            } // TransactionViewHolder constructor

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();

                Log.d("SWAG", "Position: " + Integer.toString(position));
            } // onClick

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
