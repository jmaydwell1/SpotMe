package edu.neu.madcourse.spotme.database.models;


public class User {
    public String name;
    public String tokenId;
    public String dob;
    public String gender;
    public String phone;
    public String email;
    public UserLocation location;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String tokenId, String name, String phone, String dob, String gender, String email, UserLocation location) {
        this.tokenId = tokenId;
        this.name = name;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }
}
