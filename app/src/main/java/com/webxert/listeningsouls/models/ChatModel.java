package com.webxert.listeningsouls.models;

/**
 * Created by hp on 12/10/2018.
 */


public class ChatModel {

    String email;
    String view_type;
    String message;

    public ChatModel() {
    }

    public ChatModel(String email, String view_type, String message) {
        this.email = email;
        this.view_type = view_type;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getView_type() {
        return view_type;
    }

    public void setView_type(String view_type) {
        this.view_type = view_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
