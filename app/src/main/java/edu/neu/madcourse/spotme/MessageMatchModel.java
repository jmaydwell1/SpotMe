package edu.neu.madcourse.spotme;

public class MessageMatchModel {
    String nameOfMatch, dateOfMatch;
    int messageIcon, matchIcon;

    public MessageMatchModel(String nameOfMatch, String dateOfMatch,
                             int messageIcon, int matchIcon) {
        this.nameOfMatch = nameOfMatch;
        this.dateOfMatch = dateOfMatch;
        this.messageIcon = messageIcon;
        this.matchIcon = matchIcon;
    }

    public String getNameOfMatch() { return nameOfMatch; }

    public String getDateOfMatch() {
        return dateOfMatch;
    }

    public int getMessageIcon() {
        return messageIcon;
    }

    public int getMatchIcon() {
        return matchIcon;
    }

}

