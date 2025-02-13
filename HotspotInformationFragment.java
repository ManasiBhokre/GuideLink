package com.example.guidelink;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HotspotInformationFragment extends Fragment {

    private static final String ARG_HOTSPOT = "hotspot";
    private Hotspot hotspot;
    private RatingBar ratingBar;
    private RecyclerView recyclerViewNearbySpots;

    public static HotspotInformationFragment newInstance(Hotspot hotspot) {
        HotspotInformationFragment fragment = new HotspotInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HOTSPOT, hotspot); // Ensure Hotspot implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotspot_information, container, false);

        if (getArguments() != null) {
            hotspot = (Hotspot) getArguments().getSerializable(ARG_HOTSPOT);
        }

        recyclerViewNearbySpots = view.findViewById(R.id.recyclerViewNearbySpots);

        ratingBar = view.findViewById(R.id.hotspotRating);
        ratingBar.setRating(Float.parseFloat(hotspot.getRatingAsString()));

        TextView tvHotspotName = view.findViewById(R.id.hotspotName);
        TextView tvHotspotCity = view.findViewById(R.id.hotspotCity);
        TextView tvHotspotDescription = view.findViewById(R.id.hotspotDescription);

        tvHotspotName.setText(hotspot.getPlacename());
        tvHotspotCity.setText("City: " + hotspot.getCity());
        tvHotspotDescription.setText("Description: " + hotspot.getDescription());

        Button btnViewPhotos = view.findViewById(R.id.btnViewPhotos);
        Button btnExploreNearby = view.findViewById(R.id.btnExploreNearby);

        btnViewPhotos.setOnClickListener(v -> {
            String searchUrl = "https://www.google.com/search?tbm=isch&q=" + hotspot.getPlacename() + " " + hotspot.getCity();
            PhotoGalleryFragment photoGalleryFragment = PhotoGalleryFragment.newInstance(searchUrl);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.myframe, photoGalleryFragment) // Make sure to replace with your actual container ID
                    .addToBackStack(null)
                    .commit();
        });

        btnExploreNearby.setOnClickListener(v -> {
            String city = hotspot.getCity(); // Get the city from the hotspot
            fetchNearbySpots(city);
        });



        return view;
    }

    private void fetchNearbySpots(String city) {
        // Fetch nearby spots based on city
        List<Guide> nearbySpots = fetchNearbySpotsFromDatabase(city);

        if (nearbySpots != null && !nearbySpots.isEmpty()) {
            // Initialize adapter only if list is non-empty
            recyclerViewNearbySpots.setVisibility(View.VISIBLE);
            Context context = getContext();
            if (context != null) { // Ensure context is not null
                NearbySpotAdapter adapter = new NearbySpotAdapter(nearbySpots, context);
                recyclerViewNearbySpots.setAdapter(adapter);
                recyclerViewNearbySpots.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Log.e("HotspotInformation", "Context is null, adapter initialization failed");
            }
        } else {
            Toast.makeText(getContext(), "No nearby spots found.", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Guide> fetchNearbySpotsFromDatabase(String city) {
        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        List<Guide> nearbySpots = dbHelper.getGuidesByCity(city);
        return nearbySpots;
    }
}
