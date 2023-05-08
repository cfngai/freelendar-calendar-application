package com.example.comp4521project.ui.monthly;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.util.IEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysofMonth;
    private final OnItemListener onItemListener;

    private final LocalDate date;
    private Context mContext;

    public CalendarAdapter(Context context, LocalDate date, ArrayList<String> daysofMonth, OnItemListener onItemListener) {
        this.daysofMonth = daysofMonth;
        this.onItemListener = onItemListener;
        this.date = date;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight()* 0.1333333);
        return new CalendarViewHolder(view, onItemListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        holder.dayofMonth.setText(daysofMonth.get(position));
        try {
            LocalDate checkDate = LocalDate.of(date.getYear(), date.getMonthValue(), Integer.parseInt(daysofMonth.get(position)));
            List<IEvent> events = MainActivity.dataLoader.getEventsByDate(checkDate);
            int status_priority = 2;
            for (IEvent event: events) {
                if (event.getStatus() < status_priority) status_priority = event.getStatus();
            }
            switch (status_priority) {
                case -1:
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                    break;
                case 0:
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
                    break;
                case 1:
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                    break;
                default:
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }
        } catch (Exception ex) {
            // Empty Grid
            return;
        }


    }

    @Override
    public int getItemCount() {
        return daysofMonth.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
}