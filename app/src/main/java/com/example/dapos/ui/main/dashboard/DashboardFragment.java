package com.example.dapos.ui.main.dashboard;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView accountIdView = root.findViewById(R.id.cryptoAccountText);
        accountIdView.setText(LoginRepository.getInstance(null).getUser().getUserId());
        TextView heightText = root.findViewById(R.id.blockchainHeightText);

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
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
