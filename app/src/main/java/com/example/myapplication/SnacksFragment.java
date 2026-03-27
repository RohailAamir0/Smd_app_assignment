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

        txtTotal = view.findViewById(R.id.txtSnackTotal);

        ArrayList<Snack> snacks = new ArrayList<>();
        snacks.add(new Snack("Popcorn", "Large / Buttered", 8, R.drawable.snack_popcorn));
        snacks.add(new Snack("Nachos", "With Cheese Dip", 8, R.drawable.snack_nachos));
        snacks.add(new Snack("Soft Drink", "Large / Any Flavor", 6, R.drawable.snack_drink));
        snacks.add(new Snack("Candy Mix", "Assorted Candies", 6, R.drawable.snack_candy));
        snacks.add(new Snack("Hot Dog", "With Mustard", 7, R.drawable.snack_hotdog));

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
