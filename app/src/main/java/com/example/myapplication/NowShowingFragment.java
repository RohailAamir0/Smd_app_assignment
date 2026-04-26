package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.MovieAdapter;
import com.example.myapplication.model.Movie;
import com.example.myapplication.util.MovieJsonParser;

import java.util.ArrayList;

public class NowShowingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_showing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load movies from JSON asset (not hardcoded)
        ArrayList<Movie> movies = MovieJsonParser.getNowShowing(requireContext());

        RecyclerView rv = view.findViewById(R.id.rvNowShowing);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(new MovieAdapter(requireContext(), movies, movie -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle args = new Bundle();
            args.putSerializable("movie", movie);
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }));
    }
}
