package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.model.Movie;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            String directMovie = getIntent().getStringExtra("direct_movie_name");

            if (directMovie != null) {
                // Launched from HomeActivity "Book Seats" — open SeatSelectionFragment directly
                Movie movie = getMovieByName(directMovie);
                SeatSelectionFragment fragment = new SeatSelectionFragment();
                Bundle args = new Bundle();
                args.putSerializable("movie", movie);
                fragment.setArguments(args);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            } else {
                // Normal entry point — load HomeFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new HomeFragment())
                        .commit();
            }
        }
    }

    /**
     * Returns the correct Movie object for the given name (matching HomeActivity's 4 movies).
     */
    private Movie getMovieByName(String name) {
        switch (name) {
            case "The Dark Knight":
                return new Movie(name, "Action/Crime", "152 min",
                        R.drawable.onboarding, "https://www.youtube.com/watch?v=EXeTwQWrcwY", false);
            case "Inception":
                return new Movie(name, "Sci-Fi", "148 min",
                        R.drawable.inception, "https://www.youtube.com/watch?v=YoHD9XEInc0", false);
            case "Interstellar":
                return new Movie(name, "Sci-Fi", "169 min",
                        R.drawable.interstellar, "https://www.youtube.com/watch?v=zSWdZVtXT7E", false);
            case "The Shawshank Redemption":
                return new Movie(name, "Drama", "142 min",
                        R.drawable.onboarding, "https://www.youtube.com/watch?v=6hB3S9bIaco", false);
            default:
                return new Movie(name, "Action", "120 min",
                        R.drawable.onboarding, "", false);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
