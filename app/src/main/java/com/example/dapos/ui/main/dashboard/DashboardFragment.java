package com.example.dapos.ui.main.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dapos.R;
import com.example.dapos.data.LoginRepository;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public static void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView accountIdView = root.findViewById(R.id.cryptoAccountText);
        accountIdView.setText(LoginRepository.getInstance(null).getUser().getUserId());
        TextView heightText = root.findViewById(R.id.blockchainHeightText);
        accountIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = accountIdView.getText().toString();
                setClipboard(getContext(), s);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        dashboardViewModel.getHeight().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long s) {
                if (s == null) {
                    s = 0L;
                }
                heightText.setText(s.toString());
            }
        });
        TextView accountBalanceView = root.findViewById(R.id.balanceText);
        dashboardViewModel.getAccountBalance().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) {
                    s = "0";
                }
                accountBalanceView.setText(s);
            }
        });
        dashboardViewModel.getErrorText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (!s.contains("account")) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }
}
