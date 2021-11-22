package edu.neu.madcourse.spotme.database.models;

public class PotentialMatch {
    private String name;
    private String dob;
    private String gender;
    private String phone;
    private String tokenId;

    public PotentialMatch() {
    }

    public PotentialMatch(String name, String dob, String gender, String phone, String tokenId) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.tokenId = tokenId;
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
}
