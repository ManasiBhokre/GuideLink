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

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private final List<Booking> bookings;
    private final Context context;
    private final FragmentManager fragmentManager;

    public BookingAdapter(List<Booking> bookings, Context context, FragmentManager fragmentManager) {
        this.bookings = bookings;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvGuideName.setText("Name: " + booking.getGuideName());
        holder.tvTourDate.setText("Date: " + booking.getTourDate());

        SQLiteHelper dbHelper = new SQLiteHelper(context);
        holder.tvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.updateBookingStatus("completed",booking.getBookingId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGuideName, tvTourDate, tvCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuideName = itemView.findViewById(R.id.tvGuideName);
            tvTourDate = itemView.findViewById(R.id.tvDate);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
        }
    }
}