package edu.neu.madcourse.spotme.database.models;

import java.util.List;

public class UserSports {
    List<String> sports;

    public UserSports() {
    }

    public UserSports(List<String> sports) {
        this.sports = sports;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }
}
