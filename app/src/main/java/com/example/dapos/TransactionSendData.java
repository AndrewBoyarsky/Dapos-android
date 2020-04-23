package com.example.dapos;

import lombok.Data;

@Data
public class TransactionSendData {
    private String pass;
    private String data;
    private String account;
    private String recipient;
    private long feeProvider;
    private long amount;
    private boolean toSelf;
}
