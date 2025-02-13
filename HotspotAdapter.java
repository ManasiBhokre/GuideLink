package com.example.guidelink;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class HotspotAdapter extends ArrayAdapter<Hotspot> {
    private Context context;
    private List<Hotspot> hotspotList;

    public HotspotAdapter(Context context, List<Hotspot> hotspotList) {
        super(context, 0, hotspotList);
        this.context = context;
        this.hotspotList = hotspotList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hotspot, parent, false);
        }

        Hotspot hotspot = getItem(position);
        if (hotspot == null) {
            Log.e("HotspotAdapter", "Hotspot object is null for position: " + position);
            return convertView; // Handle appropriately
        }

        // Find views by ID
        TextView tvHotspotName = convertView.findViewById(R.id.tv_name);
        TextView tvCity = convertView.findViewById(R.id.tv_city);
        TextView tvRating = convertView.findViewById(R.id.tv_rating);
        TextView tvDescription = convertView.findViewById(R.id.tv_description);

        // Set values for each view
        tvHotspotName.setText(hotspot.getPlacename());
        tvCity.setText(hotspot.getCity());
        tvRating.setText(hotspot.getRatingAsString());  // Assuming a method to get formatted rating
        tvDescription.setText(hotspot.getDescription());

        // Set click listener to open HotspotInformationFragment
        tvHotspotName.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            HotspotInformationFragment fragment = HotspotInformationFragment.newInstance(hotspot);
            transaction.replace(R.id.myframe, fragment); // Replace myframe with the container ID in your layout
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return convertView;
    }
}

