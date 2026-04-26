package com.example.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final Context context;
    private final List<Booking> bookings;

    public BookingAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Set movie poster image
        if (booking.getMovieImage() != null && !booking.getMovieImage().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    booking.getMovieImage(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgPoster.setImageResource(resId);
            }
        }

        holder.tvMovieName.setText(booking.getMovieName());
        holder.tvDateTime.setText(booking.getDateTime());
        holder.tvSeats.setText(booking.getSeats() + " ticket(s)");
        holder.tvPrice.setText("$" + booking.getTotalPrice());

        holder.btnCancel.setOnClickListener(v -> showCancelDialog(booking, position));
    }

    private void showCancelDialog(Booking booking, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    cancelBooking(booking, position);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelBooking(Booking booking, int position) {
        // Check if booking is in the future
        long now = System.currentTimeMillis();
        if (booking.getTimestamp() <= now) {
            Toast.makeText(context, "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove from Firebase
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (userId == null) return;

        FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(userId)
                .child(booking.getBookingId())
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    bookings.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, bookings.size());
                    Toast.makeText(context, "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Cancellation failed. Try again.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieName, tvDateTime, tvSeats, tvPrice;
        Button btnCancel;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgBookingPoster);
            tvMovieName = itemView.findViewById(R.id.tvBookingMovieName);
            tvDateTime = itemView.findViewById(R.id.tvBookingDateTime);
            tvSeats = itemView.findViewById(R.id.tvBookingSeats);
            tvPrice = itemView.findViewById(R.id.tvBookingPrice);
            btnCancel = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
