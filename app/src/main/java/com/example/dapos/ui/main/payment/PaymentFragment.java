package com.example.dapos.ui.main.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dapos.ChatHist;
import com.example.dapos.ChatMessage;
import com.example.dapos.HttpClient;
import com.example.dapos.LedgerEntry;
import com.example.dapos.R;
import com.example.dapos.SendMoneyActivity;
import com.example.dapos.data.LoginRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.example.dapos.ui.main.dashboard.DashboardFragment.setClipboard;


public class PaymentFragment extends Fragment {

    private PaymentViewModel paymentViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paymentViewModel =
                ViewModelProviders.of(this).get(PaymentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_payments, container, false);
        TableLayout table = root.findViewById(R.id.paymentTable);
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "PAYMENT");
        try {
            String userId = LoginRepository.getInstance(null).getUser().getUserId();
            String fragments = HttpClient.getInstance().get(params, "accounts/" + userId + "/ledger");
            List<LedgerEntry> entries = HttpClient.mapper.readValue(fragments, new TypeReference<List<LedgerEntry>>() {
            });
            for (LedgerEntry chatMessage : entries) {
                TextView fromTextView = new TextView(getContext());
                fromTextView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                fromTextView.setMaxWidth(400);
                if (userId.equalsIgnoreCase(chatMessage.getSender())) {
                    fromTextView.setText("ME");
                } else {
                    fromTextView.setText(chatMessage.getSender());
                    fromTextView.setTextSize(9);
                }
                fromTextView.setHeight(100);
                fromTextView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String s = fromTextView.getText().toString();
                                                        if (s.equalsIgnoreCase("ME")) {
                                                            s = LoginRepository.getInstance(null).getUser().getUserId();
                                                        }
                                                        setClipboard(getContext(), s);
                                                        Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                );
                TextView toTextView = new TextView(getContext());
                toTextView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                toTextView.setMaxWidth(400);
                toTextView.setHeight(100);
                if (userId.equalsIgnoreCase(chatMessage.getRecipient())) {
                    toTextView.setText("ME");
                } else {
                    toTextView.setText(chatMessage.getRecipient());
                    toTextView.setTextSize(9);
                }
                toTextView.setPadding(20, 0, 0, 0);
                toTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = toTextView.getText().toString();
                        if (s.equalsIgnoreCase("ME")) {
                            s = LoginRepository.getInstance(null).getUser().getUserId();
                        }
                        setClipboard(getContext(), s);
                        Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                });
                TextView heightView = new TextView(getContext());
                heightView.setText(String.valueOf(chatMessage.getHeight()));
                TextView amount = new TextView(getContext());
                amount.setText(String.valueOf(chatMessage.getAmount()));
                amount.setPadding(20, 0, 0, 0);

                TableRow tableRow = new TableRow(getContext());
                tableRow.addView(fromTextView);
                tableRow.addView(toTextView);
                tableRow.addView(amount);
                tableRow.addView(heightView);
                tableRow.setPadding(0, 20, 0, 0);
//                tableRow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), ChatHist.class);
//                        TableRow v1 = (TableRow) v;
//                        View account = v1.getChildAt(0);
//                        CharSequence text = ((TextView) account).getText();
//                        String s1 = text.toString();
//                        intent.putExtra("recipient", s1);
//                        startActivity(intent);
//                    }
//                });

                table.addView(tableRow);
            }
        } catch (
                IOException e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        View view = root.findViewById(R.id.sendMoney);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendMoneyActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
