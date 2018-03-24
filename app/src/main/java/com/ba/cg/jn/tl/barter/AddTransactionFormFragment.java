package com.ba.cg.jn.tl.barter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransactionFormFragment extends Fragment {

    EditText eSearchFriends;
    FloatingActionButton addFriendsButton;
    RecyclerView eResultList;
    private AddTransactionFormPresenter presenter;

    public AddTransactionFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_transaction_form, container, false);
        android.support.v7.app.ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();

//        final EditText transactionNameEditText = v.findViewById(R.id.transactionNameEditText);
//        final EditText peopleEditText = v.findViewById(R.id.peopleEditText);

//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#47445e")));
        bar.setTitle("Create a new transaction");

//        eSearchFriends = v.findViewById(R.id.SearchFriendseditText);
        addFriendsButton = v.findViewById(R.id.addFriendsButton);

//        eResultList = v.findViewById(R.id.resultsFriendsSearch);
        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get friends list from firebase
                //query Firebase to see users that match user's friendlist
                FacebookUtils.getTaggableFriends();

                //if user is Facebook user


            }
        });

        //Facebook friend search

//        final Spinner transactionTypeSpinner = (Spinner) v.findViewById(R.id.transactionTypeSpinner);
//        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
//                R.array.transaction_types, android.R.layout.simple_spinner_item);
//        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        transactionTypeSpinner.setAdapter(transactionTypeAdapter);
//
//        final EditText cashValueEditText = v.findViewById(R.id.cashValueEditText);
//        final EditText unitValueEditText = v.findViewById(R.id.unitValueEditText);
//
//        Spinner unitSpinner = (Spinner) v.findViewById(R.id.unitSpinner);
//        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this.getActivity(),
//                R.array.dummy_units, android.R.layout.simple_spinner_item);
//        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        unitSpinner.setAdapter(unitAdapter);
//
//        final EditText transactionNotesEditText = v.findViewById(R.id.transactionNotesEditText);
//
        Button createTransactionButton = v.findViewById(R.id.createTransactionButton);
        createTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        presenter = new AddTransactionFormPresenter(this);
    }

    public void createTransaction(String transactionName, String people, String transactionType,
                                  Double cashValue, Double unitValue, String notes) {
        // TODO: add values to firebase

        getActivity().getSupportFragmentManager().popBackStack();
    }
}
