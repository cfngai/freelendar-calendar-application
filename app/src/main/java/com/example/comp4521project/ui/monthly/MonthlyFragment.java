package com.example.comp4521project.ui.monthly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521project.databinding.FragmentMonthlyBinding;

public class MonthlyFragment extends Fragment {

    private FragmentMonthlyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MonthlyViewModel monthlyViewModel =
                new ViewModelProvider(this).get(MonthlyViewModel.class);

        binding = FragmentMonthlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMonthly;
        monthlyViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}