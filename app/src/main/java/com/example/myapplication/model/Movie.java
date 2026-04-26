package com.example.myapplication.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private String name;
    private String genre;
    private String duration;
    private int imageResId;
    private String imageName;
    private String trailerUrl;
    private boolean isComingSoon;

    // Original constructor (backward compat — used nowhere new)
    public Movie(String name, String genre, String duration, int imageResId, String trailerUrl, boolean isComingSoon) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.imageResId = imageResId;
        this.imageName = "";
        this.trailerUrl = trailerUrl;
        this.isComingSoon = isComingSoon;
    }

    // New constructor for JSON-parsed movies (uses image name string)
    public Movie(String name, String genre, String duration, String imageName, String trailerUrl, boolean isComingSoon) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.imageResId = 0;
        this.imageName = imageName;
        this.trailerUrl = trailerUrl;
        this.isComingSoon = isComingSoon;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getImageResId() { return imageResId; }
    public String getImageName() { return imageName; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isComingSoon() { return isComingSoon; }
}
