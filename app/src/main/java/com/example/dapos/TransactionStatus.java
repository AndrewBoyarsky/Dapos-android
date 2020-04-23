package com.example.dapos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TransactionStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status);
        TextView heightText = findViewById(R.id.transactionHeightText);
        heightText.setText(getIntent().getStringExtra("height"));
        TextView hashText = findViewById(R.id.transactionHashText);
        hashText.setText(getIntent().getStringExtra("hash"));
    }
}
