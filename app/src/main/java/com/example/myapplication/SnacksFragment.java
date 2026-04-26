package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.adapter.SnackAdapter;
import com.example.myapplication.model.Snack;
import com.example.myapplication.util.SnackDatabaseHelper;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private SnackAdapter adapter;
    private TextView txtTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String movie = requireArguments().getString("movie_name");
        int seats = requireArguments().getInt("seat_count", 0);
        int ticketPrice = requireArguments().getInt("ticket_price", 0);
        String movieImage = requireArguments().getString("movie_image", "");

        txtTotal = view.findViewById(R.id.txtSnackTotal);

        // Load snacks from SQLite database (not hardcoded)
        SnackDatabaseHelper dbHelper = new SnackDatabaseHelper(requireContext());
        ArrayList<Snack> snacks = dbHelper.getAllSnacks();

        adapter = new SnackAdapter(requireContext(), snacks, this::updateTotal);

        ListView listView = view.findViewById(R.id.listSnacks);
        listView.setAdapter(adapter);

        view.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            int snacksTotal = adapter.getTotalCost();

            TicketSummaryFragment fragment = new TicketSummaryFragment();
            Bundle args = new Bundle();
            args.putString("movie_name", movie);
            args.putInt("seat_count", seats);
            args.putInt("ticket_price", ticketPrice);
            args.putInt("snacks_price", snacksTotal);
            args.putString("movie_image", movieImage); // forward movie image
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
    }

    private void updateTotal() {
        if (adapter != null && txtTotal != null) {
            txtTotal.setText("Snacks Total: $" + adapter.getTotalCost());
        }
    }
}
