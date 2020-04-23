package com.example.dapos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dapos.data.LoginRepository;

import java.io.IOException;
import java.util.HashMap;

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
        Button button = findViewById(R.id.sendMessageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar progressBar = findViewById(R.id.messageSendProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                try {
                    TextView text = findViewById(R.id.newMessageField);
                    CharSequence message = text.getText();
                    TextView t2 = findViewById(R.id.messageSendRecipient);
                    CharSequence recipientSequence = t2.getText();
                    TransactionSendData data = new TransactionSendData();
                    data.setAccount(LoginRepository.getInstance(null).getUser().getUserId());
                    boolean me = recipientSequence.toString().equalsIgnoreCase("me");
                    if (!me) {
                        data.setRecipient(recipientSequence.toString());
                    }
                    data.setData(message.toString());
                    data.setPass(LoginRepository.getInstance(null).getUser().getPass());

                    data.setToSelf(me);
                    try {
                        String response = HttpClient.getInstance().postBody(data, "txs/messages");
                        TransactionSendResponse value = HttpClient.mapper.readValue(response, TransactionSendResponse.class);
                        Intent intent1 = new Intent(getBaseContext(), TransactionStatus.class);
                        intent1.putExtra("height", String.valueOf(value.getResult().getHeight()));
                        intent1.putExtra("hash", value.getResult().getHash());
                        startActivity(intent1);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
