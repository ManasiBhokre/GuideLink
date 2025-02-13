package com.example.guidelink;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class BookedFragment extends Fragment {

    private Context context;
    private SQLiteHelper sqliteHelper;
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private FragmentManager fragmentManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        sqliteHelper = new SQLiteHelper(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Fetch requests and set up the adapter
        bookingList = fetchRequests();
        bookingAdapter = new BookingAdapter(bookingList, context, fragmentManager);
        recyclerView.setAdapter(bookingAdapter);

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

    private List<Booking> fetchRequests() {
        List<Booking> bookings = new ArrayList<>();

        // Fetch guideId from session
        SessionManager sessionManager = new SessionManager(context);
        int guideId = sessionManager.getGuideId();

        // Get readable database
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        // Query the bookingrequest table
        Cursor cursor = db.query(
                "bookingrequest",          // Table name
                null,                      // Columns (null fetches all)
                "guide_id = ? AND status = ?",            // Selection criteria
                new String[]{String.valueOf(guideId),"confirmed"}, // Selection arguments
                null,                      // Group by
                null,                      // Having
                null                       // Order by
        );


        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Fetch values for each column
                int uId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String tourDate = cursor.getString(cursor.getColumnIndexOrThrow("tour_date"));
                int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                Cursor cursor1 = db.query(
                        "users",          // Table name
                        null,                      // Columns (null fetches all)
                        "uid = ?",            // Selection criteria
                        new String[]{String.valueOf(uId)}, // Selection arguments
                        null,                      // Group by
                        null,                      // Having
                        null                       // Order by
                );

                if (cursor1 != null) {
                    while (cursor1.moveToNext()) {
                        // Fetch values for each column
                        String guideName = cursor1.getString(cursor1.getColumnIndexOrThrow("uname"));

                        // Add the request to the list
                        bookings.add(new Booking(guideName, tourDate, bookingId));
                    }
                }
                cursor1.close();
            }
            cursor.close();
        }

        return bookings;
    }
}
