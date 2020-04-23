package com.example.dapos;

import lombok.Data;

@Data
public class AccountResponse {
    private Long height;
    private String cryptoId;
    private String publicKey;
    private Long balance;
}
