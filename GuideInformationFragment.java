package com.example.guidelink;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class GuideInformationFragment extends Fragment {
    private static final String ARG_GUIDE = "guide";
    private Guide guide;
    RatingBar ratingBar;
    SQLiteHelper sqLiteHelperl;

    public static GuideInformationFragment newInstance(Guide guide) {
        GuideInformationFragment fragment = new GuideInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GUIDE, guide); // Use the correct key
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_information, container, false);

        if (getArguments() != null) {
            // Fetch the guide object using the correct key
            guide = (Guide) getArguments().getSerializable(ARG_GUIDE);
        }

        if (guide != null) {
            ratingBar = view.findViewById(R.id.userRating);
            float rating = guide.getRating();
            ratingBar.setRating(rating);

            TextView tvGuideName = view.findViewById(R.id.userName);
            TextView tvGuideEmail = view.findViewById(R.id.userEmail);
            TextView tvGuidePhone = view.findViewById(R.id.userPhone);
            TextView guideExperience = view.findViewById(R.id.guideExperience);
            TextView guideLanguages = view.findViewById(R.id.guideLanguages);
            TextView guideCity = view.findViewById(R.id.guideCity);

            String languages = guide.getLanguages();
            String formattedLanguages = languages != null ? languages.replace(",", "\n") : "N/A";

            sqLiteHelperl = new SQLiteHelper(getActivity());

            String email = guide.getEmail();
            Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
            int guideId = sqLiteHelperl.getGuideId(email);

//             Store guideId in SessionManager
            SessionManager sessionManager = new SessionManager(getActivity());
            sessionManager.createGuideSession(guideId);

            tvGuideName.setText(guide.getName());
            tvGuideEmail.setText(guide.getEmail());
            tvGuidePhone.setText(guide.getContact());
            guideExperience.setText("Experience: " + guide.getExperience() + " years");
            guideLanguages.setText("Languages: " + formattedLanguages);
            guideCity.setText("City: " + guide.getCity());
        } else {
            // Guide object is null, log error for debugging
            Toast.makeText(getContext(), "Guide information is unavailable.", Toast.LENGTH_SHORT).show();
        }

        Button btnBookGuide = view.findViewById(R.id.btnBookGuide);
        Button btnChat = view.findViewById(R.id.btnChat);

        btnBookGuide.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.myframe, new BookingFormFragment());
            ft.commit();
        });


        btnChat.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Opening chat...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
