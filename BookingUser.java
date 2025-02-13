package com.example.guidelink;

public class BookingUser {
    private String tourDate;
    private int bookingId;
    private String userName;

    public BookingUser(String userName, String tourDate, int bookingId) {
        this.tourDate = tourDate;
        this.bookingId = bookingId;
        this.userName = userName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getTourDate() {
        return tourDate;
    }

    public String getUserName() { return userName; }
}