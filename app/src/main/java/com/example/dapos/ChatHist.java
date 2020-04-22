package com.example.dapos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dapos.data.LoginRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ChatHist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_hist);
        Intent intent = getIntent();
        String recipient = intent.getStringExtra("recipient");
        HashMap<String, String> params = new HashMap<>();
        String userId = LoginRepository.getInstance(null).getUser().getUserId();
        params.put("sender", userId);
        params.put("pass", LoginRepository.getInstance(null).getUser().getPass());
        boolean isMe = true;
        if (!recipient.equalsIgnoreCase("ME")) {
            params.put("recipient", recipient);
            isMe = false;
        }
        try {
            TextView chatter = findViewById(R.id.chatterText);
            TextView label = findViewById(R.id.chatHistLabel);
            chatter.setText(" " + recipient);
            if (isMe) {
                chatter.setTextSize(30);
            } else {
//                chatter.setText(recipient);
            }
            String messages = HttpClient.getInstance().get(params, "messages");
            List<ChatHistoryMessage> chatHistoryMessages = HttpClient.mapper.readValue(messages, new TypeReference<List<ChatHistoryMessage>>() {
            });
            LinearLayout layout = findViewById(R.id.chatHistPanel);
            for (ChatHistoryMessage chatHistoryMessage : chatHistoryMessages) {
                TextView view = new TextView(getBaseContext());
                boolean fromMe = isMe || chatHistoryMessage.getSender().equalsIgnoreCase(userId);
                view.setText("From: " + (fromMe ? "ME" : chatHistoryMessage.getSender()) + ", at " + chatHistoryMessage.getHeight() + "\n" + chatHistoryMessage.getMessage());
                view.setPadding(30, 50, 0, 0);
                view.setTextColor(Color.WHITE);
                view.setBackgroundColor(Color.GRAY);
                layout.addView(view);
            }
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }
}
