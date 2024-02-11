package com.example.projekt.dto;

public class Message {
    private String message;

    public Message() {}

    public Message(String text) {
        this.message = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
