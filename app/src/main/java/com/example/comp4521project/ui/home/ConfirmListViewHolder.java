package com.example.comp4521project.ui.home;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.R;

public class ConfirmListViewHolder extends RecyclerView.ViewHolder {

    public final TextView event_id_des;
    public final Button btn_confirm;
    public final Button btn_delete;

    public ConfirmListViewHolder(@NonNull View itemView) {
        super(itemView);
        event_id_des = itemView.findViewById(R.id.event_id_des);
        btn_confirm = itemView.findViewById(R.id.home_confirmBtn);
        btn_delete = itemView.findViewById(R.id.home_Delete);
    }
}