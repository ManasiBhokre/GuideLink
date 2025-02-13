package com.example.guidelink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LandingFragment extends Fragment {

    private Button btnLogin;
    private TextView tvSignup, tvGuideLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        // Initialize views
        btnLogin = view.findViewById(R.id.getstarted);
        tvSignup = view.findViewById(R.id.tvLogin);
        tvGuideLogin = view.findViewById(R.id.tvGuide);

        // Set click listener for Login button
        btnLogin.setOnClickListener(v -> {
            // Replace with the LoginFragment
            replaceFragment(new LoginFragment());
        });

        // Set click listener for Signup TextView
        tvSignup.setOnClickListener(v -> {
            // Replace with the RegistrationFragment
            replaceFragment(new RegistrationFragment());
        });

        // Set click listener for Guide Login TextView
        tvGuideLogin.setOnClickListener(v -> {
            // Replace with the GuideLoginFragment
            replaceFragment(new GuideLoginFragment());
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myframe, fragment); // Make sure 'fragment_container' is the ID of your container
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
