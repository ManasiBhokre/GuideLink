package com.example.guidelink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class ViewSpotFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewHotspots;
    private TextView tvNoResults;
    private SQLiteHelper dbHelper;

    private ArrayList<Hotspot> hotspotList;
    private ArrayAdapter<Hotspot> hotspotAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_spot, container, false);

        // Initialize UI elements
        searchView = view.findViewById(R.id.search_view);
        listViewHotspots = view.findViewById(R.id.list_view_guides); // reuse list_view_guides ID from XML
        tvNoResults = view.findViewById(R.id.tv_no_results);

        // Set up the database helper
        dbHelper = new SQLiteHelper(getContext());

        // Initialize hotspot list
        hotspotList = new ArrayList<>();

        // Set up the adapter for the ListView
        hotspotAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, hotspotList);
        listViewHotspots.setAdapter(hotspotAdapter);

        // Set search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchHotspots(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    hotspotList.clear();
                    hotspotAdapter.notifyDataSetChanged();
                    listViewHotspots.setVisibility(View.GONE);
                    tvNoResults.setVisibility(View.GONE);
                }
                return false;
            }
        });

        listViewHotspots.setOnItemClickListener((parent, view1, position, id) -> {
            Hotspot selectedHotspot = hotspotList.get(position);
            // Navigate to HotspotInformationFragment with the selected hotspot's data
            openHotspotInformationFragment(selectedHotspot);
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

    // Fetch hotspots based on search query
    private void fetchHotspots(String query) {
        hotspotList.clear();
        List<Hotspot> fetchedHotspots = dbHelper.searchHotspotsByName(query);

        if (fetchedHotspots.isEmpty()) {
            listViewHotspots.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            hotspotList.addAll(fetchedHotspots);
            hotspotAdapter.notifyDataSetChanged();
            listViewHotspots.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }

    // Open HotspotInformationFragment and pass the selected hotspot's details
    private void openHotspotInformationFragment(Hotspot hotspot) {
        HotspotInformationFragment hotspotInformationFragment = HotspotInformationFragment.newInstance(hotspot);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.myframe, hotspotInformationFragment); // Replace myframe with your actual container ID
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
