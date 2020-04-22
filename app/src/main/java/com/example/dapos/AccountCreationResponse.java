package com.example.dapos;

import lombok.Data;

@Data
public class AccountCreationResponse {
    private String account;
    private String publicKey;
    private String password;
}
