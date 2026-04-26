package com.example.myapplication.model;

public class Booking {
    private String bookingId;
    private String movieName;
    private String movieImage;
    private String dateTime;
    private int seats;
    private int totalPrice;
    private long timestamp;

    // Required empty constructor for Firebase
    public Booking() {}

    public Booking(String bookingId, String movieName, String movieImage,
                   String dateTime, int seats, int totalPrice, long timestamp) {
        this.bookingId = bookingId;
        this.movieName = movieName;
        this.movieImage = movieImage;
        this.dateTime = dateTime;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getMovieImage() { return movieImage; }
    public void setMovieImage(String movieImage) { this.movieImage = movieImage; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
