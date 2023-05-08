package com.example.comp4521project.ui.monthly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.databinding.FragmentMonthlyBinding;
import com.example.comp4521project.ui.IFragment;
import com.example.comp4521project.ui.weekly.WeeklyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthlyFragment extends IFragment implements CalendarAdapter.OnItemListener{

    private FragmentMonthlyBinding binding;

    private TextView month;
    private RecyclerView calendarView;
    private LocalDate selectedDate;
    private Button monthBefore;
    private Button monthAfter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // MonthlyViewModel monthlyViewModel =
        //        new ViewModelProvider(this).get(MonthlyViewModel.class);
        MainActivity.currentFrag = this;

        binding = FragmentMonthlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        month = binding.monthTV;
        calendarView = binding.calendar;
        monthBefore = binding.btnbefore;
        monthAfter = binding.btnafter;
        selectedDate = LocalDate.now();
        setMonthView();

        monthBefore.setOnClickListener(monthbefore);
        monthAfter.setOnClickListener(monthafter);
        Log.d("haha", "called");
        return root;
    }

    private void setMonthView() {
        month.setText(monthFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext() ,selectedDate ,daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 7);
        calendarView.setLayoutManager(layoutManager);
        calendarView.setAdapter(calendarAdapter);
    }


    private ArrayList<String> daysInMonthArray(LocalDate Date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(Date);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <=42; ++i) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }
            else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;

    }

    private String monthFromDate(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private View.OnClickListener monthbefore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectedDate = selectedDate.minusMonths(1);
            setMonthView();
        }
    };

    private View.OnClickListener monthafter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectedDate = selectedDate.plusMonths(1);
            setMonthView();
        }
    };

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")){

            BottomNavigationView bnv = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
            //WeeklyFragment.selectedWeek =;
            bnv.setSelectedItemId(R.id.navigation_weekly);
        }
    }

    @Override
    public void refreshContent() {
        setMonthView();
    }
}