package com.example.comp4521project.ui.financialhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.databinding.FragmentFinancialhelperBinding;
import com.example.comp4521project.ui.IFragment;

public class FinancialHelperFragment extends IFragment {

    private FragmentFinancialhelperBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFrag = this;

        FinancialHelperViewModel financialHelperViewModel =
                new ViewModelProvider(this).get(FinancialHelperViewModel.class);

        binding = FragmentFinancialhelperBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFinancialhelper;
        financialHelperViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refreshContent() {

    }
}