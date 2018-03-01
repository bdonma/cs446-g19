package com.ba.cg.jn.tl.barter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Onboarding1Fragment extends Fragment {
//    private Onboarding1Listener mCallback;

    private int count = 0;

    public Onboarding1Fragment() {
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
        mainImage.setImageResource(R.drawable.pic);
//        mainImage.setImageURI("https://ibb.co/egS6DH");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    mainImage.setImageResource(R.drawable.money);
                    titleText.setText("You can create your own transaction");
                    count++;
                } else {
                    getActivity().finish();
                }
            }
        });

        return v;
    }

}
