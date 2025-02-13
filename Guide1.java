package com.example.guidelink;

public class Guide1 {
    private String name;
    private String city;
    private String availability;
    private float rating;
    private String profilePic;

    public Guide1(String name, String city, String availability, float rating, String profilePic) {
        this.name = name;
        this.city = city;
        this.availability = availability;
        this.rating = rating;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAvailability() {
        return availability;
    }

    public float getRating() {
        return rating;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
