package com.example.guidelink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class HotspotListAdapter extends ArrayAdapter<Hotspot> {

    private Context context;
    private ArrayList<Hotspot> hotspots;
    RatingBar ratingBar;

    public HotspotListAdapter(Context context, ArrayList<Hotspot> hotspots) {
        super(context, 0, hotspots);
        this.context = context;
        this.hotspots = hotspots;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Hotspot hotspot = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recommended_spot, parent, false);
        }

        // Lookup views
        ratingBar = convertView.findViewById(R.id.userRating);
        ImageView ivHotspotImage = convertView.findViewById(R.id.ivHotspotImage);
        TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
        TextView tvCity = convertView.findViewById(R.id.tvCity);

        // Set rating value
        try {
            float rating = Float.parseFloat(hotspot.getRatingAsString()); // Convert to float
            ratingBar.setRating(rating);
        } catch (NumberFormatException e) {
            ratingBar.setRating(0); // Set a default rating in case of an error
            Log.e("HotspotListAdapter", "Error parsing rating: " + e.getMessage());
        }

        // Populate other views
        tvPlaceName.setText(hotspot.getPlacename());
        tvCity.setText(hotspot.getCity());

        // Load the image from the URI using ContentResolver
        if (hotspot.getImageUrl() != null) {
            Uri imageUri = Uri.parse(hotspot.getImageUrl());
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ivHotspotImage.setImageBitmap(bitmap); // Set the bitmap to the ImageView
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ivHotspotImage.setImageResource(R.drawable.placeholder_image); // Fallback image
            } catch (SecurityException e) {
                Log.e("HotspotListAdapter", "Permission issue: " + e.getMessage());
            }
        } else {
            ivHotspotImage.setImageResource(R.drawable.placeholder_image); // Fallback if imageUrl is null
        }

        // Return the completed view to render on screen
        return convertView;
    }

}


