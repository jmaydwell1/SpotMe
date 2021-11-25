package edu.neu.madcourse.spotme.database.models;

public class Match {
    private String name;
    private String picture;
    private boolean match;

    public Match() {
    }

    public Match(String name, String picture, boolean match) {
        this.name = name;
        this.picture = picture;
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

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
