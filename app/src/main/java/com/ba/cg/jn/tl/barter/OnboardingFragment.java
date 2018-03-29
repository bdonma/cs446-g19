package com.ba.cg.jn.tl.barter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingFragment extends Fragment {

    private int count = 0;

    public OnboardingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_onboarding1_fragment, container, false);
        Button nextBtn = v.findViewById(R.id.next_button);
        final TextView titleText = v.findViewById(R.id.title);

        final SimpleDraweeView mainImage = v.findViewById(R.id.onboarding_view);
        mainImage.setImageResource(R.drawable.friends);
//        mainImage.setImageURI("https://ibb.co/egS6DH");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    mainImage.setImageResource(R.drawable.barter);
                    titleText.setText("Pay someone back with everday things instead!");
                    count++;
                } else if (count == 1) {
                    mainImage.setImageResource(R.drawable.approve);
                    titleText.setText("Approve each transaction to know what you owe");
                    count++;
                } else if (count == 2) {
                    mainImage.setImageResource(R.drawable.startnow);
                    titleText.setText("Start making Transactions now!");
                    count++;
                } else {
                    getActivity().finish();
                }
            }
        });

        return v;
    }

}
