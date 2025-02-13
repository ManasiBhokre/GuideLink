package com.example.guidelink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NearbySpotAdapter extends RecyclerView.Adapter<NearbySpotAdapter.NearbySpotViewHolder> {


    private  List<Guide> guideList;
    private  Context context;

    public NearbySpotAdapter(List<Guide> guideList, Context context) {
        this.guideList = guideList;
        this.context = context;
    }

    @NonNull
    @Override
    public NearbySpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_spot, parent, false);
        return new NearbySpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbySpotViewHolder holder, int position) {
        Guide guide = guideList.get(position);

        // Assuming you have methods in NearbySpotViewHolder to bind data to views
        holder.bind(guide);

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) context;
                Fragment fragment = GuideInformationFragment.newInstance(guide);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.myframe, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    // Define NearbySpotViewHolder as an inner class
    public class NearbySpotViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSpotName, textViewSpotCity;

        public NearbySpotViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSpotName = itemView.findViewById(R.id.tvGuideName); // Ensure correct ID
            textViewSpotCity = itemView.findViewById(R.id.tvGuideCity); // Ensure correct ID
        }

        public void bind(Guide guide) {
            textViewSpotName.setText(guide.getName());
            textViewSpotCity.setText(guide.getCity());
        }
    }

}

