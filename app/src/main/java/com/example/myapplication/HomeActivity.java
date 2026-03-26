package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Book Seats buttons
        Button book1 = findViewById(R.id.btnBook1);
        Button book2 = findViewById(R.id.btnBook2);
        Button book3 = findViewById(R.id.btnBook3);
        Button book4 = findViewById(R.id.btnBook4);

        book1.setOnClickListener(v -> openSeats("The Dark Knight"));
        book2.setOnClickListener(v -> openSeats("Inception"));
        book3.setOnClickListener(v -> openSeats("Interstellar"));
        book4.setOnClickListener(v -> openSeats("The Shawshank Redemption"));

        // Trailer buttons
        Button trailer1 = findViewById(R.id.btnTrailer1);
        Button trailer2 = findViewById(R.id.btnTrailer2);
        Button trailer3 = findViewById(R.id.btnTrailer3);
        Button trailer4 = findViewById(R.id.btnTrailer4);

        trailer1.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=EXeTwQWrcwY"));
        trailer2.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=YoHD9XEInc0"));
        trailer3.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=zSWdZVtXT7E"));
        trailer4.setOnClickListener(v -> openTrailer("https://www.youtube.com/watch?v=6hB3S9bIaco"));
    }

    private void openSeats(String movieName) {
        // SeatSelectionActivity replaced by SeatSelectionFragment inside MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("direct_movie_name", movieName);
        startActivity(intent);
    }

    private void openTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}


