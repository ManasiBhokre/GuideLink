package com.example.guidelink;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SubmitHotspotActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker

    private EditText etPlaceName, etCity, etDescription, etRating, etImageUrl;
    private Button btnSubmit, btnBrowse;
    private ImageView ivSelectedImage;
    private Uri imageUri; // URI to hold the selected image's location
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_hotspot);

        // Initialize UI elements
        etPlaceName = findViewById(R.id.etPlaceName);
        etCity = findViewById(R.id.etCity);
        etDescription = findViewById(R.id.etDescription);
        etRating = findViewById(R.id.etRating);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBrowse = findViewById(R.id.btnBrowse);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        // Initialize the database helper
        dbHelper = new SQLiteHelper(this);

        // Set the button click listeners
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHotspot();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Get the URI of the selected image
            etImageUrl.setText(imageUri.toString()); // Set URI to EditText
            ivSelectedImage.setVisibility(View.VISIBLE); // Show ImageView
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivSelectedImage.setImageBitmap(bitmap); // Display the image in ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitHotspot() {
        String placeName = etPlaceName.getText().toString();
        String city = etCity.getText().toString();
        String description = etDescription.getText().toString();
        String ratingString = etRating.getText().toString();

        // Validate input
        if (TextUtils.isEmpty(placeName) || TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(ratingString) || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = Float.parseFloat(ratingString);
        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "Rating must be between 1 and 5", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the data into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_PLACENAME, placeName);
        values.put(SQLiteHelper.COLUMN_CITY, city);
        values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);
        values.put(SQLiteHelper.COLUMN_RATING, rating);
        values.put(SQLiteHelper.COLUMN_IMAGE_URL, imageUri.toString()); // Save the image URI

        Log.d("SubmitHotspotActivity", "Inserting: " + values.toString());

        long newRowId = db.insert(SQLiteHelper.TABLE_HOTSPOT, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Hotspot submitted successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Error submitting hotspot", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etPlaceName.setText("");
        etCity.setText("");
        etDescription.setText("");
        etRating.setText("");
        etImageUrl.setText("");
        ivSelectedImage.setVisibility(View.GONE); // Hide ImageView
    }
}
