package com.example.comp4521project.ui.monthly;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonthlyViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MonthlyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is monthly fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}