package com.example.comp4521project.ui.weekly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.AddEventIntent;
import com.example.comp4521project.ConfirmEventIntent;
import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.ReviewEventIntent;
import com.example.comp4521project.util.IEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimeTableViewHolder>{

    private final LocalDate date;

    private final ArrayList<LocalDate> daysInWeek;
    private final ArrayList<Integer> TimetableCell;
    private final OnItemListener onItemListener;
    private Context mContext;
    private ArrayList<Integer> idStored;
    private ArrayList<Integer> eventsStored;

    public TimetableAdapter(Context context, LocalDate date, ArrayList<LocalDate> daysInWeek, ArrayList<Integer> timetableCell, OnItemListener onItemListener) {
        this.mContext = context;
        this.date = date;
        this.daysInWeek = daysInWeek;
        TimetableCell = timetableCell;
        this.onItemListener = onItemListener;

        idStored = new ArrayList<>();
        eventsStored = new ArrayList<>();
        for (int i = 0; i < TimetableCell.size(); i++) {
            idStored.add(-1);
            eventsStored.add(0);
        }
        for (int i = 0 ; i < daysInWeek.size() ; i++) {
            List<IEvent> events = MainActivity.dataLoader.getEventsByDate(daysInWeek.get(i));
            if (events == null) continue;
            int num = 0;
            for (IEvent event : events) {
                int gridLast = (event.getEndTime().toInt() - event.getStartTime().toInt())/30;
                int startGrid = (event.getStartTime().toInt())/30;
                num++;

                for (int j = 0 ; j < gridLast; j++) {
                    idStored.set(i + 1 + 8 * (startGrid + j), event.getID());
                    eventsStored.set(i + 1 + 8 * (startGrid + j), num);
                }
            }
        }
    }

    @NonNull
    @Override
    public TimeTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.timetable_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.05);

        return new TimeTableViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTableViewHolder holder, int position) {

        int col = TimetableCell.get(position) % 8;
        int row = TimetableCell.get(position) / 8;


        if (col == 0) { // on the show time columns
            int hour = row / 2;
            int minute = (row % 2) * 30;

            String time = String.format("%02d:%02d", hour, minute);
            holder.wordsInTimetable.setText(time);
            holder.wordsInTimetable.setTypeface(null, Typeface.BOLD);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.purple_200));
        }
        else { // color IEvent

            holder.wordsInTimetable.setText("");
            //holder.wordsInTimetable.setGravity(Gravity.CENTER);
            //holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
            int eventID = idStored.get(position);
            if (eventID == -1) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                return;
            }

            int color = eventsStored.get(position) % 4;
            IEvent event = MainActivity.dataLoader.getEventByID(eventID);
            int status = event.getStatus();

            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, getColor(status, color)));


        }

    }

    private int getColor(int status, int num) {
        if (status == 1) {
            switch (num) {
                case 0:
                    return R.color.green1;
                case 1:
                    return R.color.green2;
                case 2:
                    return R.color.green3;
                default:
                    return R.color.green4;
            }
        } else if (status == 0) {
            switch (num) {
                case 0:
                    return R.color.yellow1;
                case 1:
                    return R.color.yellow2;
                case 2:
                    return R.color.yellow3;
                default:
                    return R.color.yellow4;
            }
        }
        switch (num) {
            case 0:
                return R.color.blue1;
            case 1:
                return R.color.blue2;
            case 2:
                return R.color.blue3;
            default:
                return R.color.blue4;
        }
    }

    @Override
    public int getItemCount() {
        return TimetableCell.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position);
    }

    public void clickOn(int position, Context context, ActivityResultLauncher resultLauncher) {
        if (position % 8 != 0)
        {
            //Click Grid
            int eventID = idStored.get(position);
            if (eventID == -1) return;

            IEvent event = MainActivity.dataLoader.getEventByID(eventID);
            if (event.getStatus()==0) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",eventID);

                Intent confirmIntent = new Intent(context, ConfirmEventIntent.class);
                confirmIntent.putExtras(bundle);
                resultLauncher.launch(confirmIntent);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("id",eventID);

                Intent reviewIntent = new Intent(context, ReviewEventIntent.class);
                reviewIntent.putExtras(bundle);
                resultLauncher.launch(reviewIntent);
            }

        }
    }
}
