package com.example.comp4521project.ui.weekly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.databinding.FragmentWeeklyBinding;
import com.example.comp4521project.ui.IFragment;
import com.example.comp4521project.ui.monthly.CalendarAdapter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeeklyFragment extends IFragment implements CalendarAdapter.OnItemListener, TimetableAdapter.OnItemListener{


    private FragmentWeeklyBinding binding;
    public static LocalDate SelectedWeek = LocalDate.now();


    private TextView month;
    private RecyclerView weekCalendarView;
    private RecyclerView TimetableView;
    private Button weekBefore;
    private Button weekAfter;

    private ArrayList<LocalDate> daysInWeek;
    private TimetableAdapter timetableAdapter;

    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    // Handle the Intent
                }
                MainActivity.currentFrag.refreshContent();
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFrag = this;

        binding = FragmentWeeklyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        month = binding.monthTV;
        weekCalendarView = binding.weekCalendar;
        TimetableView = binding.timetable;
        weekBefore = binding.btnweekbefore;
        weekAfter = binding.btnweekafter;

        setWeekView();
        setTimetableView();

        weekBefore.setOnClickListener(weekbefore);
        weekAfter.setOnClickListener(weekafter);

        return root;
    }

    private void setWeekView() {
        month.setText(monthFromDate(SelectedWeek));
        daysInWeek = daysInWeekArray(SelectedWeek);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext() , SelectedWeek ,daysInWeek, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 7);
        weekCalendarView.setLayoutManager(layoutManager);
        weekCalendarView.setAdapter(calendarAdapter);
    }

    private void setTimetableView(){

        ArrayList<Integer> TimetableCell = TimetableCellArray();
        timetableAdapter = new TimetableAdapter(getContext(), SelectedWeek, daysInWeek , TimetableCell, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), 8);
        TimetableView.setLayoutManager(layoutManager);
        TimetableView.setAdapter(timetableAdapter);
    }

    private ArrayList<LocalDate> daysInWeekArray(LocalDate Date) {
        ArrayList<LocalDate> daysInWeekArray = new ArrayList<>();

        LocalDate week_sunday = FindSundayOnWeek(Date);
        LocalDate end_week = week_sunday.plusWeeks(1);

        while (week_sunday.isBefore((end_week))){

            daysInWeekArray.add(week_sunday);
            week_sunday = week_sunday.plusDays(1);

        }
        Log.d("haha",daysInWeekArray+"");
        return daysInWeekArray;
    }

    private ArrayList<Integer> TimetableCellArray(){
        ArrayList<Integer> timetableCellArray = new ArrayList<>();

        for (int i = 0 ; i < 49*8 ; ++i) {
            timetableCellArray.add(i);
        }

        return timetableCellArray;
    }

    private LocalDate FindSundayOnWeek(LocalDate Date) {

        LocalDate oneWeekAgo = Date.minusWeeks(1);

        while (Date.isAfter(oneWeekAgo)) {

            if(Date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return Date;
            }

            Date = Date.minusDays(1);

        }
        // never go here
        return null;

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

    @Override
    public void refreshContent() {
        setWeekView();
        setTimetableView();
    }

    private String DateToString(LocalDate date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        return date.format(formatter);
    }

    private View.OnClickListener weekbefore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectedWeek = SelectedWeek.minusWeeks(1);
            setWeekView();
            setTimetableView();
        }
    };

    private View.OnClickListener weekafter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectedWeek = SelectedWeek.plusWeeks(1);
            setWeekView();
            setTimetableView();
        }
    };

    @Override
    public void onItemClick(int position, String x) {

    }


    @Override
    public void onItemClick(int position) {
        timetableAdapter.clickOn(position, this.getActivity(), resultLauncher);
        //Toast.makeText(getContext().getApplicationContext(), "clicking"+ String.valueOf(position), Toast.LENGTH_LONG).show();

    }
}