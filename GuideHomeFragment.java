package com.example.guidelink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GuideHomeFragment extends Fragment {

    private Button btnLogout;
    private LinearLayout tileEditProfile, tileRequests, tileChatting, tileBookings, tileRatingsFeedback, tileViewItinerary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_home, container, false);

        // Initialize UI elements
        btnLogout = view.findViewById(R.id.btn_logout);
        tileEditProfile = view.findViewById(R.id.tile_edit_profile);
        tileChatting = view.findViewById(R.id.tile_chatting);
        tileRequests = view.findViewById(R.id.tile_viewRequests);
        tileBookings = view.findViewById(R.id.tile_bookings);
        tileRatingsFeedback = view.findViewById(R.id.tile_ratings_feedback);
        tileViewItinerary = view.findViewById(R.id.tile_view_itinerary);

        // Logout functionality
        btnLogout.setOnClickListener(v -> logout());

        // Tile click listeners
        tileEditProfile.setOnClickListener(v -> openEditProfile());
//        tileChatting.setOnClickListener(v -> openChatting());
        tileRequests.setOnClickListener(v -> openRequests());
        tileBookings.setOnClickListener(v -> openBookings());
//        tileRatingsFeedback.setOnClickListener(v -> openRatingsFeedback());
//        tileViewItinerary.setOnClickListener(v -> openViewItinerary());

        return view;
    }

    // Example method to log out and return to login page
    private void logout() {
        // Navigate back to the LoginFragment
        Toast.makeText(getContext(), "Logging out...", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.myframe, new LandingFragment())
                .commit();
    }

    private void openEditProfile() {
        // Navigate to Edit Profile
        getParentFragmentManager().beginTransaction()
                .replace(R.id.myframe, new EditProfileFragment())
                .addToBackStack(null)
                .commit();
    }

//    private void openChatting() {
//        // Navigate to Chatting screen
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.myframe, new ChatFragment())
//                .addToBackStack(null)
//                .commit();
//    }

    private void openRequests() {
        // Navigate to Bookings screen
        getParentFragmentManager().beginTransaction()
                .replace(R.id.myframe, new RequestFragment())
                .addToBackStack(null)
                .commit();
    }

    private void openBookings() {
        // Navigate to Bookings screen
        getParentFragmentManager().beginTransaction()
                .replace(R.id.myframe, new BookedFragment())
                .addToBackStack(null)
                .commit();
    }

//    private void openRatingsFeedback() {
//        // Navigate to View Ratings and Feedback
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.myframe, new RatingsFeedbackFragment())
//                .addToBackStack(null)
//                .commit();
//    }

//    private void openViewItinerary() {
//        // Navigate to View Itinerary screen
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.myframe, new ItineraryFragment())
//                .addToBackStack(null)
//                .commit();
//    }
}
