package com.example.guidelink;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RemoveGuide extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private EditText etGuideId;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_guide);
        dbHelper = new SQLiteHelper(this);
        etGuideId = findViewById(R.id.etGuideId);
        tvResult = findViewById(R.id.tvResult);

        Button btnDeleteGuide = findViewById(R.id.btnDeleteGuide);
        btnDeleteGuide.setOnClickListener(v -> {
            String guideIdStr = etGuideId.getText().toString();
            if (!guideIdStr.isEmpty()) {
                int g_id = Integer.parseInt(guideIdStr);
                boolean isDeleted = dbHelper.deleteGuideById(g_id);

                if (isDeleted) {
                    tvResult.setText("Guide with ID " + g_id + " deleted successfully.");
                } else {
                    tvResult.setText("Guide with ID " + g_id + " not found.");
                }
            } else {
                tvResult.setText("Please enter a valid Guide ID.");
            }
        });
    }
}