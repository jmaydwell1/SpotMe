package edu.neu.madcourse.spotme.database.models;

public class User {
    public String email;
    public String tokenId;
    public String dob;
    public String gender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String tokenId, String dob, String gender) {
        this.email = email;
        this.tokenId = tokenId;
        this.dob = dob;
        this.gender = gender;
    }

}
