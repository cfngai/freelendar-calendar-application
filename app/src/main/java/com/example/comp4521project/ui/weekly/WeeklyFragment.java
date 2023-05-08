package com.example.comp4521project.ui.weekly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.databinding.FragmentWeeklyBinding;
import com.example.comp4521project.ui.IFragment;

import java.time.LocalDate;

public class WeeklyFragment extends IFragment {

    private FragmentWeeklyBinding binding;
    public static LocalDate SelectedWeek = LocalDate.now();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFrag = this;

        WeeklyViewModel weeklyViewModel =
                new ViewModelProvider(this).get(WeeklyViewModel.class);

        binding = FragmentWeeklyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textWeekly;
        weeklyViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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