package com.example.comp4521project.ui.weekly;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.R;

public class TimeTableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView wordsInTimetable;
    private final TimetableAdapter.OnItemListener onItemListener;

    public TimeTableViewHolder(@NonNull View itemView, TimetableAdapter.OnItemListener onItemListener) {
        super(itemView);
        this.wordsInTimetable = itemView.findViewById(R.id.cellTimeText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition());

    }
}