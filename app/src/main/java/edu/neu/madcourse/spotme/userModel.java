package edu.neu.madcourse.spotme;

public class userModel {
    String fullName;
    int userImgInt;

    public userModel(String fullName, int userImgInt) {
        this.fullName = fullName;
        this.userImgInt = userImgInt;
        //this.sportIconInt = sportIconInt;
    }


    public String getFullName() {
        return fullName;
    }

    public int getUserImgInt() {
        return userImgInt;
    }

}
