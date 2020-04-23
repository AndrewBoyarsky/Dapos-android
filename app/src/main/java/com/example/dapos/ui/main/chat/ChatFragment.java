package com.example.dapos.ui.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.dapos.ChatHistoryMessage;
import com.example.dapos.ChatMessage;
import com.example.dapos.HttpClient;
import com.example.dapos.NewMessageActivity;
import com.example.dapos.R;
import com.example.dapos.data.LoginRepository;
import com.example.dapos.ui.main.payment.PaymentViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dapos.ui.main.dashboard.DashboardFragment.setClipboard;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chats, container, false);
        Map<String, String> str = new HashMap<>();
        String currentUserId = LoginRepository.getInstance(null).getUser().getUserId();
        str.put("account", currentUserId);
        String s = null;
        View viewById = root.findViewById(R.id.newMessageButton);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewMessageActivity.class);
                startActivity(intent);
            }
        });
        try {
            s = HttpClient.getInstance().get(str, "messages/chats");
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (s != null) {
            try {
                List<ChatMessage> chatMessages = HttpClient.mapper.readValue(s, new TypeReference<List<ChatMessage>>() {});
                TableLayout table = root.findViewById(R.id.chatTable);
                for (ChatMessage chatMessage : chatMessages) {
                    TextView accountTextView = new TextView(getContext());
                    if (chatMessage.isToSelf()) {
                        accountTextView.setText("ME");
                    } else if (chatMessage.getSender().equalsIgnoreCase(currentUserId)) {
                        accountTextView.setText(chatMessage.getRecipient());
                        accountTextView.setTextSize(9);
                    } else {
                        accountTextView.setText(chatMessage.getSender());
                        accountTextView.setTextSize(9);
                    }
                    accountTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s = accountTextView.getText().toString();
                            if (s.equalsIgnoreCase("ME")) {
                                s = LoginRepository.getInstance(null).getUser().getUserId();
                            }
                            setClipboard(getContext(), s);
                            Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    });
                    TextView timeView = new TextView(getContext());
                    timeView.setPadding(120, 0, 0, 0);
                    timeView.setText(String.valueOf(chatMessage.getHeight()));
                    TextView messageView = new TextView(getContext());
                    messageView.setText("*********");
                    messageView.setPadding(50, 0, 0, 0);
                    TableRow tableRow = new TableRow(getContext());
                    tableRow.addView(accountTextView);
                    tableRow.addView(timeView);
                    tableRow.addView(messageView);
                    tableRow.setPadding(0, 20, 0, 0);
                    tableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ChatHist.class);
                            TableRow v1 = (TableRow) v;
                            View account = v1.getChildAt(0);
                            CharSequence text = ((TextView) account).getText();
                            String s1 = text.toString();
                            intent.putExtra("recipient", s1);
                            startActivity(intent);
                        }
                    });
                    table.addView(tableRow);
                }
            } catch (JsonProcessingException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        return root;
    }

}
