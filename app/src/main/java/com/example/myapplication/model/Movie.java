package com.example.myapplication.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String genre;
    private String duration;
    private int imageResId;
    private String trailerUrl;
    private boolean isComingSoon;

    public Movie(String name, String genre, String duration, int imageResId, String trailerUrl, boolean isComingSoon) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.imageResId = imageResId;
        this.trailerUrl = trailerUrl;
        this.isComingSoon = isComingSoon;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getImageResId() { return imageResId; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isComingSoon() { return isComingSoon; }
}
