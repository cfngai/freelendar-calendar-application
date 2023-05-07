package com.example.comp4521project.ui.financialhelper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FinancialHelperViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FinancialHelperViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is financial fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}