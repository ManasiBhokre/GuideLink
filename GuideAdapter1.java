package com.example.guidelink;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuideAdapter1 extends RecyclerView.Adapter<GuideAdapter1.GuideViewHolder> {

    private final ArrayList<Guide1> guideList;

    public GuideAdapter1(ArrayList<Guide1> guideList) {
        this.guideList = guideList;
    }

    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide1, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        Guide1 guide = guideList.get(position);

        holder.tvGuideName.setText(guide.getName());
        holder.tvCity.setText(guide.getCity());
        holder.tvRating.setText(String.format("Rating: %.1f", guide.getRating()));

        // Load the profile image if a URL or file path is available
        if (!TextUtils.isEmpty(guide.getProfilePic())) {
            Bitmap bitmap = BitmapFactory.decodeFile(guide.getProfilePic());
            holder.ivProfilePic.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(v -> {
            // Handle guide click event
            Toast.makeText(holder.itemView.getContext(), "Selected: " + guide.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public static class GuideViewHolder extends RecyclerView.ViewHolder {
        TextView tvGuideName, tvCity, tvRating;
        ImageView ivProfilePic;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuideName = itemView.findViewById(R.id.tvGuideName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvRating = itemView.findViewById(R.id.tvRating);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }
}

