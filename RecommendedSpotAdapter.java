package com.example.guidelink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommendedSpotAdapter extends RecyclerView.Adapter<RecommendedSpotAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Hotspot> recommendedSpots;

    public RecommendedSpotAdapter(Context context, ArrayList<Hotspot> recommendedSpots) {
        this.context = context;
        this.recommendedSpots = recommendedSpots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommended_spot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotspot hotspot = recommendedSpots.get(position);

        holder.tvPlaceName.setText(hotspot.getPlacename());
        holder.tvCity.setText(hotspot.getCity());

        // Load image from URL using Picasso or any other image loader library
        Picasso.get().load(hotspot.getImageUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return recommendedSpots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaceName, tvCity;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvCity = itemView.findViewById(R.id.tvCity);
            ivImage = itemView.findViewById(R.id.ivHotspotImage);
        }
    }

}

