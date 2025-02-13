package com.example.guidelink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ArrayAdapter;

public class GuideRegistrationFragment extends Fragment {

    private EditText etGuideName, etGuideEmail, etGuidePassword, etGuideContact, etExperience, etCity, etHourlyRate;
    private Button btnRegister, btnUploadCertificate;
    private ImageView ivProfilePic;
    private ProgressDialog progressDialog;
    private Uri profilePicUri = null;

    private SQLiteHelper sqLiteHelper;
    private static final int PICK_IMAGE_REQUEST = 1;

    View view;

    // Add fields for availability spinner, languages checkboxes, and gender radio buttons
    private Spinner spinnerAvailability;
    private CheckBox checkboxEnglish, checkboxHindi, checkboxMarathi, checkboxKokani, checkboxTelugu, checkboxUrdu, checkboxSindhi, checkboxMalwani;
    private RadioGroup radioGroupGender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_registration, container, false);

        // Initialize UI elements
        etGuideName = view.findViewById(R.id.et_guide_name);
        etGuideEmail = view.findViewById(R.id.et_guide_email);
        etGuidePassword = view.findViewById(R.id.et_guide_password);
        etGuideContact = view.findViewById(R.id.et_guide_contact);
        etExperience = view.findViewById(R.id.et_experience);
        etCity = view.findViewById(R.id.et_city);
        etHourlyRate = view.findViewById(R.id.et_hourly_rate);
        btnRegister = view.findViewById(R.id.btn_register);
        btnUploadCertificate = view.findViewById(R.id.btn_upload_certificate);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);

        // Initialize new fields
        spinnerAvailability = view.findViewById(R.id.spinner_availability);
        checkboxEnglish = view.findViewById(R.id.cbLanguageEnglish);
        checkboxHindi = view.findViewById(R.id.cbLanguageHindi);
        checkboxMarathi = view.findViewById(R.id.cbLanguageMarathi);
        radioGroupGender = view.findViewById(R.id.radio_group_gender);

        progressDialog = new ProgressDialog(getContext());
        sqLiteHelper = new SQLiteHelper(getContext());

        // Upload Certificate (Image) Handler
        btnUploadCertificate.setOnClickListener(v -> openImagePicker());

        // Register Button Click Handler
        btnRegister.setOnClickListener(v -> registerGuide());

        // Setup availability spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.availability_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailability.setAdapter(adapter);

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            ivProfilePic.setImageURI(profilePicUri);
        }
    }

    private void registerGuide() {
        String guideName = etGuideName.getText().toString().trim();
        String guideEmail = etGuideEmail.getText().toString().trim();
        String guidePassword = etGuidePassword.getText().toString().trim();
        String guideContact = etGuideContact.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String hourlyRateString = etHourlyRate.getText().toString().trim();
        String availability = spinnerAvailability.getSelectedItem().toString();

        // Check for empty fields
        if (TextUtils.isEmpty(guideName) || TextUtils.isEmpty(guideEmail) || TextUtils.isEmpty(guidePassword)
                || TextUtils.isEmpty(guideContact) || TextUtils.isEmpty(experience) || TextUtils.isEmpty(city)
                || TextUtils.isEmpty(hourlyRateString) || TextUtils.isEmpty(availability)) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email already exists
        if (sqLiteHelper.checkEmailExists(guideEmail)) {
            Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert hourly rate to double
        double hourlyRate;
        try {
            hourlyRate = Double.parseDouble(hourlyRateString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid hourly rate", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        progressDialog.setMessage("Registering Guide...");
        progressDialog.show();

        // Profile pic handling
        String profilePicPath = (profilePicUri != null) ? profilePicUri.toString() : "";

        // Collect languages
        StringBuilder languages = new StringBuilder();
        if (checkboxEnglish.isChecked()) languages.append("English, ");
        if (checkboxHindi.isChecked()) languages.append("Hindi, ");
        if (checkboxMarathi.isChecked()) languages.append("Marathi, ");

        // Remove trailing comma and space
        if (languages.length() > 0) {
            languages.setLength(languages.length() - 2);
        }

        // Determine gender
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        String gender = null;
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = view.findViewById(selectedGenderId);
            gender = selectedRadioButton.getText().toString();
        }

        // Insert the guide into the database
        boolean insertSuccess = sqLiteHelper.insertGuide(guideName, guideContact, guideEmail, guidePassword,
                experience, 0.0, city, availability, profilePicPath, languages.toString(), hourlyRate, gender);

        progressDialog.dismiss();

        if (insertSuccess) {
            Toast.makeText(getContext(), "Guide Registered Successfully", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.myframe, new GuideLoginFragment());
            ft.commit();
        } else {
            Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
