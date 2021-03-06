package com.example.dapos.ui.main.dashboard;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dapos.AccountResponse;
import com.example.dapos.BlockchainSpec;
import com.example.dapos.HttpClient;
import com.example.dapos.data.LoginRepository;

import java.io.IOException;
import java.util.HashMap;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class DashboardViewModel extends ViewModel implements Runnable{

    private MutableLiveData<String> accountBalance = new MutableLiveData<>();
    private MutableLiveData<Long> height = new MutableLiveData<>();
    private MutableLiveData<String> errorText = new MutableLiveData<>();

    public DashboardViewModel() {
        height.setValue(0L);
        accountBalance.setValue("0");
        new Thread(this::run).start();
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            try {
                String s = HttpClient.getInstance().get(new HashMap<>(), "blockchain/status");
                BlockchainSpec spec = HttpClient.mapper.readValue(s, BlockchainSpec.class);
                height.postValue(spec.getHeight());
                String accountResponse = HttpClient.getInstance().get(new HashMap<>(), "accounts/" + LoginRepository.getInstance(null).getUser().getUserId());
                AccountResponse response = HttpClient.mapper.readValue(accountResponse, AccountResponse.class);
                accountBalance.postValue(String.valueOf(response.getBalance()));
            } catch (IOException e) {
                errorText.postValue("Network error: " + e.getMessage());
            }
            Thread.sleep(5000);
        }
    }
}