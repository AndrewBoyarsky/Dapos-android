package com.example.dapos.ui.main.feeprov;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeeProvViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeeProvViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}