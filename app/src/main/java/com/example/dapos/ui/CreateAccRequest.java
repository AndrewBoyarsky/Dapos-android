package com.example.dapos.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CreateAccRequest {
    private String pass;
    private int type = 2;

    public CreateAccRequest(String pass) {
        this.pass = pass;
    }
}
