package com.example.dapos;

import lombok.Data;

@Data
public class ChatMessage {
    private long height;
    private long mid;
    private boolean toSelf;
    private String sender;
    private String recipient;
}
