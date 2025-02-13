package com.example.guidelink;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ViewGuideFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewGuides;
    private TextView tvNoResults;
    private SQLiteHelper dbHelper;

    private ArrayList<Guide> guideList; // List to hold guides fetched from the database
    private ArrayAdapter<Guide> guideAdapter; // Adapter for ListView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_guide, container, false);

        // Initialize UI elements
        searchView = view.findViewById(R.id.search_view);
        listViewGuides = view.findViewById(R.id.list_view_guides);
        tvNoResults = view.findViewById(R.id.tv_no_results);

        // Set up the database helper
        dbHelper = new SQLiteHelper(getContext());

        // Initialize guide list
        guideList = new ArrayList<>();

        // Set up the adapter for the ListView
        guideAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, guideList);
        listViewGuides.setAdapter(guideAdapter);

        // Set search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchGuides(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    guideList.clear();
                    guideAdapter.notifyDataSetChanged();
                    listViewGuides.setVisibility(View.GONE);
                    tvNoResults.setVisibility(View.GONE);
                }
                return false;
            }
        });

        // Handle item clicks in the ListView
        listViewGuides.setOnItemClickListener((parent, view1, position, id) -> {
            Guide selectedGuide = guideList.get(position);
            // Navigate to GuideInformationFragment with the selected guide's data
            openGuideInformationFragment(selectedGuide);
        });

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

    // Fetch guides based on search query
    private void fetchGuides(String query) {
        guideList.clear();
        List<Guide> fetchedGuides = dbHelper.searchGuidesByName(query);

        if (fetchedGuides.isEmpty()) {
            listViewGuides.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            guideList.addAll(fetchedGuides);
            guideAdapter.notifyDataSetChanged();
            listViewGuides.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }

    // Open GuideInformationFragment and pass the selected guide's details
    private void openGuideInformationFragment(Guide guide) {
        // Create a new instance of GuideInformationFragment with the guide data
        GuideInformationFragment guideInformationFragment = GuideInformationFragment.newInstance(guide);

        // Begin the transaction and replace the current fragment with GuideInformationFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.myframe, guideInformationFragment); // Ensure the correct container ID
        transaction.addToBackStack(null); // Add to back stack so the user can navigate back
        transaction.commit(); // Commit the transaction
    }

}
