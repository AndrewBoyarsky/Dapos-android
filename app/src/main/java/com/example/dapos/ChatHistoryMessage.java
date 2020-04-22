package com.example.dapos;

import lombok.Data;

@Data
public class ChatHistoryMessage {
    private String message;
    private String recipient;
    private long height;
    private String sender;
}
