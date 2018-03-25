package com.ba.cg.jn.tl.barter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements DashboardViewInterface {

    private android.support.v7.app.ActionBar bar;
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
        } // switch
    } // onOptionsItemSelected

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dashboard_fragment, container, false);

        TextView userGreeting = v.findViewById(R.id.current_user_text);
        userGreeting.setText(FirebaseUtilities.getUser().getDisplayName());

        bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setTitle("Dashboard");
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

    public void showAmountsOfCurrentUser(float amountIOwed, float amountIAmDue) {
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        TextView amountIOweTV = getView().findViewById(R.id.amountOwedTextValue);
        String owedText = "-$" + df.format(amountIOwed);
        amountIOweTV.setText(owedText);

        String amountIAmDueText = "$" + df.format(amountIAmDue);
        TextView amountIAmDueTV = getView().findViewById(R.id.amountDueTextValue);
        amountIAmDueTV.setText(amountIAmDueText);
    }

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

        // TODO: Complete the UI changes for transactions
        @Override
        public void onBindViewHolder(TransactionViewHolder holder, int position) {
            Transaction transaction = mTransactions.get(position);
            holder.name.setText(transaction.getName());
//            holder.dateCreated.setText("March 23, 2018");

            holder.cashValue.setText("$ " + Float.toString(transaction.getCashValue()));
            holder.barterValue.setText("Barter Value: " + Float.toString(transaction.getBarterValue()));


            // TODO: Fade transaction if it's not accepted yet
            if (transaction.getIsActive()) {
                // transaction is active
            } else {
                // the transaction is not active yet (acknowledged by all parties)
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
                cashValue = (TextView) transactionView.findViewById(R.id.cashValueEditText);
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
