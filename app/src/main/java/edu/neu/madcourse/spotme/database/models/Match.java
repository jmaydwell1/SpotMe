package edu.neu.madcourse.spotme.database.models;

public class Match {
    private String name;
    private String picture;
    private String date;
    private boolean match;

    public Match() {
    }

    public Match(String name, String picture, String date, boolean match) {
        this.name = name;
        this.picture = picture;
        this.date = date;
        this.match = match;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
