package com.example.guidelink;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SearchView searchView;
    private ListView lvResults, lvRecommendedSpots;
    ImageButton btnMenu, logout;
    RecyclerView rvRecommendedGuides;

    private SQLiteHelper dbHelper;
    private ArrayList<Hotspot> hotspotList;
    private ArrayList<Hotspot> recommendedList;
    private HotspotListAdapter hotspotListAdapter, recommendedListAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the views
        searchView = v.findViewById(R.id.searchView);
        lvResults = v.findViewById(R.id.lvResults);
        lvRecommendedSpots = v.findViewById(R.id.lvRecommendedSpots);
        TextView tvRecommended = v.findViewById(R.id.tvRecommended);
        btnMenu = v.findViewById(R.id.btnMenu);
        logout = v.findViewById(R.id.logout);
        rvRecommendedGuides = v.findViewById(R.id.rvRecommendedGuides);


        // Initialize the database helper
        dbHelper = new SQLiteHelper(getActivity());

        // Initialize the lists
        hotspotList = new ArrayList<>();
        recommendedList = new ArrayList<>();

        // Load recommended spots from the database
        loadRecommendedSpots();

        // Set up search functionality
        setupSearchView();


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.myframe, new MenuFragment());
                ft.commit();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.myframe, new LandingFragment());
                ft.commit();
            }
        });


// Inside onCreateView or after initializing `lvRecommendedSpots`

        lvRecommendedSpots.setOnItemClickListener((parent, view1, position, id) -> {
            if (!hotspotList.isEmpty() && position < hotspotList.size()) {
                Hotspot selectedHotspot = hotspotList.get(position);
                // Navigate to HotspotInformationFragment with the selected hotspot's data
                openHotspotInformationFragment(selectedHotspot);
            } else {
                Log.e("HomeFragment", "hotspotList is empty or index out of bounds");
            }
        });


        return v;
    }
    private void openHotspotInformationFragment(Hotspot hotspot) {
        HotspotInformationFragment hotspotInformationFragment = HotspotInformationFragment.newInstance(hotspot);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.myframe, hotspotInformationFragment); // Replace myframe with your actual container ID
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchHotspots(query);
                } else {
                    Toast.makeText(getActivity(), "Enter search query", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    searchHotspots(newText);
                } else {
                    hotspotList.clear();
                    if (hotspotListAdapter != null) {
                        hotspotListAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now you can access the media
                // You might want to re-trigger the image selection here if needed
            } else {
                Toast.makeText(getActivity(), "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            Uri imageUri = data.getData();
//
//            if (imageUri != null) {
//                // Grant URI permission correctly
//                final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                if (takeFlags != 0) {
//                    getActivity().getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
//                }
//
//                loadImage(imageUri);
//            }
//        }
//    }

    private void loadImage(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Assuming you have an ImageView to display the selected image
            ImageView ivSelectedImage = getView().findViewById(R.id.ivSelectedImage); // Ensure this ID is correct
            ivSelectedImage.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Image not found.", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Permission denied to access this image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecommendedGuides() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<Guide1> guideList = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM guides WHERE rating >= 4.0", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String guideName = cursor.getString(cursor.getColumnIndexOrThrow("g_name"));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                    String availability = cursor.getString(cursor.getColumnIndexOrThrow("availability"));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                    String profilePic = cursor.getString(cursor.getColumnIndexOrThrow("profile_pic"));

                    // Add to the guide list
                    guideList.add(new Guide1(guideName, city, availability, rating, profilePic));
                } while (cursor.moveToNext());
            }

            // Set adapter for recommended guides
            GuideAdapter1 guideAdapter = new GuideAdapter1(guideList);
            rvRecommendedGuides.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvRecommendedGuides.setAdapter(guideAdapter);

        } catch (Exception e) {
            Log.e("HomeFragment", "Error loading recommended guides: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void loadRecommendedSpots() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM hotspot WHERE rating >= 4.0", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String placeName = cursor.getString(cursor.getColumnIndexOrThrow("place_name"));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                    // Add to the recommended list
                    recommendedList.add(new Hotspot(placeName, city, description, rating, imageUrl));
                } while (cursor.moveToNext());
            }

            // Set adapter for recommended spots
            recommendedListAdapter = new HotspotListAdapter(getActivity(), recommendedList);
            lvRecommendedSpots.setAdapter(recommendedListAdapter);
            recommendedListAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e("HomeFragment", "Error loading recommended spots: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void searchHotspots(String query) {
        hotspotList.clear();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM hotspot WHERE place_name LIKE ? OR city LIKE ?", new String[]{"%" + query + "%", "%" + query + "%"});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String placeName = cursor.getString(cursor.getColumnIndexOrThrow("place_name"));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                    hotspotList.add(new Hotspot(placeName, city, description, rating, imageUrl));
                } while (cursor.moveToNext());
            }
            // Set adapter for search results
            hotspotListAdapter = new HotspotListAdapter(getActivity(), hotspotList);
            lvResults.setAdapter(hotspotListAdapter);
            hotspotListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("HomeFragment", "Error searching hotspots: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
