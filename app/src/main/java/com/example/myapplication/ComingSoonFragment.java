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

import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Oppenheimer", "Drama/History", "180 min",
                R.drawable.openheimer, "https://www.youtube.com/watch?v=uYPbbksJxIg", true));
        movies.add(new Movie("Dune: Part Two", "Sci-Fi", "166 min",
                R.drawable.inception, "https://www.youtube.com/watch?v=Way9Dexny3w", true));
        movies.add(new Movie("Gladiator II", "Action", "148 min",
                R.drawable.interstellar, "https://www.youtube.com/watch?v=f6UWlF0F90A", true));

        RecyclerView rv = view.findViewById(R.id.rvComingSoon);
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
