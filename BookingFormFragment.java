package com.example.guidelink;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

public class BookingFormFragment extends Fragment {

    private static final String ARG_GUIDE_NAME = "guideName";
    private static final String ARG_GUIDE_EMAIL = "guideEmail";
    private static final String ARG_GUIDE_CONTACT = "guideContact";

    private EditText etUserId, etGuideId, etTourDate, etSpecialRequests;
    private Spinner spinnerTourDuration;
    private ImageButton btnSelectTourDate;
    private CheckBox checkBoxAgreement;
    private CheckBox[] travelerRangeChecks, languageChecks, ageRangeChecks, interestChecks;
    private RadioButton radioBudget1, radioBudget2, radioBudget3, radioBudget4;
    private Button btnSendRequest;
    private SQLiteHelper dbHelper;
    private ProgressDialog progressDialog;

    private static final String CHANNEL_ID = "GuideNotificationChannel";

    public static BookingFormFragment newInstance(Guide guide) {
        BookingFormFragment fragment = new BookingFormFragment();
        Bundle args = new Bundle();

        args.putString(ARG_GUIDE_NAME, guide.getName());
        args.putString(ARG_GUIDE_EMAIL, guide.getEmail());
        args.putString(ARG_GUIDE_CONTACT, guide.getContact());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        View view = inflater.inflate(R.layout.fragment_form_booking, container, false);
        initializeViews(view);
        dbHelper = new SQLiteHelper(getActivity());

        btnSendRequest.setOnClickListener(v -> saveBookingRequest());

        ImageButton btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Navigate back or close the fragment
            FragmentManager fm6 = getParentFragmentManager();
            FragmentTransaction ft6 = fm6.beginTransaction();
            ft6.replace(R.id.myframe,new ViewGuideFragment());
            ft6.commit();
        });

        return view;
    }

    private void initializeViews(View view) {
        etUserId = view.findViewById(R.id.etUserId);
        etGuideId = view.findViewById(R.id.etGuideId);
        etTourDate = view.findViewById(R.id.etTourDate);
        etSpecialRequests = view.findViewById(R.id.etSpecialRequests);
        spinnerTourDuration = view.findViewById(R.id.spinnerTourDuration);
        checkBoxAgreement = view.findViewById(R.id.cbBookingAgreement);
        btnSendRequest = view.findViewById(R.id.btnSubmitBooking);
        btnSelectTourDate = view.findViewById(R.id.btnSelectTourDate);

        travelerRangeChecks = new CheckBox[]{
                view.findViewById(R.id.cbTravelerRange1),
                view.findViewById(R.id.cbTravelerRange2),
                view.findViewById(R.id.cbTravelerRange3)
        };
        languageChecks = new CheckBox[]{
                view.findViewById(R.id.cbLanguageEnglish),
                view.findViewById(R.id.cbLanguageHindi),
                view.findViewById(R.id.cbLanguageMarathi),
                view.findViewById(R.id.cbLanguageKonkani),
                view.findViewById(R.id.cbLanguageTelugu),
                view.findViewById(R.id.cbLanguageUrdu),
                view.findViewById(R.id.cbLanguageMalwani),
                view.findViewById(R.id.cbLanguageSindhi)
        };
        ageRangeChecks = new CheckBox[]{
                view.findViewById(R.id.cbAgeRangeChild),
                view.findViewById(R.id.cbAgeRangeTeen),
                view.findViewById(R.id.cbAgeRangeAdult),
                view.findViewById(R.id.cbAgeRangeSenior)
        };
        interestChecks = new CheckBox[]{
                view.findViewById(R.id.cbInterest1),
                view.findViewById(R.id.cbInterest2),
                view.findViewById(R.id.cbInterest3)
        };
        radioBudget1 = view.findViewById(R.id.radioBudget1);
        radioBudget2 = view.findViewById(R.id.radioBudget2);
        radioBudget3 = view.findViewById(R.id.radioBudget3);

        btnSelectTourDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etTourDate.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveBookingRequest() {

        if (!checkBoxAgreement.isChecked()) {
            Toast.makeText(getActivity(), "Please accept the booking agreement", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate required fields
        if (isEmpty(etTourDate) || isEmpty(etSpecialRequests)) {
            Toast.makeText(getActivity(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String tourDate = etTourDate.getText().toString().trim();
        String specialRequests = etSpecialRequests.getText().toString().trim();
        String tourDuration = spinnerTourDuration.getSelectedItem() != null ? spinnerTourDuration.getSelectedItem().toString() : "";

        // Collect checked options
        String travelerRange = collectCheckedItems(travelerRangeChecks);
        String languagePreference = collectCheckedItems(languageChecks);
        String ageRange = collectCheckedItems(ageRangeChecks);
        String interests = collectCheckedItems(interestChecks);
        String budgetRange = getSelectedBudgetRange();
        String agreement = String.valueOf(checkBoxAgreement.isChecked() ? 1 : 0);


        boolean insertSuccess = dbHelper.insertRequest(tourDate, specialRequests, tourDuration, travelerRange, languagePreference, ageRange, interests, budgetRange, agreement);
        progressDialog.dismiss();

        if (insertSuccess) {
            Toast.makeText(getContext(), "Request sent successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Request sending failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendGuideNotification(int guideId, String title, String content) {
        // Create Notification Channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Guide Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your app's icon
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(guideId, builder.build());  // Use guideId as unique notification ID
    }


    private String collectCheckedItems(CheckBox[] checkBoxes) {
        StringBuilder checkedItems = new StringBuilder();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                checkedItems.append(checkBox.getText()).append(", ");
            }
        }
        // Remove the last comma and space if any items were checked
        return checkedItems.length() > 0 ? checkedItems.substring(0, checkedItems.length() - 2) : "";
    }

    private String getSelectedBudgetRange() {
        if (radioBudget1.isChecked()) return "Under Rs.2000";
        if (radioBudget2.isChecked()) return "Rs.2000 - Rs.5000";
        if (radioBudget3.isChecked()) return "Rs.5000 - Rs.10000";
        if (radioBudget4.isChecked()) return "Over Rs.10000";
        return ""; // Return an empty string if no budget is selected
    }


    // Helper method to check if EditText is empty
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

}
