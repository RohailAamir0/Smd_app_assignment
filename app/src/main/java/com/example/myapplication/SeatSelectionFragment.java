package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SeatSelectionFragment extends Fragment {

    // All 48 seat IDs — clean uniform grid (8 per row × 6 rows)
    private static final int[] ALL_SEAT_IDS = {
        R.id.seatA1, R.id.seatA2, R.id.seatA3, R.id.seatA4, R.id.seatA5, R.id.seatA6, R.id.seatA7, R.id.seatA8,
        R.id.seatB1, R.id.seatB2, R.id.seatB3, R.id.seatB4, R.id.seatB5, R.id.seatB6, R.id.seatB7, R.id.seatB8,
        R.id.seatC1, R.id.seatC2, R.id.seatC3, R.id.seatC4, R.id.seatC5, R.id.seatC6, R.id.seatC7, R.id.seatC8,
        R.id.seatD1, R.id.seatD2, R.id.seatD3, R.id.seatD4, R.id.seatD5, R.id.seatD6, R.id.seatD7, R.id.seatD8,
        R.id.seatE1, R.id.seatE2, R.id.seatE3, R.id.seatE4, R.id.seatE5, R.id.seatE6, R.id.seatE7, R.id.seatE8,
        R.id.seatF1, R.id.seatF2, R.id.seatF3, R.id.seatF4, R.id.seatF5, R.id.seatF6, R.id.seatF7, R.id.seatF8
    };

    // Seat ID resource names for SharedPreferences persistence
    private static final String[] SEAT_NAMES = {
        "seatA1","seatA2","seatA3","seatA4","seatA5","seatA6","seatA7","seatA8",
        "seatB1","seatB2","seatB3","seatB4","seatB5","seatB6","seatB7","seatB8",
        "seatC1","seatC2","seatC3","seatC4","seatC5","seatC6","seatC7","seatC8",
        "seatD1","seatD2","seatD3","seatD4","seatD5","seatD6","seatD7","seatD8",
        "seatE1","seatE2","seatE3","seatE4","seatE5","seatE6","seatE7","seatE8",
        "seatF1","seatF2","seatF3","seatF4","seatF5","seatF6","seatF7","seatF8"
    };

    int selectedSeats = 0;
    final int SEAT_PRICE = 500;
    ArrayList<Button> seatButtons = new ArrayList<>();
    boolean[] isSelected;   // currently toggled by this user in this session
    boolean[] isBooked;     // pre-booked from a previous booking (persisted)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seat_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Movie movie = (Movie) requireArguments().getSerializable("movie");
        String movieName = movie.getName();
        String movieImage = movie.getImageName() != null && !movie.getImageName().isEmpty()
                ? movie.getImageName() : "";
        String prefKey = "booked_seats_" + movieName.replaceAll("\\s+", "_");

        TextView txtMovieName = view.findViewById(R.id.txtMovieName);
        TextView txtTotal = view.findViewById(R.id.txtTotal);
        txtMovieName.setText(movieName);

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        // Load previously booked seat names from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("CineFAST_Prefs", 0);
        Set<String> bookedSet = prefs.getStringSet(prefKey, new HashSet<>());

        isSelected = new boolean[ALL_SEAT_IDS.length];
        isBooked   = new boolean[ALL_SEAT_IDS.length];

        // Bind seat buttons and apply booked state
        for (int i = 0; i < ALL_SEAT_IDS.length; i++) {
            Button seat = view.findViewById(ALL_SEAT_IDS[i]);
            seatButtons.add(seat);

            if (bookedSet.contains(SEAT_NAMES[i])) {
                // Previously booked — grey, non-clickable
                isBooked[i] = true;
                seat.setBackgroundTintList(requireContext().getResources()
                        .getColorStateList(R.color.seat_booked, null));
                seat.setEnabled(false);
            }
        }

        View footerNowShowing = view.findViewById(R.id.footerNowShowing);
        View footerComingSoon = view.findViewById(R.id.footerComingSoon);

        if (movie.isComingSoon()) {
            // Disable all seats
            for (Button seat : seatButtons) seat.setEnabled(false);
            footerComingSoon.setVisibility(View.VISIBLE);
            footerNowShowing.setVisibility(View.GONE);

            view.findViewById(R.id.btnWatchTrailer).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
                startActivity(intent);
            });

        } else {
            footerNowShowing.setVisibility(View.VISIBLE);
            footerComingSoon.setVisibility(View.GONE);

            Button btnSnacks = view.findViewById(R.id.btnSnacks);
            btnSnacks.setEnabled(false);

            // Wire click listeners for available seats
            for (int i = 0; i < seatButtons.size(); i++) {
                if (!isBooked[i]) {
                    final int index = i;
                    seatButtons.get(i).setOnClickListener(v -> {
                        toggleSeat(index);
                        updateTotal(txtTotal, btnSnacks);
                    });
                }
            }

            // Book Seats → persist booked seats, show Toast, go to TicketSummary
            view.findViewById(R.id.btnBook).setOnClickListener(v -> {
                if (selectedSeats == 0) {
                    Toast.makeText(requireContext(), "Please select at least one seat", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Save newly selected seats into SharedPreferences
                Set<String> updatedBooked = new HashSet<>(bookedSet);
                for (int i = 0; i < ALL_SEAT_IDS.length; i++) {
                    if (isSelected[i]) updatedBooked.add(SEAT_NAMES[i]);
                }
                prefs.edit().putStringSet(prefKey, updatedBooked).apply();

                Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                navigateToTicketSummary(movieName, movieImage, selectedSeats, selectedSeats * SEAT_PRICE, 0);
            });

            btnSnacks.setOnClickListener(v -> {
                SnacksFragment fragment = new SnacksFragment();
                Bundle args = new Bundle();
                args.putString("movie_name", movieName);
                args.putString("movie_image", movieImage);
                args.putInt("seat_count", selectedSeats);
                args.putInt("ticket_price", selectedSeats * SEAT_PRICE);
                // Save booked seats before going to snacks
                Set<String> updatedBooked = new HashSet<>(bookedSet);
                for (int i = 0; i < ALL_SEAT_IDS.length; i++) {
                    if (isSelected[i]) updatedBooked.add(SEAT_NAMES[i]);
                }
                prefs.edit().putStringSet(prefKey, updatedBooked).apply();
                fragment.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            });
        }
    }

    private void toggleSeat(int index) {
        Button seat = seatButtons.get(index);
        if (isSelected[index]) {
            seat.setBackgroundTintList(requireContext().getResources()
                    .getColorStateList(R.color.seat_available, null));
            selectedSeats--;
            isSelected[index] = false;
        } else {
            seat.setBackgroundTintList(requireContext().getResources()
                    .getColorStateList(R.color.seat_selected, null));
            selectedSeats++;
            isSelected[index] = true;
        }
    }

    private void updateTotal(TextView txtTotal, Button btnSnacks) {
        txtTotal.setText("Total: $" + (selectedSeats * SEAT_PRICE));
        if (btnSnacks != null) btnSnacks.setEnabled(selectedSeats > 0);
    }

    private void navigateToTicketSummary(String movieName, String movieImage, int seats, int ticketPrice, int snacksPrice) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putString("movie_name", movieName);
        args.putString("movie_image", movieImage);
        args.putInt("seat_count", seats);
        args.putInt("ticket_price", ticketPrice);
        args.putInt("snacks_price", snacksPrice);
        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
