package com.example.guidelink;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditUserProfileFragment extends Fragment {

    private EditText etUserName, etUserEmail, etUserContact;
    private Button btnSaveProfile, btnUploadProfilePic;
    private ImageView ivProfilePic;

    private static final int PICK_IMAGE = 1;
    private SQLiteHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        // Initialize UI elements
        etUserName = view.findViewById(R.id.et_user_name);
        etUserEmail = view.findViewById(R.id.et_user_email);
        etUserContact = view.findViewById(R.id.et_user_contact);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);


        // Set up the DatabaseHelper
        dbHelper = new SQLiteHelper(getContext());

        // Load existing user details
        loadUserProfile();

        // Handle save profile action
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Navigate back or close the fragment
            FragmentManager fm6 = getParentFragmentManager();
            FragmentTransaction ft6 = fm6.beginTransaction();
            ft6.replace(R.id.myframe,new HomeFragment());
            ft6.commit();
        });


        return view;
    }


    @SuppressLint("Range")
    private void loadUserProfile() {
        SessionManager sessionManager = new SessionManager(getContext());
        int userId = sessionManager.getUserId();

        Cursor cursor = dbHelper.getUserDetails(userId); // Assuming userId=1 for demo
        if (cursor != null && cursor.moveToFirst()) {
            etUserName.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_NAME)));
            etUserEmail.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_EMAIL)));
            etUserContact.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CONTACT)));
            cursor.close();
        } else {
            Toast.makeText(getContext(), "Error loading user profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfile() {
        String userName = etUserName.getText().toString();
        String userEmail = etUserEmail.getText().toString();
        String userContact = etUserContact.getText().toString();

        boolean isUpdated = dbHelper.updateUserProfile(1, userName, userEmail, userContact);

        if (isUpdated) {
            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                if (selectedImage != null) {
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ivProfilePic.setImageBitmap(bitmap);


                } else {
                    Toast.makeText(getContext(), "Image selection failed", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to get the real file path from URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }
}
