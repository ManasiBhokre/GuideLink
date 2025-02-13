package com.example.guidelink;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView tvRegistration;

    SQLiteHelper dbHelper;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize DatabaseHelper and SessionManager
        dbHelper = new SQLiteHelper(getActivity());
        sessionManager = new SessionManager(getActivity());

        // Link UI elements with code
        emailEditText = v.findViewById(R.id.etEmail);
        passwordEditText = v.findViewById(R.id.etPassword);
        loginButton = v.findViewById(R.id.btnLogin);
        tvRegistration = v.findViewById(R.id.tvregister);

        tvRegistration.setOnClickListener(view -> {
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.myframe, new RegistrationFragment());
            ft.commit();
        });

        // Set OnClickListener for Login button
        loginButton.setOnClickListener(v1 -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate email and password
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                // Check if user exists in the database
                boolean isValid = dbHelper.checkUser(email, password);

                if (isValid) {
                    // Fetch the username and userId for the session
                    String userName = dbHelper.getUserName(email);
                    int userId = dbHelper.getUserId(email);

                    if (userId == -1) {
                        Toast.makeText(getActivity(), "Error retrieving user ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.createLoginSession(userId, userName);
                    Toast.makeText(getActivity(), "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to HomeFragment
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.myframe, new HomeFragment());
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}