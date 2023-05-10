package com.example.comp4521project.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.util.IEvent;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class ConfirmListAdapter extends RecyclerView.Adapter<ConfirmListViewHolder> {

    private final ArrayList<IEvent> events;
    private final OnButtonClickListener onButtonClickListener;
    private Context mContext;

    public ConfirmListAdapter(Context Context , ArrayList<IEvent> events, OnButtonClickListener onButtonClickListener) {
        this.mContext = Context;
        this.events = events;
        this.onButtonClickListener = onButtonClickListener;
        Log.d("confirm View", "create success");
        Log.d("confirm View", "event size" + events.size());
    }

    @NonNull
    @Override
    public ConfirmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.confirm_item, parent, false);

        Log.d("confirm View", "create View Holder");
        return new ConfirmListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmListViewHolder holder, int position) {

        IEvent event = events.get(position);
        String message = event.getTitle() + "\n" + event.getDescription();

        holder.event_id_des.setText(message);


        holder.btn_confirm.setTag(event.getID());
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID = (int) view.getTag();

                MainActivity.dataLoader.updateEventStatus(mContext, ID, 1);
                events.removeIf(e->e.getID() == ID);
                notifyDataSetChanged();
                onButtonClickListener.onButtonClick();
            }
        });

        holder.btn_delete.setTag(event.getID());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID = (int) view.getTag();

                MainActivity.dataLoader.removeEvent(mContext,ID);
                events.removeIf(e->e.getID() == ID);
                notifyDataSetChanged();
                onButtonClickListener.onButtonClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface OnButtonClickListener {
        void onButtonClick();
    }
}