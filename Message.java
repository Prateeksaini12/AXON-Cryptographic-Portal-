package com.project.model;

public class Message {

    private int id;
    private String code;
    private String text;
    private boolean viewed;

    // Constructor (without id)
    public Message(String code, String text, boolean viewed) {
        this.code = code;
        this.text = text;
        this.viewed = viewed;
    }

    // Constructor (with id)
    public Message(int id, String code, String text, boolean viewed) {
        this.id = id;
        this.code = code;
        this.text = text;
        this.viewed = viewed;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public boolean isViewed() {
        return viewed;
    }

    // Setters
    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}