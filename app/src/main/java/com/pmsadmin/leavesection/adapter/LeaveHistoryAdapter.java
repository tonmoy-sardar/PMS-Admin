package com.pmsadmin.leavesection.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pmsadmin.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.LeaveHistoryViewHolder> {
    Activity activity;
    public LeaveHistoryAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public LeaveHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_history, parent, false);

        return new LeaveHistoryAdapter.LeaveHistoryViewHolder(activity,itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveHistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class LeaveHistoryViewHolder extends RecyclerView.ViewHolder {

        public LeaveHistoryViewHolder(Activity activity,View itemView) {
            super(itemView);
        }
    }
}
