package com.example.comp4521project.ui.monthly;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView dayofMonth;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayofMonth = itemView.findViewById(R.id.CellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayofMonth.getText());
    }
}