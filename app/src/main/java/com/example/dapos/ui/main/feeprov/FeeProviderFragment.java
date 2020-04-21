package com.example.dapos.ui.main.feeprov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.dapos.R;


public class FeeProviderFragment extends Fragment {

    private FeeProvViewModel feeProvViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feeProvViewModel =
                ViewModelProviders.of(this).get(FeeProvViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fee_provs, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        feeProvViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
