package com.example.comp4521project.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.databinding.FragmentHomeBinding;
import com.example.comp4521project.ui.IFragment;
import com.example.comp4521project.util.IEvent;
import com.example.comp4521project.util.Time;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends IFragment implements ConfirmListAdapter.OnButtonClickListener{

    private FragmentHomeBinding binding;

    private ImageView calendar;
    private ListView todo_list;


    private ArrayList<Map<String, Object>> mList;

    private ArrayList<String> Todo_time;
    private ArrayList<String> Todo_name;
    private ViewPager2 viewPager;

    private TextView financialResult;

    private LocalDate today;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFrag = this;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        calendar = binding.smallCalendarView;

        //todo_list
        todo_list = binding.todayList;
        today = LocalDate.now();
        calendar.setOnClickListener(clickCalendar);


        // confirm_list
        viewPager = binding.viewPager;

        // financial help
        financialResult = binding.homeFinancialResult;
        financialResult.setOnClickListener(clickFinancialResult);

        setTodoList();
        setConfirmView();
        setFinancialView();



        return root;
    }

    private void setFinancialView() {
        LocalDate firstDay = today.withDayOfMonth(1);
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        List<IEvent> events = MainActivity.dataLoader.getEventsByPeriod(firstDay, lastDay);

        Log.d("financial helper", "before for loop");
        float Completed_income = 0;
        float Total_income = 0;

        if (events != null) {
            for (IEvent event : events) {

                if (event.getStatus() == 1) { // work completed
                    Completed_income += event.getIncome();
                }

                Total_income += event.getIncome();
            }
        }
        Log.d("financial helper", "after for loop");

        String output = "\n";

        output += "Confirmed Income: $" + String.valueOf(Completed_income) + "\n\n";
        output += "Total Income: $" + String.valueOf(Total_income)+ "\n";

        financialResult.setText(output);

    }

    private void setConfirmView(){

        ArrayList<IEvent> events = confirmEventArray();

        if (events == null){
            return;
        }

        ConfirmListAdapter confirmListAdapter = new ConfirmListAdapter(getContext(),events, this);
        viewPager.setAdapter(confirmListAdapter);
    }


    private ArrayList<IEvent> confirmEventArray(){
        List<IEvent> events = MainActivity.dataLoader.getEventsByPeriod(LocalDate.now().minusDays(90), LocalDate.now(), 0);

        if (events == null) // no work today
        {
            return null;
        }
        else
        {

            Collections.sort(events, new Comparator<IEvent>(){
                public int compare(IEvent o1, IEvent o2){
                    return o1.getStartTime().toInt() - o2.getStartTime().toInt();
                }

            });

            ArrayList<IEvent> unconfirmEventArray = new ArrayList<>(events);

            return unconfirmEventArray;
        }

    }

    private void setTodoList() {

        List<IEvent> events = MainActivity.dataLoader.getEventsByDateWithTargetStatus(today, -1); // get today event

        mList = new ArrayList<>();


        Todo_time =  new ArrayList<>();
        Todo_name = new ArrayList<>();


        if (events == null) // no work today
        {
            Todo_time.add("NO");
            Todo_name.add("Upcoming work");

        }
        else {
            // sort
            Collections.sort(events, new Comparator<IEvent>(){
                public int compare(IEvent o1, IEvent o2){
                    return o1.getStartTime().toInt() - o2.getStartTime().toInt();
                }

            });
            for (IEvent event : events) {


                if (event.getStatus() == -1) { // get upcoming event
                    // add event time
                    Time _time = event.getStartTime();
                    String str_event_time = String.format("%02d:%02d", _time.getHour(), _time.getMinute());

                    Todo_time.add(str_event_time);

                    // add event name
                    String _name = event.getTitle();
                    Todo_name.add(_name);

                }
            }

        }

        for (int i = 0; i < Todo_name.size(); ++i) {
            Map<String, Object> item = new HashMap<>();
            item.put("todo_time", Todo_time.get(i));
            item.put("todo_name", Todo_name.get(i));
            mList.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(getContext(), mList,
                R.layout.list_todo,
                new String[]{"todo_time", "todo_name"},
                new int[]{R.id.todo_time, R.id.todo_name});
        todo_list.setAdapter(adapter);


    }


    private View.OnClickListener clickCalendar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            BottomNavigationView bnv = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
            bnv.setSelectedItemId(R.id.navigation_monthly);
        }
    };

    private View.OnClickListener clickFinancialResult = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            BottomNavigationView bnv = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
            bnv.setSelectedItemId(R.id.navigation_financialhelper);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refreshContent() {
        setTodoList();
        setConfirmView();
        setFinancialView();

    }

    @Override
    public void onButtonClick() {
        setFinancialView();
    }
}
