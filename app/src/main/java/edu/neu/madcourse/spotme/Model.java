package edu.neu.madcourse.spotme;

public class Model {

    private String text;
    private boolean isSelected = false;

    public Model(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
