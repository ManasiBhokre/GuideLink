package com.example.guidelink;

import android.os.Bundle;
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

public class RegistrationFragment extends Fragment {

    EditText nameEditText, passwordEditText, emailEditText, contactEditText;
    Button registerButton;
    TextView tvLogin;

    SQLiteHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        // Initialize DatabaseHelper
        dbHelper = new SQLiteHelper(getActivity());

        // Link UI elements with code
        nameEditText = v.findViewById(R.id.etName);
        passwordEditText = v.findViewById(R.id.etPassword);
        emailEditText = v.findViewById(R.id.etEmail);
        contactEditText = v.findViewById(R.id.etContact);
        registerButton = v.findViewById(R.id.btnRegister);
        tvLogin = v.findViewById(R.id.tvLogin);

        // Set OnClickListener for the Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input data from the user
                String name = nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String contact = contactEditText.getText().toString().trim();
                String regDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

                // Validate the input fields
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.checkUserExist(email)) {
                    Toast.makeText(getActivity(), "Email already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert user data into the database
                    long result = dbHelper.insertUser(name, password, email, contact, regDate);

                    if (result != -1) {
                        Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to login or another page after registration
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.myframe, new LoginFragment())
                                .commit();
                    } else {
                        Toast.makeText(getActivity(), "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.myframe, new LoginFragment())
                        .commit();
            }
        });

        return v;
    }
}
