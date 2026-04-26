package com.example.myapplication.model;

public class Snack {
    private String name;
    private String description;
    private int price;
    private int imageResId;   // 0 if loaded from DB
    private String imageName; // used when loaded from DB

    // Original constructor (backward compat)
    public Snack(String name, String description, int price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.imageName = "";
    }

    // New constructor for SQLite-loaded snacks (uses image name string)
    public Snack(String name, String description, int price, String imageName) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = 0;
        this.imageName = imageName;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getImageName() { return imageName; }
}
