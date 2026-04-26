package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnMovieClickListener {
        void onBookSeats(Movie movie);
    }

    private final Context context;
    private final ArrayList<Movie> movies;
    private final OnMovieClickListener listener;

    public MovieAdapter(Context context, ArrayList<Movie> movies, OnMovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        // Resolve image: prefer imageResId, fall back to name-based lookup
        if (movie.getImageResId() != 0) {
            holder.imgPoster.setImageResource(movie.getImageResId());
        } else if (movie.getImageName() != null && !movie.getImageName().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    movie.getImageName(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgPoster.setImageResource(resId);
            }
        }

        holder.tvName.setText(movie.getName());
        holder.tvInfo.setText(movie.getGenre() + " / " + movie.getDuration());

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            context.startActivity(intent);
        });

        holder.btnBook.setOnClickListener(v -> listener.onBookSeats(movie));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvName, tvInfo;
        Button btnTrailer, btnBook;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgMoviePoster);
            tvName = itemView.findViewById(R.id.tvMovieName);
            tvInfo = itemView.findViewById(R.id.tvMovieInfo);
            btnTrailer = itemView.findViewById(R.id.btnMovieTrailer);
            btnBook = itemView.findViewById(R.id.btnMovieBook);
        }
    }
}
