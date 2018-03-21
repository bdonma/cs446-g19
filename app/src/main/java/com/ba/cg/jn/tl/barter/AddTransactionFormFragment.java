package com.ba.cg.jn.tl.barter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.facebook.FacebookSdk;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransactionFormFragment extends Fragment {


    EditText eSearchFriends;
    Button addFriendsButton;
    RecyclerView eResultList;

    public AddTransactionFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_transaction_form, container, false);

        final EditText transactionNameEditText = v.findViewById(R.id.transactionNameEditText);
        final EditText peopleEditText = v.findViewById(R.id.peopleEditText);


        eSearchFriends = v.findViewById(R.id.SearchFriendseditText);
        addFriendsButton = v.findViewById(R.id.addFriendsButton);

        eResultList = v.findViewById(R.id.resultsFriendsSearch);
        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get friends list from firebase

                //query Firebase to see users that match user's friendlist

                //if user is Facebook user


            }
        });

        //Facebook friend search

        final Spinner transactionTypeSpinner = (Spinner) v.findViewById(R.id.transactionTypeSpinner);
        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.transaction_types, android.R.layout.simple_spinner_item);
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSpinner.setAdapter(transactionTypeAdapter);

        final EditText cashValueEditText = v.findViewById(R.id.cashValueEditText);
        final EditText unitValueEditText = v.findViewById(R.id.unitValueEditText);

        Spinner unitSpinner = (Spinner) v.findViewById(R.id.unitSpinner);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.dummy_units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        final EditText transactionNotesEditText = v.findViewById(R.id.transactionNotesEditText);

        Button createTransactionButton = v.findViewById(R.id.createTransactionButton);
        createTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String transactionName = transactionNameEditText.getText().toString();
                String people = peopleEditText.getText().toString();
                String transactionType = transactionTypeSpinner.getSelectedItem().toString();
                Double cashValue = Double.parseDouble(cashValueEditText.getText().toString().isEmpty() ? "0" : cashValueEditText.getText().toString());
                Double unitValue = Double.parseDouble(unitValueEditText.getText().toString().isEmpty() ? "0" : unitValueEditText.getText().toString());
                String notes = transactionNotesEditText.getText().toString();

                createTransaction(transactionName, people, transactionType, cashValue,
                        unitValue, notes);
            }
        });

        return v;
    }

    public void createTransaction(String transactionName, String people, String transactionType,
                                  Double cashValue, Double unitValue, String notes) {
        // TODO: add values to firebase

        getActivity().getSupportFragmentManager().popBackStack();
    }
}
