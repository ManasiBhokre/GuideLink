package com.example.guidelink;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private final List<Request> requests;
    private final Context context;
    private final FragmentManager fragmentManager;

    public RequestAdapter(List<Request> requests, Context context, FragmentManager fragmentManager) {
        this.requests = requests;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = requests.get(position);
        holder.tvUserId.setText("Name: " + request.getUserName());
        holder.tvTourDate.setText("Date: " + request.getTourDate());
        holder.tvGuideId.setText("Budget: "+ request.getBudget());
        holder.tvSpecialRequests.setText("Intrests: " + request.getIntrests());

        SQLiteHelper dbHelper = new SQLiteHelper(context);

        holder.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Update status to "confirmed" in bookingrequests table
                dbHelper.updateBookingStatus("confirmed",request.getBookingId());
            }
        });

        holder.tvDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update status to "declined" in bookingrequests table
                dbHelper.updateBookingStatus("declined",request.getBookingId());
            }
        });

        holder.tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentManager != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.myframe, new GuideHomeFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(context, "FragmentManager is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserId, tvGuideId, tvTourDate, tvSpecialRequests;
        TextView tvAccept, tvDecline, tvChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvGuideId = itemView.findViewById(R.id.tvGuideId);
            tvTourDate = itemView.findViewById(R.id.tvTourDate);
            tvSpecialRequests = itemView.findViewById(R.id.tvSpecialRequests);
            tvAccept = itemView.findViewById(R.id.tvAccept);
            tvDecline = itemView.findViewById(R.id.tvDecline);
            tvChat = itemView.findViewById(R.id.tvChat);
        }
    }
}
