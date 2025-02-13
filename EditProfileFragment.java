package com.example.guidelink;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileFragment extends Fragment {

    private EditText etGuideName, etGuideEmail, etGuideContact, etExperience, etCity, etHourlyRate;
    private Button btnSaveProfile, btnUploadProfilePic, btnUpdateStatus;
    private ImageView ivProfilePic;
    private TextView tvCurrentStatus;

    private static final int PICK_IMAGE = 1;
    private SQLiteHelper dbHelper;
    Context context;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Initialize UI elements
        etGuideName = view.findViewById(R.id.et_guide_name);
        etGuideEmail = view.findViewById(R.id.et_guide_email);
        etGuideContact = view.findViewById(R.id.et_guide_contact);
        etExperience = view.findViewById(R.id.et_experience);
        etCity = view.findViewById(R.id.et_city);
        etHourlyRate = view.findViewById(R.id.et_hourly_rate);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);
        btnUploadProfilePic = view.findViewById(R.id.btn_upload_profile_pic);
        btnUpdateStatus = view.findViewById(R.id.btnUpdateStatus);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        tvCurrentStatus = view.findViewById(R.id.CurrentStatus);

        // Set up the SQLiteHelper
        dbHelper = new SQLiteHelper(getContext());

        // Load existing profile details
        loadProfile();

        // Handle profile picture upload
        btnUploadProfilePic.setOnClickListener(v -> {
            // Start the image picker to select an image
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Handle save profile action
        btnSaveProfile.setOnClickListener(v -> saveProfile());

        btnUpdateStatus.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(getContext());
            int guideId = sessionManager.getGuideId();

            if (dbHelper.updateStatus(guideId)) {
                // Fetch the updated status
                Cursor cursor = dbHelper.getGuideDetails(guideId);
                if (cursor != null && cursor.moveToFirst()) {
                    String updatedStatus = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_AVAILABILITY));
                    tvCurrentStatus.setText("Current Status: "+updatedStatus);
                    Toast.makeText(getContext(), "Status updated to: " + updatedStatus, Toast.LENGTH_SHORT).show();
                }
                if (cursor != null) cursor.close();
            } else {
                Toast.makeText(getContext(), "Failed to update status!", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Navigate back or close the fragment
            FragmentManager fm6 = getParentFragmentManager();
            FragmentTransaction ft6 = fm6.beginTransaction();
            ft6.replace(R.id.myframe,new GuideHomeFragment());
            ft6.commit();
        });
        return view;
    }

    // Load the existing profile details from the database
    @SuppressLint("Range")
    private void loadProfile() {

        SessionManager sessionManager = new SessionManager(getContext());
        int guideId = sessionManager.getGuideId();

        Cursor cursor = dbHelper.getGuideDetails(guideId); // Assuming guideId=1 for demo
        if (cursor.moveToFirst()) {
            etGuideName.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_G_NAME)));
            etGuideEmail.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_G_EMAIL)));
            etGuideContact.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_G_CONTACT)));
            etExperience.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_EXPERIENCE)));
            etCity.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CITY)));
            etHourlyRate.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_HOURLY_RATE)));

            // Optionally load profile picture if stored
            String profilePicPath = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PROFILE_PIC));
            if (profilePicPath != null && !profilePicPath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(profilePicPath);
                ivProfilePic.setImageBitmap(bitmap);
            }
        }
        cursor.close();
    }

    // Save the profile changes to the database
    private void saveProfile() {
        String guideName = etGuideName.getText().toString();
        String guideEmail = etGuideEmail.getText().toString();
        String guideContact = etGuideContact.getText().toString();
        String experience = etExperience.getText().toString();
        String city = etCity.getText().toString();
        String hourlyRate = etHourlyRate.getText().toString();

        // Assuming the profile picture path is being updated
        String profilePicPath = ""; // Update logic for profile pic (if needed)

        // Update the guide's profile in the database
        dbHelper.updateGuideProfile(1, guideName, guideEmail, guideContact, experience, city, hourlyRate, profilePicPath);

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    // Handle profile picture selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                ivProfilePic.setImageBitmap(bitmap);

                // Save the image path in the database (optional, handle actual saving logic)
                String imagePath = selectedImage.getPath();
                dbHelper.updateGuideProfile(1, etGuideName.getText().toString(), etGuideEmail.getText().toString(),
                        etGuideContact.getText().toString(), etExperience.getText().toString(),
                        etCity.getText().toString(), etHourlyRate.getText().toString(), imagePath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

