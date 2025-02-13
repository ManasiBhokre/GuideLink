package com.example.guidelink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class GuideLoginFragment extends Fragment {

    private EditText etLoginEmail, etLoginPassword;
    private TextView btnRegisterNav;
    private Button btnLogin;
    private SQLiteHelper sqLiteHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize UI elements
        etLoginEmail = view.findViewById(R.id.etEmail);
        etLoginPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegisterNav = view.findViewById(R.id.tvregister);

        sqLiteHelper = new SQLiteHelper(getContext());

        // Login button click handler
        btnLogin.setOnClickListener(v -> loginGuide());

        // Navigate to registration fragment
        btnRegisterNav.setOnClickListener(v -> {
            // Replace the current fragment with the registration fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.myframe, new GuideRegistrationFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loginGuide() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        // Check if email and password are entered
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate the guide using SQLite
        boolean isAuthenticated = sqLiteHelper.authenticateGuide(email, password);
        int guideId = sqLiteHelper.getGuideId(email);

        if (isAuthenticated) {
            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

            SessionManager sessionManager = new SessionManager(getActivity());
            sessionManager.createGuideSession(guideId);

            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.myframe,new GuideHomeFragment());
            ft.commit();
            // Navigate to the next screen or dashboard
        } else {
            Toast.makeText(getContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
        }
    }
}
