package com.example.comp4521project.ui.weekly;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeeklyViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WeeklyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is weekly fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}