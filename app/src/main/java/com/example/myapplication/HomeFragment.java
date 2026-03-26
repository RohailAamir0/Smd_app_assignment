package com.example.myapplication;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.adapter.HomePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Now Showing" : "Coming Soon");
        }).attach();

        // Three-dots menu
        view.findViewById(R.id.btnMenu).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenu().add(0, 1, 0, "View Last Booking");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    showLastBooking();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void showLastBooking() {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("CineFAST_Prefs", 0);
        String movie = prefs.getString("last_movie", null);
        int seats = prefs.getInt("last_seats", -1);
        int total = prefs.getInt("last_total", -1);

        String message;
        if (movie == null || seats == -1) {
            message = "No previous booking found.";
        } else {
            message = "Movie: " + movie + "\nSeats: " + seats + "\nTotal Price: $" + total;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Last Booking")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
