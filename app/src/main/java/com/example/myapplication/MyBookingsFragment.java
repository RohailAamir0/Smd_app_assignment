package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.BookingAdapter;
import com.example.myapplication.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsFragment extends Fragment {

    private RecyclerView rvBookings;
    private TextView tvNoBookings;
    private List<Booking> bookingList;
    private BookingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvBookings = view.findViewById(R.id.rvBookings);
        tvNoBookings = view.findViewById(R.id.tvNoBookings);

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(requireContext(), bookingList);
        rvBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvBookings.setAdapter(adapter);

        loadBookingsFromFirebase();
    }

    private void loadBookingsFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            tvNoBookings.setVisibility(View.VISIBLE);
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance()
                .getReference("bookings").child(userId);

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot bookingSnap : snapshot.getChildren()) {
                    Booking booking = bookingSnap.getValue(Booking.class);
                    if (booking != null) {
                        booking.setBookingId(bookingSnap.getKey());
                        bookingList.add(booking);
                    }
                }

                adapter.notifyDataSetChanged();

                if (bookingList.isEmpty()) {
                    tvNoBookings.setVisibility(View.VISIBLE);
                    rvBookings.setVisibility(View.GONE);
                } else {
                    tvNoBookings.setVisibility(View.GONE);
                    rvBookings.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvNoBookings.setVisibility(View.VISIBLE);
                tvNoBookings.setText("Failed to load bookings.");
            }
        });
    }
}
