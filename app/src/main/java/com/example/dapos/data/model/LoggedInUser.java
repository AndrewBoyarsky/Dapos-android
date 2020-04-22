package com.example.dapos.data.model;

import lombok.Data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Data
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String pass;

    public LoggedInUser(String userId, String displayName,String pass) {
        this.userId = userId;
        this.displayName = displayName;
        this.pass = pass;
    }

    public String getUserId() {
        return userId;
    }


    public String getDisplayName() {
        return displayName;
    }
}
