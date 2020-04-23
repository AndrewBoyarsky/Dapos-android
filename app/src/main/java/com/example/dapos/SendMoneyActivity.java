package com.example.dapos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dapos.data.LoginRepository;
import com.example.dapos.data.model.LoggedInUser;

import java.io.IOException;

public class SendMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        Button but = findViewById(R.id.sendMoneyButton);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recipe = findViewById(R.id.moneySendRecipient);
                TextView amount = findViewById(R.id.moneyAmountText);
                TextView message = findViewById(R.id.paymentMessageField);
                TransactionSendData data = new TransactionSendData();
                LoggedInUser user = LoginRepository.getInstance(null).getUser();
                data.setPass(user.getPass());
                data.setAccount(user.getUserId());
                data.setRecipient(recipe.getText().toString());
                data.setAmount(Long.parseLong(amount.getText().toString()));
                data.setData(message.getText().toString());
                try {
                    String tx = HttpClient.getInstance().postBody(data, "txs/payments");
                    TransactionSendResponse response = HttpClient.mapper.readValue(tx, TransactionSendResponse.class);
                    Intent intent = new Intent(getApplicationContext(), TransactionStatusActivity.class);
                    intent.putExtra("hash", response.getResult().getHash());
                    intent.putExtra("height", String.valueOf(response.getResult().getHeight()));
                    startActivity(intent);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
