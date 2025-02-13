package com.example.guidelink;

public class Booking {
    private String guideName;
    private String tourDate;
    private int bookingId;

    public Booking(String guideName, String tourDate, int bookingId) {
        this.guideName = guideName;
        this.tourDate = tourDate;
        this.bookingId = bookingId;
    }

    public String getGuideName() {
        return guideName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getTourDate() {
        return tourDate;
    }
}