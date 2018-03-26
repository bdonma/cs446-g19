package com.ba.cg.jn.tl.barter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransactionFormFragment extends Fragment implements AddTransactionViewInterface {

    private AddTransactionFormPresenter mPresenter;
    private android.support.v7.app.ActionBar mActionBar;
    private AutoCompleteTextView mPeopleEditText;

    EditText mTransactionNameEditText;
    EditText mTransactionNotesEditText;
    EditText mCashValueEditText;
    EditText mBarterValueEditText;
    EditText mBarterUnitEditText;
    RadioGroup mBorrowedLoaned;

    String mTargetUser;

    public AddTransactionFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPresenter = new AddTransactionFormPresenter(this);

        final View v = inflater.inflate(R.layout.fragment_add_transaction_form, container, false);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("Create a new transaction");
        }

        mTransactionNameEditText = v.findViewById(R.id.transactionNameEditText);
        mTransactionNotesEditText = v.findViewById(R.id.transactionNotesEditText);
        mCashValueEditText = v.findViewById(R.id.cashValueEditText);
        mBarterValueEditText = v.findViewById(R.id.barterValueEditText);
        mBarterUnitEditText = v.findViewById(R.id.barterUnitEditText);
        mBorrowedLoaned = v.findViewById(R.id.borrowedLoanedRadioGroup);
        mPeopleEditText = v.findViewById(R.id.peopleEditText);

        mPeopleEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.getUserUuid(mPeopleEditText.getText().toString());
            }
        });


//        addFriendsButton = v.findViewById(R.id.addFriendsButton);
//        eResultList = v.findViewById(R.id.resultsFriendsSearch);
//        addFriendsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: get friends list from firebase
//                //query Firebase to see users that match user's friendlist
////                FacebookUtils.getTaggableFriends();
//                //if user is Facebook user
//            }
//        });

        //Facebook friend search

//        final Spinner transactionTypeSpinner = (Spinner) v.findViewById(R.id.transactionTypeSpinner);
//        ArrayAdapter<CharSequence> transactionTypeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
//                R.array.transaction_types, android.R.layout.simple_spinner_item);
//        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        transactionTypeSpinner.setAdapter(transactionTypeAdapter);
//
//        final EditText mCashValueEditText = v.findViewById(R.id.mCashValueEditText);
//        final EditText unitValueEditText = v.findViewById(R.id.unitValueEditText);
//
//        Spinner unitSpinner = (Spinner) v.findViewById(R.id.unitSpinner);
//        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this.getActivity(),
//                R.array.dummy_units, android.R.layout.simple_spinner_item);
//        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        unitSpinner.setAdapter(unitAdapter);
//
//        final EditText mTransactionNotesEditText = v.findViewById(R.id.mTransactionNotesEditText);
//
        Button createTransactionButton = v.findViewById(R.id.createTransactionButton);
        createTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBorrowed;
                float cashValueFloat;
                float barterValueFloat;
                HashMap<String, Boolean> targetUserIds = new HashMap<>();
                HashMap<String, Boolean> acceptUserIds = new HashMap<>();

                mPresenter.getUserUuid(mPeopleEditText.getText().toString());
                int borrowedLoanedSelection = mBorrowedLoaned.getCheckedRadioButtonId();
                String transactionName = mTransactionNameEditText.getText().toString();
                String cashValue = mCashValueEditText.getText().toString();
                String barterValue = mBarterValueEditText.getText().toString();
                String barterUnit = mBarterUnitEditText.getText().toString();
                String notes = mTransactionNotesEditText.getText().toString();

                if (borrowedLoanedSelection == R.id.borrowedRadioButton) {
                    isBorrowed = true;
                } else if (borrowedLoanedSelection == R.id.loanedRadioButton) {
                    isBorrowed = false;
                } else {
                    isBorrowed = false;
                }

                try {
                    barterValueFloat = barterValue.length() == 0 ? -1 : Float.parseFloat(barterValue);
                } catch (Exception e) {
                    barterValueFloat = -1;
                }

                try {
                    cashValueFloat = Float.parseFloat(cashValue);
                } catch (Exception e) {
                    cashValueFloat = -1;
                }

                targetUserIds.put(mTargetUser, true);
                acceptUserIds.put(FirebaseUtilities.getUser().getUid(), true);

                mPresenter.createTransaction(transactionName, targetUserIds, cashValueFloat, barterValueFloat,
                        barterUnit, isBorrowed, notes, acceptUserIds);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.startGettingFacebookFriends();
    }

    @Override
    public void addPeopleAdapter(List<String> adapterFriends) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, adapterFriends);
        mPeopleEditText.setAdapter(adapter);
    }

    @Override
    public void sendingTargetId(String targetId) {
        mTargetUser = targetId;
    }

    @Override
    public void sendToast() {
        Toast.makeText(getContext(), "Firebase database error", Toast.LENGTH_SHORT);
    }
}
