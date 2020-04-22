package com.example.dapos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NewMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        Intent intent = getIntent();
        String recipient = intent.getStringExtra("recipient");
        TextView view = findViewById(R.id.messageSendRecipient);
        if (recipient != null) {
            view.setText(recipient);
        }
    }
}
