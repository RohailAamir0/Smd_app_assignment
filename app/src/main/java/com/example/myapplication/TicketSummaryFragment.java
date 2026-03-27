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
        int total = tPrice + sPrice;

        // Populate views
        ((TextView) view.findViewById(R.id.txtMovieTitle)).setText(movie != null ? movie : "Unknown Movie");
        ((TextView) view.findViewById(R.id.txtSeatInfo)).setText(seats + " seat(s)");
        ((TextView) view.findViewById(R.id.txtTicketPrice)).setText(tPrice + " USD");
        ((TextView) view.findViewById(R.id.txtSnackInfo)).setText(sPrice > 0 ? "Snacks selected" : "No snacks");
        ((TextView) view.findViewById(R.id.txtSnackPrice)).setText(sPrice + " USD");
        ((TextView) view.findViewById(R.id.txtTotalPrice)).setText(total + " USD");

        // Save to SharedPreferences (last booking info)
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("CineFAST_Prefs", 0);
        prefs.edit()
                .putString("last_movie", movie)
                .putInt("last_seats", seats)
                .putInt("last_total", total)
                .apply();

        // Show booking confirmed toast
        Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_LONG).show();

        // Back button → one step back
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        // Done button → clear entire back stack and return to Home
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
}
