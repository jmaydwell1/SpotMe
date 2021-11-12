package edu.neu.madcourse.spotme.database.models;

public class User {
    public String username;
    public String tokenId;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String tokenId) {
        this.username = username;
        this.tokenId = tokenId;
    }

    public String getUsername() {
        return username;
    }
}
