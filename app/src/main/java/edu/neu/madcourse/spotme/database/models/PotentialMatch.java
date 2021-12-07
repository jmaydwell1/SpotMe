package edu.neu.madcourse.spotme.database.models;

import java.util.List;

public class PotentialMatch {
    private String name;
    private String dob;
    private String gender;
    private String phone;
    private String tokenId;
    private String picture;
    private String email;
    private List<String> sports;
    private String latitude;
    private String longitude;

    public PotentialMatch() {
    }

    public PotentialMatch(String name, String dob, String gender, String phone, String tokenId, String picture, String email, List<String> sports, String latitude, String longitude) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.tokenId = tokenId;
        this.picture = picture;
        this.email = email;
        this.sports = sports;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
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

    public String getTokenId() {
        return tokenId;
    }

    public String getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getSports() {
        return sports;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
