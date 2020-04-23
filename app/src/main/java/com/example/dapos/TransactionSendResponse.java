package com.example.dapos;

import lombok.Data;

@Data
public class TransactionSendResponse {
    String id;
    Result result;

    @Data
    public static class Result {
        String hash;
        long height = -1;
    }
}