package com.example.myapplication.util;

import android.content.Context;

import com.example.myapplication.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MovieJsonParser {

    private static ArrayList<Movie> parseMovies(Context context, Boolean filterComingSoon) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                boolean isComingSoon = obj.getBoolean("isComingSoon");

                if (filterComingSoon == null || filterComingSoon == isComingSoon) {
                    Movie movie = new Movie(
                            obj.getString("name"),
                            obj.getString("genre"),
                            obj.getString("duration"),
                            obj.getString("image"),
                            obj.getString("trailerUrl"),
                            isComingSoon
                    );
                    movies.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<Movie> getNowShowing(Context context) {
        return parseMovies(context, false);
    }

    public static ArrayList<Movie> getComingSoon(Context context) {
        return parseMovies(context, true);
    }

    public static ArrayList<Movie> getAllMovies(Context context) {
        return parseMovies(context, null);
    }
}
