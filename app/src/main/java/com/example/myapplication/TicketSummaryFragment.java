package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.util.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TicketSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String movie = requireArguments().getString("movie_name");
        int seats = requireArguments().getInt("seat_count", 0);
        int tPrice = requireArguments().getInt("ticket_price", 0);
        int sPrice = requireArguments().getInt("snacks_price", 0);
        String movieImage = requireArguments().getString("movie_image", "");
        int total = tPrice + sPrice;

        // Populate views
        ((TextView) view.findViewById(R.id.txtMovieTitle)).setText(movie != null ? movie : "Unknown Movie");
        ((TextView) view.findViewById(R.id.txtSeatInfo)).setText(seats + " seat(s)");
        ((TextView) view.findViewById(R.id.txtTicketPrice)).setText(tPrice + " USD");
        ((TextView) view.findViewById(R.id.txtSnackInfo)).setText(sPrice > 0 ? "Snacks selected" : "No snacks");
        ((TextView) view.findViewById(R.id.txtSnackPrice)).setText(sPrice + " USD");
        ((TextView) view.findViewById(R.id.txtTotalPrice)).setText(total + " USD");

        // Save to SharedPreferences (last booking — backward compat)
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("CineFAST_Prefs", 0);
        prefs.edit()
                .putString("last_movie", movie)
                .putInt("last_seats", seats)
                .putInt("last_total", total)
                .apply();

        // Save booking to Firebase Realtime Database
        saveBookingToFirebase(movie, movieImage, seats, total);

        // Show booking confirmed toast
        Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_LONG).show();

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        // Done button
        view.findViewById(R.id.btnDone).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE));

        String ticketMessage = "🎬 CineFAST Booking Confirmation\n\n" +
                "Movie: " + movie + "\nSeats: " + seats +
                "\nTicket Price: $" + tPrice + "\nSnacks: $" + sPrice +
                "\nTotal: $" + total + "\n\nThank you for booking with CineFAST!";

        // Email
        view.findViewById(R.id.btnEmail).setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket - " + movie);
            emailIntent.putExtra(Intent.EXTRA_TEXT, ticketMessage);
            if (emailIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(requireContext(), "No email app found", Toast.LENGTH_SHORT).show();
            }
        });

        // WhatsApp
        view.findViewById(R.id.btnWhatsApp).setOnClickListener(v -> {
            try {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, ticketMessage);
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException e) {
                Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBookingToFirebase(String movieName, String movieImage, int seats, int totalPrice) {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() == null) return;

            String userId = auth.getCurrentUser().getUid();
            DatabaseReference bookingsRef = FirebaseDatabase.getInstance()
                    .getReference("bookings").child(userId);

            // Generate booking ID
            String bookingId = bookingsRef.push().getKey();
            if (bookingId == null) return;

            // Get current date/time
            long timestamp = System.currentTimeMillis();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date(timestamp));

            // Build booking data
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("movieName", movieName);
            bookingData.put("movieImage", movieImage);
            bookingData.put("seats", seats);
            bookingData.put("totalPrice", totalPrice);
            bookingData.put("dateTime", dateTime);
            bookingData.put("timestamp", timestamp);

            bookingsRef.child(bookingId).setValue(bookingData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
