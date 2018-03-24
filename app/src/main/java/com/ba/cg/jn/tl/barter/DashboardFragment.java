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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // TODO: TIFF LOOK @ THIS. Is this the correct way to like dismiss things
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.onboarding_menu_button:
                Intent onBoardingIntent = new Intent(getContext(), OnboardingActivity.class);
                startActivity(onBoardingIntent);
                return true;
            case R.id.delete_account_menu_button:
                mCallback.callbackDeleteAccount();
                getActivity().onBackPressed();
                return true;
            case R.id.sign_out_menu_button:
                mCallback.callbackSignOut();
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dashboard_fragment, container, false);

        TextView userGreeting = v.findViewById(R.id.user_greeting);
        userGreeting.setText(getString(R.string.user_greeting, FirebaseUtilities.getUser().getDisplayName()));

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.startAddTransactionFragment();

                Transaction newTransaction = new Transaction();
                newTransaction.setName("Transaction3");
                newTransaction.setCreatorId("Ghwuwu82btUVbN4j5ADO6mX11mu1");

                Map<String, Boolean> targetIds = new HashMap<String, Boolean>();

                targetIds.put(FirebaseUtilities.getUser().getUid(), true);
                newTransaction.setTargetUserIds(targetIds);

                newTransaction.setCashValue((float) 20);
                newTransaction.setBarterValue(20);
                newTransaction.setBarterUnit("Dinner");
                newTransaction.setIsBorrowed(false);
                newTransaction.setIsActive(false);
                newTransaction.setIsCompleted(false);
                newTransaction.setNotes("Birthday party dinner");

                DatabaseReference ref = FirebaseUtilities.getDatabaseReference().child("transactions").push();
                String key = ref.getKey();

                FirebaseUtilities.getDatabaseReference().child("transactions").child(key).setValue(newTransaction);
//                FirebaseUtilities.getDatabaseReference().child("users").child(FirebaseUtilities.getUser().getUid()).child("transactions");

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
        mPresenter.getInitialListOfTransaction();
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
            holder.name.setText(transaction.getName());
//            holder.dateCreated.setText("March 23, 2018");

            holder.cashValue.setText("$ " + Float.toString(transaction.getCashValue()));
            holder.barterValue.setText("Barter Value: " + Float.toString(transaction.getBarterValue()));

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
                barterValue = (TextView) transactionView.findViewById(R.id.barterValue);

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
