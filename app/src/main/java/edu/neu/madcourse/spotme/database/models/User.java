package edu.neu.madcourse.spotme.database.models;


public class User {
    public String name;
    public String tokenId;
    public String dob;
    public String gender;
    public String phone;
    public String email;
    public String picture;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String tokenId, String dob, String gender, String phone, String email, String picture) {
        this.name = name;
        this.tokenId = tokenId;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }
}
