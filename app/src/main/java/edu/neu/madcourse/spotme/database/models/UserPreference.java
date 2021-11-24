package edu.neu.madcourse.spotme.database.models;

import java.util.List;

public class UserPreference {

    private int distance;
    private List<String> genders;
    private int maxAge;
    private int minAge;
    private List<String> sports;

    public UserPreference() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserPreference(int distance, List<String> genders, int maxAge, int minAge, List<String> sports) {
        this.distance = distance;
        this.genders = genders;
        this.maxAge = maxAge;
        this.minAge = minAge;
        this.sports = sports;
    }

    public int getDistance() {
        return distance;
    }

    public List<String> getGenders() {
        return genders;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public List<String> getSports() {
        return sports;
    }
}
